package com.iboxpay.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.iboxpay.entity.ConfigInfo;
import com.iboxpay.entity.ErrorInfo;
import com.iboxpay.service.DataCenterLogAnalysisService;
import com.iboxpay.util.CollectionUtils;
import com.iboxpay.util.RemoteOpUtils;

import ch.ethz.ssh2.Connection;

public class DataCenterLogAnalysisServiceImpl implements DataCenterLogAnalysisService {

  private Connection conn;

  private ConfigInfo configInfo;

  public DataCenterLogAnalysisServiceImpl(ConfigInfo configInfo) throws Exception {
    String host = configInfo.getHost();
    String username = configInfo.getUsername();
    String password = configInfo.getPassword();
    this.conn = RemoteOpUtils.getConnection(host, username, password);
    this.configInfo = configInfo;
  }

  @Override
  public List<ErrorInfo> analyzeLogFromDataCenter(String dateStr) throws Exception {
    String logErrDir = configInfo.getLogErrDir();
    String logErrFilename = configInfo.getLogErrFileName();

    StringBuffer sb = new StringBuffer();
    sb.append("cd " + logErrDir);
    sb.append(" && ");
    sb.append("grep -n 'ERROR' " + logErrFilename + "." + dateStr + "-*.*");

    List<String> resultList = RemoteOpUtils.exececuteCommand(conn, sb.toString());

    if (CollectionUtils.isEmpty(resultList)) {
      return null;
    }

    // 获取基本异常接口信息
    List<ErrorInfo> errInfoList = getGeneralErrorInfo(resultList);
    for (ErrorInfo errInfo : errInfoList) {
      setDetailedErrInfo(errInfo, configInfo);
      setRequestAndResponse(errInfo, configInfo, dateStr);
    }

    return errInfoList;
  }

  private void setDetailedErrInfo(ErrorInfo errInfo, ConfigInfo configInfo) throws IOException {
    String errDesc = errInfo.getErrDesc();
    int lineNo = errInfo.getLineNo();
    String errLocation = errInfo.getErrLocation();
    String logErrDir = configInfo.getLogErrDir();

    String command = null;
    if ("缺少必填sql参数!".equals(errDesc)) {
      command = "sed -n '" + lineNo + "," + (lineNo + 7) + "p' " + errLocation;
    } else {
      command = "sed -n '" + lineNo + "," + (lineNo + 30) + "p' " + errLocation;
    }

    StringBuffer sb = new StringBuffer("cd " + logErrDir);
    sb.append(" && ");
    sb.append(command);

    List<String> resultList = RemoteOpUtils.exececuteCommand(conn, sb.toString());
    sb = new StringBuffer();
    for (String result : resultList) {
      sb.append(result + "\r\n");
    }

    errInfo.setErrDetail(sb.toString());
  }

  private void setRequestAndResponse(ErrorInfo errInfo, ConfigInfo configInfo, String dateStr) throws IOException {
    String transId = errInfo.getTransId();
    String logInterDir = configInfo.getLogInterDir();
    String logInterFileName = configInfo.getLogInterFileName();
    StringBuffer sb = new StringBuffer("cd " + logInterDir);
    sb.append(" && ");
    sb.append("grep '" + transId + "' " + logInterFileName + "." + dateStr + "-*.*");

    List<String> resultList = RemoteOpUtils.exececuteCommand(conn, sb.toString());
    sb = new StringBuffer();
    Pattern reqPattern = Pattern.compile(".*(\\[request\\].*\\{.*\"serviceId\":\"(\\w+)\".*\\}).*");
    Pattern respPattern = Pattern.compile(".*(\\[response\\].*\\{.*\"resp\":\\{.*\\}.*\\}).*");
    Matcher reqMatcher = null;
    Matcher respMatcher = null;
    String interName = null;
    String reqOrResp = null;
    int count = 0;
    for (String result : resultList) {
      reqMatcher = reqPattern.matcher(result);
      respMatcher = respPattern.matcher(result);
      if (reqMatcher.find()) {
        if (StringUtils.isBlank(interName)) {
          interName = reqMatcher.group(2);
        }
        reqOrResp = reqMatcher.group(1);
      } else if (respMatcher.find()) {
        reqOrResp = respMatcher.group(1);
      }

      if (count++ > 1) {
        break;
      }

      sb.append(reqOrResp + "\r\n");
    }

    errInfo.setInterfaceName(interName);
    errInfo.setReqAndResp(sb.toString());
  }

  // 获取错误接口信息
  private List<ErrorInfo> getGeneralErrorInfo(List<String> resultList) {
    List<ErrorInfo> errInfoList = new ArrayList<>();

    Pattern pattern = Pattern
        .compile("([a-z]+.log[\\d\\-\\.]*):(\\d+):([\\d\\-\\s\\.:]+)\\s*ERROR.*transactionId\\[(\\d+)\\]\\s*-\\s*(.*)");

    for (String result : resultList) {
      Matcher matcher = pattern.matcher(result);
      if (matcher.find()) {
        ErrorInfo errInfo = new ErrorInfo();
        errInfo.setErrLocation(matcher.group(1));
        errInfo.setLineNo(Integer.parseInt(matcher.group(2)));
        errInfo.setDate(matcher.group(3));
        errInfo.setTransId(matcher.group(4));
        errInfo.setErrDesc(matcher.group(5));
        errInfoList.add(errInfo);
      }
    }

    return errInfoList;
  }

}
