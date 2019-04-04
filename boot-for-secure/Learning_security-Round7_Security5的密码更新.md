- 使用Springboot 2.x去运行SpringSecurity，出现问题：
```text
Spring Security 无法登陆，报错：There is no PasswordEncoder mapped for the id “null”
```
- 这是因为Spring security 5.0中新增了多种加密方式，也改变了密码的格式
```text
Such that id is an identifier used to look up which PasswordEncoder should be used and encodedPassword is the original 
encoded password for the selected PasswordEncoder. The id must be at the beginning of the password, start with { and end with }. 
If the id cannot be found, the id will be null. For example, the following might be a list of passwords encoded using different id. 
All of the original passwords are "password".

{bcrypt}$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG 
{noop}password 
{pbkdf2}5d923b44a6d129f3ddf3e3c8d29412723dcbde72445e8ef6bf3b508fbf17fa4ed4d6b99ca763d8dc 
{scrypt}$e0801$8bWJaSu2IKSn9Z9kM+TPXfOc/9bdYSrN1oD9qfVThWEwdRTnO7re7Ei+fUZRJ68k9lTyuTeUp4of4g24hHnazw==$OAOec05+bXxvuu/1qZ6NUR+xQYvYv7BeL1QxwRpY5Pc=  
{sha256}97cde38028ad898ebc02e690819fa220e88c62e0699403e94fff291cfffaf8410849f27605abcbc0


要求将加密方式和加密后的结果一同存入数据库中，便于解密方式的灵活变化
```
- Spring 5新支持的加密方式
```text
bcrypt - BCryptPasswordEncoder (Also used for encoding)
ldap - LdapShaPasswordEncoder
MD4 - Md4PasswordEncoder
MD5 - new MessageDigestPasswordEncoder("MD5")
noop - NoOpPasswordEncoder
pbkdf2 - Pbkdf2PasswordEncoder
scrypt - SCryptPasswordEncoder
SHA-1 - new MessageDigestPasswordEncoder("SHA-1")
SHA-256 - new MessageDigestPasswordEncoder("SHA-256")
sha256 - StandardPasswordEncoder
```