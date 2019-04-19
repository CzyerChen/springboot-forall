> 使用IdClass注解方式

### 复合ID类定义
- 注意序列化
- 需要默认的public无参数的构造方法
- 重写equals和hashCode方法
```text
public class RunAsIdClass implements Serializable {
    private Long fromUserId;//授予身份帐号
    private Long toUserId;//被授予身份帐号

    public Long getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(Long fromUserId) {
        this.fromUserId = fromUserId;
    }

    public Long getToUserId() {
        return toUserId;
    }

    public void setToUserId(Long toUserId) {
        this.toUserId = toUserId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RunAsIdClass userRunAs = (RunAsIdClass) o;

        if (fromUserId != null ? !fromUserId.equals(userRunAs.fromUserId) : userRunAs.fromUserId != null) return false;
        if (toUserId != null ? !toUserId.equals(userRunAs.toUserId) : userRunAs.toUserId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = fromUserId != null ? fromUserId.hashCode() : 0;
        result = 31 * result + (toUserId != null ? toUserId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UserRunAs{" +
                "fromUserId=" + fromUserId +
                ", toUserId=" + toUserId +
                '}';
    }
}

```

### 实体类定义
- 核心作用@IdClass(RunAsIdClass.class)
- 注意两个主键都加上@Id哦
```text
@Entity
@Table(name = "sys_user_runas")
@IdClass(RunAsIdClass.class)
public class UserRunAs implements Serializable {
    private Long fromUserId;//授予身份帐号
    private Long toUserId;//被授予身份帐号
   

    @Id
    public Long getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(Long fromUserId) {
        this.fromUserId = fromUserId;
    }

    @Id
    public Long getToUserId() {
        return toUserId;
    }

    public void setToUserId(Long toUserId) {
        this.toUserId = toUserId;
    }
}
```

### 使用
- 将自定义的联合主键标识为ID
```text
public interface UserRunAsRepository extends JpaRepository<UserRunAs, RunAsIdClass> ,UserRunAsRepositoryCustom{

    List<Long> findByFromUserId(Long fromUserId);

    @Query(value = "SELECT  from_user_id FROM sys_user_runas WHERE  to_user_id =:toUserId",nativeQuery = true)
    List<Long> findFromUserIds(Long toUserId);
}


RunAsIdClass runAsIdClass = new RunAsIdClass("1","2");
entityManager.find(UserRunAs.class,runAsIdClass);

```