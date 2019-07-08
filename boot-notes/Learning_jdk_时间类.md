> jdk的时间类在8中有很大的变化，之前了解到这个，主要是因为SimpleDateFomat会在多线程的情况下，发生混乱的问题（虽然说建议ThreadLocal来存放时间就好了，可是就是想再体验一下），因而就愈发开始关注时间的format

> 对比SimplDateFormat和现在的操作，来实现时间parse和时间format的操作

### jdk8之前
- Date作为时间
- SimpleDateFormat作为操作类
#### String -> Date
- SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
- Date dateParsed  = simpleDateFormat.parse("2019-01-01");

#### Date -> String
- SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
- String formatDate = simpleDateFormat.format(new Date());

### jdk8及之后
- 推荐用Instant作为时间,他们的转变也很方便
- 一些基础转换
```text
Date 转 Instant
 Instant instant = new Date().toInstant();

Instant 转 Date
 Date from = Date.from(Instant.now());
 
LocalDateTime转Date
  Date date1 = Date.from(ldt1.atZone(ZoneId.systemDefault()).toInstant());
  
Date 转LocalDateTime
  LocalDateTime dt1 = LocalDateTime.ofInstant(date1.toInstant(),ZoneId.systemDefault());
```
- DateTimeformatter作为操作类

#### String -> LocalDateTime
```text
String str = "1986-04-08 12:30";
DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
LocalDateTime dateTime = LocalDateTime.parse(str, formatter);
```

#### LocalDateTime -> String 
```text
localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
```

#### Date --> LocalDate
```text
        Date date = new Date();
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();

        // atZone()方法返回在指定时区从此Instant生成的ZonedDateTime。
        LocalDate localDate = instant.atZone(zoneId).toLocalDate();
```
#### LocalDate --> Date
```text
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDate localDate = LocalDate.now();
        ZonedDateTime zdt = localDate.atStartOfDay(zoneId);

        Date date = Date.from(zdt.toInstant());
```
