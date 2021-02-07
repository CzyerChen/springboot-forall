/**
 * Author:   claire
 * Date:    2021-01-07 - 13:59
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2021-01-07 - 13:59          V1.14.0
 */
package com.learning.entity;

import lombok.Data;

import java.util.Date;

/**
 * 功能简述 <br/> 
 * 〈〉
 *
 * @author claire
 * @date 2021-01-07 - 13:59
 */
@Data
public class Product {
    private Long productId;
    private String code;
    private String name;
    private String description;
    private Date createTime;
}
