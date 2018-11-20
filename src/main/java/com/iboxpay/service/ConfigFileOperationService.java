package com.iboxpay.service;

import java.io.File;
import java.util.Map;

import com.iboxpay.entity.ConfigInfo;

public interface ConfigFileOperationService {

  public Map<String, ConfigInfo> readConfigFileToMap();

  public Map<String, ConfigInfo> readConfigFileToMap(File file);

  public void writeToConfigFile(Map<String, ConfigInfo> configMap);
}
