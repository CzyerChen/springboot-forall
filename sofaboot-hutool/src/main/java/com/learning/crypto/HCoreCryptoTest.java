/**
 * Author:   claire
 * Date:    2021-02-09 - 17:09
 * Description: 加解密方法测试
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2021-02-09 - 17:09          V1.17.0          加解密方法测试
 */
package com.learning.crypto;

import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import cn.hutool.dfa.WordTree;

import javax.crypto.SecretKey;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * 功能简述
 * 〈加解密方法测试〉
 *
 * @author claire
 * @date 2021-02-09 - 17:09
 */
public class HCoreCryptoTest {

    public static void main(String[] args) throws UnsupportedEncodingException {
       /*
        * SecureUtil.aes
        * SecureUtil.des
        *
        * SecureUtil.md5
        * SecureUtil.sha1
        * SecureUtil.hmac
        * SecureUtil.hmacMd5
        * SecureUtil.hmacSha1
        *
        *
        * SecureUtil.rsa
        * SecureUtil.dsa
        *
        * SecureUtil.simpleUUID
        *
        * SecureUtil.generateKey 针对对称加密生成密钥
        * SecureUtil.generateKeyPair 生成密钥对（用于非对称加密）
        * SecureUtil.generateSignature 生成签名（用于非对称加密）
        *
        *
        * 对称加密-SymmetricCrypto
        *
        * 非对称加密-AsymmetricCrypto
        *
        * 摘要加密-Digester
        *
        * 消息认证码算法-HMac
        *
        * 签名和验证-Sign
        *
        * 国密算法工具-SmUtil
        */

       /*
        * 敏感词查找
        *
        */
//        WordTree tree = new WordTree();
//        tree.addWord("大");
//        tree.addWord("大土豆");
//        tree.addWord("土豆");
//        tree.addWord("刚出锅");
//        tree.addWord("出锅");
//
//        String text = "我有一颗大土豆，刚出锅的";

        // 匹配到【大】，就不再继续匹配了，因此【大土豆】不匹配
        // 匹配到【刚出锅】，就跳过这三个字了，因此【出锅】不匹配（由于刚首先被匹配，因此长的被匹配，最短匹配只针对第一个字相同选最短）
//        List<String> matchAll = tree.matchAll(text, -1, false, false);
//        matchAll.toString();

//        String testuser01 = new String(SecureUtil.aes().encrypt("testuser01"));
//        System.out.println(testuser01);

//生成一个随机秘钥
//        byte[] keyRandom = SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue()).getEncoded();
//        将秘钥转为Base64
//        String keyRandomEncode = Base64.encode(keyRandom);
//        System.out.println(keyRandomEncode);

        String key = "93E/L+71Mnt5h8mV5v/ihQ==";


        //将Base64编码的秘钥的格式进行解码转换
        byte[] keyByte = Base64.decode(key);
        //加密
        AES aes = SecureUtil.aes(keyByte);        //构建
        byte[] encryptData = aes.encrypt("metadata");   //加密
        byte[] encryptData2 = aes.encrypt("metadata123");   //加密
        //加密后的数据转为Base64
        String encryptDataEncode = Base64.encode(encryptData);
        String encryptDataEncode2 = Base64.encode(encryptData2);
        System.out.println(encryptDataEncode);
        System.out.println(encryptDataEncode2);
        //将Base64编码加密数据和秘钥的格式进行解码转换
        byte[] data2 = Base64.decode(encryptDataEncode);
        byte[] key2 = Base64.decode(key);
        //解密
        AES aes2 = SecureUtil.aes(key2);
        String decrypt = aes2.decryptStr(data2);
        System.out.println(decrypt);

    }
}
