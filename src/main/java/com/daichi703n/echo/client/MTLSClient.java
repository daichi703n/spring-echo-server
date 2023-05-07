package com.daichi703n.echo.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.security.KeyStore;

public class MTLSClient implements AutoCloseable {

    private BufferedReader in;

    private PrintWriter out;

    private static final Logger log = LogManager.getLogger(MTLSClient.class);

    public MTLSClient(String ip, int port, String keyStoreName, char[] keyStorePassword, String trustStoreName, char[] trustStorePassword) throws Exception {
        // KeyStore
        KeyStore keyStore = KeyStore.getInstance("pkcs12");
        keyStore.load(new FileInputStream(keyStoreName), keyStorePassword);

        // KeyManagerFactory from KeyStore
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
        keyManagerFactory.init(keyStore, keyStorePassword);

        // TrustStore from KeyManagerFactory
        KeyStore trustStore = KeyStore.getInstance("pkcs12");
        trustStore.load(new FileInputStream(trustStoreName), trustStorePassword);

        // TrustManagerFactory from TrustStore
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
        trustManagerFactory.init(trustStore);

        // SSLContext from TrustManagerFactory
        SSLContext ctx = SSLContext.getInstance("TLS");
        ctx.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);

        // SSLSocketFactory from SSLContext
        SSLSocketFactory ssf = ctx.getSocketFactory();
        SSLSocket clientSocket = (SSLSocket) ssf.createSocket(ip, port);

        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new PrintWriter(clientSocket.getOutputStream(), true);
    }

    public String sendMessage(String msg) throws IOException {
        log.info("Send: {}", msg);

        out.println(msg);
        return in.readLine();
    }

    @Override
    public void close() throws IOException {
        in.close();
        out.close();
    }
}
