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

### Export Cert
```
keytool -exportcert -rfc -keystore src/main/resources/keystore/serverkey.p12 -storetype PKCS12 -storepass password -alias serverkey -file src/main/resources/keystore/server.crt
---
Certificate stored in file <src/main/resources/keystore/server.crt>
```

```
$ openssl x509 -text -in src/main/resources/keystore/server.crt 
Certificate:
    Data:
        Version: 3 (0x2)
        Serial Number: 1537237068 (0x5ba0604c)
        Signature Algorithm: sha256WithRSAEncryption
        Issuer: C = Unknown, ST = Unknown, L = Unknown, O = Unknown, OU = Unknown, CN = daichi703n
        Validity
            Not Before: Apr 10 05:21:40 2023 GMT
            Not After : Apr  7 05:21:40 2033 GMT
        Subject: C = Unknown, ST = Unknown, L = Unknown, O = Unknown, OU = Unknown, CN = daichi703n
        Subject Public Key Info:
            Public Key Algorithm: rsaEncryption
                RSA Public-Key: (2048 bit)
                Modulus:
                    00:8d:b1:b9:cb:58:c2:85:2a:3c:76:a7:1a:e5:15:
                    ...
                    98:af:54:27:0e:12:3c:9c:53:8a:55:23:77:10:d4:
                    8e:57
                Exponent: 65537 (0x10001)
        X509v3 extensions:
            X509v3 Subject Key Identifier: 
                BD:E9:AC:42:B3:4E:68:02:86:A8:DF:A4:8C:0A:3A:24:F9:1B:5C:49
            X509v3 Subject Alternative Name: 
                IP Address:127.0.0.1, DNS:localhost
    Signature Algorithm: sha256WithRSAEncryption
         5e:69:a2:7e:67:d1:ba:76:22:51:8a:1e:93:8d:52:f9:f2:3d:
         ...
         1c:8d:bf:84:5c:19:7e:b9:cd:c4:e6:f2:70:73:44:6f:45:e0:
         05:bd:bd:55
-----BEGIN CERTIFICATE-----
MIIDmTCCAoGgAwIBAgIEW6BgTDANBgkqhkiG9w0BAQsFADBvMRAwDgYDVQQGEwdV
...
NZB8ur+orW9XXVxOQW5ZNcwibn40gzo1T3yKZE3x8tbCHd9Hv4SoHI2/hFwZfrnN
xObycHNEb0XgBb29VQ==
-----END CERTIFICATE-----
```

Verification: OK

```
$ openssl s_client -connect localhost:8444 -verifyCAfile ./src/main/resources/keystore/server.crt
CONNECTED(00000003)
Can't use SSL_get_servername
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
Verification: OK
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
    Session-ID: 94120A620EE539467EBCBBD8C3948F2565D4897628CB67226D12C988F751455A
    Session-ID-ctx:
    Master-Key: D04B8065A454EFCF14E3567B1AFA682726FFD5CB4E2355EA917C636CF05E580B8563BE599C4F19DD05F06A02C03F8D02
    PSK identity: None
    PSK identity hint: None
    SRP username: None
    Start Time: 1681113001
    Timeout   : 7200 (sec)
    Verify return code: 0 (ok)
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

## Migrate to CA Signed Cert

### Generate Certs
```
openssl genrsa -aes256 -out ca.key 4096
<password>
openssl req -subj "/C=JP/ST=Tokyo/CN=daichi703n CA" -new -x509 -days 36500 -key ca.key -sha256 -out ca.crt
<password>
openssl genrsa -out server.key 4096
openssl req -subj "/C=JP/ST=Tokyo/OU=daichi703n/CN=localhost" -sha256 -new -key server.key -out server.csr
echo subjectAltName = DNS:localhost,IP:127.0.0.1 > extfile.cnf
echo extendedKeyUsage = serverAuth >> extfile.cnf
openssl x509 -days 36500 -req -in server.csr -CA ./ca.crt -CAkey ./ca.key -CAcreateserial -extfile extfile.cnf -out server.crt
<password>
```

```
openssl pkcs12 -export -inkey ca.key -in ca.crt -out ca.p12
<ca.key password>
<p12 password>
```

```
openssl pkcs12 -export -inkey server.key -in server.crt -out server.p12
<p12 password>
```

```
cp ca.p12 src/main/resources/keystore/daichi703n-ca.p12
cp server.p12 src/main/resources/keystore/daichi703n-server.p12
```

### After Replace Certs
```
$ openssl s_client -connect localhost:8444 -verifyCAfile ca.crt
CONNECTED(00000003)
Can't use SSL_get_servername
depth=1 C = JP, ST = Tokyo, CN = daichi703n CA
verify return:1
depth=0 C = JP, ST = Tokyo, OU = daichi703n, CN = localhost
verify return:1
---
Certificate chain
 0 s:C = JP, ST = Tokyo, OU = daichi703n, CN = localhost
   i:C = JP, ST = Tokyo, CN = daichi703n CA
---
Server certificate
-----BEGIN CERTIFICATE-----
MIIFPjCCAyagAwIBAgIUKinWGgGrBwYuyNDOl4i+ZfRDsP0wDQYJKoZIhvcNAQEL
...
wP4aGn0MXzMN1T6+R1rAyEA3CUZkPUVpaqnd1QWBj1ULXcJ4lB4qTSEmRFAFZegJ
6A0=
-----END CERTIFICATE-----
subject=C = JP, ST = Tokyo, OU = daichi703n, CN = localhost

issuer=C = JP, ST = Tokyo, CN = daichi703n CA

---
No client certificate CA names sent
Peer signing digest: SHA256
Peer signature type: RSA-PSS
Server Temp Key: X25519, 253 bits
---
SSL handshake has read 2072 bytes and written 382 bytes
Verification: OK
---
New, TLSv1.2, Cipher is ECDHE-RSA-AES256-GCM-SHA384
Server public key is 4096 bit
Secure Renegotiation IS supported
Compression: NONE
Expansion: NONE
No ALPN negotiated
SSL-Session:
    Protocol  : TLSv1.2
    Cipher    : ECDHE-RSA-AES256-GCM-SHA384
    Session-ID: 3C52C05E47CC7B6D61E5FE7FF0CFDC58978F69F93BBFDBCA3642F4177536E3F9
    Session-ID-ctx:
    Master-Key: 3D526B7A34D1A21E3227530034666DFE2918EE813FC470E6EA8461579C8BD4555D6F87144801080DBCD308AE28B8C5B0
    PSK identity: None
    PSK identity hint: None
    SRP username: None
    Start Time: 1681129383
    Timeout   : 7200 (sec)
    Verify return code: 0 (ok)
    Extended master secret: yes
---
hi
hi
.
bye
closed
```

## mTLS

### Generate Client Cert
```
openssl genrsa -out client.key 4096
openssl req -subj "/C=JP/ST=Tokyo/OU=daichi703n/CN=client" -new -key client.key -out client.csr 
echo basicConstraints = CA:FALSE > extfile.cnf
echo nsCertType = client, email >> extfile.cnf
echo subjectKeyIdentifier = hash >> extfile.cnf
echo authorityKeyIdentifier = keyid,issuer >> extfile.cnf
echo keyUsage = critical, nonRepudiation, digitalSignature, keyEncipherment >> extfile.cnf
echo extendedKeyUsage = clientAuth, emailProtection >> extfile.cnf
openssl x509 -days 3650 -req -in client.csr -CA ./ca.crt -CAkey ./ca.key -CAcreateserial -extfile extfile.cnf -out client.crt
```

```
$ openssl x509 -text -in ./client.crt 
Certificate:
    Data:
        Version: 3 (0x2)
        Serial Number:
            2a:29:d6:1a:01:ab:07:06:2e:c8:d0:ce:97:88:be:65:f4:43:b0:ff
        Signature Algorithm: sha256WithRSAEncryption
        Issuer: C = JP, ST = Tokyo, CN = daichi703n CA
        Validity
            Not Before: Apr 10 13:12:06 2023 GMT
            Not After : Apr  7 13:12:06 2033 GMT
        Subject: C = JP, ST = Tokyo, OU = daichi703n, CN = client
        Subject Public Key Info:
            Public Key Algorithm: rsaEncryption
                RSA Public-Key: (4096 bit)
                Modulus:
                    00:bf:a8:a0:9f:43:eb:6d:cc:e0:80:2a:4c:82:7f:
                    ...
                    0c:6c:c0:95:eb:5a:f1:24:32:1d:48:d6:d8:52:df:
                    ef:8b:d1
                Exponent: 65537 (0x10001)
        X509v3 extensions:
            X509v3 Basic Constraints: 
                CA:FALSE
            Netscape Cert Type: 
                SSL Client, S/MIME
            X509v3 Subject Key Identifier: 
                3C:0E:A9:1F:C4:4A:9A:65:35:C0:06:FA:F3:D5:6F:1C:EC:F4:08:78
            X509v3 Authority Key Identifier: 
                keyid:4F:60:29:30:4C:71:08:13:72:D1:20:B5:BF:7B:A4:40:07:FE:70:4F

            X509v3 Key Usage: critical
                Digital Signature, Non Repudiation, Key Encipherment
            X509v3 Extended Key Usage: 
                TLS Web Client Authentication, E-mail Protection
    Signature Algorithm: sha256WithRSAEncryption
         43:d7:19:c8:f7:c3:c1:86:c0:fa:65:6f:d2:53:f7:35:06:7b:
         ...
         43:3e:b9:eb:8a:5f:74:63
-----BEGIN CERTIFICATE-----
MIIFlzCCA3+gAwIBAgIUKinWGgGrBwYuyNDOl4i+ZfRDsP8wDQYJKoZIhvcNAQEL
...
IXwLVMTXyUITE8JN159aganAkEoRRinj7cJaMKILQ5HyqhVDPrnril90Yw==
-----END CERTIFICATE-----
```

```
openssl pkcs12 -export -inkey client.key -in client.crt -out client.p12
<p12 password>
```

```
cp client.p12 src/main/resources/keystore/daichi703n-client.p12
```

### Client Cert Required
```
$ openssl s_client -connect localhost:8333 -verifyCAfile ./cert/ca.crt
CONNECTED(00000003)
Can't use SSL_get_servername
depth=1 C = JP, ST = Tokyo, CN = daichi703n CA
verify return:1
depth=0 C = JP, ST = Tokyo, OU = daichi703n, CN = localhost
verify return:1
139927674361152:error:14094412:SSL routines:ssl3_read_bytes:sslv3 alert bad certificate:../ssl/record/rec_layer_s3.c:1543:SSL alert number 42
---
Certificate chain
 0 s:C = JP, ST = Tokyo, OU = daichi703n, CN = localhost
   i:C = JP, ST = Tokyo, CN = daichi703n CA
---
Server certificate
-----BEGIN CERTIFICATE-----
MIIFPjCCAyagAwIBAgIUKinWGgGrBwYuyNDOl4i+ZfRDsP0wDQYJKoZIhvcNAQEL
...
wP4aGn0MXzMN1T6+R1rAyEA3CUZkPUVpaqnd1QWBj1ULXcJ4lB4qTSEmRFAFZegJ
6A0=
-----END CERTIFICATE-----
subject=C = JP, ST = Tokyo, OU = daichi703n, CN = localhost

issuer=C = JP, ST = Tokyo, CN = daichi703n CA

---
Acceptable client certificate CA names
C = JP, ST = Tokyo, CN = daichi703n CA
Client Certificate Types: ECDSA sign, RSA sign, DSA sign
Requested Signature Algorithms: ECDSA+SHA256:ECDSA+SHA384:ECDSA+SHA512:RSA-PSS+SHA256:RSA-PSS+SHA384:RSA-PSS+SHA512:RSA-PSS+SHA256:RSA-PSS+SHA384:RSA-PSS+SHA512:RSA+SHA256:RSA+SHA384:RSA+SHA512:DSA+SHA256:ECDSA+SHA224:RSA+SHA224:DSA+SHA224:ECDSA+SHA1:RSA+SHA1:DSA+SHA1
Shared Requested Signature Algorithms: ECDSA+SHA256:ECDSA+SHA384:ECDSA+SHA512:RSA-PSS+SHA256:RSA-PSS+SHA384:RSA-PSS+SHA512:RSA-PSS+SHA256:RSA-PSS+SHA384:RSA-PSS+SHA512:RSA+SHA256:RSA+SHA384:RSA+SHA512:DSA+SHA256:ECDSA+SHA224:RSA+SHA224:DSA+SHA224:ECDSA+SHA1:RSA+SHA1:DSA+SHA1
Peer signing digest: SHA256
Peer signature type: RSA-PSS
Server Temp Key: X25519, 253 bits
---
SSL handshake has read 2140 bytes and written 394 bytes
Verification: OK
---
New, TLSv1.2, Cipher is ECDHE-RSA-AES256-GCM-SHA384
Server public key is 4096 bit
Secure Renegotiation IS supported
Compression: NONE
Expansion: NONE
No ALPN negotiated
SSL-Session:
    Protocol  : TLSv1.2
    Cipher    : ECDHE-RSA-AES256-GCM-SHA384
    Session-ID: 12ECB746FD4C2459D4E41BD1287222B1231EE769C545438589BF3DD0FC87C984
    Session-ID-ctx:
    Master-Key: 26233C70BDD345AF3BDC029B43B412F5ECC9D2C79B85A0CCB453F52F34DE2CDC031692A4BED610C60A75FE574BD64926
    PSK identity: None
    PSK identity hint: None
    SRP username: None
    Start Time: 1681132839
    Timeout   : 7200 (sec)
    Verify return code: 0 (ok)
    Extended master secret: yes
---
```

```

INFO 20654 --- [           main] com.daichi703n.echo.EchoApplication      : Started EchoApplication in 8.905 seconds (JVM running for 9.881)
INFO 20654 --- [asyncExecutor-2] com.daichi703n.echo.socket.MTlsSocket    : Start mTLS Socket
INFO 20654 --- [asyncExecutor-3] com.daichi703n.echo.socket.TlsSocket     : Start TLS Socket
INFO 20654 --- [asyncExecutor-1] com.daichi703n.echo.socket.EchoSocket    : Start Echo Socket
INFO 20654 --- [asyncExecutor-1] com.daichi703n.echo.socket.EchoSocket    : Start listening port 8888
INFO 20654 --- [asyncExecutor-2] com.daichi703n.echo.socket.MTlsSocket    : Start listening port 8333
INFO 20654 --- [asyncExecutor-3] com.daichi703n.echo.socket.TlsSocket     : Start listening port 8444
INFO 20654 --- [on(1)-127.0.0.1] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring DispatcherServlet 'dispatcherServlet'
INFO 20654 --- [on(1)-127.0.0.1] o.s.web.servlet.DispatcherServlet        : Initializing Servlet 'dispatcherServlet'
INFO 20654 --- [on(1)-127.0.0.1] o.s.web.servlet.DispatcherServlet        : Completed initialization in 3 ms
INFO 20654 --- [       Thread-1] com.daichi703n.echo.socket.MTlsSocket    : Accepted connection from 0:0:0:0:0:0:0:1 on port 58201
javax.net.ssl.SSLHandshakeException: Empty server certificate chain
        at java.base/sun.security.ssl.Alert.createSSLException(Alert.java:131)
        at java.base/sun.security.ssl.Alert.createSSLException(Alert.java:117)
        at java.base/sun.security.ssl.TransportContext.fatal(TransportContext.java:336)
        at java.base/sun.security.ssl.TransportContext.fatal(TransportContext.java:292)
        at java.base/sun.security.ssl.TransportContext.fatal(TransportContext.java:283)
        at java.base/sun.security.ssl.CertificateMessage$T12CertificateConsumer.onCertificate(CertificateMessage.java:390)
        at java.base/sun.security.ssl.CertificateMessage$T12CertificateConsumer.consume(CertificateMessage.java:375)
        at java.base/sun.security.ssl.SSLHandshake.consume(SSLHandshake.java:392)
        at java.base/sun.security.ssl.HandshakeContext.dispatch(HandshakeContext.java:443)
        at java.base/sun.security.ssl.HandshakeContext.dispatch(HandshakeContext.java:421)
        at java.base/sun.security.ssl.TransportContext.dispatch(TransportContext.java:182)
        at java.base/sun.security.ssl.SSLTransport.decode(SSLTransport.java:171)
        at java.base/sun.security.ssl.SSLSocketImpl.decode(SSLSocketImpl.java:1418)
        at java.base/sun.security.ssl.SSLSocketImpl.readHandshakeRecord(SSLSocketImpl.java:1324)
        at java.base/sun.security.ssl.SSLSocketImpl.startHandshake(SSLSocketImpl.java:440)
        at java.base/sun.security.ssl.SSLSocketImpl.ensureNegotiated(SSLSocketImpl.java:829)
        at java.base/sun.security.ssl.SSLSocketImpl$AppInputStream.read(SSLSocketImpl.java:920)
        at java.base/sun.nio.cs.StreamDecoder.readBytes(StreamDecoder.java:284)
        at java.base/sun.nio.cs.StreamDecoder.implRead(StreamDecoder.java:326)
        at java.base/sun.nio.cs.StreamDecoder.read(StreamDecoder.java:178)
        at java.base/java.io.InputStreamReader.read(InputStreamReader.java:181)
        at java.base/java.io.BufferedReader.fill(BufferedReader.java:161)
        at java.base/java.io.BufferedReader.readLine(BufferedReader.java:326)
        at java.base/java.io.BufferedReader.readLine(BufferedReader.java:392)
        at com.daichi703n.echo.socket.MTlsSocket$EchoClientHandler.run(MTlsSocket.java:105)
```

### with Cliect Cert
```
$ openssl s_client -connect localhost:8333 -verifyCAfile ./cert/ca.crt -cert cert/client.crt -key cert/client.key
CONNECTED(00000003)
Can't use SSL_get_servername
depth=1 C = JP, ST = Tokyo, CN = daichi703n CA
verify return:1
depth=0 C = JP, ST = Tokyo, OU = daichi703n, CN = localhost
verify return:1
---
Certificate chain
 0 s:C = JP, ST = Tokyo, OU = daichi703n, CN = localhost
   i:C = JP, ST = Tokyo, CN = daichi703n CA
---
Server certificate
-----BEGIN CERTIFICATE-----
MIIFPjCCAyagAwIBAgIUKinWGgGrBwYuyNDOl4i+ZfRDsP0wDQYJKoZIhvcNAQEL
...
wP4aGn0MXzMN1T6+R1rAyEA3CUZkPUVpaqnd1QWBj1ULXcJ4lB4qTSEmRFAFZegJ
6A0=
-----END CERTIFICATE-----
subject=C = JP, ST = Tokyo, OU = daichi703n, CN = localhost

issuer=C = JP, ST = Tokyo, CN = daichi703n CA

---
Acceptable client certificate CA names
C = JP, ST = Tokyo, CN = daichi703n CA
Client Certificate Types: ECDSA sign, RSA sign, DSA sign
Requested Signature Algorithms: ECDSA+SHA256:ECDSA+SHA384:ECDSA+SHA512:RSA-PSS+SHA256:RSA-PSS+SHA384:RSA-PSS+SHA512:RSA-PSS+SHA256:RSA-PSS+SHA384:RSA-PSS+SHA512:RSA+SHA256:RSA+SHA384:RSA+SHA512:DSA+SHA256:ECDSA+SHA224:RSA+SHA224:DSA+SHA224:ECDSA+SHA1:RSA+SHA1:DSA+SHA1
Shared Requested Signature Algorithms: ECDSA+SHA256:ECDSA+SHA384:ECDSA+SHA512:RSA-PSS+SHA256:RSA-PSS+SHA384:RSA-PSS+SHA512:RSA-PSS+SHA256:RSA-PSS+SHA384:RSA-PSS+SHA512:RSA+SHA256:RSA+SHA384:RSA+SHA512:DSA+SHA256:ECDSA+SHA224:RSA+SHA224:DSA+SHA224:ECDSA+SHA1:RSA+SHA1:DSA+SHA1
Peer signing digest: SHA256
Peer signature type: RSA-PSS
Server Temp Key: X25519, 253 bits
---
SSL handshake has read 2184 bytes and written 2357 bytes
Verification: OK
---
New, TLSv1.2, Cipher is ECDHE-RSA-AES256-GCM-SHA384
Server public key is 4096 bit
Secure Renegotiation IS supported
Compression: NONE
Expansion: NONE
No ALPN negotiated
SSL-Session:
    Protocol  : TLSv1.2
    Cipher    : ECDHE-RSA-AES256-GCM-SHA384
    Session-ID: BE8AAEF6F00C99617C47D9DE5BB6E4CFB709697358F3F7E512CEDCD069406E88
    Session-ID-ctx:
    Master-Key: 24724C11C912B191661527CE1E3F54149A164A4711563C77463B6EA482FC65AE73F46C4626A430C132D67108E6CCE672
    PSK identity: None
    PSK identity hint: None
    SRP username: None
    Start Time: 1681133066
    Timeout   : 7200 (sec)
    Verify return code: 0 (ok)
    Extended master secret: yes
---
Hello
Hello
World
World
!
!
.
bye
closed
```

```
INFO 20654 --- [       Thread-2] com.daichi703n.echo.socket.MTlsSocket    : Accepted connection from 0:0:0:0:0:0:0:1 on port 58419
INFO 20654 --- [       Thread-2] com.daichi703n.echo.socket.MTlsSocket    : Received: Hello
INFO 20654 --- [       Thread-2] com.daichi703n.echo.socket.MTlsSocket    : Received: World
INFO 20654 --- [       Thread-2] com.daichi703n.echo.socket.MTlsSocket    : Received: !
INFO 20654 --- [       Thread-2] com.daichi703n.echo.socket.MTlsSocket    : Received: .
INFO 20654 --- [       Thread-2] com.daichi703n.echo.socket.MTlsSocket    : Closing connection
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
- [【Java】SSL通信を実装する](https://ohs30359.hatenablog.com/entry/2016/07/16/174029)
