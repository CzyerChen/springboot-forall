/**
 * Author:   claire
 * Date:    2022/8/8 - 10:24 上午
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2022/8/8 - 10:24 上午          V1.0.0
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
 * @date 2022/8/8 - 10:24 上午
 * @since 1.0.0
 */
public enum SmsTypeEnum {
    AUTHCODE(1, "验证码短信"),
    NOTIFICATION(2, "通知短信"),
    MARKETING(3, "营销短信"),
    MONITORING(4, "监测短信");

    private final int value;
    private final String name;
    private static final Map<Integer, SmsTypeEnum> lookup;

    SmsTypeEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return this.value;
    }


    public String getName() {
        return this.name;
    }

    static {
        lookup = new HashMap<>();

        for (SmsTypeEnum e : EnumSet.allOf(SmsTypeEnum.class)) {
            lookup.put(Integer.valueOf(e.value), e);
        }
    }

    public static SmsTypeEnum find(Integer value) {
        if (value == null) {
            return null;
        }
        SmsTypeEnum data = lookup.get(value);
        return data;
    }
}
