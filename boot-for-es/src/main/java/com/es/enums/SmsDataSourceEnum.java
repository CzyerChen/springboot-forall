/**
 * Author:   claire
 * Date:    2022/8/8 - 10:23 上午
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2022/8/8 - 10:23 上午          V1.0.0
 */
package com.es.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * 功能简述 
 * 〈〉
 *
 * @author claire
 * @date 2022/8/8 - 10:23 上午
 * @since 1.0.0
 */
public enum SmsDataSourceEnum {
    UNKNOWN(-1, "未知"),
    PLATFORM(10, "统一平台实时"),
    OLD_SMS(20, "短信组老数据"),
    OLD_PLATFORM(21, "统一平台老数据"),
    SYNC(30, "同步");
    private final int value;

    SmsDataSourceEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }


    private final String name;
    private static final Map<Integer, SmsDataSourceEnum> lookup;

    static {
        lookup = new HashMap<>();


        for (SmsDataSourceEnum e : EnumSet.allOf(SmsDataSourceEnum.class)) {
            lookup.put(Integer.valueOf(e.value), e);
        }
    }

    public static SmsDataSourceEnum find(Integer value) {
        if (value == null) {
            return null;
        }
        SmsDataSourceEnum data = lookup.get(value);
        return data;
    }

    public int getValue() {
        return this.value;
    }

    public String getName() {
        return this.name;
    }
}



