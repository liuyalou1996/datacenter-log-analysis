package com.iboxpay.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.iboxpay.entity.ConfigInfo;
import com.iboxpay.service.ConfigFileOperationService;
import com.iboxpay.util.JsonUtils;

public class ConfigFileOperationServiceImpl implements ConfigFileOperationService {

  private static Logger logger = Logger.getLogger(ConfigFileOperationServiceImpl.class);

  private static final File file;

  static {
    String path = System.getProperty("user.dir") + File.separator + "config/server.json";
    file = new File(path);
  }

  @Override
  public Map<String, ConfigInfo> readConfigFileToMap() {
    return readConfigFileToMap(file);
  }

  @Override
  public Map<String, ConfigInfo> readConfigFileToMap(File file) {
    Map<String, ConfigInfo> configMap = null;
    try {
      if (file.exists()) {
        String content = FileUtils.readFileToString(file, Charset.forName("UTF-8"));
        if (StringUtils.isNotBlank(content)) {
          // 泛型参数必须手动指定
          configMap = JSON.parseObject(content, new TypeReference<LinkedHashMap<String, ConfigInfo>>() {});
        }
      }
    } catch (IOException e) {
      logger.error("fail to read the file -> server.json.", e);
    }
    return configMap;
  }

  @Override
  public void writeToConfigFile(Map<String, ConfigInfo> configMap) {
    String jsonStr = JsonUtils.toPrettyJsonString(configMap);
    try {
      FileUtils.writeStringToFile(file, jsonStr, Charset.forName("UTF-8"));
    } catch (IOException e) {
      logger.error("fail to write configuration into server.json", e);
    }
  }

}
