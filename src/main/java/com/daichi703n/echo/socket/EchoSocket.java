package com.daichi703n.echo.socket;

import java.net.*;
import java.io.*;

import org.springframework.stereotype.Service;

@Service
public class EchoSocket {
  private ServerSocket serverSocket;

  public void start(int port) throws IOException {
    serverSocket = new ServerSocket(port);
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
          System.out.println("Received: "+inputLine);
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
