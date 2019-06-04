### 什么是Mybatis
- Mybatis是一个半ORM（对象关系映射）框架，它内部封装了JDBC，开发时只需要关注SQL语句本身，不需要花费精力去处理加载驱动、创建连接、创建statement等繁杂的过程
- MyBatis 可以使用 XML 或注解来配置和映射原生信息
- 通过xml 文件或注解的方式将要执行的各种 statement 配置起来，并通过java对象和 statement中sql的动态参数进行映射生成最终执行的sql语句，最后由mybatis框架执行sql并将结果映射为java对象并返回

### Mybatis的优缺点
- 基于Mybatis就可以知道为什么要用mybatis了，但是也要注意到它的缺点

#### 优点
- 基于SQL，有很高的灵活性
- 减少了一半JDBC的冗余代码
- 很好地与Spring与各种数据库兼容
- 支持标签映射 
#### 缺点
- 由于需要手动编写SQL ，那么SQL编写的工作量会比较大，需要有较好的SQL编写习惯
- SQL的编写依赖于数据库，移植性差


### 特点
- 1."半自动化"的ORM实现，所有的功能需要通过编写SQL来实现
- 2.基于XML的SQL配置
- 3.提供了对底层JDBC数据访问的封装
- 4.提供强大的数据映射功能（传入参数映射和结果数据映射）

### 一个单纯的mybatis书写方式
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