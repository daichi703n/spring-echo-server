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
public class TLSSocket {

    private static final Logger log = LogManager.getLogger(TLSSocket.class);

    @EventListener
    @Async
    public void run(ContextRefreshedEvent cre) throws Exception {
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
            char[] keyStorePassword) throws Exception {
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
                new EchoClientHandler(sslServerSocket.accept()).start();
            }
        }
    }

    private static class EchoClientHandler extends Thread {
        private final Socket clientSocket;

        public EchoClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            InetAddress clientAddress = clientSocket.getInetAddress();
            int clientPort = clientSocket.getPort();

            log.info(
                    "Accepted connection from {} on port {}",
                    clientAddress.getHostAddress(),
                    clientPort
            );

            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    log.info("Received: {}", inputLine);

                    if (inputLine.equals(".")) {
                        out.println("bye");
                        break;
                    } else {
                        out.println(inputLine);
                    }
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
