> 这个问题的研究，源于异常OneToMany时候的异常，etchType在ManyToOne是EAGER的,在OneToMany上默认的是LAZY
```text
org.hibernate.lazyinitializationexception could not initialize proxy - no session
```

### Hibernate懒加载
- 延迟初始化错误是运用Hibernate开发项目时最常见的错误

- 如果对一个类或者集合配置了延迟检索策略，那么必须当代理类实例或代理集合处于持久化状态（即处于Session范围内）时，才能初始化它

- 如果在游离状态时才初始化它，就会产生延迟初始化错误

- Hibernate采用CGLIB工具来生成持久化类的代理类,CGLIB是一个功能强大的Java字节码生成工具，它能够在程序运行时动态生成扩展 Java类或者实现Java接口的代理类


### 原因

- 由于LAZY没有加载数据，因而你在获取一对多的多的数据的时候，此时查询已结束，无法获取session

### [解决办法](https://www.cnblogs.com/hellxz/p/9037597.html)
1.可以在Group中设置FetchType.EAGER,得到对应的User表，缓存。如下：
```text
@OneToMany(mappedBy="store",fetch=FetchType.EAGER )
@JoinColumn(name="store_id")
```
2.web.xml添加过滤器OpenSessionInViewFilter

3.spring boot的配置文件application.properties添加spring.jpa.open-in-view=true

4.spring boot的配置文件application.properties添加spring.jpa.properties.hibernate.enable_lazy_load_no_trans=true

5.在出问题的实体类上加@Proxy(lazy = false)

6.手动获取session
```text
以下代码先通过Session的load()方法加载Customer对象，然后访问它的name属性： 

tx = session.beginTransaction(); 
Customer customer=(Customer)session.load(Customer.class,new Long(1)); 
customer.getCname(); 
tx.commit(); 

```
### 附加：java.lang.StackOverflowError异常
- 这个属于比较基础性的错误，就是不重写tostring，或者使用默认实现都会带上彼此注入的属性，比如store中的List<Product>和Product中的store，所以就是重写ToString ,把他们去掉就好了