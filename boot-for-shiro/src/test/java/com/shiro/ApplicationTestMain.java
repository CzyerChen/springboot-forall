package com.shiro;

import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.credential.PasswordService;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.codec.Hex;
import org.apache.shiro.crypto.AesCipherService;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.*;
import org.apache.shiro.crypto.hash.format.Base64Format;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.util.SimpleByteSource;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.security.Key;

/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -04 - 15 9:20
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ShiroSecurityApplication.class)
@WebAppConfiguration
public class ApplicationTestMain {

    @Test
    public void test1(){
        String  str = "admin";
        String base64Str = Base64.encodeToString(str.getBytes());
        String decodeToString = Base64.decodeToString(base64Str);
        Assert.assertEquals(str,decodeToString);
    }

    @Test
    public void test2(){
        String str = "admin";
        String salt = "test";
        String str2 = new Md5Hash(str,salt).toString(); // 可以指定散列次数，new Md5Hash(str, salt, 2).toString()
        String strBase64 =  new Md5Hash(str,salt).toBase64();

        String shaStr2 = new Sha256Hash(str,salt).toString();//还有Sha1 Sha512

        String sha1Str = new SimpleHash("SHA-1", str, salt).toString();//统一指定散列算法，内部使用了Java 的MessageDigest实现
    }

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



    @Test
    public  void test6(){
        String user = "admin";
        String pass = "test";
        String salt = new SecureRandomNumberGenerator().nextBytes().toHex();
        SimpleHash simpleHash = new SimpleHash("md5", pass, user + salt, 2);
        String passEncode = simpleHash.toString();
        System.out.println(passEncode);

        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(user, pass, "aa");

        CredentialsMatcher matcher = new HashedCredentialsMatcher();
        ((HashedCredentialsMatcher) matcher).setHashAlgorithmName("md5");
        ((HashedCredentialsMatcher) matcher).setHashIterations(2);
        ((HashedCredentialsMatcher) matcher).setStoredCredentialsHexEncoded(true);

        simpleAuthenticationInfo.setCredentials(ByteSource.Util.bytes(user+salt));
    }



}
