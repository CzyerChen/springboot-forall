/**
 * Author:   claire
 * Date:    2021-02-07 - 15:38
 * Description: 日志文件过滤名称
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2021-02-07 - 15:38          V1.17.0          日志文件过滤名称
 */
package com.redission.constant;

/**
 * 功能简述 <br/> 
 * 〈日志文件过滤名称〉
 *
 * @author claire
 * @date 2021-02-07 - 15:38
 */
public enum LogFileName {
    /**
     *
     */
    BIZ1("biz1"),
    /**
     *
     */
    BIZ2("biz2");

    private final String name;

    LogFileName(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }}
