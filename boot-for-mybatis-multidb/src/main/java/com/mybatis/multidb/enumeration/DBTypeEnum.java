/**
 * Author:   claire
 * Date:    2020-02-08 - 14:46
 * Description: db枚举类
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2020-02-08 - 14:46          V1.3.1           db枚举类
 */
package com.mybatis.multidb.enumeration;

/**
 * 功能简述 <br/> 
 * 〈db枚举类〉
 *
 * @author claire
 * @date 2020-02-08 - 14:46
 */
public enum DBTypeEnum {
    /**
     *
     */
    db1("db1"),
    /**
     *
     */
    db2("db2"),
    /**
     *
     */
    db3("db3");
    private String value;

    DBTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
