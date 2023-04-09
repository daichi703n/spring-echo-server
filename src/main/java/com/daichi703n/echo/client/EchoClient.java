package com.daichi703n.echo.client;

import java.net.*;
import java.io.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EchoClient {
  private Socket clientSocket;
  private PrintWriter out;
  private BufferedReader in;
  
  private static final Logger log = LogManager.getLogger(EchoClient.class);
  
  public void startConnection(String ip, int port) throws UnknownHostException, IOException {
      clientSocket = new Socket(ip, port);
      out = new PrintWriter(clientSocket.getOutputStream(), true);
      in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
  }

  public String sendMessage(String msg) throws IOException {
      log.info("Send: {}", msg);
      out.println(msg);
      String resp = in.readLine();
      return resp;
  }

  public void stopConnection() throws IOException {
      in.close();
      out.close();
      clientSocket.close();
  }

}
