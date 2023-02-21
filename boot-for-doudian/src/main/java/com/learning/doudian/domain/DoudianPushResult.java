/**
 * Author:   claire
 * Date:    2022/10/24 - 1:45 下午
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2022/10/24 - 1:45 下午          V1.0.0
 */
package com.learning.doudian.domain;

import lombok.Data;

/**
 * 功能简述 
 * 〈〉
 *
 * @author claire
 * @date 2022/10/24 - 1:45 下午
 * @since 1.0.0
 */
@Data
public class DoudianPushResult {
    private Integer code;
    private String msg;
}
