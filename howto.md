# How to use this lab

## �A�v���P�[�V�����̍\��
�ȉ��̃G���h�|�C���g�ŏؖ����֘A�̋������m�F���邱�Ƃ��ł���B

| �|�[�g  | �v���g�R�� | �G���h�|�C���g | �T�v                                        |
|------|-------|---------|-------------------------------------------|
| 8888 | TCP   | -       | Simple TCP Server                         |
| 8444 | TCP   | -       | TLS Server                                |
| 8333 | TCP   | -       | mTLS Server                               |
| 8080 | HTTP  | /echo   | Using `EchoClient`                        |
| 8080 | HTTP  | /tls    | Using `TLSClient`                         |
| 8080 | HTTP  | /mtls   | Using `MTLSClient`                        |
| 8080 | HTTP  | /mtls   | Using `MTLSClient` with dummy certificate |

### Simple TCP Server
```shell
telnet localhost 8888
```

### TLS Server
```shell
openssl s_client -connect localhost:8444 -CAfile src/main/resources/keystore/ca.crt
```

### mTLS Server
```shell
openssl s_client -connect localhost:8333 \
  -CAfile src/main/resources/keystore/ca.crt \
  -cert src/main/resources/keystore/client.crt \
  -key src/main/resources/keystore/client.key
```

### Using `EchoClient`
```shell
curl -XPOST localhost:8080/echo -d 'Hello World!'
```

### Using `TLSClient`
```shell
curl -XPOST localhost:8080/tls -d 'Hello World!'
```

### Using `MTLSClient`
```shell
curl -XPOST localhost:8080/mtls -d 'Hello World!'
```

### Using `EchoClient` with dummy certificate
```shell
curl -XPOST localhost:8080/mtls -H 'Dummy-Certificate: true' -d 'Hello World!'

...
java.security.SignatureException: Signature does not match.
```

## �ؖ������쐬����
![java-keytool-9b5915d8fc17.png](https://programacho.blob.core.windows.net/images/java-keytool-9b5915d8fc17.png)

���O�Ɉȉ��̃f�B���N�g���Ɉړ����Ă��������B
```shell
cd src/main/resources/keystore
```

### �@CA�ؖ������쐬����
```shell
keytool -genkeypair \
  -alias ca \
  -keyalg RSA \
  -keysize 2048 \
  -validity 3650 \
  -keystore ca.p12 \
  -storetype pkcs12 \
  -storepass password \
  -keypass password

�����͉��ł����B
  [Unknown]:  daichi703n
...
```

### �ACA�ؖ������G�N�X�|�[�g����
```shell
keytool -exportcert \
  -alias ca \
  -file ca.crt \
  -keystore ca.p12 \
  -storetype pkcs12 \
  -storepass password \
  -rfc
```

### �I�v�V�����FCA�ؖ������m�F����
```shell
openssl x509 -in ca.crt -text -noout
```

### �BCA�ؖ����̔閧�����G�N�X�|�[�g����
```shell
openssl pkcs12 \
  -in ca.p12 \
  -out ca.key \
  -nocerts \
  -nodes \
  -passin pass:password
```

### �C�T�[�o�[�ؖ������쐬����
```shell
keytool -genkeypair \
  -alias server \
  -keyalg RSA \
  -keysize 2048 \
  -validity 3650 \
  -keystore server.p12 \
  -storetype pkcs12 \
  -storepass password \
  -keypass password

�����͉��ł����B
  [Unknown]:  daichi703n
...
```

### �D�T�[�o�[�ؖ����̏������N�G�X�g���쐬����
```shell
keytool -certreq \
  -alias server \
  -file server.csr \
  -keystore server.p12 \
  -storetype pkcs12 \
  -storepass password
```

### �ECA�ؖ����ŃT�[�o�[�ؖ�������������
���O�Ɉȉ��̃t�@�C����`cert.cnf`�Ƃ������̂ō쐬���Ă����B
```conf
[ v3_ca ]
basicConstraints = CA:FALSE
keyUsage = digitalSignature, keyEncipherment
```

```shell
openssl x509 -req \
  -in server.csr \
  -CA ca.crt \
  -CAkey ca.key \
  -out server.crt \
  -CAcreateserial \
  -days 3650 \
  -sha256 \
  -extfile cert.cnf \
  -extensions v3_ca
```

### �F���������T�[�o�[�ؖ������C���|�[�g����
���O�ɃT�[�o�[�ؖ������C���|�[�g����ɂ������Ď��O�ɋ[��CA�ؖ�����M������悤�ݒ肷��B

```shell
keytool -importcert \
  -trustcacerts \
  -alias ca \
  -file ca.crt \
  -keystore server.p12 \
  -storetype pkcs12 \
  -storepass password
```

```shell
keytool -importcert \
  -alias server \
  -file server.crt \
  -keystore server.p12 \
  -storetype pkcs12 \
  -storepass password
```

### �I�v�V�����F�T�[�o�[�ؖ��������؂���
```shell
openssl verify -CAfile ca.crt server.crt

server.crt: OK
```

### �N���C�A���g�ؖ������쐬����
�T�[�o�[�ؖ����̍쐬�Ɠ��l�̎菇�����{����B

```shell
keytool -genkeypair \
  -alias client \
  -keyalg RSA \
  -keysize 2048 \
  -validity 3650 \
  -keystore client.p12 \
  -storetype pkcs12 \
  -storepass password \
  -keypass password

openssl pkcs12 \
  -in client.p12 \
  -out client.key \
  -nocerts \
  -nodes \
  -passin pass:password

keytool -certreq \
  -alias client \
  -file client.csr \
  -keystore client.p12 \
  -storetype pkcs12 \
  -storepass password

openssl x509 -req \
  -in client.csr \
  -CA ca.crt \
  -CAkey ca.key \
  -out client.crt \
  -CAcreateserial \
  -days 3650 \
  -sha256 \
  -extfile cert.cnf \
  -extensions v3_ca

keytool -importcert \
  -trustcacerts \
  -alias ca \
  -file ca.crt \
  -keystore client.p12 \
  -storetype pkcs12 \
  -storepass password

keytool -importcert \
  -alias client \
  -file client.crt \
  -keystore client.p12 \
  -storetype pkcs12 \
  -storepass password
```

### �_�~�[�ؖ������쐬����
```shell
keytool -genkeypair \
  -alias ca \
  -keyalg RSA \
  -keysize 2048 \
  -validity 3650 \
  -keystore dummy.p12 \
  -storetype pkcs12 \
  -storepass password \
  -keypass password
```

### �t�@�C������ύX����
```shell
mv ca.p12 daichi703n-ca.p12
mv server.p12 daichi703n-server.p12
mv client.p12 daichi703n-client.p12
mv dummy.p12 daichi703n-dummy.p12
```
