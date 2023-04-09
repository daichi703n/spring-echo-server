# Spring Echo Server
Echo server for socket connection.

## TCP Socket Server

```
$ telnet localhost 8888
Trying ::1...
Connected to localhost.
Escape character is '^]'.
hello
hello
world
world
!
!
.
bye
Connection closed by foreign host.
```

```
INFO 6130 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8080 (http) with context path ''
INFO 6130 --- [           main] com.daichi703n.echo.EchoApplication      : Started EchoApplication in 7.428 seconds (JVM running for 8.147)
INFO 6130 --- [asyncExecutor-1] com.daichi703n.echo.socket.EchoSocket    : Start Echo Socket
INFO 6130 --- [asyncExecutor-1] com.daichi703n.echo.socket.EchoSocket    : Start listening port 8888
INFO 6130 --- [on(1)-127.0.0.1] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring DispatcherServlet 'dispatcherServlet'
INFO 6130 --- [on(1)-127.0.0.1] o.s.web.servlet.DispatcherServlet        : Initializing Servlet 'dispatcherServlet'
INFO 6130 --- [on(1)-127.0.0.1] o.s.web.servlet.DispatcherServlet        : Completed initialization in 4 ms
INFO 6130 --- [       Thread-1] com.daichi703n.echo.socket.EchoSocket    : Received: hello
INFO 6130 --- [       Thread-1] com.daichi703n.echo.socket.EchoSocket    : Received: world
INFO 6130 --- [       Thread-1] com.daichi703n.echo.socket.EchoSocket    : Received: !
INFO 6130 --- [       Thread-1] com.daichi703n.echo.socket.EchoSocket    : Received: .
INFO 6130 --- [       Thread-1] com.daichi703n.echo.socket.EchoSocket    : Closing connection
```

## References
- [A Guide to Java Sockets](https://www.baeldung.com/a-guide-to-java-sockets)
- [誤解しがちなThreadPoolTaskExecutorの設定](https://ik.am/entries/443)
