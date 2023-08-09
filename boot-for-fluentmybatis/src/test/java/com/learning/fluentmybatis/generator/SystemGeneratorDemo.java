/**
 * Author:   claire
 * Date:    2023/8/8 - 4:19 下午
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2023/8/8 - 4:19 下午          V1.0.0
 */
package com.learning.fluentmybatis.generator;

import cn.org.atool.fluent.mybatis.metadata.DbType;
import cn.org.atool.generator.FileGenerator;
import cn.org.atool.generator.annotation.Table;
import cn.org.atool.generator.annotation.Tables;
import com.learning.fluentmybatis.FluentMybatisApplication;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 功能简述
 * 〈〉
 *
 * @author claire
 * @date 2023/8/8 - 4:19 下午
 * @since 1.0.0
 */
@SpringBootTest(classes = FluentMybatisApplication.class)
public class SystemGeneratorDemo {
    // 数据源 url
    static final String url = "jdbc:postgresql://127.0.0.1:5432/dev";
    // 数据库用户名
    static final String username = "user";
    // 数据库密码
    static final String password = "pass";
    static final String dbDriver="org.postgresql.Driver";

    @Test
    public void generate() throws Exception {
        // 引用配置类，build方法允许有多个配置类
        FileGenerator.build(Empty.class);
    }

    @Tables(
            // 设置数据库连接信息
            url = url, username = username, password = password,
            dbType = DbType.POSTGRE_SQL,
            driver = dbDriver,
            // 设置entity类生成src目录, 相对于 user.dir
            srcDir = "src/main/java",
            // 设置entity类的package值
            basePack = "com.learning.fluentmybatis",
            // 设置dao接口和实现的src目录, 相对于 user.dir
            daoDir = "src/main/java",
            // 设置哪些表要生成Entity文件
            schema = "public",
            tables = {@Table(value = {"product_info"})}
    )
    static class Empty { //类名随便取, 只是配置定义的一个载体
    }
}
