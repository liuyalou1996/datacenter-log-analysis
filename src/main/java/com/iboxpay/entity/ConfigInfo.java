package com.iboxpay.entity;

public class ConfigInfo {

  private String host;
  private int port;
  private String username;
  private String password;
  private String logErrDir;
  private String logInterDir;
  private String logErrFileName;
  private String logInterFileName;

  public String getHost() {
    return host;
  }

  public int getPort() {
    return port;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public String getLogErrDir() {
    return logErrDir;
  }

  public String getLogInterDir() {
    return logInterDir;
  }

  public String getLogErrFileName() {
    return logErrFileName;
  }

  public String getLogInterFileName() {
    return logInterFileName;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public void setPort(int port) {
    this.port = port;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setLogErrDir(String logErrDir) {
    this.logErrDir = logErrDir;
  }

  public void setLogInterDir(String logInterDir) {
    this.logInterDir = logInterDir;
  }

  public void setLogErrFileName(String logErrFileName) {
    this.logErrFileName = logErrFileName;
  }

  public void setLogInterFileName(String logInterFileName) {
    this.logInterFileName = logInterFileName;
  }

  @Override
  public String toString() {
    return "ConfigInfo [host=" + host + ", port=" + port + ", username=" + username + ", password=" + password
        + ", logErrDir=" + logErrDir + ", logInterDir=" + logInterDir + ", logErrFileName=" + logErrFileName
        + ", logInterFileName=" + logInterFileName + "]";
  }

}
