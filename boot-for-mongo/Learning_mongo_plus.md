## 通过阅读博客了解并学习mongo-plus的使用和原理
http://blog.didispace.com/springbootmongodb-plus/

## mongoDB 连接URI的书写方式
- 标准URI连接方案
```text
mongodb://[username:password@]host1[:port1][,...hostN[:portN]]][/[database][?options]]
``` 
- 独立MongoDB
```text
mongodb://127.0.0.1:27017/admin
```
- 独立MongoDB 带auth
```text
mongodb://username:password@127.0.0.1:27017/admin
```
- 集群副本
```text
mongodb://mongodb0.example.com:27017,mongodb1.example.com:27017,mongodb2.example.com:27017/admin?replicaSet=myRepl
```

## mongoDB 连接URI中密码包含特殊字符的问题
- mongodb通常的URI书写方式是：String uri ="mongodb://username:password@127.0.0.1:27017/dbname";
- 如果当用户名或者密码中包含一些特殊字符的需要使用unicode编码进行转义，如包含符号@，冒号：，斜杠/或百分号％字符，请使用百分比编码方式消除歧义
- 比如username为ab@c，password为ha:ha），程序将无法分辨真正的username和password，从而导致程序无法正常访问mongodb
- 对username和password中包含的@和:进行url编码，@的url编码为%40，:的编码为%3a，其余特殊字符自行进行转换替换
```text
例如：
"mongodb://ab@C:he:he@127.0.0.1:27017/dbname"
如果不转义，无法访问
转义后：
"mongodb://ab%40C:he%30he@127.0.0.1:27017/dbname"
```

