> 这边并有打算去解决HashMap源码，这边是就今天考到的一个知识忙点做一下总结

> How to initialize the HashMap with default values?

### Method-1: static block 
```text
 public static final Map<String,Weekday> weekdayMap;

    static {
        Map<String,Weekday> aMap = new HashMap<String,Weekday>();
        aMap.put("星期一", Weekday.MONDAY);
        aMap.put("星期二", Weekday.TUESDAY);
        aMap.put("星期三", Weekday.WEDNESDAY);
        aMap.put("星期四", Weekday.THURSDAY);
        aMap.put("星期五", Weekday.FRIDAY);
        aMap.put("星期六", Weekday.SATURDAY);
        aMap.put("星期日", Weekday.SUNDAY);

        weekdayMap = Collections.unmodifiableMap(aMap);
   }
```

### Method-2(not recommend) : Anonymous internal classes ：with Memory Leak risk
```text
public static final Map<String,Weekday> weekdayMap = new HashMap<String,Weekday>(){{
        put("星期一", Weekday.MONDAY);
        put("星期二", Weekday.TUESDAY);
        put("星期三", Weekday.WEDNESDAY);
        put("星期四", Weekday.THURSDAY);
        put("星期五", Weekday.FRIDAY);
        put("星期六", Weekday.SATURDAY);
        put("星期日", Weekday.SUNDAY);

    }};
```

### Method-3: with the of from ImmutableMap : but limit at most 5 KV pairs
```text
/*Map<String, Weekday> left = ImmutableMap.of(
               "星期一", Weekday.MONDAY,
               "星期二", Weekday.TUESDAY,
               "星期三", Weekday.WEDNESDAY,
               "星期四",Weekday.THURSDAY,
               "星期五",Weekday.FRIDAY,
               "星期六",Weekday.SATURDAY,
               "星期日",Weekday.SUNDAY);*/
```
### Method-4 :with the builder from ImmutableMap
```text
  public static final Map<String, Weekday> weekdayMap = ImmutableMap.<String, Weekday>builder()
            .put("星期一", Weekday.MONDAY)
               .put("星期二", Weekday.TUESDAY)
               .put("星期三", Weekday.WEDNESDAY)
               .put("星期四",Weekday.THURSDAY)
               .put("星期五",Weekday.FRIDAY)
               .put("星期六",Weekday.SATURDAY)
               .put("星期日",Weekday.SUNDAY)
               .build();
```