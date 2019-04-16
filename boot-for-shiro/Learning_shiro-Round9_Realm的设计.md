> 基于角色权限的设计，非基于角色资源的设计
> 使用关系表，而非使用外键设计


### 用户 角色  权限

- 用户：包括：编号(id)、用户名(username)、密码(password)、盐(salt)、是否锁定(locked)；是否锁定用于封禁用户使用，其实最好使用Enum 字段存储，可以实现更复杂的用户状态实现

- 角色：编号(id)、角色标识符（role）、描述（description）、是否可用（available）

- 权限：：编号（id）、权限标识符（permission）、描述（description）、是否可用（available）

- 两个关系实体：用户-角色实体（用户编号、角色编号，且组合为复合主键）；角色-权限实体（角色编号、权限编号，且组合为复合主键

### 继承AuthorizingRealm
- 需要实现doGetAuthenticationInfo，doGetAuthorizationInfo，一个负责身份认证，一个负责权限认证
#### AuthenticationToken
```text

                    AuthenticationToken  I
                              |
                              |
                              |
            |----------------------------------------|
RememberMeAuthenticationToken           HosAuthticationToken
            |-------------------------------------|  
                               |
                               |
                       UsernamePasswordToken 

 Shiro 提供了一个直接拿来用的UsernamePasswordToken，用于实现用户名/密码Token组，
 
 另外其实现了RememberMeAuthenticationToken和HostAuthenticationToken，可以实现记住
 
 我及主机验证的支持
```
#### AuthenticationInfo
```text
                          AuthenticationInfo I
                                   |
                                   |
                                   |
        |--------------------------|----------------------------|
     Account         MergableAuthticationInfo       SaltedAuthenticationInfo
         |                    |     |                    |         |
         |--------------------|-----|--------------------|         |
                      |             |------------------------------|
                      |                              |
                SimpleAccount                   SimpleAuthticationInfo
      
  1. 如果Realm 是AuthenticatingRealm 子类，则提供给AuthenticatingRealm 内部使用的CredentialsMatcher进行凭据验证
  
  2. 提供给SecurityManager来创建Subject
    
```
#### PrincipalCollection
- 多重身份信息的保存 PrincipalCollection
```text
        Serializable               Iterable 
             |                         |
             |-------------------------|
                          |
                  PrincipalCollectio
                      |         |
                      |         |
              |-------|         |-------|
              |                         |
   MutablePrincipalCollection      PrincipalMap
              |                         |
     SimplePrincipalCollection    SimplePrincipalMap

PrincipalCollection聚合了多个，此处最需要注意的是getPrimaryPrincipal，如果只有一

个Principal 那么直接返回即可，如果有多个Principal，则返回第一个（因为内部使用Map

存储，所以可以认为是返回任意一个）
```

#### AuthorizationInfo
```text
                 AuthorizationInfo I 
                        |
                        |
        |----------------------------------|
     Account                     SimpleAuthticationInfo
        |                          大多数时候使用这个即可
   SimpleAccount                  获取角色/权限信息用于授权验证
   
SimpleAccountRealm子类
实现动态角色/权限维护的
   
```

#### Subject
```text
            Subject I
                |
                |
           DelegatingSubject
           
    Subject是Shiro 的核心对象，基本所有身份验证、授权都是通过Subject完成
```
1.身份信息获取
2.身份验证
3.角色授权验证
4.权限授权验证
5.会话
6.退出
7.RunAs
8.多线程



