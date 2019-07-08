package com.forjpa.repository;

import com.forjpa.domain.Human;
import com.forjpa.domain.QHuman;
import com.forjpa.domain.QMail;
import com.forjpa.model.HumanDTO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.query.JpaQueryCreator;
import org.springframework.data.jpa.repository.query.JpaQueryExecution;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -04 - 26 15:25
 */
public class HumanRepositoryImpl implements HumanRepositoryCustom {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private HumanRepository humanRepository;
    @Autowired
    private EntityManager entityManager;

    /**
     * 可以自定义查询，不过sql的方式还是有点暴力
     *
     * @param mail
     * @return
     */
    public List<Human> getHumanByMail(String mail) {
        List<Human> list = new ArrayList<Human>();
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
                human.setEmailId(1);
                human.setMobile(phone);
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

                //复杂条件组合示例  成绩的一些约束,内连接，左连接，右连接
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

    public void getHuman() {
        List humans = entityManager.createNativeQuery("select * from t_human").getResultList();
        entityManager.createNativeQuery("update t_human set name ='lily' where id = 1").executeUpdate();

    }

    public void getHuman1() {
        //动态条件
        QHuman qHuman = QHuman.human;
        //分页排序
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "id"));
        PageRequest pageRequest = new PageRequest(0, 10, sort);


        com.querydsl.core.types.Predicate predicate = qHuman.age.longValue().lt(40).and(qHuman.name.like("claire"));
        getHuman2(predicate);
        Page<Human> all = humanRepository.findAll(predicate, pageRequest);
    }


    public void getHuman2(com.querydsl.core.types.Predicate predicate) {
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        JPAQuery<Tuple> jpaQuery = jpaQueryFactory.select(QHuman.human, QMail.mail)
                .from(QHuman.human)
                .leftJoin(QMail.mail)
                .on(QHuman.human.emailId.longValue().eq(QMail.mail.id.longValue()));

        jpaQuery.where(predicate);
        //etchCount的时候上面的orderBy不会被执行 不用太担心性能问题
        long total = jpaQuery.fetchCount();
        List<Tuple> fetch = jpaQuery.fetch();
    }

    public void getHuman3(com.querydsl.core.types.Predicate predicate, Pageable pageable) {
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

    /**
     * 封装一个$ 存放所有对象
     * JPAQueryFactory factory = new JPAQueryFactory(entityManager);
     * factory.select($.pcardCardOrder)
     * .select($.pcardVcardMake.vcardMakeDes)
     * .leftJoin($.pcardVcardMake).on($.pcardCardOrder.makeId.eq($.pcardVcardMake.vcardMakeId))
     * //......省略
     */


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

    public  void getHuman7(){
        QHuman qHuman = QHuman.human;
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        //使用booleanTemplate充当where子句或where子句的一部分
        List<Human> list = queryFactory.selectFrom(qHuman).where(Expressions.booleanTemplate("{} = \"claire\"", qHuman.name)).fetch();
        //上面的写法，当booleanTemplate中需要用到多个占位时
        List<Human> list1 = queryFactory.selectFrom(qHuman).where(Expressions.booleanTemplate("{0} = \"claire\" and {1} = \"shanghai\"", qHuman.name,qHuman.address)).fetch();

        //使用stringTemplate充当查询语句的某一部分
        String date = queryFactory.select(Expressions.stringTemplate("DATE_FORMAT({0},'%Y-%m-%d')", qHuman.createTime)).from(qHuman).fetchFirst();
        //在where子句中使用stringTemplate
        Integer id = queryFactory.select(qHuman.id).from(qHuman).where(Expressions.stringTemplate("DATE_FORMAT({0},'%Y-%m-%d')", qHuman.createTime).eq("2018-03-19")).fetchFirst();
    }

}
