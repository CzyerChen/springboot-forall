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
public enum SmsUidTypeEnum {
    MOBILE(0, "手机号码"),
    IMEI(11, "imei");
    private final int value;
    private final String name;
    private static final Map<Integer, SmsUidTypeEnum> lookup;

    SmsUidTypeEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }


    static {
        lookup = new HashMap<>();


        for (SmsUidTypeEnum e : EnumSet.allOf(SmsUidTypeEnum.class)) {
            lookup.put(Integer.valueOf(e.value), e);
        }
    }

    public static SmsUidTypeEnum find(Integer value) {
        if (value == null) {
            return null;
        }
        SmsUidTypeEnum data = lookup.get(value);
        return data;
    }

    public int getValue() {
        return this.value;
    }

    public String getName() {
        return this.name;
    }

}
