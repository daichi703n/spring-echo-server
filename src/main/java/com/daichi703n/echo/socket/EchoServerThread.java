package com.daichi703n.echo.socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class EchoServerThread extends Thread {

    private static final Logger log = LogManager.getLogger(EchoServerThread.class);

    private final Socket clientSocket;

    public EchoServerThread(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        InetAddress clientAddress = clientSocket.getInetAddress();

        log.info(
                "Accepted connection from {} on port {}",
                clientAddress.getHostAddress(),
                clientSocket.getPort()
        );

        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
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

            clientSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
