package com.iboxpay.entity;

public class ErrorInfo {

  private String interfaceName;

  private String errLocation;

  private String date;

  private Integer lineNo;

  private String transId;

  private String reqAndResp;

  private String errDesc;

  private String errDetail;

  public String getInterfaceName() {
    return interfaceName;
  }

  public String getDate() {
    return date;
  }

  public String getErrLocation() {
    return errLocation;
  }

  public void setErrLocation(String errLocation) {
    this.errLocation = errLocation;
  }

  public String getTransId() {
    return transId;
  }

  public String getReqAndResp() {
    return reqAndResp;
  }

  public String getErrDesc() {
    return errDesc;
  }

  public String getErrDetail() {
    return errDetail;
  }

  public void setInterfaceName(String interfaceName) {
    this.interfaceName = interfaceName;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public void setTransId(String transId) {
    this.transId = transId;
  }

  public void setReqAndResp(String reqAndResp) {
    this.reqAndResp = reqAndResp;
  }

  public void setErrDesc(String errDesc) {
    this.errDesc = errDesc;
  }

  public void setErrDetail(String errDetail) {
    this.errDetail = errDetail;
  }

  public Integer getLineNo() {
    return lineNo;
  }

  public void setLineNo(Integer lineNo) {
    this.lineNo = lineNo;
  }

  @Override
  public String toString() {
    return "ErrorInfo [interfaceName=" + interfaceName + ", errLocation=" + errLocation + ", date=" + date + ", lineNo="
        + lineNo + ", transId=" + transId + ", reqAndResp=" + reqAndResp + ", errDesc=" + errDesc + ", errDetail="
        + errDetail + "]";
  }

}
