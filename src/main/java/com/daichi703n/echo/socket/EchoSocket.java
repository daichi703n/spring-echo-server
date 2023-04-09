package com.daichi703n.echo.socket;

import java.net.*;
import java.io.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EchoSocket {
  private ServerSocket serverSocket;
  
  private static final Logger log = LogManager.getLogger(EchoSocket.class);
  
  @EventListener
  @Async
  public void run(ContextRefreshedEvent cse) {
    EchoSocket server=new EchoSocket();
    log.info("Start Echo Socket");
    try {
      server.start(8888);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public void start(int port) throws IOException {
    serverSocket = new ServerSocket(port);
    log.info("Start listening port {}",port);
    while (true)
      new EchoClientHandler(serverSocket.accept()).start();
  }
  
  public void stop() throws IOException {
    serverSocket.close();
  }
  
  private static class EchoClientHandler extends Thread {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public EchoClientHandler(Socket socket) {
      this.clientSocket = socket;
    }

    public void run() {
      try{
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(
          new InputStreamReader(clientSocket.getInputStream()));
          
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
          log.info("Received: "+inputLine);
          if (".".equals(inputLine)) {
            out.println("bye");
            break;
          }
        }

        in.close();
        out.close();
        clientSocket.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
