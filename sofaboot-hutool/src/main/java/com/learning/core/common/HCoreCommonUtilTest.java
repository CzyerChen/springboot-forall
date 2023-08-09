/**
 * Author:   claire
 * Date:    2021-02-09 - 16:06
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2021-02-09 - 16:06          V1.17.0
 */
package com.learning.core.common;

import cn.hutool.core.annotation.Alias;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.lang.Validator;

import java.util.*;

/**
 * 功能简述 
 * 〈〉
 *
 * @author claire
 * @date 2021-02-09 - 16:06
 */
public class HCoreCommonUtilTest {

    public static void main(String[] args){
        /*===============HashMap扩展-Dict,简化操作======================*/
        Dict dict = Dict.create()
                .set("key1", 1)//int
                .set("key2", 1000L)//long
                .set("key3", DateTime.now());//Date

        /*===============单例工具-Singleton,懒汉模式，简化操作======================*/
        //Singleton.get(Class<?> class)

        /*===============断言-Assert,相比java原生，避了一些坑======================*/

        /*===============二进码十进数-BCD======================*/

        /*===============控制台打印封装-Console======================*/

        /*===============字段验证器-Validator，验证字段是否符合 逻辑======================*/
        boolean isEmail = Validator.isEmail("test@gmail.com");

        /*===============字符串格式化-StrFormatter======================*/

        /*===============树结构工具-TreeUtil,构建菜单树、树形结构等======================*/

        /*===============Bean工具-BeanUtil======================*/
        // map to bean
//        HashMap<String, Object> map = CollUtil.newHashMap();
//        map.put("a_name", "Joe");
//        map.put("b_age", 12);
//        // 设置别名，用于对应bean的字段名
//        HashMap<String, String> mapping = CollUtil.newHashMap();
//        mapping.put("a_name", "name");
//        mapping.put("b_age", "age");
//        HCoreCommonUtilTest test = BeanUtil.mapToBean(map, HCoreCommonUtilTest.class, CopyOptions.create().setFieldMapping(mapping));

        //bean to map
        HCoreCommonUtilTest person = new HCoreCommonUtilTest();
         BeanUtil.beanToMap(person);

        //@Alias("aliasSubName")设置别名

        
        /*===============集合工具-CollUtil======================*/
        String[] col= new String[]{"a","b","c","d","e"};
        List<String> colList = CollUtil.newArrayList(col);
        String str = CollUtil.join(colList, "#"); //str -> a#b#c#d#e


        //Integer比较器
        Comparator<Integer> comparator = new Comparator<Integer>(){
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        };

        //新建三个列表，CollUtil.newArrayList方法表示新建ArrayList并填充元素
        List<Integer> list1 = CollUtil.newArrayList(1, 2, 3);
        List<Integer> list2 = CollUtil.newArrayList(4, 5, 6);
        List<Integer> list3 = CollUtil.newArrayList(7, 8, 9);

        //参数表示把list1,list2,list3合并并按照从小到大排序后，取0~2个（包括第0个，不包括第2个），结果是[1,2]
        @SuppressWarnings("unchecked")
        List<Integer> result = CollUtil.sortPageAll(0, 2, comparator, list1, list2, list3);
        System.out.println(result);     //输出 [1,2]

        //sortEntrySetToList方法

        //popPart方法

        //newHashMap、newHashSet、newArrayList方法

        //append方法

        // resize方法
        
        //addAll方法
        //sub方法
        // isEmpty、isNotEmpty方法
        //zip方法
        Collection<String> keys = CollUtil.newArrayList("a", "b", "c", "d");
        Collection<Integer> values = CollUtil.newArrayList(1, 2, 3, 4);

        // {a=1,b=2,c=3,d=4}
        Map<String, Integer> map1 = CollUtil.zip(keys, values);
        //filter方法

        /*===============列表工具-ListUtil======================*/
        
        /*===============Iterator工具-IterUtil======================*/
        
        /*===============有界优先队列-BoundedPriorityQueue======================*/
        //使用场景：  我有一个用户表，这个表根据用户名被Hash到不同的数据库实例上，我要找出这些用户中最热门的5个
        //设置容量，根据比较规则，自动筛出前5
        
        /*===============线程安全的HashSet-ConcurrentHashSet======================*/
        
        /*===============Map工具-MapUtil 封装操作更便捷======================*/
        
        /*===============双向查找Map-BiMap ======================*/

        /*===============Base62编码解码-Base62/Base64编码解码-Base64/Base32编码解码-Base32======================*/

        /*===============CSV文件处理工具-CsvUtil======================*/

        /*===============可复用字符串生成器-StrBuilder,reset方法======================*/

        /*===============Unicode编码转换工具-UnicodeUtil======================*/

        /*===============字符串切割-StrSpliter======================*/
        String str1 = "a, ,efedsfs,   ddf";
        //参数：被切分字符串，分隔符逗号，0表示无限制分片数，去除两边空格，忽略空白项
//        List<String> split = StrSpliter.split(str1, ',', 0, true, true);

        /*===============注解工具-AnnotationUtil======================*/
        //getAnnotations 获取指定类、方法、字段、构造等上的注解列表
        //getAnnotation 获取指定类型注解
        //getAnnotationValue 获取指定注解属性的值
        //getRetentionPolicy 获取注解类的保留时间，可选值 SOURCE（源码时），CLASS（编译时），RUNTIME（运行时），默认为 CLASS
        //getTargetType 获取注解类可以用来修饰哪些程序元素，如 TYPE, METHOD, CONSTRUCTOR, FIELD, PARAMETER 等
        //isDocumented 是否会保存到 Javadoc 文档中
        //isInherited 是否可以被继承，默认为 false

        /*===============异常工具-ExceptionUtil======================*/

        /*==============数学相关-MathUtil======================*/

        /*===============线程工具-ThreadUtil/自定义线程池-ExecutorBuilder/高并发测试-ConcurrencyTester======================*/

        /*===============图片工具-ImgUtil 缩放、裁剪、转为黑白、加水印 ======================*/

        /*===============网络工具-NetUtil IP等操作======================*/

        /*===============URL生成器-UrlBuilder======================*/
    }
}
