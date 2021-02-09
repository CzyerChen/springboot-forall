/**
 * Author:   claire
 * Date:    2021-02-09 - 18:09
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2021-02-09 - 18:09          V1.17.0
 */
package com.learning.spring;

import cn.hutool.aop.ProxyUtil;
import cn.hutool.bloomfilter.BitMapBloomFilter;
import cn.hutool.extra.cglib.CglibUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.script.ScriptUtil;
import cn.hutool.system.SystemUtil;

import java.io.IOException;

/**
 * 功能简述
 * 〈spring测试类〉
 *
 * @author claire
 * @date 2021-02-09 - 18:09
 */
public class HCoreSpringTest {

    public static void main(String[] args) throws IOException {
        SpringUtil.getBean("testDemo");
        //CglibUtil.copy();
        BitMapBloomFilter filter = new BitMapBloomFilter(10);
        //ProxyUtil.proxy();
        ScriptUtil.eval("print('Script test!');");
        SystemUtil.getJvmSpecInfo();
    }
}
