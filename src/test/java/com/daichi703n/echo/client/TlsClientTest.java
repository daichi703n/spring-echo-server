package com.daichi703n.echo.client;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import org.junit.jupiter.api.Test;

public class TlsClientTest {
  
  @Test
  public void sendMessage_thenCorrect() throws UnknownHostException, IOException, NoSuchAlgorithmException, KeyManagementException, KeyStoreException, CertificateException{
    TlsClient client = new TlsClient();
    client.startConnection("127.0.0.1", 8444);
    String resp1 = client.sendMessage("hello");
    String resp2 = client.sendMessage("world");
    String resp3 = client.sendMessage("!");
    String resp4 = client.sendMessage(".");
    
    assertEquals("hello", resp1);
    assertEquals("world", resp2);
    assertEquals("!", resp3);
    assertEquals("bye", resp4);
    
    client.stopConnection();
  }

}
