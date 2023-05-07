package com.daichi703n.echo.client;

import java.net.*;
import java.io.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EchoClient implements AutoCloseable {
    private final Socket clientSocket;

    private final BufferedReader in;

    private final PrintWriter out;

    private static final Logger log = LogManager.getLogger(EchoClient.class);

    public EchoClient(String ip, int port) {
        try {
            this.clientSocket = new Socket(ip, port);
            this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            this.out = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
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
