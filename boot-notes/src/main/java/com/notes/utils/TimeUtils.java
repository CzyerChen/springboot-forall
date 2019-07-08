package com.notes.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class TimeUtils {
  public static void main(String[] args) throws ParseException {
      //=============LocalDate是一个不可变的类，它表示默认格式(yyyy-MM-dd)的日期================//
      LocalDate l1 = LocalDate.now();
      LocalDate l2 = LocalDate.of(2019, Month.MAY, 29);
      LocalDate l3 = LocalDate.now(ZoneId.of("Asia/Shanghai"));
      LocalDate l4 = LocalDate.ofEpochDay(365);
      LocalDate l5 = LocalDate.ofYearDay(2019, 200);


      //===========代表一个符合人类可读格式的时间，默认格式是hh:mm:ss.zzz==========//
      LocalTime t1 = LocalTime.now();
      LocalTime t2 = LocalTime.of(14, 41, 30, 33);
      LocalTime t3 = LocalTime.now(ZoneId.of("Asia/Shanghai"));
      //Getting date from the base date i.e 01/01/1970
      LocalTime t4 = LocalTime.ofSecondOfDay(20000);


      //==========表示一组日期-时间，默认格式是yyyy-MM-dd-HH-mm-ss.zzz==========//
      LocalDateTime ldt1 = LocalDateTime.now();
      LocalDateTime ldt2 = LocalDateTime.of(LocalDate.now(), LocalTime.now());
      LocalDateTime ldt3 = LocalDateTime.of(2019, Month.MAY, 14, 41, 30, 30);
      LocalDateTime ldt4 = LocalDateTime.now(ZoneId.of("Asia/Shanghai"));
      LocalDateTime ldt5 = LocalDateTime.ofEpochSecond(2000, 0, ZoneOffset.UTC);

      //===========Instant类是用在机器可读的时间格式上的，它以Unix时间戳的形式存储日期时间====//
      Instant i1 = Instant.now();
      Instant i2 = Instant.ofEpochMilli(i1.toEpochMilli());


      //========Duration==========//
      Duration duration = Duration.ofDays(30);



      //======日期之间的计算==============//
      LocalDate today = LocalDate.now();
      System.out.println("Year "+today.getYear()+" is Leap Year? "+today.isLeapYear());

      //Compare two LocalDate for before and after
      System.out.println("Today is before 01/01/2015? "+today.isBefore(LocalDate.of(2015,1,1)));

      //Create LocalDateTime from LocalDate
      System.out.println("Current Time="+today.atTime(LocalTime.now()));

      //plus and minus operations
      System.out.println("10 days after today will be "+today.plusDays(10));
      System.out.println("3 weeks after today will be "+today.plusWeeks(3));
      System.out.println("20 months after today will be "+today.plusMonths(20));

      System.out.println("10 days before today will be "+today.minusDays(10));
      System.out.println("3 weeks before today will be "+today.minusWeeks(3));
      System.out.println("20 months before today will be "+today.minusMonths(20));

      //Temporal adjusters for adjusting the dates
      System.out.println("First date of this month= "+today.with(TemporalAdjusters.firstDayOfMonth()));
      LocalDate lastDayOfYear = today.with(TemporalAdjusters.lastDayOfYear());
      System.out.println("Last date of this year= "+lastDayOfYear);

      Period period = today.until(lastDayOfYear);
      System.out.println("Period Format= "+period);
      System.out.println("Months remaining in the year= "+period.getMonths());



      //======提到JDK7 SimpleDateFormat就能想到线程不安全，JDK8开始怎么做的呢？======//
      LocalDate date = LocalDate.now();
      System.out.println("Default format of LocalDate="+date);
      System.out.println(date.format(DateTimeFormatter.ofPattern("d::MMM::uuuu")));
      System.out.println(date.format(DateTimeFormatter.BASIC_ISO_DATE));

      LocalDateTime dateTime = LocalDateTime.now();
      System.out.println("Default format of LocalDateTime="+dateTime);
      System.out.println(dateTime.format(DateTimeFormatter.ofPattern("d::MMM::uuuu HH::mm::ss")));
      System.out.println(dateTime.format(DateTimeFormatter.BASIC_ISO_DATE));

      Instant timestamp = Instant.now();
      System.out.println("Default format of Instant="+timestamp);

      LocalDateTime dt = LocalDateTime.parse("27::Apr::2014 21::39::48",
              DateTimeFormatter.ofPattern("d::MMM::uuuu HH::mm::ss"));
      System.out.println("Default format after parsing = "+dt);

      //=======向下兼容，允许时间类型的转换=======//
      //Date to Instant
      Instant ts = new Date().toInstant();
      //Now we can convert Instant to LocalDateTime or other similar classes
      LocalDateTime d = LocalDateTime.ofInstant(ts,
              ZoneId.of(ZoneId.SHORT_IDS.get("PST")));
      System.out.println("Date = "+d);

      //Calendar to Instant
      Instant time = Calendar.getInstance().toInstant();
      System.out.println(time);
      //TimeZone to ZoneId
      ZoneId defaultZone = TimeZone.getDefault().toZoneId();
      System.out.println(defaultZone);

      //ZonedDateTime from specific Calendar
      ZonedDateTime gregorianCalendarDateTime = new GregorianCalendar().toZonedDateTime();
      System.out.println(gregorianCalendarDateTime);

      //Date API to Legacy classes
      Date dt2 = Date.from(Instant.now());
      System.out.println(dt2);

      TimeZone tz = TimeZone.getTimeZone(defaultZone);
      System.out.println(tz);

      GregorianCalendar gc = GregorianCalendar.from(gregorianCalendarDateTime);
      System.out.println(gc);


      SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
      String formatDate = simpleDateFormat.format(new Date());

      Date dateParsed  = simpleDateFormat.parse("2019-01-01");

      Date date1 = Date.from(ldt1.atZone(ZoneId.systemDefault()).toInstant());
      LocalDateTime dt1 = LocalDateTime.ofInstant(date1.toInstant(),ZoneId.systemDefault());

      ldt1.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));


  }

}
