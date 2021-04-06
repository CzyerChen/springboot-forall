/**
 * Author:   claire
 * Date:    2021-04-05 - 14:00
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2021-04-05 - 14:00          V1.17.0
 */
package com.learning.ignite.entity;

import java.io.Serializable;

/**
 * 功能简述 
 * 〈〉
 *
 * @author claire
 * @date 2021-04-05 - 14:00
 * @since 1.1.0
 */
public class Person implements Serializable {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
