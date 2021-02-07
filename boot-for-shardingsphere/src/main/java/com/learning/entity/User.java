/**
 * Author:   claire
 * Date:    2021-01-07 - 11:24
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2021-01-07 - 11:24          V1.14.0
 */
package com.learning.entity;

import lombok.Data;

import java.util.Date;

/**
 * 功能简述 <br/> 
 * 〈〉
 *
 * @author claire
 * @date 2021-01-07 - 11:24
 */
@Data
public class User {
    private Long userId;
    private String idNumber;
    private String name;
    private Integer age;
    private Integer gender;
    private Date birthDate;
}
