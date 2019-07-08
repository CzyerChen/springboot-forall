> 有时候我们会觉得简单的单表的字段查询不能满足需求，我希望用原生SQL自己去做查询，这种自定义怎么实现呢？

### 方法一：使用@Query注解开启SQL支持
- 这不是JPA想要做的设计里面，为了扩大市场，增加对mybatis转型者的友好支持，增加对初学者的友好支持，就给出了自定义SQL的操作
- 简单使用@Query,建立本地查询
```text
    //方法一：使用参数 like
    @Query(value = "select id,name,email,phone from t_human where email like %:email% ",nativeQuery = true)
    Human findByEmailMatch(@Param("email") String email);

    //使用参数 = 
    @Query(value = "select * from t_human where name=:name",nativeQuery = true) //这边注意冒号等号和参数之间不能有空格，不然无法识别冒号
    Human findHumanByName(@Param("name")String name);

    //方法二：不使用参数，用占位符取代参数列表里的参数
    @Query(value = "select * from t_human t where t.name=?1", nativeQuery = true) //这个映射简单粗暴
    List<Human> findByName(String name);
    
    //方法三：动态替换表名，这个表名源于@Entity(name= "t_human") entity注入后会缓存起来
    @Query(value = "select * from #{#entityName} t where t.name=?1", nativeQuery = true)
    List<Human> findByName1(String name);
```
#### 补充一个最新学习JPA的投影
- 需求:目前用到JPA都会返回对象，一般都是源对象，如果在对象字段很多的时候，其实我们的查询不应该返回整个对象，而是一个承载数据的DTO
- 之前在学习QueryDSL的时候，了解到可以通过映射的方式，减少一些返回的字段，或者是通过Projections.constructor（其实根本也是投影），来实现DTO字段的映射
- 但是其实JPA本身就是支持投影的，也看到网上说资料确实比较少，所以很多博客中都忽略了
- 步骤一：定义一个承接数据的DTO
```text
public interface HumanDTOs {
    @Value("#{target.name + ' ' + target.age}") //还可以对返回字段进行操作
    String getFull();

    String  getName();

    Integer  getAge();
}

```
- 步骤二：进行SQL的书写
```text

    @Query("select c.name as name ,c.age as age from Human c")//
    Collection<HumanDTOs> findAllProjectedBy(); 
```
- 步骤三：测试，没有问题
```text
 @Test
    public void test25(){
        Collection<HumanDTOs> humanByProjection = humanRepository.findAllProjectedBy();
        humanByProjection.forEach(h ->{
            Integer age = h.getAge();
            String name = h.getName();
            String full = h.getFull();
        });
    }
```

### 方法二：使用自定义查询类
- 创建EntityNameRepositoryCustom 接口，创建EntityNameRepositoryImpl 类实现EntityNameRepositoryCustom接口的方法
- 在EntityNameRepository处添加继承EntityNameRepositoryCustom
```text
public interface HumanRepository  extends JpaRepository<Human,Integer>, JpaSpecificationExecutor<Human>,HumanRepositoryCustom 

public class HumanRepositoryImpl implements HumanRepositoryCustom {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private HumanRepository humanRepository;

    /**
     * 可以自定义查询，不过sql的方式还是有点暴力
     *
     * @param mail
     * @return
     */
    @Override
    public List<Human> getHumanByMail(String mail) {
        List<Human> list = new ArrayList<>();
        String sql = "select * from t_human where email like '%" + mail + "%'";
        /*List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql);
        Iterator<Map<String, Object>> iterator = maps.iterator();
        while (iterator.hasNext()) {
            Map<String, Object> next = iterator.next();
            Human human = new Human();
            int id = Integer.valueOf(next.get("id").toString());
            String name = String.valueOf(next.get("name"));
            String email = String.valueOf(next.get("email"));
            String phone = String.valueOf(next.get("phone"));
            int age = Integer.valueOf(next.get("age").toString());
            human.setId(id);
            human.setName(name);
            human.setPhone(phone);
            human.setEmail(email);
            human.setAge(age);
            list.add(human);
        }*/
        jdbcTemplate.query(sql, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {
                Human human = new Human();
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String phone = resultSet.getString("phone");
                int age = resultSet.getInt("age");
                human.setId(id);
                human.setName(name);
                human.setEmail(email);
                human.setPhone(phone);
                human.setAge(age);
                list.add(human);
            }

        });
        return list;
    }


    /**
     * 需要集成JpaSpecificationExecutor<Human>,实现复杂查询
     *
     * @param name
     * @param email
     * @param maxAge
     * @return
     */
    @Override
    public List<Human> getHuman(String name, String email, int maxAge) {
        Specification<Human> specification = new Specification<Human>() {

            @Override
            public Predicate toPredicate(Root<Human> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                //用于暂时存放查询条件的集合
                List<Predicate> predicatesList = new ArrayList<>();
                //--------------------------------------------
                //查询条件示例
                //equal示例
                if (!StringUtils.isEmpty(name)) {
                    Predicate nameP = cb.equal(root.get("name"), name);
                    predicatesList.add(nameP);
                }
                //like示例
                if (!StringUtils.isEmpty(email)) {
                    Predicate emailP = cb.like(root.get("email"), '%' + email + '%');
                    predicatesList.add(emailP);
                }
                //between示例
                Predicate ageP = cb.between(root.get("age"), 10, maxAge);
                predicatesList.add(ageP);


                //关联表查询示例 老师课程之类的信息
              /*  if (!StringUtils.isEmpty(courseName)) {
                    Join<Student,Teacher> joinTeacher = root.join("teachers",JoinType.LEFT);
                    Predicate coursePredicate = cb.equal(joinTeacher.get("courseName"), courseName);
                    predicatesList.add(coursePredicate);
                }*/

                //复杂条件组合示例  成绩的一些约束
                /*if (chineseScore!=0 && mathScore!=0 && englishScore!=0 && performancePoints!=0) {
                    Join<Student,Examination> joinExam = root.join("exams",JoinType.LEFT);
                    Predicate predicateExamChinese = cb.ge(joinExam.get("chineseScore"),chineseScore);
                    Predicate predicateExamMath = cb.ge(joinExam.get("mathScore"),mathScore);
                    Predicate predicateExamEnglish = cb.ge(joinExam.get("englishScore"),englishScore);
                    Predicate predicateExamPerformance = cb.ge(joinExam.get("performancePoints"),performancePoints);
                    //组合
                    Predicate predicateExam = cb.or(predicateExamChinese,predicateExamEnglish,predicateExamMath);
                    Predicate predicateExamAll = cb.and(predicateExamPerformance,predicateExam);
                    predicatesList.add(predicateExamAll);
                }*/
                //--------------------------------------------
                //排序示例(先根据id排序，后根据名字排序)
                query.orderBy(cb.asc(root.get("id")), cb.asc(root.get("name")));
                //--------------------------------------------
                Predicate[] predicates = new Predicate[predicatesList.size()];
                return cb.and(predicatesList.toArray(predicates));
            }


        };

        return humanRepository.findAll(specification);
    }
}
```
- 如果觉得自己写specification很麻烦的话，[有第三方库支持](https://github.com/wenhao/jpa-spec),可以想queryDSL一样有简单的接口传递参数
```text
<dependency>
    <groupId>com.github.wenhao</groupId>
    <artifactId>jpa-spec</artifactId>
    <version>3.1.1</version>
</dependency>
```
- 上面都是除了单表的业务需求，接下来看一下多表的Specification如何处理
[来自社区的教程,有一个基础的认识，复杂的多表任务也可以很好的应对](http://www.spring4all.com/article/164)

- 更多例子来自于社区[单表的Specification工厂](http://www.spring4all.com/article/471) ,[多表的关联查询](http://www.spring4all.com/article/472)





### JPA的一些本地查询
#### 单表的删除和更新
- 删除 deleteById, deleteAll,delete某一个对象，查询出来再删除
- 更新 save的那一套，查询出来再删除
- 本地SQL
```text
@Modifying  ---- 注意这个注解，不能缺少
@Transactional
@Query("delete from human h where h.id = ?1")
int deleteByHumanId(Integer id)

或者用EntityManager实现
HashSet<Transaction> transactions = new HashSet<Transaction>();
... 
entityManager.createQuery(
  "DELETE FROM Transaction e WHERE e IN (:transactions)").
  setParameter("transactions", new ArrayList<Transaction>(
  transactions)).executeUpdate();

```

#### 范围查询
- 使用EntityManager的本地查询接口
```text
Query query = entityManager.createQuery(“select * from Users where name in(?name)”);
query.setParameter(“name”, names);//names是一个name集合

另一种写法，一种JPQL 一种HQL
String sql = "DELETE FROM fb_user_role  WHERE role_id IN (:roleId)";
entityManager.createQuery(sql).setParameter("roleId", new ArrayList<Integer>(
                roleId)).executeUpdate();

```



### 多表进行delete和update
- JPA我们知道对单表的操作是很方便的，但是多表联合的删除和更新怎么实现呢？
- 可以使用QueryDsl的多表关联查询，或者通过EntityManager来执行本地SQL


### 补充又一个新知识:JPA中@Query也可以来做分页
- 虽然说分页处理有很多很优雅的做法，比如原生的支持，比如Specification的支持，比如queryDSL也能很优雅的做分页处理
- 但是这里要分享的是@Query这样的SQL操作，也可以和上面三种一样来实现分页
- [原文地址](http://www.spring4all.com/article/290),这边只是做一个尝试
- 步骤
```text
现有基础上

1.继承MethodInterceptor，重写invoke方法执行其他代理获得Jpql返回结果集后

2.自定义RepositoryProxyPostProcessor，在postProcess时向Repository注入JpqlBeanMethodInterceptor

3.自定义JpaRepositoryFactoryBean，创建RepositoryFactory时，向其加入我们自定义的RepositoryProxyPostProcessor

4.JpaRepositoryFactoryBean起作用
@Configuration
@EnableJpaRepositories(repositoryFactoryBeanClass = GmRepositoryFactoryBean.class)
@EnableSpringDataWebSupport
public class JpaDataConfig {

}

```

### [Spring社区提供的JPA的进阶知识](http://www.iocoder.cn/Spring-Data-JPA/good-collection/)
```text
《技术专题讨论第二期总结：如何对 JPA 或者 MyBatis 进行技术选型》
《Spring Data Jpa 让@Query复杂查询分页支持实体返回》
《JPA的 多表 复杂查询 详细篇》
《Spring Boot 两种多数据源配置：JdbcTemplate、Spring-data-jpa》

《Repository方法名查询推导（Query Derivation From Method Names）的实现原理》
《Spring Data JPA 方法名查询推导的实现原理2后半部分》
《Spring Data JPA 源码阅读笔记：Repository方法名查询推导的实现原理 3 》
```
