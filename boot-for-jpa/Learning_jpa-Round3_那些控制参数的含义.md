### mapperBy
- 只有OneToOne，OneToMany，ManyToMany上才有mappedBy属性，ManyToOne没有该属性
- 定义在被拥有方，指向拥有方
- 被拥有方定义mapperBy和joinColumn/joinColumns不能同时存在，因为其中一个就能够标识拥有和被拥有的关系了，mappedBy这方定义JoinColumn/JoinTable总是失效的
- mappedBy的值应该为一的一方在多的一方的属性的属性名


### CascadeType -- 级联类型
- CascadeType.ALL -- 拥有所有权限
- CascadeType.REFRESH -- 级联刷新
```text
在于并发操作，能够获取最新的持久化状态
```
- CascadeType.DETACH -- 级联脱管/游离操作
```text
一般情况，如果你要删除一个实体，但是它有外键无法删除，但是有了这个权限就可以
```
- CascadeType.MERGE  -- 级联更新（合并）操作
```text
关联实体的数据会及时更新
```
- CascadeType.REMOVE -- 级联删除权限
```text
相关联的实体全部删除
```
- CascadeType.PERSIST
```text
含义： 一对多的情况下，多中删除某和一中关联的实体，这个时候数据库是没有了，可是一处还保留着这个关系，这个时候一save了以下，刚才多中被删除的又被持久化到数据库中了，这一步是需要persist权限的
```


### JoinColumn
#### 一对一 JoinCloumn写在主控方那里 
```text
  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "address_id",referencedColumnName = "aid")
```
- name默认: 被关联表表名_被关联表主键,例如address_aid,用来表示本表指向另一个表的外键名
- 这边就是采用了自定义命名，然后指向的列名叫aid

#### 一对一 主键关联
```text
@OneToOne(cascade={CascadeType.ALL})
@PrimaryKeyJoinColumn(name = "id", referencedColumnName="aid") 
```
- 另一张表主键为aid,name被取名为id

#### 一对多单向
```text
@OneToMany(cascade=CascadeType.ALL) 
@JoinColumn(name="product_pid")//另一个表指向本表的外键
```
- 多的一方（Product）没有注解，一的一方（Store）有注解
- 如果一的一方不加@JoinColumn指定外键字段的话，Hibernate会自动生成一张中间表Store_Product来进行绑定

#### 多对一单向
```text
  @ManyToOne(cascade = {CascadeType.MERGE,CascadeType.REFRESH},optional = false)//optional=false,表示store不能为空
  @JoinColumn(name = "store_sid")
```
- 若不用@JoinColumn指定外键字段的话，不会生成中间表，而是在Product表中生成一列指向Store的外键

#### 一对多 多对一 使用中间表
```text
@JoinTable(name = "Store_Product", 
joinColumns = {@JoinColumn(name = "store_sid")},
inverseJoinColumns = {@JoinColumn(name = "product_pid")})
```
#### 一对多双向  -- 最常用
```text
@OneToMany(cascade=CascadeType.ALL) 
@JoinColumn(name="product_pid")

@ManyToOne(cascade = CascadeType.ALL, optional = false)
@JoinColumn(name="store_sid")
```

#### 多对多单向
- 多对多在上面的演示中我们也看到了，是通过中间表来维系关系的
```text
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(joinColumns = @JoinColumn(name = "user_id"),inverseJoinColumns = @JoinColumn(name = "permisson_id"))//请注意，这边是默认配置，其实@JoinTable可以省略，但是如果你的定义不符合默认规范，就一定要书写咯
    private List<Permisson> permissonList;
    
    @ManyToMany(mappedBy = "permissonList",fetch = FetchType.LAZY)

    
```

