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

## TLS Socket Server

```
$ openssl s_client -connect localhost:8444
CONNECTED(00000003)
Can't use SSL_get_servername
depth=0 C = Unknown, ST = Unknown, L = Unknown, O = Unknown, OU = Unknown, CN = daichi703n
verify error:num=18:self signed certificate
verify return:1
depth=0 C = Unknown, ST = Unknown, L = Unknown, O = Unknown, OU = Unknown, CN = daichi703n
verify return:1
---
Certificate chain
 0 s:C = Unknown, ST = Unknown, L = Unknown, O = Unknown, OU = Unknown, CN = daichi703n
   i:C = Unknown, ST = Unknown, L = Unknown, O = Unknown, OU = Unknown, CN = daichi703n
---
Server certificate
-----BEGIN CERTIFICATE-----
MIIDmTCCAoGgAwIBAgIEW6BgTDANBgkqhkiG9w0BAQsFADBvMRAwDgYDVQQGEwdV
...
NZB8ur+orW9XXVxOQW5ZNcwibn40gzo1T3yKZE3x8tbCHd9Hv4SoHI2/hFwZfrnN
xObycHNEb0XgBb29VQ==
-----END CERTIFICATE-----
subject=C = Unknown, ST = Unknown, L = Unknown, O = Unknown, OU = Unknown, CN = daichi703n

issuer=C = Unknown, ST = Unknown, L = Unknown, O = Unknown, OU = Unknown, CN = daichi703n

---
No client certificate CA names sent
Peer signing digest: SHA256
Peer signature type: RSA-PSS
Server Temp Key: X25519, 253 bits
---
SSL handshake has read 1395 bytes and written 382 bytes
Verification error: self signed certificate
---
New, TLSv1.2, Cipher is ECDHE-RSA-AES256-GCM-SHA384
Server public key is 2048 bit
Secure Renegotiation IS supported
Compression: NONE
Expansion: NONE
No ALPN negotiated
SSL-Session:
    Protocol  : TLSv1.2
    Cipher    : ECDHE-RSA-AES256-GCM-SHA384
    Session-ID: ACBAD3700DDA72EC7DB56836158BECC346A9536B610E1222D4C65060E8656711
    Session-ID-ctx:
    Master-Key: 1B5507F2E4B0EC20AD06BBAD26CF7188C7F4C469D405DD59E327E08483F569089C80C2A2EB6CC57F6B6B7067C286861E
    PSK identity: None
    PSK identity hint: None
    SRP username: None
    Start Time: 1681109066
    Timeout   : 7200 (sec)
    Verify return code: 18 (self signed certificate)
    Extended master secret: yes
---
hello
hello
world
world
!
!
.
bye
closed
```

```
INFO 1622 --- [           main] com.daichi703n.echo.EchoApplication      : Started EchoApplication in 6.577 seconds (JVM running for 6.95)
INFO 1622 --- [asyncExecutor-1] com.daichi703n.echo.socket.EchoSocket    : Start Echo Socket
INFO 1622 --- [asyncExecutor-2] com.daichi703n.echo.socket.TlsSocket     : Start TLS Socket
INFO 1622 --- [asyncExecutor-1] com.daichi703n.echo.socket.EchoSocket    : Start listening port 8888
INFO 1622 --- [asyncExecutor-2] com.daichi703n.echo.socket.TlsSocket     : Start listening port 8444
INFO 1622 --- [on(2)-127.0.0.1] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring DispatcherServlet 'dispatcherServlet'
INFO 1622 --- [on(2)-127.0.0.1] o.s.web.servlet.DispatcherServlet        : Initializing Servlet 'dispatcherServlet'
INFO 1622 --- [on(2)-127.0.0.1] o.s.web.servlet.DispatcherServlet        : Completed initialization in 3 ms
INFO 1622 --- [       Thread-1] com.daichi703n.echo.socket.TlsSocket     : Accepted connection from 0:0:0:0:0:0:0:1 on port 63895
INFO 1622 --- [       Thread-1] com.daichi703n.echo.socket.TlsSocket     : Received: hello
INFO 1622 --- [       Thread-1] com.daichi703n.echo.socket.TlsSocket     : Received: world
INFO 1622 --- [       Thread-1] com.daichi703n.echo.socket.TlsSocket     : Received: !
INFO 1622 --- [       Thread-1] com.daichi703n.echo.socket.TlsSocket     : Received: .
INFO 1622 --- [       Thread-1] com.daichi703n.echo.socket.TlsSocket     : Closing connection
```

### Generate Cert
```
keytool -genkeypair -alias serverkey -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore src/main/resources/keystore/serverkey.p12 -validity 3650 -storepass password -ext san=ip:127.0.0.1,dns:localhost
---
What is your first and last name?
  [Unknown]:  daichi703n
What is the name of your organizational unit?
  [Unknown]:  
What is the name of your organization?
  [Unknown]:  
What is the name of your City or Locality?
  [Unknown]:  
What is the name of your State or Province?
  [Unknown]:  
What is the two-letter country code for this unit?
  [Unknown]:  
Is CN=daichi703n, OU=Unknown, O=Unknown, L=Unknown, ST=Unknown, C=Unknown correct?
  [no]:  yes
```

## References
- [A Guide to Java Sockets](https://www.baeldung.com/a-guide-to-java-sockets)
- [誤解しがちなThreadPoolTaskExecutorの設定](https://ik.am/entries/443)
- [Introduction to SSL in Java](https://www.baeldung.com/java-ssl)
- [SSL Handshake Failures](https://www.baeldung.com/java-ssl-handshake-failures)
- **[JavaのSSLSocketを使う](https://qiita.com/jkomatsu/items/d65ccbc6f8e7d73a9972)**
- ~~[Java HTTPS Client Certificate Authentication](https://www.baeldung.com/java-https-client-certificate-authentication)~~ HTTPS
- ~~[HTTPS using Self-Signed Certificate in Spring Boot](https://www.baeldung.com/spring-boot-https-self-signed-certificate)~~ HTTPS
- ~~[SSLServerSocket and certificate setup](https://stackoverflow.com/questions/53323855/sslserversocket-and-certificate-setup)~~
