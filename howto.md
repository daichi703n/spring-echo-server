# How to use this lab

## アプリケーションの構成
以下のエンドポイントで証明書関連の挙動を確認することができる。

| ポート  | プロトコル | エンドポイント | 概要                                        |
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

## 証明書を作成する
![java-keytool-9b5915d8fc17.png](https://programacho.blob.core.windows.net/images/java-keytool-9b5915d8fc17.png)

事前に以下のディレクトリに移動してください。
```shell
cd src/main/resources/keystore
```

### ①CA証明書を作成する
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

姓名は何ですか。
  [Unknown]:  daichi703n
...
```

### ②CA証明書をエクスポートする
```shell
keytool -exportcert \
  -alias ca \
  -file ca.crt \
  -keystore ca.p12 \
  -storetype pkcs12 \
  -storepass password \
  -rfc
```

### オプション：CA証明書を確認する
```shell
openssl x509 -in ca.crt -text -noout
```

### ③CA証明書の秘密鍵をエクスポートする
```shell
openssl pkcs12 \
  -in ca.p12 \
  -out ca.key \
  -nocerts \
  -nodes \
  -passin pass:password
```

### ④サーバー証明書を作成する
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

姓名は何ですか。
  [Unknown]:  daichi703n
...
```

### ⑤サーバー証明書の署名リクエストを作成する
```shell
keytool -certreq \
  -alias server \
  -file server.csr \
  -keystore server.p12 \
  -storetype pkcs12 \
  -storepass password
```

### ⑥CA証明書でサーバー証明書を署名する
事前に以下のファイルを`cert.cnf`という名称で作成しておく。
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

### ⑦署名したサーバー証明書をインポートする
事前にサーバー証明書をインポートするにあたって事前に擬似CA証明書を信頼するよう設定する。

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

### オプション：サーバー証明書を検証する
```shell
openssl verify -CAfile ca.crt server.crt

server.crt: OK
```

### クライアント証明書を作成する
サーバー証明書の作成と同様の手順を実施する。

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

### ダミー証明書を作成する
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

### ファイル名を変更する
```shell
mv ca.p12 daichi703n-ca.p12
mv server.p12 daichi703n-server.p12
mv client.p12 daichi703n-client.p12
mv dummy.p12 daichi703n-dummy.p12
```
