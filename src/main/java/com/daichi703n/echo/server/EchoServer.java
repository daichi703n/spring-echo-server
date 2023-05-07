package com.daichi703n.echo.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ServerSocket;

@Component
public class EchoServer {

    private static final Logger log = LogManager.getLogger(EchoServer.class);

    @EventListener
    @Async
    public void run(ContextRefreshedEvent event) {
        log.info("Start Echo Socket");

        start(8888);
    }

    private void start(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            log.info("Start listening port {}", port);

            while (true) {
                new EchoServerThread(serverSocket.accept()).start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
