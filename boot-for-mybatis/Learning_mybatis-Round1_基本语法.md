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

### 四、一个单纯的mybatis书写方式
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
