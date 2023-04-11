package com.daichi703n.echo.socket;

import java.net.*;
import java.io.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EchoSocket {

    private static final Logger log = LogManager.getLogger(EchoSocket.class);

    @EventListener
    @Async
    public void run(ContextRefreshedEvent cre) throws IOException {
        log.info("Start Echo Socket");

        start(8888);
    }

    public void start(int port) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);

        log.info("Start listening port {}", port);

        while (true) {
            new EchoClientHandler(serverSocket.accept()).start();
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
