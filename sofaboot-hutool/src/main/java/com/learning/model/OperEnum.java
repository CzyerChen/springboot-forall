/**
 * Author:   claire
 * Date:    2021-02-22 - 09:58
 * Description: 操作枚举类
 * History:
 * <author>          <time>                    <version>          <desc>
 * claire          2021-02-22 - 09:58          V1.17.0        操作枚举类
 */
package com.learning.model;

/**
 * 功能简述 
 * 〈操作枚举类〉
 *
 * @author claire
 * @date 2021-02-22 - 09:58
 * @since 1.17.0
 */
public enum OperEnum {
    MORE_THAN(0),
    LESS_THAN(1),
    BETWEEN(2);

    final private Integer code;

    OperEnum(Integer code){
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }}
