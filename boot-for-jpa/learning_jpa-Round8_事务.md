> 跟随[系列讲解](https://blog.csdn.net/dm_vincent/article/details/52566964)，学习JPA中的事务

### 事务的特性 -ACID
- 原子性Atomic，事务被原子性地执行，意味着能够保证一组操作能够都被执行，或都不执行

- 隔离性Isolation,事务进行过程中的变更，只对内部可见，外部是不可见的

- 一致性Consistence ，数据应该是正确的

- 持久性Durability 事务执行的变更在事务提交后任然生效


### JAVEEE中的事务
#### resource-local的本地事务
```text
利用jdbc Datasource接口，手动维护begin commit rollback 的操作，完成事务操作
```
#### container的容器事务 
```text
容器事务的执行和资源的维护，都是交给容器完成的，一般指使用了Java Transaction API(JTA)的事务
```
- 编码中显示划分
- 容器自动划分

### EJB中的事务
- 使用原生JTA接口实现称为基于Bean的事务，BMT
```text
@Stateless
@TransactionManagement(TransactionManagementType.BEAN)

这种需要操作者手动维护事务的开始、提交和回滚操作
```
- 使用容器额安城事务的自动划分称为基于容器的事务CMT
```text
通过@TransactionAttribute这个注解来完成，
@Stateful
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS),

MANDATORY: 运行当前方法时要求存在处于active状态的事务。如果没有active状态的事务则会抛出异常。
REQUIRED: 运行当前方法时，希望处于事务中。当容器碰到这个注解时，会检查当前是否存在active状态的事务。如果存在，就直接使用它；如果不存在，就创建(create)一个供它使用。(这个选项是最常用的，也是默认选项)
REQUIRES_NEW: 运行的当前方法如何总是希望能够运行在仅属于自己的事务中，就可以使用这个选项。也就是说，这个方法内对于资源的操作的提交以及回滚都和调用栈中其它事务无关。
SUPPORTS: 运行的当前方法对于又没有事务采取一种”无所谓”的态度。有也可以，没有也可以。因此可以推断出在此方法中定义的逻辑通常和资源没有太大的关系。
NOT_SUPPORTED: 运行的当前方法对事务有”消极”的态度。如果发现了处于active状态的事务，会尝试让容器挂起(suspend)该事务。因此也可以推断出在此方法中定义的逻辑通常和资源没有太大的关系。
NEVER: 当运行的当前方法采用此选项时，如果运行时发现有active状态的事务，那么会直接抛出一个异常。

如果出现异常可采用EJBContext setRollbackOnly()的方法来标注事务回滚
```

### Spring中的事务
#### Resource-local的本地事务
#### Global的全局事务-容器事务
- 使用原生JTA接口实现称为编程式事务管理
```text
需要使用的两个对象：TransactionTemplate，PlatformTransactionManager

TransactionTemplate类似于jdbcTemplate，直接操控数据库底层的事务，TransactionTemplate.execute()

PlatformTransactionManager配合DefaultTransactionDefinition 使用，txManager.getTransaction(def)
```
- 使用容器额安城事务的自动划分称为声明式事务管理
```text
如何实现
1.在@Configuration的JavaConfig类中使用@EnableTransactionManagement
2.在需要使用事务的类或者方法上使用@Transactional

原理：
结合@Transactional的注解，通过Spring AOP,生成代理对象使用TransactionInterceptor并结合PlatformTransactionManager来实现事务，通过AOP的事前事后的增强来完成事务的开始，事务的提交或者回滚，能够很好地将业务和事务的逻辑解耦
```