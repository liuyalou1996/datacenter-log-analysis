package com.iboxpay.ssh2.factory;

import java.io.IOException;

import org.apache.log4j.Logger;

import ch.ethz.ssh2.Connection;

public class SSH2ConnectionFactory {

  private static Logger logger = Logger.getLogger(SSH2ConnectionFactory.class);

  private Connection conn;

  private String host;
  private String username;
  private String password;

  public SSH2ConnectionFactory(String host, String username, String password) {
    this.host = host;
    this.username = username;
    this.password = password;
  }

  public String getHost() {
    return host;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public Connection getSSH2Connection() throws IOException {
    try {
      conn = new Connection(this.host);
      // 连接到远程服务器
      conn.connect();
      // 身份认证
      boolean isAuthenticated = conn.authenticateWithPassword(this.username, this.password);
      if (!isAuthenticated) {
        throw new IllegalArgumentException("invalid username or password!");
      }
      logger.info("successfully connect to " + this.host);
    } catch (IOException e) {
      throw e;
    }

    return conn;
  }
}
