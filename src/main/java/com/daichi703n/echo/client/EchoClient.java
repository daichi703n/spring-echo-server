package com.daichi703n.echo.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class EchoClient implements AutoCloseable {
    private final BufferedReader in;

    private final PrintWriter out;

    private static final Logger log = LogManager.getLogger(EchoClient.class);

    public EchoClient(String ip, int port) {
        try {
            Socket clientSocket = new Socket(ip, port);

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
    }
}
