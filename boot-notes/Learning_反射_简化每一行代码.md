> 遇到的问题：一张记录表，记录了10个业务的字段，一个入参type说明了要修改哪个字段，最初是通过switch(type)case...来做的

> 但是涉及这样子的判断多了，每次都要不断的switch，并且case里面不同方法有不同的处理，一个公共的switch并不能够满足

> 又不能在每一个方法中都写一个10个case的switch，代码太过臃肿了，因而产生了今天的记录，当然更加臃肿的if else更加能够简化啦

### 利用策略+工厂+反射，简化臃肿的switch
- 现在定义一个常量类，表示了type和方法的映射
```java
public enum LeaveTypeEnum {
    CASUAL_LEAVE("事假","setCasualLeave","setClBackup","getCasualLeave","getClBackup","setCasual","getCasual"),
    SICK_LEAVE("带薪病假","setSickLeave","setSlBackup","getSickLeave","getSlBackup","setSick","getSick"),
    SICK_LEAVE_NORMAL("普通病假","setSickLeaveNormal","setSlnBackup","getSickLeaveNormal","getSlnBackup", "setSickNormal", "getSickNormal"),
    MARRIAGE_LEAVE("婚假", "setMarriageLeave", "setMlBackup", "getMarriageLeave", "getMlBackup", "setMarriage", "getMarriage"),
    FUNERAL_LEAVE("丧假", "setFuneralLeave", "setFlBackup", "getFuneralLeave", "getFlBackup", "setFuneral", "getFuneral"),
    MATERNITY_LEAVE("产假","setMaternityLeave","setMnlBackup","getMaternityLeave","getMnlBackup","setMaternity","getMaternity"),
    MATERNITY_LEAVE_1("产假-难产假", "setMaternityLeave", "setMnlBackup", "getMaternityLeave", "getMnlBackup", "", ""),
    MATERNITY_LEAVE_2("产假-多胞胎假","setMaternityLeave","setMnlBackup","getMaternityLeave","getMnlBackup","",""),
    MATERNITY_LEAVE_3("产假-哺乳假", "setMaternityLeave", "setMnlBackup", "getMaternityLeave","getMnlBackup","",""),
    MATERNITY_LEAVE_4("产假-产检假", "setMaternityLeave", "setMnlBackup", "getMaternityLeave", "getMnlBackup", "setMaterPaternity","getMaterPaternity"),
    MATERNITY_LEAVE_5("产假-流产假", "setMaternityLeave", "setMnlBackup", "getMaternityLeave", "getMnlBackup", "", ""),
    PATERNITY_LEAVE("陪产假","setPaternityLeave","setPnlBackup","getPaternityLeave","getPnlBackup","setMaterPaternity","getMaterPaternity"),
    ANNUAL_LEAVE("年假", "setAnnualLeave", "setAlBackup", "getAnnualLeave", "getAlBackup", "setAnnual", "getAnnual");


    private String name;
    private String setMethod;
    private String setSubMethod;
    private String getMethod;
    private String getSubMethod;
    private String setCalMethod;
    private String getCalMethod;

    LeaveTypeEnum(String name,String setMethod,String setSubMethod,String getMethod,String getSubMethod,String setCalMethod,String getCalMethod) {
        this.name = name;
        this.setMethod = setMethod;
        this.setSubMethod = setSubMethod;
        this.getMethod = getMethod;
        this.getSubMethod = getSubMethod;
        this.setCalMethod = setCalMethod;
        this.getCalMethod = getCalMethod;
    }

    public String getName() {
        return name;
    }

    public static String getMethodByNameForSet(String name) {
        for (LeaveTypeEnum leaveType : LeaveTypeEnum.values()) {
            if (leaveType.name.equals(name)) {
                return leaveType.setMethod;
            }
        }
        return null;
    }

    public static String getSubMethodByNameForSet(String name) {
        for (LeaveTypeEnum leaveType : LeaveTypeEnum.values()) {
            if (leaveType.name.equals(name)) {
                return leaveType.setSubMethod;
            }
        }
        return null;
    }

    public static String getMethodByNameForGet(String name) {
        for (LeaveTypeEnum leaveType : LeaveTypeEnum.values()) {
            if (leaveType.name.equals(name)) {
                return leaveType.getMethod;
            }
        }
        return null;
    }

    public static String getSubMethodByNameForGet(String name) {
        for (LeaveTypeEnum leaveType : LeaveTypeEnum.values()) {
            if (leaveType.name.equals(name)) {
                return leaveType.getSubMethod;
            }
        }
        return null;
    }

    public static String getMethodByNameCalForSet(String name) {
        for (LeaveTypeEnum leaveType : LeaveTypeEnum.values()) {
            if (leaveType.name.equals(name)) {
                return leaveType.setCalMethod;
            }
        }
        return null;
    }

    public static String getMethodByNameCalForGet(String name) {
        for (LeaveTypeEnum leaveType : LeaveTypeEnum.values()) {
            if (leaveType.name.equals(name)) {
                return leaveType.getCalMethod;
            }
        }
        return null;
    }
}
```
- 以上一个映射类和反射就能够解决问题，但是为了更简化并且利用上一些设计模式，就尝试了策略+工厂

### 接口
```java
public interface InvokeStrategy {
    Double getByColumn(String type,String leaveType,String methodName,Object obj);
```
### 接口实现类
```java
  @Slf4j
  public class UserVacationInvokeHandler implements InvokeStrategy {
      @Override
      public Double getByColumn(String type,String leaveType,String methodName, Object obj) {
          UserVacation userVacation = (UserVacation) obj;
          try {
              Method method = UserVacation.class.getDeclaredMethod(methodName);
              Invokable<UserVacation, Object> invokable =
                      (Invokable<UserVacation, Object>) Invokable.from(method);
              Double invoke = (Double) invokable.invoke(userVacation);
              return invoke;
          }catch (Exception e ){
              log.error("方法调用失败,type：{}，method：{}",type,methodName);
          }
          return null;
      }
  
      @Override
      public void setByColumn(String type, String leaveType,String methodName,Double param, Object obj) {
          UserVacation userVacation = (UserVacation) obj;
          try {
              Method method = UserVacation.class.getDeclaredMethod(methodName,Double.class);
              Invokable<UserVacation, Object> invokable =
                      (Invokable<UserVacation, Object>) Invokable.from(method);
             invokable.invoke(userVacation,param);
          }catch (Exception e ){
              log.error("方法调用失败,type：{}，method：{}",type,methodName);
          }
      }
  }

```
### 策略工厂
```java
public class InvokeStrategyFactory {
    private static InvokeStrategyFactory factory = new InvokeStrategyFactory();
    private InvokeStrategyFactory(){
    }
    private static Map<String,InvokeStrategy> strategyMap = new HashMap<>();
    static{
        strategyMap.put("UserVacation", new UserVacationInvokeHandler());
        strategyMap.put("UserVacationCal", new UserVacationCalInvokeHandler());
    }
    public InvokeStrategy creator(String type){
        return strategyMap.get(type);
    }
    public static InvokeStrategyFactory getInstance(){
        return factory;
    }

}
```

### 策略上下文
```java
public class InvokeStrategyContext {
    private InvokeStrategy strategy;

    public Double getByColumn(String type, String leaveType, String methodName,Object obj) {
        strategy = InvokeStrategyFactory.getInstance().creator(type);
        return strategy.getByColumn(type, leaveType, methodName,obj);
    }

    public void setByColumn(String type, String leaveType,String methodName, Double param, Object obj) {
        strategy = InvokeStrategyFactory.getInstance().creator(type);
        strategy.setByColumn(type, leaveType,methodName, param, obj);
    }

    public InvokeStrategy getStrategy() {
        return strategy;
    }

    public void setStrategy(InvokeStrategy strategy) {
        this.strategy = strategy;
    }
}

```

### 使用
```text
        InvokeStrategyContext context = new InvokeStrategyContext();
        Lock lock = new ReentrantLock();
        lock.lock();
        try {
        //只需这两行，就替换掉了一大堆的switch，并且能够轻松的进行中间操作，不再臃肿
            Double result = context.getByColumn("UserVacation", dto.getType(), methodOld, userVacation);
            context.setByColumn("UserVacation", dto.getLeaveType(), setMethod, result, userVacation);
        } catch (Exception e) {
            lock.unlock();
            log.error("更新uservation出现异常");
        } finally {
            lock.unlock();
        }
```