package com.es.enums;


import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;


public enum OperatorsEnum {
    CTCC(0, "电信"),
    CMCC(1, "移动"),
    CUCC(2, "联通"),
    UNKNOW(3, "未知"),
    UNLIMITED(-1, "不限");

    private int value;
    private String name;
    private static final Map<Integer, OperatorsEnum> lookup;

    OperatorsEnum(int value, String name) {
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


        for (OperatorsEnum e : EnumSet.<OperatorsEnum>allOf(OperatorsEnum.class)) {
            lookup.put(Integer.valueOf(e.value), e);
        }
    }

    public static OperatorsEnum find(Integer value) {
        OperatorsEnum data = lookup.get(value);
        if (value == null) {
            return UNKNOW;
        }
        return data;
    }
}