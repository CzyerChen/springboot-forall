/**
 * Author:   claire
 * Date:    2020-12-30 - 15:47
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2020-12-30 - 15:47          V1.13.0
 */
package com.es;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

/**
 * 功能简述 <br/> 
 * 〈〉
 *
 * @author claire
 * @date 2020-12-30 - 15:47
 */
@SpringBootApplication
@EnableElasticsearchRepositories(basePackages = {"com.es.repository"})
public class ElasticseachTestApplication {

    public static void main(String[] args){
        SpringApplication.run(ElasticseachTestApplication.class,args);
    }
}
