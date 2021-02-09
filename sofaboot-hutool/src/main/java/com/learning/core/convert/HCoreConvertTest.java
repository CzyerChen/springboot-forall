/**
 * Author:   claire
 * Date:    2021-02-09 - 14:07
 * Description: convert测试
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2021-02-09 - 14:07          V1.17.0          convert测试
 */
package com.learning.core.convert;

import cn.hutool.core.convert.Convert;

import java.util.Date;
import java.util.List;

/**
 * 功能简述 
 * 〈convert测试〉
 *
 * @author claire
 * @date 2021-02-09 - 14:07
 */
public class HCoreConvertTest {

    public static void main(String[] args){
        int a = 1;
        String aStr = Convert.toStr(a);

        long[] b = {1,2,3,4,5};
        String bStr = Convert.toStr(b);

        String[] c = { "1", "2", "3", "4" };
        Integer[] intArray = Convert.toIntArray(c);

        long[] d = {1,2,3,4,5};
        Integer[] intArray2 = Convert.toIntArray(d);

        String e = "2017-05-06";
        Date value = Convert.toDate(e);

        Object[] f = {"a", "你", "好", "", 1};
        List<?> list = Convert.convert(List.class, f);
        List<?> list2 = Convert.toList(a);

        //自定义类型转换，半角全角切换，16进制，Unicode和字符串转换，编码转换，时间单位转换，金额大小写转换，原始类和包装类转换
        //通过源码了解到，自定义注册自定义转换器步骤
        /*
         * 自定义Converter                   public static class CustomConverter implements Converter<String>
         * 获取注册中心实例，注册自定义转换器   ConverterRegistry converterRegistry = ConverterRegistry.getInstance();   converterRegistry.putCustom(String.class, CustomConverter.class);
         * 最终执行转换                      String result = converterRegistry.convert(String.class, a);
         *
         * 注册中心通过Map维护类型与处理器的映射关系
         * getInstance通过单例的方式实现
         */
        
    }
}
