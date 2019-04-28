> 逻辑关系注解  -- 声明实体模型

> 物理关系注解  -- 声明数据库表中对应的关系

### 通过Field访问实体状态
```text
@Entity(name = "t_human")
@Table(name = "t_human")//Defaults to the entity name.
@NoArgsConstructor
@AllArgsConstructor
@Data
@Access(AccessType.FIELD)
public class Human {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  int id;
    private String name;
    private String email;
    private String phone;
    private int age;
}
```
### 通过Property访问实体状态
```text
@Entity(name = "t_human")
@Table(name = "t_human")//Defaults to the entity name.
@Access(AccessType.PROPERTY)
public class Human {
   
    private  int id;
    private String name;
    private String email;
    private String phone;
    private int age;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}

```

### 混合的使用Access实现数据库字段的转换
```text
@Entity(name = "t_human")
@Table(name = "t_human")//Defaults to the entity name.
@Access(AccessType.FIELD)
public class Human {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  int id;
    private String name;
    private String email;
    @Transient
    private String mobile;
    private int age;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Access(AccessType.PROPERTY)
    @Column(name = "phone")
    public String getPhoneFromDb() {
        return mobile;
    }

    public void setPhoneFromDb(String phone) {
        this.mobile = phone;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}

```


### 主键生成策略 --AUTO
- 会在TABLE，SEQUENCE以及IDENTITY中自动选一种

### 主键生成策略 --TABLE
- 通过数据库table的定义，对主键进行初始化
```text
@TableGenerator(name="HUMAN_Gen", table="ID_GEN", pkColumnName="GEN_NAME", valueColumnName="GEN_VAL", pkColumnValue="H_Gen", initialValue=1000, allocationSize=100)
@Id 
@GeneratedValue(generator="HUMAN_Gen")
private int id;
```
- 设置好主键参数后，就从初始值开始按照一次分配数量获取主键值
- HUMAN_Gen这个表需要在数据库事先定义好

### 主键生成策略 --SEQUENCE
- 通过指定序列，给主键赋值

### 主键生成策略 --IDENTITY
- 主键自增，需要数据库的字段也开启了主键自增即可，通过标识，hibernate底层数据持久化的时候就会帮你实现主键自增，缺点就是持久化之前无法获取实体的主键

### 数据类型
#### @Basic(fetch=FetchType.LAZY)  -- 懒加载
####  @Lob  -- 大文件
```text
@Basic(fetch=FetchType.LAZY)
@Lob 
@Column(name="PIC")
```

#### @Enumerated(EnumType.STRING) -- 枚举类型
#### 时间类型
- Java时间类型：java.util.Date以及java.util.Calendar -- 需要转化
Calendar ---> @Temporal(TemporalType.DATE)
- JDBC时间类型：java.sql.Date，java.sql.Time以及java.sql.Timestamp

#### 序列化
- transient 标识某一个字段不参与序列化
- @transient 不映射到数据存储上，但还是会参与序列化
#### 嵌套类型
- 嵌套父类 @Embedded 
- 嵌套的实体类@Embeddable
- 修改属性名：
```text
@AttributeOverrides({
        @AttributeOverride(name="street", column=@Column(name="area")),
        @AttributeOverride(name="city", column=@Column(name="province"))
    })

```
 
### 单值映射和集体映射
- 结合前面介绍的一对多多对多的介绍
