package com.daichi703n.echo;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.daichi703n.echo.socket.EchoSocket;

@SpringBootApplication
public class EchoApplication {

	public static void main(String[] args) {
		SpringApplication.run(EchoApplication.class, args);
    echoSocket(args);
	}

  public static void echoSocket(String[] args) {
    EchoSocket server=new EchoSocket();
    System.out.println("Start Echo Socket");
    try {
      server.start(8888);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

}
