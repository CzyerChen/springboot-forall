/**
 * Author:   claire
 * Date:    2021-02-09 - 14:23
 * Description: 时间日期处理类测试
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2021-02-09 - 14:23          V1.17.0          时间日期处理类测试
 */
package com.learning.core.datetime;

import cn.hutool.core.date.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

/**
 * 功能简述
 * 〈时间日期处理类测试〉
 *
 * @author claire
 * @date 2021-02-09 - 14:23
 */
public class HCoreDatetimeTest {
    public static void main(String[] args) {
        /*
         * DateUtil 针对日期时间操作提供一系列静态方法
         * DateTime 提供类似于Joda-Time中日期时间对象的封装，继承自Date类，并提供更加丰富的对象方法。
         * FastDateFormat 提供线程安全的针对Date对象的格式化和日期字符串解析支持。此对象在实际使用中并不需要感知，相关操作已经封装在DateUtil和DateTime的相关方法中。
         * DateBetween 计算两个时间间隔的类，除了通过构造新对象使用外，相关操作也已封装在DateUtil和DateTime的相关方法中。
         * TimeInterval 一个简单的计时器类，常用于计算某段代码的执行时间，提供包括毫秒、秒、分、时、天、周等各种单位的花费时长计算，对象的静态构造已封装在DateUtil中。
         * DatePattern 提供常用的日期格式化模式，包括String类型和FastDateFormat两种类型。
         */

        /*挺符合国内经常要算的各种日期要求*/
        //当前时间
        Date date = DateUtil.date();
        //当前时间
        Date date2 = DateUtil.date(Calendar.getInstance());
        //当前时间
        Date date3 = DateUtil.date(System.currentTimeMillis());
        //当前时间字符串，格式：yyyy-MM-dd HH:mm:ss
        String now = DateUtil.now();
        //当前日期字符串，格式：yyyy-MM-dd
        String today = DateUtil.today();

        /*
         * DateUtil.parse方法会自动识别一些常用格式
         * yyyy-MM-dd HH:mm:ss
         * yyyy-MM-dd
         * HH:mm:ss
         * yyyy-MM-dd HH:mm
         * yyyy-MM-dd HH:mm:ss.SSS
         */
        String dateStr = "2017-03-01";
        Date date4 = DateUtil.parse(dateStr);

        String dateStr2 = "2017-03-01";
        Date date5 = DateUtil.parse(dateStr2, "yyyy-MM-dd");

        String dateStr3 = "2017-03-01 22:33:23";
        Date date6 = DateUtil.parse(dateStr3);

        //一天的开始，结果：2017-03-01 00:00:00
        Date beginOfDay = DateUtil.beginOfDay(date6);

        //一天的结束，结果：2017-03-01 23:59:59
        Date endOfDay = DateUtil.endOfDay(date6);

        /*===============时间偏移,时间差======================*/
        String dateStr4 = "2017-03-01 22:33:23";
        Date date7 = DateUtil.parse(dateStr);

        //结果：2017-03-03 22:33:23
        Date newDate = DateUtil.offset(date7, DateField.DAY_OF_MONTH, 2);

        //常用偏移，结果：2017-03-04 22:33:23
        DateTime newDate2 = DateUtil.offsetDay(date7, 3);

        //常用偏移，结果：2017-03-01 19:33:23
        DateTime newDate3 = DateUtil.offsetHour(date7, -3);
        //昨天
        DateUtil.yesterday();
        //明天
        DateUtil.tomorrow();
        //上周
        DateUtil.lastWeek();
        //下周
        DateUtil.nextWeek();
        //上个月
        DateUtil.lastMonth();
        //下个月
        DateUtil.nextMonth();
        
        /*===============计时器功能======================*/
        TimeInterval timer = DateUtil.timer();
        //someting
        timer.interval();//花费毫秒数
        timer.intervalRestart();//返回花费时间，并重置开始时间
        timer.intervalMinute();//花费分钟数

        /*===============星座，属相，闰年，年龄======================*/

        /*===============强大的农历都支持======================*/
        //通过公历构建
        ChineseDate date8 = new ChineseDate(DateUtil.parseDate("2020-01-25"));
        // 一月
        date8.getChineseMonth();
        // 正月
        date8.getChineseMonthName();
        // 初一
        date8.getChineseDay();
        // 庚子
        date8.getCyclical();
        // 生肖：鼠
        date8.getChineseZodiac();
        // 传统节日（部分支持，逗号分隔）：春节
        date8.getFestivals();
        // 庚子鼠年 正月初一
        date8.toString();

        /*===============JDK8+ LocalDateTime======================*/
        LocalDateTime localDateTime = LocalDateTimeUtil.parse("2020-01-23", DatePattern.NORM_DATE_PATTERN);
        LocalDate localDate = LocalDateTimeUtil.parseDate("2020-01-23");

        
    }
}
