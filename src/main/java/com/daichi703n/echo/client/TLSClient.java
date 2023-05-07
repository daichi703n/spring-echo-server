package com.daichi703n.echo.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

public class TLSClient implements AutoCloseable {

    private final BufferedReader in;

    private final PrintWriter out;

    private final SSLSocket clientSocket;

    private static final Logger log = LogManager.getLogger(TLSClient.class);

    public TLSClient(String ip, int port, String trustStoreName, char[] trustStorePassword) {
        try {
            // KeyStore
            KeyStore keyStore = KeyStore.getInstance("pkcs12");
            keyStore.load(new FileInputStream(trustStoreName), trustStorePassword);

            // TrustManagerFactory from KeyStore
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
            trustManagerFactory.init(keyStore);

            // SSLContext from TrustManagerFactory
            SSLContext ctx = SSLContext.getInstance("TLS");
            ctx.init(null, trustManagerFactory.getTrustManagers(), null);

            // SSLSocketFactory from SSLContext
            SSLSocketFactory sslSocketFactory = ctx.getSocketFactory();
            SSLSocket clientSocket = (SSLSocket) sslSocketFactory.createSocket(ip, port);

            this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            this.out = new PrintWriter(clientSocket.getOutputStream(), true);
            this.clientSocket = clientSocket;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
        clientSocket.close();
    }
}
