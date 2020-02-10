/**
 * Author:   claire
 * Date:    2020-02-08 - 17:59
 * Description: FB数据库连接池
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2020-02-08 - 17:59          V1.3.1           FB数据库连接池
 */
package com.mybatis.multidb.config.selfdefined.properties;

import lombok.Data;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;

/**
 * 功能简述 <br/> 
 * 〈FB数据库连接池配置〉
 *
 * @author claire
 * @date 2020-02-08 - 17:59
 */
@Data
@ConfigurationProperties(prefix = "spring.datasource")
public class FbDataSourceProperties implements BeanClassLoaderAware, InitializingBean {
    private String appName;
    private String env;

    private ClassLoader classLoader;
    private EmbeddedDatabaseConnection embeddedDatabaseConnection = EmbeddedDatabaseConnection.NONE;


    @Override
    public void afterPropertiesSet() throws Exception {
        this.embeddedDatabaseConnection = EmbeddedDatabaseConnection
                .get(this.classLoader);
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

}
