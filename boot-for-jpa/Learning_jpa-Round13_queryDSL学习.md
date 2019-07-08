> 在看到JPA的时候，发现查询都是很简单，不需要涉及原生的SQL，利用一些对象的方法就可以实现查询，是最终转化为HQL在于DB层面去交互的

> 今天就来学习一下queryDSL

> 能很好地支持 单表查询、多表查询、多表关联、复杂表达式、分页查询、数据库函数、子查询、排序、对接JpaRepository的对象

> [内容部分参考](https://www.jianshu.com/p/69dcb1b85bbb)

### 什么是queryDSL
- QueryDSL仅仅是一个通用的查询框架，专注于通过Java API构建类型安全的SQL查询
- Querydsl可以通过一组通用的查询API为用户构建出适合不同类型ORM框架或者是SQL的查询语句,也就是这是ORM之上的一个查询框架
- 借助QueryDSL可以在任何支持的ORM框架或者SQL平台上以一种通用的API方式来构建查询
- 目前QueryDSL支持的平台包括JPA,JDO,SQL,Java Collections,RDF,Lucene,Hibernate Search

### 添加maven依赖
```text
  <dependency>
            <groupId>com.querydsl</groupId>
            <artifactId>querydsl-jpa</artifactId>
            <version>${querydsl.version}</version>
        </dependency>
        <dependency>
            <groupId>com.querydsl</groupId>
            <artifactId>querydsl-apt</artifactId>
            <version>${querydsl.version}</version>
            <scope>provided</scope>
        </dependency>
```
### maven compile
- 添加依赖之后，如果与数据库对应的实体类已经书写完毕，那就执行mvn compile操作，就能够生成对应的查询类
- 可以看到target下面，classes里面有自动生成的QXXXXX

### QueryDSL例子 -- JPA和queryDSL结合
- 可以很好的进行查询，不论是单表还是多表关联，都可以提供很好的接口，也能够和JPA通过QuerydslPredicateExecutor接口做很好的衔接
- 也能够很自如地处理分页的数据，利用JPA自带的Pageable就可以了 
- 本地查询主要使用EntityManager接口里的方法
```text
public interface EntityManager {
   public void persist(Object entity);
   public <T> T find(Class <T> entityClass, Object primaryKey);
   public <T> T getReference(Class <T> entityClass, Object primaryKey);
   public <T> T merge(T entity);
   public void remove(Object entity);
   public void lock(Object entity, LockModeType lockMode);

   public void refresh(Object entity);
   public boolean contains(Object entity);
   public void clear( );

   public void joinTransaction( );
   public void flush( );
   public FlushModeType getFlushMode( );
   public void setFlushMode(FlushModeType type);

   public Query createQuery(String queryString);
   public Query createNamedQuery(String name);
   public Query createNativeQuery(String sqlString);
   public Query createNativeQuery(String sqlString, String resultSetMapping);
   public Query createNativeQuery(String sqlString, Class resultClass);

   public Object getDelegate( );

   public void close( );
   public boolean isOpen( );
}
主要是以下两种
createNativeQuery  --- 需要立刻传递参数
createNamedQuery   --- 可以先编译sql 后通过实体类传递参数
```
- 首先，在现有的HumanRepository上添加接口QuerydslPredicateExecutor
- 其次，在自定义的repo实现中进行查询自定义
```text
 public void getHuman(){
        List humans = entityManager.createNativeQuery("select * from t_human").getResultList();
        entityManager.createNativeQuery("update t_human set name ='lily' where id = 1").executeUpdate();

    }

    public void getHuman1(){
        //动态条件
        QHuman qtCity = QHuman.human;
        //分页排序
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC,"id"));
        PageRequest pageRequest = new PageRequest(0,10,sort);

        QHuman human1 = QHuman.human;
        com.querydsl.core.types.Predicate predicate = human1.age.longValue().lt(40).and(human1.name.like("claire"));
        getHuman2(predicate);
        humanRepository.findAll(predicate,pageRequest);
    }


    public  void getHuman2(com.querydsl.core.types.Predicate predicate){
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        //多表关联
        JPAQuery<Tuple> jpaQuery = jpaQueryFactory.select(QHuman.human, QMail.mail)
                .from(QHuman.human)
                .leftJoin(QMail.mail)
                .on(QHuman.human.emailId.longValue().eq(QMail.mail.id.longValue()));

        jpaQuery.where(predicate);
        //fetchCount的时候上面的orderBy不会被执行 不用太担心性能问题
        long total = jpaQuery.fetchCount();
        List<Tuple> fetch = jpaQuery.fetch();
    }

    public  void getHuman3(com.querydsl.core.types.Predicate predicate, Pageable pageable){
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        JPAQuery<Tuple> jpaQuery = jpaQueryFactory.select(QHuman.human, QMail.mail)
                .from(QHuman.human)
                .leftJoin(QMail.mail)
                .on(QHuman.human.emailId.longValue().eq(QMail.mail.id.longValue()));

        jpaQuery.where(predicate);
        jpaQuery.offset(pageable.getOffset())
                .limit(pageable.getPageSize());
        /**
         * 和上面不同之处在于这里使用了offset和limit限制查询结果.并且返回一个QueryResults,该类会自动实现count查询和结果查询,并进行封装
         */
        QueryResults<Tuple> tupleQueryResults = jpaQuery.fetchResults();

    }

```

### 下面有更多例子 --- 纯queryDSL的书写
- 包括一些实体的映射，不需要写SqlResultSetMapping,一些复杂的条件查询，和Specification做很好的条件组合，做表关联查询，使用MYSQL的函数等
```text
public void getHuman4() {
        QHuman qHuman = QHuman.human;
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        //查询字段-select()
        List<String> nameList = queryFactory.select(qHuman.name).from(qHuman).fetch();
        //查询实体-selectFrom()
        List<Human> memberList = queryFactory.selectFrom(qHuman).fetch();
        //查询并将结果封装至dto中
        List<HumanDTO> dtoList = queryFactory.select(Projections.constructor(HumanDTO.class, qHuman.name, qHuman.age)).from(qHuman)/*.leftJoin(qm.favoriteInfoDomains, qf)*/.fetch();
        //去重查询-selectDistinct()
        List<String> distinctNameList = queryFactory.selectDistinct(qHuman.name).from(qHuman).fetch();
       //获取首个查询结果-fetchFirst()
        Human firstMember = queryFactory.selectFrom(qHuman).fetchFirst();
       //获取唯一查询结果-fetchOne()
       //当fetchOne()根据查询条件从数据库中查询到多条匹配数据时，会抛`NonUniqueResultException`。
        /**
         * com.querydsl.core.NonUniqueResultException: Only one result is allowed for fetchOne calls
         *
         * 	at com.querydsl.jpa.impl.AbstractJPAQuery.fetchOne(AbstractJPAQuery.java:258)
         * 	at com.forjpa.repository.HumanRepositoryImpl.getHuman4(HumanRepositoryImpl.java:238)
         * 	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
         * 	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
         * 	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
         * 	at java.lang.reflect.Method.invoke(Method.java:498)
         * 	at org.springframework.data.repository.core.support.RepositoryComposition$RepositoryFragments.invoke(RepositoryComposition.java:377)
         * 	at org.springframework.data.repository.core.support.RepositoryComposition.invoke(RepositoryComposition.java:200)
         * 	at org.springframework.data.repository.core.support.RepositoryFactorySupport$ImplementationMethodExecutionInterceptor.invoke(RepositoryFactorySupport.java:629)
         * 	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:185)
         */
        //Human anotherFirstMember = queryFactory.selectFrom(qHuman).fetchOne();
    }


    public void getHuman5() {
        //动态条件
        QHuman qHuman = QHuman.human;
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        //where后面就一个条件
        List<Human> fetch = jpaQueryFactory.selectFrom(qHuman).where(qHuman.name.like('%' + "claire" + '%')).fetch();

        //where后面几个条件
        List<Human> fetch1 = jpaQueryFactory.selectFrom(qHuman).where(qHuman.name.like('%' + "claire" + '%').and(qHuman.age.longValue().gt(10))).fetch();
        //或
        com.querydsl.core.types.Predicate predicate = qHuman.age.longValue().lt(40).and(qHuman.name.like('%'+"claire"+'%'));
        Iterable all = humanRepository.findAll(predicate);
        //或，一些别的复杂查询,可以用BooleanBuilder ,builder也可以并行或者嵌套
        BooleanBuilder builder = new BooleanBuilder();
        //like
        builder.and(qHuman.name.like('%'+"Claire"+'%'));
        //contain
       // builder.and(qHuman.address.contains("shanghai"));
        //equal示例
        //builder.and(qHuman.status.eq("1"));
        //between
        builder.and(qHuman.age.between(20, 30));
        List<Human> memberConditionList = jpaQueryFactory.selectFrom(qHuman).where(builder).fetch();
    }
    
     
    public  void getHuman6(){
        QHuman qHuman = QHuman.human;
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        //聚合函数-avg()
        Double averageAge = queryFactory.select(qHuman.age.avg()).from(qHuman).fetchOne();

        //聚合函数-concat()
        String concat = queryFactory.select(qHuman.name.concat(qHuman.address)).from(qHuman).fetchOne();

       //聚合函数-date_format()
        String date = queryFactory.select(Expressions.stringTemplate("DATE_FORMAT({0},'%Y-%m-%d')", qHuman.createTime)).from(qHuman).fetchOne();

        //子查询
        List<Human> subList = queryFactory.selectFrom(qHuman).where(qHuman.age.in(JPAExpressions.select(qHuman.age).from(qHuman))).fetch();
    }
```
### 使用Template实现QueryDSL未支持的语法
```text
        public  void getHuman7(){
        QHuman qHuman = QHuman.human;
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        //使用booleanTemplate
        List<Human> list = queryFactory.selectFrom(qHuman).where(Expressions.booleanTemplate("{} = \"claire\"", qHuman.name)).fetch();
        //booleanTemplate中多个占位
        List<Human> list1 = queryFactory.selectFrom(qHuman).where(Expressions.booleanTemplate("{0} = \"claire\" and {1} = \"shanghai\"", qHuman.name,qHuman.address)).fetch();

        //使用stringTemplate
        String date = queryFactory.select(Expressions.stringTemplate("DATE_FORMAT({0},'%Y-%m-%d')", qHuman.createTime)).from(qHuman).fetchFirst();
        //在where子句中使用stringTemplate
        Integer id = queryFactory.select(qHuman.id).from(qHuman).where(Expressions.stringTemplate("DATE_FORMAT({0},'%Y-%m-%d')", qHuman.createTime).eq("2018-03-19")).fetchFirst();
    }

```

### nativeQuery = true 
- nativeQuery 表明是本地查询，意思是数据库原生语句，？或者：对应的是指定参数的值，不需要做其他转换，就能够直接被数据库执行
- 如果没有nativeQuery=true 就不是原生的SQL语句，表名可能就不是t_user而是User了，字段也不是user_name而是userName了


### JPA 使用NamedNativeQuery+SqlResultSetMapping对返回数据封装
- 需求：对多个表（实体）查询进行sql查询，然后返回都是相同格式的数据，可能是返回多个参数，想要用同一个Response Entity Mapping，那这个很适合
- 能够实现类级别的自定义本地查询，能够将查询放置到用户Repository下当普通方法使用
- 本质：在结果映射的时候在顶层抽象方法有一个是将resultSet或者tuple元组的数据映射到实体类上，只要这一步我们实现了，其实不用这一套Mapping都是可以的

#### 实现步骤
- 步骤一：在实体类上书写NamedQuery
```text
@Entity
@Table(name = "t_human")
@Access(AccessType.FIELD)
@NamedNativeQuery(name = "testq",query = "select name,phone from t_human where name like ?1 ",resultSetMapping = "HumanEntry")
public class Human {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  int id;
    private String name;
    private int emailId;
    @Transient
    private String mobile;
    private int age;
 }
```
- 步骤二：书写映射实体
```text
@Data
@Entity
@SqlResultSetMapping(name = "HumanEntry",
entities = {@EntityResult(entityClass = HumanEntry.class)})
public class HumanEntry {
    @Id
    private String name;
    private String phone;
}

```
- 步骤三：书写调用
```text
种类一：通过EntityManager,不传递参数就可以直接拿名字进行方法的调用，如果使用参数，就可以用setParameter的方式传入
    @Autowired
    private EntityManager entityManager;
   @Test
    public void test22() {
        Query testq = entityManager.createNamedQuery("testq");
        testq.setParameter(1,"cc");
        List<HumanEntry> resultList = testq.getResultList();
        if(resultList!= null){
            resultList.forEach((HumanEntry::toString));
        }
    }


种类二：和用户repository整合
请注意这边的NamedNativeQuery的name加上了实体类的限制Human.testq
@Entity
@Table(name = "t_human")
@Access(AccessType.FIELD)
@NamedNativeQuery(name = "Human.testq",query = "select name,phone from t_human where name like ?1 ",resultSetMapping = "HumanEntry")
public class Human {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  int id;
    private String name;
    private int emailId;
    @Transient
    private String mobile;
    private int age;
 }
 
然后在repo中书写方法：
就想普通的jpa规则的方法一样，直接声明，在扫描的时候能够找到方法并执行
List<HumanEntry> testq(String name);

测试一下：
@Test
    public void test23(){
        List<HumanEntry> entries = humanRepository.testq("cc");
        if(entries!= null){
            entries.forEach(HumanEntry::toString);
        }
    }

```


