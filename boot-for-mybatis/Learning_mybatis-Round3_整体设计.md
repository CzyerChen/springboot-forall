### 一、总体流程
#### 1.加载配置并初始化
- 程序初始化加载配置文件的时候
- 配置来源于配置文件或者是java注解
- 目标把SQL加载为一个个MappedStatement对象，包含传入参数的映射，需要执行的SQL，结果映射

#### 2.接收调用请求
- 调用mybatis API的时候
- 将SQL id 传入参数对象传入API，将调用下层处理层进行处理

#### 3.处理操作请i去
- 来自API层的请求，将上层的SQL ID 和传入参数对象
- 根据SQL和ID找到MappedStatement对象
- 根据传入的参数对象，解析MappedStatement对象，得到最终的SQL和入参
- 获取数据库链接，根据SQL和入参执行数据库操作，得到最终的结果
- 根据MappedStatement对象中的结果映射配置对得到的执行结果进行转换，并得到最终结果
- 释放连接

#### 4.返回处理结果

### 二、功能架构
- 接口API层(SqlSession)：负责接受API请求
- 数据处理层（比较核心Executor）：参数映射（配置，映射解析，类型解析），SQL解析（获取，解析，生成动态SQL），SQL执行（执行器），结果映射（配置，类型转换，数据拷贝）
- 基础支撑层（Configuration）：连接管理、事务管理、配置加载、缓存处理


### 三、框架架构
```text
   注解                                            配置文件
   Mapper Annotation <----   Configuration --->  SqlMapConfig.xml /SqlMap.xm
                               基础支撑层
                                  |
                                  |
                                 \|/
                                Mapper
                                  |
                                  |
                                 \|/
        传入参数映射         Mapped Statements      输出参数映射
                               SQL解析
                              数据处理层
                                  |
                                  |
                                 \|/
                                SQL执行
                                RDBMS                                                    
```

