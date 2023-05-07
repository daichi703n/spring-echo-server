package com.daichi703n.echo.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.security.KeyStore;

@Component
public class MTLSServer {

    private static final Logger log = LogManager.getLogger(MTLSServer.class);

    @EventListener
    @Async
    public void run(ContextRefreshedEvent event) {
        log.info("Start mTLS Socket");

        start(
                8333,
                "TLSv1.2",
                "src/main/resources/keystore/daichi703n-ca.p12",
                "password".toCharArray(),
                "src/main/resources/keystore/daichi703n-server.p12",
                "password".toCharArray()
        );
    }

    public void start(
            int port,
            String tlsVersion,
            String trustStoreName,
            char[] trustStorePassword,
            String keyStoreName,
            char[] keyStorePassword) {
        try {
            KeyStore keyStore = KeyStore.getInstance("pkcs12");
            keyStore.load(
                    new FileInputStream(keyStoreName),
                    keyStorePassword
            );

            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
            keyManagerFactory.init(keyStore, keyStorePassword);

            KeyStore trustStore = KeyStore.getInstance("pkcs12");
            trustStore.load(
                    new FileInputStream(trustStoreName),
                    trustStorePassword
            );

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
            trustManagerFactory.init(trustStore);

            SSLContext ctx = SSLContext.getInstance("TLS");
            ctx.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);

            try (SSLServerSocket sslServerSocket = (SSLServerSocket) ctx
                    .getServerSocketFactory()
                    .createServerSocket(port)) {
                sslServerSocket.setEnabledProtocols(new String[]{tlsVersion});
                sslServerSocket.setNeedClientAuth(true);

                log.info("Start listening port {}", port);

                while (true)
                    new EchoServerThread(sslServerSocket.accept()).start();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
