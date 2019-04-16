> 功能：显示当前在线人数、当前在线用户，有时候可能需要强制某个用户下线

```text
获取所有会话信息，使用分页：可以获取totalSessions
Page<Session> getActiveSessions(int pageNumber, int pageSize);

redis存储会话：

session.id=会话序列化数据
session.ids=会话id Set列表
使用LLEN 获取长度，LRANGE分页获取

SET session.123 "Session序列化数据"
LPUSH session.ids 123

DEL session.123
LREM session.ids 123

LLEN session.ids

LRANGE key 0 10 #获取到会话ID
MGET session.1 session.2…… #根据第一条命令获取的会话ID 获取会话数据


```

```text
banner.txt生成
http://patorjk.com/software/taag/#p=display&f=Graffiti&t=spring%20for%20all
```