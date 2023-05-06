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
import java.io.FileInputStream;
import java.security.KeyStore;

@Component
public class TLSServer {

    private static final Logger log = LogManager.getLogger(TLSServer.class);

    @EventListener
    @Async
    public void run(ContextRefreshedEvent event) {
        log.info("Start TLS Socket");

        start(
                8444,
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
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");

            KeyStore keyStore = KeyStore.getInstance("pkcs12");
            keyStore.load(new FileInputStream(keyStoreName), keyStorePassword);

            keyManagerFactory.init(keyStore, trustStorePassword);

            SSLContext ctx = SSLContext.getInstance("TLS");
            ctx.init(keyManagerFactory.getKeyManagers(), null, null);

            try (SSLServerSocket sslServerSocket = (SSLServerSocket) ctx
                    .getServerSocketFactory()
                    .createServerSocket(port)) {
                sslServerSocket.setEnabledProtocols(new String[]{tlsVersion});
                sslServerSocket.setNeedClientAuth(false);

                log.info("Start listening port {}", port);

                while (true) {
                    new EchoServerThread(sslServerSocket.accept()).start();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
