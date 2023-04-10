package com.daichi703n.echo.socket;

import java.net.*;
import java.security.KeyStore;
import java.io.*;
import javax.net.ssl.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class MTlsSocket {
  private ServerSocket serverSocket;
  
  private static final Logger log = LogManager.getLogger(MTlsSocket.class);
    
  @EventListener
  @Async
  public void run(ContextRefreshedEvent cse) {
    MTlsSocket server=new MTlsSocket();
    log.info("Start mTLS Socket");
    try {
      server.start(8333, "TLSv1.2", "src/main/resources/keystore/daichi703n-ca.p12", "password".toCharArray(), "src/main/resources/keystore/daichi703n-server.p12", "password".toCharArray());
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public void start(int port, String tlsVersion, String trustStoreName, char[] trustStorePassword, String keyStoreName, char[] keyStorePassword) throws IOException, Exception {
    FileInputStream key_p12_file = new FileInputStream(keyStoreName);
    FileInputStream trust_p12_file = new FileInputStream(trustStoreName);
    KeyManagerFactory kmf;
    KeyStore ks;
    ks = KeyStore.getInstance("pkcs12");
    ks.load(key_p12_file, keyStorePassword);
    
    kmf = KeyManagerFactory.getInstance("SunX509");
    kmf.init(ks, keyStorePassword);

    TrustManagerFactory tmf;
    KeyStore ts;
    ts = KeyStore.getInstance("pkcs12");
    ts.load(trust_p12_file, trustStorePassword);
    
    tmf = TrustManagerFactory.getInstance("SunX509");
    tmf.init(ts);

    SSLContext ctx = SSLContext.getInstance("TLS");
    ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
    // ctx.init(kmf.getKeyManagers(), null, null);
    SSLServerSocketFactory ssf = ctx.getServerSocketFactory();
    SSLServerSocket sslServerSocket = (SSLServerSocket)ssf.createServerSocket(port);
    sslServerSocket.setEnabledProtocols(new String[]{tlsVersion});
    sslServerSocket.setNeedClientAuth(true);
    
    log.info("Start listening port {}",port);
    while (true)
      new EchoClientHandler(sslServerSocket.accept()).start();
      
      // SSLSocket sslSocket = (SSLSocket) sslServerSocket.accept();
      // InetAddress clientAddress = sslSocket.getInetAddress();
      // int clientPort = sslSocket.getPort();
      // log.info("Accepted connection from " + clientAddress.getHostAddress() + " on port " + clientPort);

      // BufferedReader in = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()));
      // String line = in.readLine();
      // log.info("Received message from client: " + line);

      // PrintWriter out = new PrintWriter(new OutputStreamWriter(sslSocket.getOutputStream()));
      // out.println("Hello, World!");
      // out.flush();

      // sslSocket.close();
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
      InetAddress clientAddress = clientSocket.getInetAddress();
      int clientPort = clientSocket.getPort();
      log.info("Accepted connection from " + clientAddress.getHostAddress() + " on port " + clientPort);

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
          out.println(inputLine);
        }
        
        log.info("Closing connection");
        in.close();
        out.close();
        clientSocket.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
