1.生成keystore文件
```text
keytool -genkey -keystore "E:\localhost.keystore" -alias localhost -keyalg RSA
```
2.配置tomcat的server.xml
```text
<Connector port="8443" protocol="HTTP/1.1" SSLEnabled="true"
maxThreads="150" scheme="https" secure="true"
clientAuth="false" sslProtocol="TLS"
keystoreFile="D:\localhost.keystore" keystorePass="123456"/>
```
keystorePass为设置的密码
