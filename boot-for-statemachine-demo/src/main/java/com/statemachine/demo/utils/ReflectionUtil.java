/**
 * Author:   claire
 * Date:    2020-12-28 - 21:21
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2020-12-28 - 21:21          V1.13.0
 */
package com.statemachine.demo.utils;

import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * 功能简述 <br/> 
 * 〈〉
 *
 * @author claire
 * @date 2020-12-28 - 21:21
 */
public class ReflectionUtil {

    /**
     * 对Spring的ReflectionUtils中getValue方法做简单封装
     *
     * @param object
     * @param key
     * @param defaultVal
     * @return
     */
    public static Object getValue(Object object, String key, Object defaultVal) {
        Field field = ReflectionUtils.findField(object.getClass(), key);
        if (Objects.isNull(field)) {
            return defaultVal;
        }
        field.setAccessible(true);
        return ReflectionUtils.getField(field, object);
    }
}