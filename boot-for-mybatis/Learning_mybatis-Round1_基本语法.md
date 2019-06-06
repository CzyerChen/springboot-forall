### 一、什么是Mybatis
- Mybatis是一个半ORM（对象关系映射）框架，它内部封装了JDBC，开发时只需要关注SQL语句本身，不需要花费精力去处理加载驱动、创建连接、创建statement等繁杂的过程
- MyBatis 可以使用 XML 或注解来配置和映射原生信息
- 通过xml 文件或注解的方式将要执行的各种 statement 配置起来，并通过java对象和 statement中sql的动态参数进行映射生成最终执行的sql语句，最后由mybatis框架执行sql并将结果映射为java对象并返回

### 二、Mybatis的优缺点
- 基于Mybatis就可以知道为什么要用mybatis了，但是也要注意到它的缺点

#### 优点
- 基于SQL，有很高的灵活性
- 减少了一半JDBC的冗余代码
- 很好地与Spring与各种数据库兼容
- 支持标签映射 
#### 缺点
- 由于需要手动编写SQL ，那么SQL编写的工作量会比较大，需要有较好的SQL编写习惯
- SQL的编写依赖于数据库，移植性差


### 三、特点
- 1."半自动化"的ORM实现，所有的功能需要通过编写SQL来实现
- 2.基于XML的SQL配置
- 3.提供了对底层JDBC数据访问的封装
- 4.提供强大的数据映射功能（传入参数映射和结果数据映射）

### 四、传统JDBC
- 第一步：获取一个连接驱动的实例
- 第二步：DriverManager 获取链接Connection,需要url username password
- 第三步：Connection获取prepareStatement
- 第四步：prepareStatement 执行查询 executeQuery("".........")
- 第五步：获取执行结果ResultSet ,这是一个数据读取数据的游标，就是以迭代的方式，从数据一个迭代才从数据库获取一条数据，类似流式的
- 5.1. 获取MetaData， 主要是数据库表的列
- 5.2. 在resultSet里面，获取对应列名的字段
- 5.3. 最后就是按你的想法把结果组装起来

### 五、JDBC到Mybatis
- ORM框架就是对原生JDBC的一步步封装,JDBC是原始的，有复杂的连接和连接的断开和销毁
#### 优化一、连接的获取和释放
- 问题描述: 数据库链接的获取和释放需要耗费大量资源，并且影响系统性能
- 解决问题：可是使用数据库连接池技术，实现线程复用、管理线程、控制线程数量，能够很好的利用现有的资源，而不用开辟新的资源

- 问题描述：连接池多种多样，可能存在各种变化
- 问题解决：统一使用DataSource来获取数据库连接,具体实现支持用户配置，再由数据库连接池来适配DataSource

#### 优化二：SQL统一存取
- 问题描述：JDBC对SQL的操作在应用代码中，然后应用代码遍布整个程序，因而SQL混乱
```text
可读性差，不利于维护以及性能调优

改动java SQL需要重新打包部署

拼凑的SQL不利于SQL的校验与纠错

```
- 解决问题：需要有一个SQL可以统一加载和配置的地方，可以在数据库也可以是一个统一的配置文件来存储所有的SQL

#### 优化三：传入参数映射和动态SQL
- 问题描述：传入参数如果是顺序的、数量明确的，那么我们就可以通过占位符的方式来传入参数，但是这个局限性是很明显的。如果不是顺序传入并且数量不明的情况如何解决呢？
- 解决问题：JSTL动态SQL标签，运用Map存储字段名和变量值，通过$表示非占位符变量，#表示占位符变量来完成，并通过if标签等实现SQL片段的拼接

#### 优化四：结果映射和结果缓存
- 问题描述：我们一般获取到的结果集都是以ResultSet的方式返回的，需要以游标的方式进行访问，如果我们能将结果先全部缓存并将其转化为实体对象列表的方式，就能大大减少应用代码量
- 解决问题：我们需要知道我们需要返回什么类型的对象（JAVA bean），需要返回什么样的数据结构（Map  List Set...）,字段如何映射，具体将哪个值拷贝到哪个值
- 结果缓存的难题：缓存数据都是key-value的格式，那么这个key怎么来呢？怎么保证唯一呢？
- 即使同一条SQL语句几次访问的过程中由于传入参数的不同，得到的执行SQL语句也是不同的。那么缓存起来的时候是多对。但是SQL语句和传入参数两部分合起来可以作为数据缓存的key值。


#### 优化五：解决重复SQL语句问题
- 问题描述：对于多个SQL查询，可能select的字段不同、可能where的条件略有不同，但是是同一张表，需要做相同的数据扫描
- 解决问题：将常用通用的SQL数据封装成SQL模块使用

### JDBC -> Mybatis的优化点
（1） 使用数据库连接池对连接进行管理

（2） SQL语句统一存放到配置文件

（3） SQL语句变量和传入参数的映射以及动态SQL

（4） 动态SQL语句的处理

（5） 对数据库操作结果的映射和结果缓存

（6） SQL语句的重复


### 五、一个单纯的mybatis书写方式
- jar包
```text
mybatis-3.0.6.jar  
og4j-1.2.16.jar  
classes12.jar 
```
- 在src下面新建mybatis.cfg.xml文件
- 新建一个Java类User.java
- 新建User.xml文件
- 将User.xml引用到mybatis.cfg.xml文件中
```text
   <mappers>  
        <mapper resource="com/user/sqlmap/User.xml" />  
    </mappers>  
```
- 测试
```text
   public static void main(String[] args) throws IOException {  
        String resource = "mybatis.cfg.xml";  
        Reader reader = Resources.getResourceAsReader(resource);  
        SqlSessionFactory ssf = new SqlSessionFactoryBuilder().build(reader);  
          
        SqlSession session = ssf.openSession();  
          
        try {  
            User user = (User) session.selectOne("User.selectUser", "1");  
            System.out.println(user);  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            session.close();  
        }  
    }  
```

### 五、什么是动态SQL
- 包含一些if where foreach的标签，实现SQL语义
- 条件判断：
```text
 <if test="title != null">
            and title = #{title}
 </if>       
```
- where 判断
```text
 <where>
            <if test="title != null">
                title = #{title}
            </if>
</where>
```
- foreach:
```text
        select * from t_blog where id in
        <foreach collection="list" index="index" item="item" open="(" separator="," close=")">
            #{item}
        </foreach>
```

### 六、如何应对多对一 多对多的嵌套查询
- 嵌套查询主要是通过resultMap实现的高级映射查询，主要分为嵌套语句查询和嵌套结果查询
#### 嵌套语句查询
- 先一次将外层的结果拿到放入resultMap ，然后再进行进一步的子查询，把绑定的内部引用对象再查询出来,再放入resultMap的对应引用对象上
```text
<resultMap type="com.foo.bean.BlogInfo" id="BlogInfo">  
    <id column="blog_id" property="blogId" />  
    <result column="title" property="title" />  
    <association property="author" column="blog_author_id"  
        javaType="com.foo.bean.Author" select="com.foo.bean.AuthorMapper.selectByPrimaryKey">  
    </association>  
    <collection property="posts" column="blog_id" ofType="com.foo.bean.Post"  
        select="com.foo.bean.PostMapper.selectByBlogId">  
    </collection>  
</resultMap>  

<select id="queryBlogInfoById" resultMap="BlogInfo" parameterType="java.math.BigDecimal">  
    SELECT  
    B.BLOG_ID,  
    B.TITLE,  
    B.AUTHOR_ID AS BLOG_AUTHOR_ID  
    FROM LOULUAN.BLOG B  
    where B.BLOG_ID = #{blogId,jdbcType=DECIMAL}  
</select>

```
#### 嵌套结果查询
- 通过联合查询将结果一次性从数据库中取出，再利用ResultMap进行映射转换成需要的对象
```text
<resultMap type="com.foo.bean.BlogInfo" id="BlogInfo">  
    <id column="blog_id" property="blogId"/>  
    <result column="title" property="title"/>  
    <association property="author" column="blog_author_id" javaType="com.foo.bean.Author">  
        <id column="author_id" property="authorId"/>  
        <result column="user_name" property="userName"/>  
        <result column="password" property="password"/>  
        <result column="email" property="email"/>  
        <result column="biography" property="biography"/>  
    </association>  
    <collection property="posts" column="blog_post_id" ofType="com.foo.bean.Post">  
        <id column="post_id" property="postId"/>  
        <result column="blog_id" property="blogId"/>  
        <result column="create_time" property="createTime"/>  
        <result column="subject" property="subject"/>  
        <result column="body" property="body"/>  
        <result column="draft" property="draft"/>  
    </collection>  

</resultMap>  


<select id="queryAllBlogInfo" resultMap="BlogInfo">  
    SELECT   
     B.BLOG_ID,  
     B.TITLE,  
     B.AUTHOR_ID AS BLOG_AUTHOR_ID,  
     A.AUTHOR_ID,  
     A.USER_NAME,  
     A.PASSWORD,  
     A.EMAIL,  
     A.BIOGRAPHY,  
     P.POST_ID,  
     P.BLOG_ID   AS BLOG_POST_ID ,  
  P.CREATE_TIME,  
     P.SUBJECT,  
     P.BODY,  
     P.DRAFT  
FROM BLOG B  
LEFT OUTER JOIN AUTHOR A  
  ON B.AUTHOR_ID = A.AUTHOR_ID  
LEFT OUTER JOIN POST P  
  ON P.BLOG_ID = B.BLOG_ID  
</select>

```
