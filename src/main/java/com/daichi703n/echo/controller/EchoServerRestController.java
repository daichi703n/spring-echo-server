package com.daichi703n.echo.controller;

import com.daichi703n.echo.client.EchoClient;
import com.daichi703n.echo.client.MTLSClient;
import com.daichi703n.echo.client.TLSClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class EchoServerRestController {

    @PostMapping("/echo")
    public String echo(@RequestBody String request) {
        try (EchoClient client = new EchoClient("127.0.0.1", 8888)) {
            return client.sendMessage(request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/tls")
    public String tls(@RequestBody String request) {
        try (TLSClient client = new TLSClient(
                "127.0.0.1",
                8444,
                "src/main/resources/keystore/daichi703n-ca.p12",
                "password".toCharArray()
        )) {
            return client.sendMessage(request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/mtls")
    public String mtls(
            @RequestHeader(name = "Dummy-Certificate", required = false) Boolean dummy,
            @RequestBody String request) {
        try (MTLSClient client = new MTLSClient(
                "127.0.0.1",
                8333,
                "src/main/resources/keystore/daichi703n-client.p12",
                "password".toCharArray(),
                dummy == null || Boolean.FALSE.equals(dummy)
                        ? "src/main/resources/keystore/daichi703n-ca.p12"
                        : "src/main/resources/keystore/daichi703n-dummy.p12",
                "password".toCharArray()
        )) {
            return client.sendMessage(request);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
