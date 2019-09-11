- 我们在使用mybatis最原始的时候，我们每次执行完SQL,获取的对象字段都是数据库中的字段，也就是无法和实体类的对应上，那么现在是怎么解决的呢？
- [参考](https://my.oschina.net/u/2278977/blog/1795969)

#### 如果result是Type,映射到一个实体类
- 除了可以写一个字段的映射到一个实体类上，如果字段是正好的，并且只差驼峰转换，那么是可以通过以下配置，让其开启驼峰转换的：
```txt
mybatis.configuration.map-underscore-to-camel-case=true
```
#### 如果result是Map
- 上面的方法是只能对应到实体类上，这个属性的作用,是作用于javabean的field的，Map是不奏效的
- 有两种自定义的解决方案：
##### 方案一：继承HashMap,重写Put函数,将mybatis返回的Map,写上自定义Map的路径,自定义的Map
- 这个其实就是在放入map的地方进行转换，其实和mybatis的核心关联也不太大了

##### 方案二：找到mybatis的Handler,通过Handler来找到转换映射关系的接口定义,继承或者实现接口,然后走自定义的转换规则来实现Map映射的驼峰转换
- 这个方法，其实就是研究mybatis,改mybatis的部分源码，感觉挑战会大一些
- 详细步骤都在这个参考里面： [参考](https://my.oschina.net/u/2278977/blog/1795969)，写的很清晰，步骤也不繁琐，并且比较实用，开始抛弃那些写烂的utils吧
- 学到一个有用的东西：
```txt
       //CaseFormat是引用的 guava库,里面有转换驼峰的,免得自己重复造轮子,pom添加
            /**
             **         <dependency>
             <groupId>com.google.guava</groupId>
             <artifactId>guava</artifactId>
             <version>27.1-jre</version>
             </dependency>
             **/
    return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL,name);
    //第一次接触，再也不用度娘找一些什么驼峰互转的东西了
```
