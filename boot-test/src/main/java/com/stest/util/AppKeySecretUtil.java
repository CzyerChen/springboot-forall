/**
 * Author:   claire
 * Date:    2023/1/31 - 4:13 下午
 * Description: appkeysecret生成器
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2023/1/31 - 4:13 下午          V1.0.0          appkeysecret生成器
 */
package com.stest.util;

import javax.crypto.Cipher;
import java.security.Key;
import java.util.UUID;

/**
 * 功能简述 
 * 〈appkeysecret生成器〉
 *
 * @author claire
 * @date 2023/1/31 - 4:13 下午
 * @since 1.0.0
 */
public class AppKeySecretUtil {
    private final static String[] chars = new String[]{"a", "b", "c", "d", "e", "f",
            "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
            "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z"};

    // 字符串默认键值
    private static String strDefaultKey = "inventec2020@#$%^&";

    //加密工具
    private Cipher encryptCipher = null;

    // 解密工具
    private Cipher decryptCipher = null;

    /**
     * 默认构造方法，使用默认密钥
     */
    public AppKeySecretUtil() throws Exception {
        this(strDefaultKey);
    }

    /**
     * 指定密钥构造方法
     * @param strKey 指定的密钥
     * @throws Exception
     */

    public AppKeySecretUtil(String strKey) throws Exception {

        // Security.addProvider(new com.sun.crypto.provider.SunJCE());
        Key key = getKey(strKey.getBytes());
        encryptCipher = Cipher.getInstance("DES");
        encryptCipher.init(Cipher.ENCRYPT_MODE, key);
        decryptCipher = Cipher.getInstance("DES");
        decryptCipher.init(Cipher.DECRYPT_MODE, key);
    }

    /**
     * 将byte数组转换为表示16进制值的字符串， 如：byte[]{8,18}转换为：0813，和public static byte[]
     *
     * hexStr2ByteArr(String strIn) 互为可逆的转换过程
     *
     * @param arrB 需要转换的byte数组
     * @return 转换后的字符串
     * @throws Exception  本方法不处理任何异常，所有异常全部抛出
     */
    public static String byteArr2HexStr(byte[] arrB) throws Exception {
        int iLen = arrB.length;
        // 每个byte用2个字符才能表示，所以字符串的长度是数组长度的2倍
        StringBuffer sb = new StringBuffer(iLen * 2);
        for (int i = 0; i < iLen; i++) {
            int intTmp = arrB[i];
            // 把负数转换为正数
            while (intTmp < 0) {
                intTmp = intTmp + 256;
            }
            // 小于0F的数需要在前面补0
            if (intTmp < 16) {
                sb.append("0");
            }
            sb.append(Integer.toString(intTmp, 16));
        }
        return sb.toString();
    }

    /**
     * 将表示16进制值的字符串转换为byte数组，和public static String byteArr2HexStr(byte[] arrB)
     * 互为可逆的转换过程
     * @param strIn 需要转换的字符串
     * @return 转换后的byte数组
     */
    public static byte[] hexStr2ByteArr(String strIn) throws Exception {
        byte[] arrB = strIn.getBytes();
        int iLen = arrB.length;
        // 两个字符表示一个字节，所以字节数组长度是字符串长度除以2
        byte[] arrOut = new byte[iLen / 2];
        for (int i = 0; i < iLen; i = i + 2) {
            String strTmp = new String(arrB, i, 2);
            arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
        }
        return arrOut;
    }

    /**
     *
     * 加密字节数组
     * @param arrB 需加密的字节数组
     * @return 加密后的字节数组
     */
    public byte[] encrypt(byte[] arrB) throws Exception {
        return encryptCipher.doFinal(arrB);
    }

    /**
     * 加密字符串
     * @param strIn 需加密的字符串
     * @return 加密后的字符串
     */
    public String encrypt(String strIn) throws Exception {
        return byteArr2HexStr(encrypt(strIn.getBytes()));
    }

    /**
     * 解密字节数组
     * @param arrB 需解密的字节数组
     * @return 解密后的字节数组
     */
    public byte[] decrypt(byte[] arrB) throws Exception {
        return decryptCipher.doFinal(arrB);
    }

    /**
     * 解密字符串
     * @param strIn 需解密的字符串
     * @return 解密后的字符串
     */
    public String decrypt(String strIn) throws Exception {
        return new String(decrypt(hexStr2ByteArr(strIn)));
    }

    /**
     * 从指定字符串生成密钥，密钥所需的字节数组长度为8位 不足8位时后面补0，超出8位只取前8位
     * @param arrBTmp 构成该字符串的字节数组
     * @return 生成的密钥
     */
    private Key getKey(byte[] arrBTmp) throws Exception {
        // 创建一个空的8位字节数组（默认值为0）
        byte[] arrB = new byte[8];
        // 将原始字节数组转换为8位
        for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {
            arrB[i] = arrBTmp[i];
        }
        // 生成密钥
        Key key = new javax.crypto.spec.SecretKeySpec(arrB, "DES");
        return key;
    }
    //生成8位appKey
    public static String getAppKey() {
        StringBuffer shortBuffer = new StringBuffer();
        //获取用户id进行字符串截取
        String uuid = UUID.randomUUID().toString().replace("-", "");
        for (int i = 0; i < 8; i++) {
            String str = uuid.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(str, 16);
            shortBuffer.append(chars[x % 0x3E]);
        }
        return shortBuffer.toString();

    }

    //生成32位appSecret
    public static String getAppSecret(String appId){
        String EncryoAppSecret="";
        try {
            AppKeySecretUtil des1 = new AppKeySecretUtil();// 使用默认密钥
            EncryoAppSecret=des1.encrypt(appId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return EncryoAppSecret;
    }

    public static void main(String[] args) {
        try {
            AppKeySecretUtil des1 = new AppKeySecretUtil();// 使用默认密钥

            String appKey = getAppKey();  //定义变量接收
            String appSecret = getAppSecret(appKey);
            System.out.println("生成的key：" + appKey);
            System.out.println("生成的seccret：" + appSecret);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
