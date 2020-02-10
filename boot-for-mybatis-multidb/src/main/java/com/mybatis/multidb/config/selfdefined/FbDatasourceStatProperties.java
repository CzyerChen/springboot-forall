/**
 * Author:   claire
 * Date:    2020-02-08 - 18:22
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2020-02-08 - 18:22          V1.3.1
 */
package com.mybatis.multidb.config.selfdefined;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 功能简述 <br/> 
 * 〈〉
 *
 * @author claire
 * @date 2020-02-08 - 18:22
 */
@ConfigurationProperties("spring.datasource.fibo")
public class FbDatasourceStatProperties {
    private String[] aopPatterns;

    public String[] getAopPatterns() {
        return this.aopPatterns;
    }

    public void setAopPatterns(String[] aopPatterns) {
        this.aopPatterns = aopPatterns;
    }

}
