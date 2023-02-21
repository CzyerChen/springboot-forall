/**
 * Author:   claire
 * Date:    2022/8/8 - 10:21 上午
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2022/8/8 - 10:21 上午          V1.0.0
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
 * @date 2022/8/8 - 10:21 上午
 * @since 1.0.0
 */
public enum AcceptTypeEnum {
    SINGLE(0, "单条"),
    BULK(1, "批量");

    private final int value;
    private final String name;
    private static final Map<Integer, AcceptTypeEnum> lookup;

    AcceptTypeEnum(int value, String name) {
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

        for (AcceptTypeEnum e : EnumSet.allOf(AcceptTypeEnum.class)) {
            lookup.put(Integer.valueOf(e.value), e);
        }
    }

    public static AcceptTypeEnum find(Integer value) {
        if (value == null) {
            return null;
        }
        AcceptTypeEnum data = lookup.get(value);
        return data;
    }
}