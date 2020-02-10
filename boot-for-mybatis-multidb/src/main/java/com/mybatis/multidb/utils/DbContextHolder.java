/**
 * Author:   claire
 * Date:    2020-02-08 - 14:49
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2020-02-08 - 14:49          V1.3.1
 */
package com.mybatis.multidb.utils;

import com.mybatis.multidb.enumeration.DBTypeEnum;

/**
 * 功能简述 <br/> 
 * 〈〉
 *
 * @author claire
 * @date 2020-02-08 - 14:49
 */
public class DbContextHolder {
    private static final ThreadLocal<String> CONTEXT_HOLDER = new ThreadLocal<String>();
    /**
     * 设置数据源
     * @param dbTypeEnum
     */
    public static void setDbType(DBTypeEnum dbTypeEnum) {
        CONTEXT_HOLDER.set(dbTypeEnum.getValue());
    }

    /**
     * 取得当前数据源
     * @return
     */
    public static String getDbType() {
        return (String) CONTEXT_HOLDER.get();
    }

    /**
     * 清除上下文数据
     */
    public static void clearDbType() {
        CONTEXT_HOLDER.remove();
    }
}
