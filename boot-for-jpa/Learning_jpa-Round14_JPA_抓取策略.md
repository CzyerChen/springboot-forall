> 从hibernate 到HQL 到JPQL ，功利的知道他们怎么用只是一个方面，现在来具体学习一下
> 开发时开启hibernate.format_sql   hibernate.use_sql_comments
## Hibernate 与DB 的抓取策略 The object-retrieval options
- hibernate 开启persistence context，通过实体对象的get方法来自动获取对象数据
- 通过ID主键进行查询
- 通过HQL/JPQL进行查询
- 通过Criteria接口，QBC/QBE 进行查询
- 通过jdbc进行SQL查询，包括调用存储过程

### JPA QL/HQL
- JPA QL 是HQL的一个子集，因而JPA QL总是一个HQL
- HQL适合抓取，但是对增删改就没有这么灵活

#### HQL的功能
```text
Session session = getSessionFactory().getCurrentSession();
Query query = session.createQuery("from User as u where u.firstname = :fname");
query.setString("fname", "John");
List list = query.list();
```
- HQL能够支持的功能有：
```text
1. 查询条件支持使用关联对象的属性

2. 只查询entity的某些属性，不会加载entity全部属性到persistence context. 这叫report query或是projection。

3.查询结果排序

4. 分页

5. 使用group by, having聚合(aggregation)，以及聚合函数，比如：sum, min, max/min.

6. outer join，当在一行要查询多个对象时

7. 可以调用标准或用户定义的SQL function.

8. 子查询(subqueries/nested queries)

9. Criteria ： QBC-query by criteria

10. Querying by example

```

### Hibernate 的特点
#### 延迟加载 -- 粗粒度控制
- Hibernate对所有的实体和集合默认使用延迟加载策略
```text
指定查询一个对象

会在persistence context当中映射一个代理对象，能返回代理对象，就不回去查询数据库

第一次查询会到数据库，通过ID的查询也暂时会返回代理对象，真正需要访问对象其他数据的时候才会查询数据库
当获取一个Item实例，不管是通过get()方法，还是load()方法然后再访问非ID的属性强迫初始化

针对数据比较多的集合，Hibernate进一步提供了Extra lazy，即使在调用集合方法size(), contains(), isEmpty()时，也不会去真正查询集合中的对象。
如果集合是Map或List，containsKey(), get()方法会直接查询数据库

```
```text
/ find()相当于Hibernate的get()
Item item = entityManager.find(Item.class, new Long(123));

// getReference()相当于Hibernate的load()
Item itemRef = entityManager.getReference(Item.class, new Long(1234));
```
- 使用Hibernate做为JPA实现，默认是启用代理的，可以为某个实体类设置关闭代理
```text
@Entity
@Table(name = "USERS")
@org.hibernate.annotations.Proxy(lazy = false)//取消延迟加载，取消代理，那就直接查询数据库
public class User implements Serializable, Comparable {
    // ...
}
```
- 在使用JPA关联映射的时候，就有关于延迟加载的配置
```text

    @ManyToOne(fetch = FetchType.EAGER)
    private User seller;

//如果是懒加载，就不能够访问到这边关联的对象，会抛出LazyInitializationException
    @ManyToOne(fetch = FetchType.LAZY)
    private User approvedBy;
```
- JPA同Hibernate的fetch plan不同，尽管Hibernate中所有的关联实体都默认是延迟加载的，但@ManyToOne和@OneToOne关联映射默认是FetchType.EAGER，但是部分JPA并没有实现懒加载，因而在to-one的部分都是默认LAZY

#### Hibernate 抓取策略
- 是延迟加载，也有三种策略 Fetch on Demand(默认) / Batch fetching /Prefetch in batch/eager fetching
- fetch on demand : 就是有访问需求的时候就去查询，一条记录就是一个select ,如果关联对象比较多的时候，压力还是比较大的
- batch fetching : 批量获取，就是将多条SQL转化为一次查询来完成，能够很好的减少数据库的压力,但是这个批量的数据集可能不是必须的，造成不必要的数据查询
- prefetch in batch : 为集合设置subselect fetching抓取策略，subselect只支持collection
- eager fetching: 不论三七二十一，把所有关联的对象都加载
- dynamic fetching strategy (利用HQL/Criteria)
- Hibernate.initialize()可以强制初始化proxy or collection wrapper，但是针对collection，集合中的每一个引用也只是初始化为一个proxy，不会真正初始化每个集合中的引用
```text
//batch fetching
@Entity
@Table(name = "USERS")
@org.hibernate.annotations.BatchSize(size = 10)
public class User { ... }

@Entity
public class Item {
    @OneToMany
    @org.hibernate.annotations.BatchSize(size = 10)
    private Set<Bid> bids = new HashSet<Bid>();
}

//prefetching in batch
@OneToMany
@org.hibernate.annotations.Fetch(
org.hibernate.annotations.FetchMode.SUBSELECT
)
private Set<Bid> bids = new HashSet<Bid>();
```
- 以上是一些批量操作，为了关联实体不进行多次select，而预加载一批量的对象，本来多条的select 语句会被转化成一条批量查询语句，减小了数据库的压力

#### egaer fetch
 - 我们通常想用many one的关系直接关联对象，我们就会采用eager的策略来获取对象，那么他是通过什么方式获取数据的呢？
 - fetch = FetchType.EAGER等价与XML映射中的fetch="join"，也就是通过表关联的方式，并且如果说明 not-null = true就会使用内连接Inner join,不然使用外连接，Outer join
 - 如果设置lazy="false",也能够禁用延迟加载，但是方式不同，就是通过访问每一个对象时候，通过一条select语句来实现对象的访问
 ```text
    @ManyToOne(fetch = FetchType.EAGER)
    @org.hibernate.annotations.Fetch(
        org.hibernate.annotations.FetchMode.SELECT
    )
    
    @OneToMany(fetch = FetchType.EAGER)
    private Set<Bid> bids = new HashSet<Bid>();
```  
### N+1问题
- 所谓N+1是指查询，查询出来一个符合条件的列表数量为N ，并且内部有一个关联的列表，总共需要N+1次select
- 可以使用prefetch inbatch 或者prefetch with subselect 来减少，但是还是会有延迟加载的问题
- 如果直接使用eager方式，就会在join的时候出现笛卡尔积的问题，因而需要谨慎，至少最好不要在全局进行使用


#### 总结
- 全局的映射(global mapping)，应该选择prefetch in batch来避免N+1问题
- 某些case下，你确实需要关联的collection而又不想在global mapping中设置eager fetching with join
- 针对特定case利用HQL/Criteria(可以称为dynamic fetching strategy)来避免N+1问题
- eager fetching with join对于<many-to-one>或<one-to-one>这种association，是种比较好的避免N+1问题的策略，因为这种to-one的关联不会造成笛卡尔积问题
- Hibernate.initialize()可以强制初始化proxy or collection wrapper，但是针对collection，集合中的每一个引用也只是初始化为一个proxy，不会真正初始化每个集合中的引用

