/**
 * Author:   claire
 * Date:    2021-01-05 - 13:41
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2021-01-05 - 13:41          V1.14.0
 */
package com.learning.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 功能简述 <br/> 
 * 〈〉
 *
 * @author claire
 * @date 2021-01-05 - 13:41
 */
@Data
@ConfigurationProperties(prefix = "sharding.ds1")
public class SecondDsProp {
    private String jdbcUrl;
    private String username;
    private String password;
    private String type;
}
