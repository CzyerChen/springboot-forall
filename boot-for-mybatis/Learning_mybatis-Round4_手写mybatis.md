> 在手写mybatis之前，我们需要知道mybatis的流程
```text
        Configuration 配置xml文件或注解
                    |
                    |
                   \|/
        SqlSessionFactory 会话工厂
          根据配置文件，创建工厂
           用于生产SqlSession
                    |
                    |
                   \|/
        SqlSession, 类似于一个请求
          实际操作数据库（增删改查）
                    |
                    |
                   \|/
        Executor 执行器 是一个接口
       SqlSession通过执行器具体操作数据库
                    |
                    |
                   \|/
           mapped Statement 
入参-->   包含SQL 入参和结果映射 ----> 输出结果
                    |
                    |
                   \|/
                  mysql        
```
- 核心需要一个自定义的Configuration来建立起基础支撑层的配置
- 需要一个Mapper 配置多个Statement
- 需要获取SqlSessionFactory 生成SqlSession 来执行数据库操作
- SqlSession需要Executor来执行SQL，获取结果

> 缺乏入参映射和结果映射的实现，基本实现了配置的读取，以及结果的流转，利用AOP的思想进行接口实现的代理

### 一、基础支撑层：负责获取数据库连接/mapper文件，解析配置文件
```text
public class SelfConfiguration {

    private static ClassLoader classLoader = ClassLoader.getSystemClassLoader();

    //---------------获取基础配置文件，获取数据库链接----------------------//
    public Connection build(String resource){
        try{
            InputStream stream = classLoader.getResourceAsStream(resource);
            SAXReader reader = new SAXReader();
            Document document = reader.read(stream);
            Element rootElement = document.getRootElement();
           //解析内容
            return evalDataSource(rootElement);
        }catch (DocumentException e) {
            e.printStackTrace();
            throw new RuntimeException("error occured while evaling xml " + resource);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    private Connection evalDataSource(Element node) throws ClassNotFoundException {
        if(!node.getName().equals("database")){
            throw new RuntimeException("miss database element");
        }

        String driveClass = null;
        String url = null;
        String username = null;
        String password = null;

        for(Object item : node.elements("property")){
            Element i = (Element)item;
            String value = getValue(i);
            String name = i.attributeValue("name");
            if(name == null || value ==null){
                throw new RuntimeException("name or value can not be null");
            }

            switch (name){
                case "driverClassName":driveClass = value;break;
                case "url":url=value;break;
                case "username":username = value;break;
                case "password":password=value;break;
                default:throw new RuntimeException("label is illegal,please check the config file");
            }
        }
        Class.forName(driveClass);
        Connection connection = null;
        try{
            connection = DriverManager.getConnection(url, username, password);

        } catch (SQLException e) {
            e.printStackTrace();
        }


        return  connection;
    }

    private String getValue(Element node){
        return node.hasContent()?node.getText():node.attributeValue("value");
    }

    //-----------------读取Mapper文件------------------------//
    public MapperBean readerMapper(String path){
        MapperBean mapperBean = new MapperBean();
        try{
            InputStream stream = classLoader.getResourceAsStream(path);
            SAXReader reader = new SAXReader();
            Document document = reader.read(stream);
            Element rootElement = document.getRootElement();
            mapperBean.setInterfaceName(rootElement.attributeValue("namespace").trim());
            List<Function> list = new ArrayList<>();
            for(Iterator iterator = rootElement.elementIterator();iterator.hasNext();){
                Function function = new Function();
                Element e = (Element)iterator.next();
                String sqlType = rootElement.getName().trim();
                String funcName = e.attributeValue("id").trim();
                String sql = e.getText();
                String resultType = e.attributeValue("resultType");
                String parameterType = e.attributeValue("parameterType");
                function.setSqltype(sqlType);
                function.setFuncName(funcName);
                function.setParameterType(parameterType);
                Object instance = null;
                try {
                    instance = Class.forName(resultType).newInstance();

                } catch (IllegalAccessException e1) {
                    e1.printStackTrace();
                } catch (InstantiationException e1) {
                    e1.printStackTrace();
                } catch (ClassNotFoundException e1) {
                    e1.printStackTrace();
                }

                function.setSql(sql);
                function.setResultType(instance);
                list.add(function);

            }
            mapperBean.setList(list);
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return mapperBean;
    }
}

```

### 二、数据处理层：包括数据的代理实现
- 代码实现并不是特别复杂，在于入参的映射，SQL的解析，SQL的执行，结果的映射，这边映射暂时并没有做，也没有复杂一对多、多对多的逻辑
```text
public class SelfSqlSession {

    private Executor executor = new SelfExecutor();

    private SelfConfiguration configuration = new SelfConfiguration();

    public <T> T selectOne(String statement,Object parameter){
        return executor.query(statement, parameter);
    }

    @SuppressWarnings("unchecked")
    public <T> T getMapper(Class<T> clas){
        //动态代理调用
        return (T) Proxy.newProxyInstance(clas.getClassLoader(),new Class[]{clas},
                new SelfMapperProxy(configuration,this));
    }

}
```
```text
public class SelfMapperProxy implements InvocationHandler {
    private SelfConfiguration configuration;

    private SelfSqlSession sqlSession;

    public SelfMapperProxy(SelfConfiguration configuration,SelfSqlSession sqlSession){
     this.configuration = configuration;
     this.sqlSession = sqlSession;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        MapperBean mapperBean = configuration.readerMapper("mapper/PersonMapper.xml");
        if(!method.getDeclaringClass().getName().equals(mapperBean.getInterfaceName())){
            return  null;
        }
        List<Function> functions = mapperBean.getList();
        if(null != functions || 0!= functions.size()){
            for(Function f : functions){
                if(method.getName().equals(f.getFuncName())){
                    return  sqlSession.selectOne(f.getSql(),String.valueOf(args[0]));
                }
            }
        }

        return null;
    }
}

```
### 三、数据接口层：实现SQL的执行
```text
public class SelfExecutor  implements Executor {
    private SelfConfiguration configuration = new SelfConfiguration();

    @Override
    public <T> T query(String statement, Object parameter) {
        Connection connection = getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setString(1,parameter.toString());
            resultSet = preparedStatement.executeQuery();

            Person1 person1 = new Person1();

            while(resultSet.next()){
                person1.setPid(resultSet.getInt(1));
                person1.setPname(resultSet.getString(2));
                person1.setSex(resultSet.getString(3));
                person1.setPhone(resultSet.getString(4));
            }
            return (T)person1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Connection getConnection(){
        try{
            Connection connection = configuration.build("SqlMapConfig.xml");
            return connection;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
```

### 四、一些辅助类
```text
public class Function {
    private String sqltype;
    private String funcName;
    private String sql;
    private Object resultType;
    private String parameterType;

    public String getSqltype() {
        return sqltype;
    }

    public void setSqltype(String sqltype) {
        this.sqltype = sqltype;
    }

    public String getFuncName() {
        return funcName;
    }

    public void setFuncName(String funcName) {
        this.funcName = funcName;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Object getResultType() {
        return resultType;
    }

    public void setResultType(Object resultType) {
        this.resultType = resultType;
    }

    public String getParameterType() {
        return parameterType;
    }

    public void setParameterType(String parameterType) {
        this.parameterType = parameterType;
    }
}
```
```text
public class MapperBean {
    private String interfaceName; //接口名
    private List<Function> list; //接口下所有方法

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public List<Function> getList() {
        return list;
    }

    public void setList(List<Function> list) {
        this.list = list;
    }
}

```
```text
public interface Executor {
    public <T> T query(String statement,Object parameter);
}

```
```text
@RunWith(SpringJUnit4ClassRunner.class)
public class SelfDefineMybatisTest {

    @Test
    public void test(){
        SelfSqlSession sqlSession = new SelfSqlSession();
        Person1Mapper mapper  = sqlSession.getMapper(Person1Mapper.class);
        Person1 person1 = mapper.getUser(1);
        Assert.assertNotNull(person1);
    }
}
```