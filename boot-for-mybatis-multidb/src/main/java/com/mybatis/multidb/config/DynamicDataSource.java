/**
 * Author:   claire
 * Date:    2020-02-08 - 14:47
 * Description: 动态数据库类
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2020-02-08 - 14:47          V1.3.1           动态数据库类
 */
package com.mybatis.multidb.config;

import com.mybatis.multidb.utils.DbContextHolder;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 功能简述 <br/> 
 * 〈动态数据库类〉
 *
 * @author claire
 * @date 2020-02-08 - 14:47
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        return  DbContextHolder.getDbType();
    }
}
