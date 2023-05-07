package com.daichi703n.echo.client;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class TLSClientTest {

    @Test
    void sendMessage_thenCorrect() throws Exception {
        try (TLSClient client = new TLSClient(
                "127.0.0.1",
                8444,
                "src/main/resources/keystore/daichi703n-ca.p12",
                "password".toCharArray()
        )) {
            String expected1 = "Hello world!";
            String actual1 = client.sendMessage("Hello world!");
            assertEquals(expected1, actual1);

            String expected2 = "bye";
            String actual2 = client.sendMessage(".");
            assertEquals(expected2, actual2);
        }
    }
}
