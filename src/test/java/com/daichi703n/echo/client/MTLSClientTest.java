package com.daichi703n.echo.client;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MTLSClientTest {

    @Test
    void sendMessage_thenCorrect() throws Exception {
        try (MTLSClient client = new MTLSClient(
                "127.0.0.1",
                8333,
                "src/main/resources/keystore/daichi703n-ca.p12",
                "password".toCharArray(),
                "src/main/resources/keystore/daichi703n-client.p12",
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
