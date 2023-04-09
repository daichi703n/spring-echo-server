# Spring Echo Server
Echo server for socket connection.

## TCP Socket Server

```
$ telnet localhost 8888
Trying ::1...
Connected to localhost.
Escape character is '^]'.
Hi
.
bye
Connection closed by foreign host.

$ telnet localhost 8888
Trying ::1...
Connected to localhost.
Escape character is '^]'.
Hi2
.
bye
Connection closed by foreign host.
```

```
INFO 18142 --- [           main] com.daichi703n.echo.EchoApplication      : Started EchoApplication in 5.821 seconds (JVM running for 6.288)
Start Echo Socket
INFO 18142 --- [on(2)-127.0.0.1] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring DispatcherServlet 'dispatcherServlet'
INFO 18142 --- [on(2)-127.0.0.1] o.s.web.servlet.DispatcherServlet        : Initializing Servlet 'dispatcherServlet'
INFO 18142 --- [on(2)-127.0.0.1] o.s.web.servlet.DispatcherServlet        : Completed initialization in 4 ms
Received: Hi
Received: .
Received: Hi2
Received: .
```

## References
- [A Guide to Java Sockets](https://www.baeldung.com/a-guide-to-java-sockets)
