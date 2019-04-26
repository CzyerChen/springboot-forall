> 其实学会了Round1中的基础操作，除了添加自定义的实现，或许这种基础操作就是一些人编程所了解的全部了，只是做一些单表的逻辑，太过简单了，很多复杂的业务会设计表关联，或者外键的方式，这才是能锻炼人能力的一部分

> 接下来，就来聊聊JPA下的多表关联

## 一、OneToOne 一对一
- 标识实体之间的一对一的关系
### 1.外键关联
#### 一对一单向 关系被维护者Address不能查询到Customer
- 表情况
```text
t_customer(cid,cname,sex,phone,address)

t_address(aid,country,province,city,area,detail)
```
- 表设计
```text
@Entity
@Table(name = "t_customer")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cid",unique = true,nullable = false)
    private int cid;
    @Column(name = "cname",nullable = false,length = 255)
    private String cname;
    @Column(name = "sex",nullable = false)
    private int sex;
    @Column(name = "phone",nullable = false)
    private String phone;
    @OneToOne(cascade = CascadeType.ALL)//Customer是关系的维护端，当删除 customer，会级联删除 address
    @JoinColumn(name = "address_id",referencedColumnName = "aid")//customer中的address_id字段参考address表中的aid字段
    private Address address;
}


@Entity
@Table(name = "t_address")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "aid",unique = true,nullable = false)
    private int aid;
    @Column(name = "country",length = 64)
    private String country;
    @Column(name = "province",length = 64)
    private String province;
    @Column(name = "city",length = 64)
    private String city;
    @Column(name = "area",length = 128)
    private String area;
    @Column(name = "detail")
    private String detail;
}

```

#### 一对一双向 关系被维护者Address能查询到Customer
- 表情况
```text
t_customer(cid,cname,sex,phone,address)

t_address(aid,country,province,city,area,detail,customer)
```
- 差异在于再address里面也能查到customer，形成了一个循环
```text
@Entity
@Table(name = "t_customer")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cid",unique = true,nullable = false)
    private int cid;
    @Column(name = "cname",nullable = false,length = 255)
    private String cname;
    @Column(name = "sex",nullable = false)
    private int sex;
    @Column(name = "phone",nullable = false)
    private String phone;
    @OneToOne(cascade = CascadeType.ALL)//Customer是关系的维护端，当删除 customer，会级联删除 address
    @JoinColumn(name = "address_id",referencedColumnName = "aid")//customer中的address_id字段参考address表中的aid字段
    private Address address;
}


@Entity
@Table(name = "t_address")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "aid",unique = true,nullable = false)
    private int aid;
    @Column(name = "country",length = 64)
    private String country;
    @Column(name = "province",length = 64)
    private String province;
    @Column(name = "city",length = 64)
    private String city;
    @Column(name = "area",length = 128)
    private String area;
    @Column(name = "detail")
    private String detail;


    @OneToOne(mappedBy = "address",cascade = {CascadeType.MERGE,CascadeType.REFRESH},optional = false)
    private Customer customer;
}

```

#### 表的查询
```text
 Customer claire = customerRepository.findByCname("claire");
 Address address = claire.getAddress();
 
  Hibernate: 
      select
          customer0_.cid as cid1_1_,
          customer0_.address_id as address_5_1_,
          customer0_.cname as cname2_1_,
          customer0_.phone as phone3_1_,
          customer0_.sex as sex4_1_ 
      from
          t_customer customer0_ 
      where
          customer0_.cname=?
```
#### 表的删除
```text
customerRepository.deleteById(1);

Hibernate: 
    select
        customer0_.cid as cid1_1_0_,
        customer0_.address_id as address_5_1_0_,
        customer0_.cname as cname2_1_0_,
        customer0_.phone as phone3_1_0_,
        customer0_.sex as sex4_1_0_,
        address1_.aid as aid1_0_1_,
        address1_.area as area2_0_1_,
        address1_.city as city3_0_1_,
        address1_.country as country4_0_1_,
        address1_.detail as detail5_0_1_,
        address1_.province as province6_0_1_ 
    from
        t_customer customer0_ 
    left outer join
        t_address address1_ 
            on customer0_.address_id=address1_.aid 
    where
        customer0_.cid=?
Hibernate: 
    delete 
    from
        t_customer 
    where
        cid=?
Hibernate: 
    delete 
    from
        t_address 
    where
        aid=?
```

### 2.表关联
- 表关联的话不是通过建立外键，通过增加一张表标识关系，不会因为外键的干扰打乱实体本身的含义
- 表情况
```text
t_people(pid,pname,sex,phone)

t_address(aid,country,province,city,area,detail)

t_people_address(pid,aid)
```
- 表的设计
```text
@Entity
@Table(name = "t_people")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class People {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pid",unique = true,nullable = false)
    private int pid;
    @Column(name = "pname",nullable = false,length = 255)
    private String pname;
    @Column(name = "sex",nullable = false)
    private int sex;
    @Column(name = "phone",nullable = false)
    private String phone;
    @OneToOne(cascade = CascadeType.ALL)//people是关系的维护端，当删除 people，会级联删除 address
    @JoinTable(name = "t_people_address",joinColumns = @JoinColumn(name = "pid"),inverseJoinColumns = @JoinColumn(name = "aid"))
    private Address address;
}

@Entity
@Table(name = "t_address")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "aid",unique = true,nullable = false)
    private int aid;
    @Column(name = "country",length = 64)
    private String country;
    @Column(name = "province",length = 64)
    private String province;
    @Column(name = "city",length = 64)
    private String city;
    @Column(name = "area",length = 128)
    private String area;
    @Column(name = "detail")
    private String detail;

}

```

- 表的查询
```text
 People claire = peopleRepository.findByPname("claire");
 Address address = claire.getAddress();
 
 Hibernate: 
     select
         people0_.pid as pid1_2_,
         people0_.phone as phone2_2_,
         people0_.pname as pname3_2_,
         people0_.sex as sex4_2_,
         people0_1_.aid as aid1_3_ 
     from
         t_people people0_ 
     left outer join
         t_people_address people0_1_ 
             on people0_.pid=people0_1_.pid 
     where
         people0_.pname=?

```

- 表的删除
```text
peopleRepository.deleteById(2);

Hibernate: 
    select
        people0_.pid as pid1_2_0_,
        people0_.phone as phone2_2_0_,
        people0_.pname as pname3_2_0_,
        people0_.sex as sex4_2_0_,
        people0_1_.aid as aid1_3_0_,
        address1_.aid as aid1_0_1_,
        address1_.area as area2_0_1_,
        address1_.city as city3_0_1_,
        address1_.country as country4_0_1_,
        address1_.detail as detail5_0_1_,
        address1_.province as province6_0_1_ 
    from
        t_people people0_ 
    left outer join
        t_people_address people0_1_ 
            on people0_.pid=people0_1_.pid 
    left outer join
        t_address address1_ 
            on people0_1_.aid=address1_.aid 
    where
        people0_.pid=?
Hibernate: 
    delete 
    from
        t_people_address 
    where
        pid=?
Hibernate: 
    delete 
    from
        t_people 
    where
        pid=?
Hibernate: 
    delete 
    from
        t_address 
    where
        aid=?

```


## 二、OneToMany 一对多 / ManyToOne 多对一
### 1.外键关联
#### 一对多 多对一的双向关联
- 一端(store)使用@OneToMany,多端(product)使用@ManyToOne
- 这种一和多关系的关系维护方和一对一不同，有多端来维护，此处就是由product维护product 和store 之间的关系
- 一端(store)使用@OneToMany注释的mappedBy="store"属性表明Store是关系被维护端
- 多端(product)使用@ManyToOne和@JoinColumn来注释属性 store,@ManyToOne表明product是多端，@JoinColumn设置在product表中的关联字段(外键)
- 表的基本情况
```text
t_store(sid,sname,address)

t_product(pid,price,pname,s_id)

```
- 表设计
```text
@Entity
@Table(name = "t_store")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int sid;
    private String sname;
    private String address;
    @OneToMany(mappedBy = "store",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<Product> product;
}


@Entity
@Table(name = "t_product")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  int pid;
    private String pname;
    private double price;
    @ManyToOne(cascade = {CascadeType.ALL,CascadeType.REFRESH},optional = false)//optional=false,表示store不能为空
    @JoinColumn(name = "s_id")
    private Store store;
}

```
- 表查询
```text
        Store achuor = storeRepository.findBySname("Auchor");
        List<Product> product = achuor.getProduct();
        int size = product.size();

Hibernate: 
    select
        store0_.sid as sid1_5_,
        store0_.address as address2_5_,
        store0_.sname as sname3_5_ 
    from
        t_store store0_ 
    where
        store0_.sname=?
Hibernate: 
    select
        product0_.s_id as s_id4_4_0_,
        product0_.pid as pid1_4_0_,
        product0_.pid as pid1_4_1_,
        product0_.pname as pname2_4_1_,
        product0_.price as price3_4_1_,
        product0_.s_id as s_id4_4_1_ 
    from
        t_product product0_ 
    where
        product0_.s_id=?
```
- 表删除
```text
Hibernate: 
    select
        store0_.sid as sid1_5_0_,
        store0_.address as address2_5_0_,
        store0_.sname as sname3_5_0_,
        product1_.s_id as s_id4_4_1_,
        product1_.pid as pid1_4_1_,
        product1_.pid as pid1_4_2_,
        product1_.pname as pname2_4_2_,
        product1_.price as price3_4_2_,
        product1_.s_id as s_id4_4_2_ 
    from
        t_store store0_ 
    left outer join
        t_product product1_ 
            on store0_.sid=product1_.s_id 
    where
        store0_.sid=?
Hibernate: 
    delete 
    from
        t_product 
    where
        pid=?
Hibernate: 
    delete 
    from
        t_product 
    where
        pid=?
Hibernate: 
    delete 
    from
        t_product 
    where
        pid=?
Hibernate: 
    delete 
    from
        t_store 
    where
        sid=?
```

#### 说明
OneToMany fetch = FetchType默认LAZY ,ManyToOne fetch = FetchType默认EAGER

### 2.表关联
- 设计表一对多的映射表，规则和一对一类似
```text
@JoinTable(name = "Store_Product", 
joinColumns = {@JoinColumn(name = "store_sid")},
inverseJoinColumns = {@JoinColumn(name = "product_pid")})
```

## ManyToMany 多对多
- 因为多对多的关系复杂，就是由一张中间表来维护的关系，默认表名主表名+下划线+从表名，字段名称默认为：主表名+下划线+主表中的主键列名，从表名+下划线+从表中的主键列名
- 多对多关系中一般不设置级联保存、级联删除、级联更新等操作
- 表情况
```text
user(id,name,sex,phone,address,email)
permisson(id,tab)
user_permisson(user_id,permisson_id)//默认形式

```
- 表设计
```text
@Entity // 标识实体类
@Table(name = "user")
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class User {
    //标识主键，指定主键生成策略
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",unique = true,nullable = false)//有很多标识的注解，这边也不一一描述作用，可以看源码的时候再说
    private int id;
    @Column(name = "name")
    private String name;
    private int sex;
    private String phone;
    private String address;
    private String email;
    @ManyToMany
    @JoinTable(joinColumns = @JoinColumn(name = "user_id"),inverseJoinColumns = @JoinColumn(name = "permisson_id"))//请注意，这边是默认配置，其实@JoinTable可以省略，但是如果你的定义不符合默认规范，就一定要书写咯
    private List<Permisson> permissonList;
}


@Entity // 标识实体类
@Table(name = "permisson")
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class Permisson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String tab;
    @ManyToMany(mappedBy = "permissonList")
    private List<User> userList;
}


```
- 表添加
```text
        Permisson permisson = new Permisson();
        permisson.setTab("add");
        permissonRepository.save(permisson);
        
Hibernate: 
    
    create table permisson (
       id integer not null auto_increment,
        tab varchar(255),
        primary key (id)
    ) engine=MyISAM
Hibernate: 
    
    create table user_permisson_list (
       user_id integer not null,
        permisson_id integer not null
    ) engine=MyISAM
Hibernate: 
    
    alter table user_permisson_list 
       add constraint FKd42mruw0yf0q63utxay44cwwq 
       foreign key (permisson_id) 
       references permisson (id)
Hibernate: 
    
    alter table user_permisson_list 
       add constraint FKptqlxtn5x75w0jo50b80uvhjr 
       foreign key (user_id) 
       references user (id)
```
```text
        User user= new User();
        user.setName("claire1");
        user.setAddress("baker street");
        user.setEmail("xxx@gmail.com");
        List<Permisson> list = new ArrayList<Permisson>();
        list.add(permissonRepository.findById(1).get());
        user.setPermissonList(list);
        userRepository.save(user);


Hibernate: 
    
    alter table user_permisson_list 
       add constraint FKd42mruw0yf0q63utxay44cwwq 
       foreign key (permisson_id) 
       references permisson (id)
Hibernate: 
    
    alter table user_permisson_list 
       add constraint FKptqlxtn5x75w0jo50b80uvhjr 
       foreign key (user_id) 
       references user (id)

Hibernate: 
    select
        permisson0_.id as id1_0_0_,
        permisson0_.tab as tab2_0_0_ 
    from
        permisson permisson0_ 
    where
        permisson0_.id=?
Hibernate: 
    insert 
    into
        user
        (address, email, name, phone, sex) 
    values
        (?, ?, ?, ?, ?)
Hibernate: 
    insert 
    into
        user_permisson_list
        (user_id, permisson_id) 
    values
        (?, ?)

```
- 表查询
```text
List<Permisson> add = permissonRepository.findByTab("add");

Hibernate: 
    select
        permisson0_.id as id1_0_,
        permisson0_.tab as tab2_0_ 
    from
        permisson permisson0_ 
    where
        permisson0_.tab=?
        
```
```text
        User claire1 = userRepository.findByName("claire1");
        List<Permisson> permissonList = claire1.getPermissonList();
        int size = permissonList.size();
      
Hibernate: 
    select
        user0_.id as id1_7_,
        user0_.address as address2_7_,
        user0_.email as email3_7_,
        user0_.name as name4_7_,
        user0_.phone as phone5_7_,
        user0_.sex as sex6_7_ 
    from
        user user0_ 
    where
        user0_.name=?
Hibernate: 
    select
        permissonl0_.user_id as user_id1_8_0_,
        permissonl0_.permisson_id as permisso2_8_0_,
        permisson1_.id as id1_0_1_,
        permisson1_.tab as tab2_0_1_ 
    from
        user_permisson_list permissonl0_ 
    inner join
        permisson permisson1_ 
            on permissonl0_.permisson_id=permisson1_.id 
    where
        permissonl0_.user_id=?
  
       
```
- 表删除 -- 删除permisson不会影响关联表，删除关系维护者User 才会把关联表的数据也清除
```text
permissonRepository.deleteById(2);

Hibernate: 
    select
        permisson0_.id as id1_0_0_,
        permisson0_.tab as tab2_0_0_ 
    from
        permisson permisson0_ 
    where
        permisson0_.id=?
Hibernate: 
    delete 
    from
        permisson 
    where
        id=?
```