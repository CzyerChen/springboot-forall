> 可以使用Shiro的RunAs 功能，即允许一个用户假装为另一个用户（如果他们允许）的身份进行访问。

- 实体
```text
public class UserRunAs implements Serializable {
    private Long fromUserId;//授予身份帐号
    private Long toUserId;//被授予身份帐号
}
```
- 服务定义
```text
public interface UserRunAsService {
    public void grantRunAs(Long fromUserId, Long toUserId);
    public void revokeRunAs(Long fromUserId, Long toUserId);
    public boolean exists(Long fromUserId, Long toUserId);
    public List<Long> findFromUserIds(Long toUserId);
    public List<Long> findToUserIds(Long fromUserId);
}
```
- JPA联合主键的设计和使用，jpa本地方法的设计和使用，包含联合主键的应用
- 自定义注解，实现获取http请求中的user信息


