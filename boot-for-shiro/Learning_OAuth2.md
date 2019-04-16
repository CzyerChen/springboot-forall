```text
目前很多开放平台如新浪微博开放平台都在使用提供开放API接口供开发者使用，随之带
来了第三方应用要到开放平台进行授权的问题，OAuth 就是干这个的，OAuth2 是OAuth
协议的下一个版本，相比OAuth1，OAuth2整个授权流程更简单安全了，但不兼容OAuth1
```

### 什么是OAuth2
- OAuth2是一种协议规范，有很多都实现了这种规范，使用Apache Oltu，其之前的名字叫Apache Amber ，是Java 版的参考实现。
- OAuth2的协议流程
```text
     
     客户端       1.请求授权          
              ------------------->  资源拥有着
                 2.授权许可
              <-------------------  资源拥有着
                 3.授权许可
              ------------------->  验证服务器
                 4. 访问令牌         
               <-----------------   验证服务器
                 5. 访问令牌        
               ----------------->   资源服务器
                 6. 受保护的资源
               <-----------------   资源服务器
           
```
1、首先通过如http://localhost:8080/chapter17-server/authorize?client_id=c1ebe466-1cdc-4bd3-ab69-77c3561b9dee&response_type=code&redirect_uri=http://localhost:9080/chapter17-client/oauth2-login访问授权页面；

2、该控制器首先检查clientId 是否正确；如果错误将返回相应的错误信息；

3、然后判断用户是否登录了，如果没有登录首先到登录页面登录；

4、登录成功后生成相应的auth code 即授权码，然后重定向到客户端地址，如http://localhost:9080/chapter17-client/oauth2-login?code=52b1832f5dff68122f4f00ae995da0ed；在重定向到的地址中会带上code 参数（授权码），接着客户端可以根据授权码去换取accesstoken。


### 数据字典
```text
user:
id
username
password
salt


client:
id
client_name
client_id
client_secret

```

### 服务端
```text
<dependency>
    <groupId>org.apache.oltu.oauth2</groupId>
    <artifactId>org.apache.oltu.oauth2.authzserver</artifactId>
    <version>0.31</version>
</dependency>
<dependency>
    <groupId>org.apache.oltu.oauth2</groupId>
    <artifactId>org.apache.oltu.oauth2.resourceserver</artifactId>
    <version>0.31</version>
</dependency>
```
- AuthController :授权控制器
- AccessTokenController ： 令牌访问控制器
- UserInfoController ： 资源控制器

### 客户端
```text
<dependency>
    <groupId>org.apache.oltu.oauth2</groupId>
    <artifactId>org.apache.oltu.oauth2.client</artifactId>
    <version>0.31</version>
</dependency>
```
- 自定义一个token
```text
public class OAuth2Token implements AuthenticationToken {
private String authCode;
private String principal;
public OAuth2Token(String authCode) {
this.authCode = authCode;
}
//.....
}
```
- OAuth2AuthenticationFilter
```text
该filter的作用类似于FormAuthenticationFilter用于oauth2 客户端的身份验证控制；如果当

前用户还没有身份验证，首先会判断url 中是否有code（服务端返回的auth code），如果

没有则重定向到服务端进行登录并授权， 然后返回auth code ； 接着

OAuth2AuthenticationFilter 会用auth code 创建OAuth2Token，然后提交给Subject.login 进

行登录；接着OAuth2Realm会根据OAuth2Token进行相应的登录逻辑
```
