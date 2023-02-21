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
public enum SmsStatusEnum {
    ACCEPTD("ACCEPTD", "提交成功"),
    DELIVRD("DELIVRD", "投递成功"),
    ET_0265("ET:0265", "投递成功"),
    UNDO("UNDO", "用户取消"),
    PAUSE("PAUSE", "暂停"),
    COMMITFAIL("COMMITFAIL", "提交失败"),
    COMMITEXCEPTION("COMMITEXCEPTION", "提交异常"),
    EXPIRED("EXPIRED", "短信超时"),
    UNDELIV("UNDELIV", "无法投递"),
    UNKNOWN("UNKNOWN", "状态不知"),
    REJECTD("REJECTD", "短信被拒绝"),
    LACKBALANCE("LACKBALANCE", "用户余额不足"),
    ET_0210("REJECTD", "号码黑名单"),
    MSG_INVALID("MSG_INVALID", "发送内容无效"),
    SUBMITING("SUBMITING", "提交中"),
    INIT("INIT", "初始状态");

    private final String value;
    private final String name;
    private static final Map<String, SmsStatusEnum> lookup;

    SmsStatusEnum(String value, String name) {
        this.value = value;
        this.name = name;
    }

    public String getValue() {
        return this.value;
    }


    public String getName() {
        return this.name;
    }

    static {
        lookup = new HashMap<>();

        for (SmsStatusEnum e : EnumSet.allOf(SmsStatusEnum.class)) {
            lookup.put(e.value, e);
        }
    }

    public static SmsStatusEnum find(String value) {
        if (value == null) {
            return null;
        }
        SmsStatusEnum data = lookup.get(value);
        return data;
    }
}
