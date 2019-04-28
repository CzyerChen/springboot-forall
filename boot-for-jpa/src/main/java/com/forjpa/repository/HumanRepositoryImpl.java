package com.forjpa.repository;

import com.forjpa.domain.Human;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.util.StringUtils;

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
}
