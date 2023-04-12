package com.daichi703n.echo.client;

import java.net.*;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.io.*;
import javax.net.ssl.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MTlsClient {
  private PrintWriter out;
  private BufferedReader in;
  
  private static final Logger log = LogManager.getLogger(MTlsClient.class);
  
  private static final String trustStoreName = "src/main/resources/keystore/daichi703n-ca.p12";
  private static final char[] trustStorePassword = "password".toCharArray();
  private static final String keyStoreName = "src/main/resources/keystore/daichi703n-client.p12";
  private static final char[] keyStorePassword = "password".toCharArray();
  
  public void startConnection(String ip, int port) throws UnknownHostException, IOException, NoSuchAlgorithmException, KeyStoreException, CertificateException, KeyManagementException, UnrecoverableKeyException {
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
    SSLSocketFactory ssf = (SSLSocketFactory) ctx.getSocketFactory();
    SSLSocket clientSocket = (SSLSocket)ssf.createSocket(ip, port); 
    
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
    // clientSocket.close();
  }

}
