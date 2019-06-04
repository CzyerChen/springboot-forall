> 之前看到文章，描述JDK7及以前的时间修改类，存在线程不安全的问题，会导致内部状态混乱，我们看看JDK8中新出的时间类都是怎么处理的
[参考知识点](http://www.importnew.com/14140.html)

### JDK 时间日期API的处理原则
- 不变性：新的日期/时间API中，所有的类都是不可变的，将方法静态化
- 关注点分离：新的API将人可读的日期时间和机器时间（unix timestamp）明确分离 
日期（Date）、时间（Time）、日期时间（DateTime）、时间戳（unix timestamp）以及时区定义了不同的类
- 清晰：在所有的类中，方法都被明确定义用以完成相同的行为,为了更好的处理问题，所有的类都使用了工厂模式和策略模式，一旦你使用了其中某个类的方法，与其他类协同工作并不困难。
- 实用操作：所有新的日期/时间API类都实现了一系列方法用以完成通用的任务，如：加、减、格式化、解析、从日期/时间中提取单独部分
- 可扩展性：新的日期/时间API是工作在ISO-8601日历系统上的，但我们也可以将其应用在非IOS的日历上

### 时间日期包
- java.time包：
这是新的Java日期/时间API的基础包，所有的主要基础类都是这个包的一部分，如：LocalDate, LocalTime, LocalDateTime, Instant, Period, Duration等等。所有这些类都是不可变的和线程安全的，在绝大多数情况下，这些类能够有效地处理一些公共的需求。
- java.time.chrono包：
这个包为非ISO的日历系统定义了一些泛化的API，我们可以扩展AbstractChronology类来创建自己的日历系统。
- java.time.format包：
这个包包含能够格式化和解析日期时间对象的类，在绝大多数情况下，我们不应该直接使用它们，因为java.time包中相应的类已经提供了格式化和解析的方法。
- java.time.temporal包：
这个包包含一些时态对象，我们可以用其找出关于日期/时间对象的某个特定日期或时间，比如说，可以找到某月的第一天或最后一天。你可以非常容易地认出这些方法，因为它们都具有“withXXX”的格式。
- java.time.zone包：
这个包包含支持不同时区以及相关规则的类。

### 如何使用
```text
 public static void main(String[] args){
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

      
  }
```