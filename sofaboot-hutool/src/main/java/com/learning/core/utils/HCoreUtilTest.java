/**
 * Author:   claire
 * Date:    2021-02-09 - 15:30
 * Description: 工具类测试
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2021-02-09 - 15:30          V1.17.0          工具类测试
 */
package com.learning.core.utils;

import cn.hutool.core.util.*;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 功能简述 
 * 〈工具类测试〉
 *
 * @author claire
 * @date 2021-02-09 - 15:30
 */
public class HCoreUtilTest {

    public static void main(String[] args){
        /*===============StrUtil======================*/

        String testStr = "";
        boolean hasBlank = StrUtil.hasBlank(testStr);

        StrUtil.removePrefix("test.txt","t");

        StrUtil.sub("ssssrraefrv",2,6);

        StrUtil.format("{} b {}","a","c"); //a b c

        /*===============HexUtil,16进制======================*/
        String str = "我是一个字符串";

        String hex = HexUtil.encodeHexStr(str, CharsetUtil.CHARSET_UTF_8);
        //hex是：
        //e68891e698afe4b880e4b8aae5ad97e7aca6e4b8b2
        String decodedStr = HexUtil.decodeHexStr(hex);

        /*==================对应Javascript中的escape()函数和unescape()函数===================*/
        //EscapeUtil.escape
        //EscapeUtil.unescape

        /*===============Hash算法-HashUtil======================*/
        /*
         * additiveHash 加法hash
         * rotatingHash 旋转hash
         * oneByOneHash 一次一个hash
         * bernstein Bernstein's hash
         * universal Universal Hashing
         * zobrist Zobrist Hashing
         * fnvHash 改进的32位FNV算法1
         * intHash Thomas Wang的算法，整数hash
         * rsHash RS算法hash
         * jsHash JS算法
         * pjwHash PJW算法
         * elfHash ELF算法
         * bkdrHash BKDR算法
         * sdbmHash SDBM算法
         * djbHash DJB算法
         * dekHash DEK算法
         * apHash AP算法
         * tianlHash TianL Hash算法
         * javaDefaultHash JAVA自己带的算法
         * mixHash 混合hash算法，输出64位的值
         */

        /*===============URL工具-URLUtil URL编解码======================*/

        /*=============== XML工具-XmlUtil XML 读写======================*/

        /*=============== 对象工具-ObjectUtil======================*/

        /*=============== 反射工具-ReflectUtil======================*/
        Method[] methods = ReflectUtil.getMethods(HCoreUtilTest.class);

        Method method = ReflectUtil.getMethod(HCoreUtilTest.class, "main");

        ReflectUtil.newInstance(HCoreUtilTest.class);

        /*=============== 泛型类型工具-TypeUtil======================*/

        /*=============== 分页工具-PageUtil 手动分页======================*/

        /*=============== 剪贴板工具-ClipboardUtil======================*/

        /*=============== 类工具-ClassUtil======================*/

        /*=============== 类加载工具-ClassLoaderUtil======================*/

        /*=============== 数字工具-NumberUtil 简化数字处理方式======================*/

        /*=============== 数组工具-ArrayUtil======================*/
        int[] a = {};
        int[] b = null;
        ArrayUtil.isEmpty(a);
        ArrayUtil.isEmpty(b);

        String[] newArray = ArrayUtil.newArray(String.class, 3);

        String[] keys = {"a", "b", "c"};
        Integer[] values = {1,2,3};
        Map<String, Integer> map = ArrayUtil.zip(keys, values, true);

        //{a=1, b=2, c=3}

        /*=============== 随机工具-RandomUtil ======================*/

        /*=============== 唯一ID工具-IdUtil  UUID ObjectId Snowflake ======================*/

        /*=============== 压缩工具-ZipUtil ======================*/
        //ZipUtil.zip();

        /*=============== 引用工具-ReferenceUtil ======================*/

        /*=============== 正则工具-ReUtil ======================*/
        String content = "ZZZaaabbbccc中文1234";
        Pattern pattern = Pattern.compile("^$", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            String result= matcher.group();
        }

        /*=============== 身份证工具-IdcardUtil ======================*/
        
        

    }
}
