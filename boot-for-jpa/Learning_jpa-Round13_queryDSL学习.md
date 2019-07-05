> 在看到JPA的时候，发现查询都是很简单，不需要涉及原生的SQL，利用一些对象的方法就可以实现查询，是最终转化为HQL在于DB层面去交互的

> 今天就来学习一下queryDSL

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

### QueryDSL例子
- 可以很好的进行查询，不论是单表还是多表关联，都可以提供很好的接口，也能够和JPA通过QuerydslPredicateExecutor接口做很好的衔接
- 也能够很自如地处理分页的数据，利用JPA自带的Pageable就可以了 
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
        JPAQuery<Tuple> jpaQuery = jpaQueryFactory.select(QHuman.human, QMail.mail)
                .from(QHuman.human)
                .leftJoin(QMail.mail)
                .on(QHuman.human.emailId.longValue().eq(QMail.mail.id.longValue()));

        jpaQuery.where(predicate);
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