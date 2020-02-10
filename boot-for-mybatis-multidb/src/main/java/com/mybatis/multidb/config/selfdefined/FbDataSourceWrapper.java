/**
 * Author:   claire
 * Date:    2020-02-09 - 09:36
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2020-02-09 - 09:36          V1.3.1
 */
package com.mybatis.multidb.config.selfdefined;

import com.mybatis.multidb.config.selfdefined.properties.FbDataSourceProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 功能简述 <br/> 
 * 〈〉
 *
 * @author claire
 * @date 2020-02-09 - 09:36
 */
@Slf4j
@ConfigurationProperties("spring.datasource.fibo")
public class FbDataSourceWrapper extends MultiFbDatasource implements InitializingBean {
    @Autowired
    private FbDataSourceProperties fbDataSourceProperties;
    /*后期看是否有需要的参数*/
    @Autowired
    private DataSourceProperties dataSourceProperties;

    public FbDataSourceWrapper(){
        super();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if(StringUtils.isNotBlank(fbDataSourceProperties.getAppName())){
            super.setAppName(fbDataSourceProperties.getAppName());
        }
       if(super.getDbName() == null){
          log.error("Datasource param : DB name is empty");
       }
       if(super.getEnv() == null  && super.getConfigCenterUrl() == null){
           log.error("Datasource params : env or config center url is empty");
       }
       if(super.getDriverClassName()==null && super.getSourceType()==null && super.getPlatform() ==null ){
           log.error("Datasource params : driver class name or source type or paltform is empty");
       }

    }
}
