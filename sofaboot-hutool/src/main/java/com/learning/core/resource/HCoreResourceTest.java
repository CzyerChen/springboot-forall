/**
 * Author:   claire
 * Date:    2021-02-09 - 15:26
 * Description: 资源类测试
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2021-02-09 - 15:26          V1.17.0          资源类测试
 */
package com.learning.core.resource;

import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.io.resource.ResourceUtil;

import java.io.IOException;
import java.util.Properties;

/**
 * 功能简述 
 * 〈资源类测试,读取系统资源文件〉
 *
 * @author claire
 * @date 2021-02-09 - 15:26
 */
public class HCoreResourceTest {

    public static void main(String[] args) throws IOException {
        String str = ResourceUtil.readUtf8Str("test.xml");

        ClassPathResource resource = new ClassPathResource("test.properties");
        Properties properties = new Properties();
        properties.load(resource.getStream());
    }
}
