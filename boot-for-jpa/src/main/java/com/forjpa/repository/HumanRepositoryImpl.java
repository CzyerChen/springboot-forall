package com.forjpa.repository;

import com.forjpa.domain.Human;
import com.forjpa.domain.QHuman;
import com.forjpa.domain.QMail;
import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    /**
     * 封装一个$ 存放所有对象
     * JPAQueryFactory factory = new JPAQueryFactory(entityManager);
     *         factory.select($.pcardCardOrder)
     *                .select($.pcardVcardMake.vcardMakeDes)
     *                .leftJoin($.pcardVcardMake).on($.pcardCardOrder.makeId.eq($.pcardVcardMake.vcardMakeId))
     *                //......省略
     */


}
