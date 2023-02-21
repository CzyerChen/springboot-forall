/**
 * Author:   claire
 * Date:    2023/2/20 - 1:03 下午
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2023/2/20 - 1:03 下午          V1.0.0
 */
package com.learning.easyexcel.common;

import lombok.Data;

import java.util.Date;

/**
 * 功能简述 
 * 〈〉
 *
 * @author claire
 * @date 2023/2/20 - 1:03 下午
 * @since 1.0.0
 */
@Data
public class User {
    private String cname;
    private String ename;
    private Integer age;
    private Date createTime;
    private Double score;
}
