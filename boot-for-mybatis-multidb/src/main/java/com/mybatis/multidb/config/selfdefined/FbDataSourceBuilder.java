/**
 * Author:   claire
 * Date:    2020-02-09 - 09:35
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2020-02-09 - 09:35          V1.3.1
 */
package com.mybatis.multidb.config.selfdefined;

import org.springframework.core.env.Environment;

/**
 * 功能简述 <br/> 
 * 〈〉
 *
 * @author claire
 * @date 2020-02-09 - 09:35
 */
public class FbDataSourceBuilder {

    public FbDataSourceBuilder(){

    }
    public static FbDataSourceBuilder create() {
        return new FbDataSourceBuilder();
    }

    public MultiFbDatasource build() {
       return new FbDataSourceWrapper();
    }

    public MultiFbDatasource build(Environment env, String prefix) {
        MultiFbDatasource multiFbDatasource = new FbDataSourceWrapper();
        multiFbDatasource.setMinEvictableIdleTimeMillis((Long)env.getProperty(prefix + "min-evictable-idle-time-millis", Long.class, 1800000L));
        multiFbDatasource.setMaxEvictableIdleTimeMillis((Long)env.getProperty(prefix + "max-evictable-idle-time-millis", Long.class, 25200000L));
        return multiFbDatasource;
    }
}
