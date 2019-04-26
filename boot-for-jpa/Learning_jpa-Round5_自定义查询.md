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