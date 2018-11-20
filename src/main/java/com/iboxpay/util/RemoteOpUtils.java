package com.iboxpay.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.iboxpay.ssh2.factory.SSH2ConnectionFactory;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

public class RemoteOpUtils {

  private static Logger logger = Logger.getLogger(RemoteOpUtils.class);

  public static Connection getConnection(String host, String username, String password) throws Exception {
    SSH2ConnectionFactory connFactory = new SSH2ConnectionFactory(host, username, password);
    return connFactory.getSSH2Connection();
  }

  public static List<String> exececuteCommand(Connection conn, String command) throws IOException {
    Session session = conn.openSession();
    logger.info("start to execute command -> " + command);
    session.execCommand(command);

    InputStream is = new StreamGobbler(session.getStdout());
    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));

    List<String> resultList = new ArrayList<>();
    String line = null;
    while ((line = reader.readLine()) != null) {
      resultList.add(line);
    }
    reader.close();
    session.close();
    return resultList;
  }
}
