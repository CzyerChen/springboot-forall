> Shiro 提供了base64和16进制字符串编码/解码的API支持，方便一些编码解码操作

### Base64 支持
```text
 @Test
    public void test1(){
        String  str = "admin";
        String base64Str = Base64.encodeToString(str.getBytes());
        String decodeToString = Base64.decodeToString(base64Str);
        Assert.assertEquals(str,decodeToString);
    }
```
- 类CodecSupport，提供了toBytes(str, "utf-8") / toString(bytes,"utf-8")用于在byte 数组/String 之间转换

### 散列算法
- 散列算法一般用于生成数据的摘要信息，是一种不可逆的算法，一般适合存储密码之类的数据，常见的散列算法如MD5、SHA等。一般进行散列时最好提供一个salt（盐）
- 对于一些密码程度比较差的账号密码，即使密码不可逆，由于加密方式常用的就这么多，很容易被猜到，，此时我们可以加一些只有系统知道的干扰数据，如用户名和ID（即盐）；这样散列的对象是“密码+用户名+ID”，这样生成的散列值相对来说更难破解
- 散列次数可以指定，散列算法支持MD5 SHA256 SHA512 SHA1 等
```text
@Test
    public void test2(){
        String str = "admin";
        String salt = "test";
        String str2 = new Md5Hash(str,salt).toString(); // 可以指定散列次数，new Md5Hash(str, salt, 2).toString()
        String strBase64 =  new Md5Hash(str,salt).toBase64();

        String shaStr2 = new Sha256Hash(str,salt).toString();//还有Sha1 Sha512

        String sha1Str = new SimpleHash("SHA-1", str, salt).toString();//统一指定散列算法，内部使用了Java 的MessageDigest实现
    }
```
- 灵活的加密写法
```text
  @Test
    public void test3(){
        DefaultHashService hashService= new DefaultHashService();//默认SHA 512
        hashService.setHashAlgorithmName("SHA-512");
        hashService.setPrivateSalt(new SimpleByteSource("test123"));//默认无
        hashService.setGeneratePublicSalt(true);//默认false
        hashService.setRandomNumberGenerator(new SecureRandomNumberGenerator());//SecureRandomNumberGenerator生成一个随机书
       /* SecureRandomNumberGenerator generator = new SecureRandomNumberGenerator();
        generator.setSeed("123".getBytes());
        String s = generator.nextBytes().toHex();*/
        hashService.setHashIterations(1);//迭代次数

        HashRequest hashRequest = new HashRequest.Builder()
                .setAlgorithmName("MD5")
                .setSource(ByteSource.Util.bytes("admin"))
                .setSalt(ByteSource.Util.bytes("test"))
                .setIterations(2)
                .build();

        String toHex = hashService.computeHash(hashRequest).toHex();
    }
```

### 对称加密的支持
- 暂时未支持非对称加密，对称加密有AES Blowfish
```text
 @Test
    public  void test4(){
        AesCipherService aesCipherService = new AesCipherService();
        aesCipherService.setKeySize(128);
        Key key = aesCipherService.generateNewKey();
        String str = "admin";

        String s1 = aesCipherService.encrypt(str.getBytes(),key.getEncoded()).toString();
        String s2 = aesCipherService.encrypt(str.getBytes(),key.getEncoded()).toBase64();
        String s3 = aesCipherService.encrypt(str.getBytes(), key.getEncoded()).toHex();

        String text = new String(aesCipherService.decrypt(Hex.decode(s3), key.getEncoded()).getBytes());

        Assert.assertEquals(str,text);
    }
```

### 加密和验证服务支持
- Shiro 默认提供了PasswordService 实现DefaultPasswordService；CredentialsMatcher 实现PasswordMatcher及HashedCredentialsMatcher
- PasswordService相对是比较建议的加密方式
- passwordservice就是封装了hashservice hashformat hashfactory 算法的细节，通过默认或者外部设置的方式进行封装
```text

    @Test
    public  void test5(){
        PasswordService passwordService = new DefaultPasswordService();
       /* ((DefaultPasswordService) passwordService).setHashFormat();
        ((DefaultPasswordService) passwordService).setHashFormatFactory();
        ((DefaultPasswordService) passwordService).setHashService();*/
       //$shiro1$SHA-256$500000$fd41UDEy9opJMGsju4O5OA==$Q9ziXTdXWHun9J5h8GrLtUNbTa34vXsasTf7F7+Pyow=
        String password = passwordService.encryptPassword("admin");
        //存入数据库
        //UCh1w5ZJ8rRlqEczJarNG9eZcVUBEzDnHMnlCPP4nok=
        String admin1 = ((DefaultPasswordService) passwordService).hashPassword("admin").toBase64();
        //4ce826b0d1a5a9161d7ffe082f93667231ebe1c794d9b1d017661bd7e4798907
        String admin2 = ((DefaultPasswordService) passwordService).hashPassword("admin").toString();
        System.out.println(admin2);
    }
    
    
1.passwordService使用DefaultPasswordService，如果有必要也可以自定义；

2.hashService 定义散列密码使用的HashService，默认使用DefaultHashService（默认SHA-256 算法）；

3.hashFormat用于对散列出的值进行格式化，默认使用Shiro1CryptFormat，另外提供了Base64Format 和HexFormat，对于有salt 的密码请自定义实现ParsableHashFormat 然后把salt格式化到散列值中；

4.hashFormatFactory用于根据散列值得到散列的密码和salt；因为如果使用如SHA 算法，那么会生成一个salt，此salt需要保存到散列后的值中以便之后与传入的密码比较时使用；默认使用DefaultHashFormatFactory；

5.passwordMatcher使用PasswordMatcher，其是一个CredentialsMatcher实现；

6.将credentialsMatcher赋值给myRealm，myRealm间接继承了AuthenticatingRealm，其在调用getAuthenticationInfo 方法获取到AuthenticationInfo 信息后， 会使用credentialsMatcher 来验证凭据是否匹配，如果不匹配将抛出IncorrectCredentialsException异常。
```
- 密码重试次数限制
```text
如在1 个小时内密码最多重试5 次，如果尝试次数超过5 次就锁定1 小时，1 小时后可再
次重试，如果还是重试失败，可以锁定如1 天，以此类推，防止密码被暴力破解。我们通
过继承HashedCredentialsMatcher，且使用Ehcache记录重试次数和超时时间

public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
String username = (String)token.getPrincipal();
//retry count + 1
Element element = passwordRetryCache.get(username);
if(element == null) {
element = new Element(username , new AtomicInteger(0));
passwordRetryCache.put(element);
}
AtomicInteger retryCount = (AtomicInteger)element.getObjectValue();
if(retryCount.incrementAndGet() > 5) {
//if retry count > 5 throw
throw new ExcessiveAttemptsException();
}
boolean matches = super.doCredentialsMatch(token, info);
if(matches) {
//clear retry count
passwordRetryCache.remove(username);
}
return matches;
}

```


