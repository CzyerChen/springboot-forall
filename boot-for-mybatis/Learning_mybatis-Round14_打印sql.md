> springboot mybatis中如何打印执行的SQL？？

- 很简单，添加配置
- 在application.properties / application.yaml中添加以下配置：
```text
logging.level.com.testdemo.mapper=debug

logging:
  level:
     com.testdemo.mapper : debug

```
- 以上是针对所有DAO层的查询都打印，如果想针对某一个mapper，则细化就可以了
