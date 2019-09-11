- 手写SQL中的大于小于符号（需要将符号进行替换）:
写法一：
```text
原符号       替换符号
<           &lt; 
<=          &lt;=
>           &gt; 
>=          &gt;=
&           &amp;
'           &apos;
"           &quot;

原SQL： select * from user where id > 1;
现SQL： select * from user where id &gt; 1

```
写法二：
```text
大于等于    <![CDATA[ >= ]]>
小于等于    <![CDATA[ <= ]]>
```

- 动态SQL：能写动态SQL就写动态SQL，动态SQL能够很好的避免参数为空的情况，尽量写有效的SQL片段，减少手写SQL的工作量
```text
select * from user 
<where>
<if test="title != null">
    AND title like #{title}
</if>
<if test="list != null && list.size() >0">
    AND user_name IN
    <foreach collection="list" index="index" item="item" open="(" separator="," close=")">
                #{item}
   </foreach>
 </if>
</where>

```
- mybatis中的choose，类似于Java中的switch,有一个条件分支是必须走到的
但是<where>标签不一样，所有的参数都是动态的，当里面的if标签都不满足的情况下，参数回事where 1=1的情况
```text
select * from user
<choose>
    <when test="title != null">
    and title like #{title%}
    </when>
    <otherwise test="name != null">
    and name = #{name}
    </otherwise>
</choose>    
```

- MAP作为parameterType
```text
mapper.java中：

public List<User> getList(@Param(value = "ew") Wrapper wrapper, 
			@Param(value = "name") String name);	

mapper.xml中：
resultMap主要做列映射，出现在数据库与实体类型字段名或者类型不同的情况，
如果字段名一致，可以采用resultMap="com.testdemo.entity.dto.DemoEntry"的方式

<select id="getList" resultMap="userMap" parameterType="java.util.Map">
		select *
		from user 
		<where>
		${ew.sqlSegment}
		</where>
		order by #{name}
	</select>
```

- IN语句
```text
@Param(value="list") List<String> list,

where name in 
<foreach collection="list",index="index" item="item" open="(" separator="," close=")">
      #{item}
</foreach>

```
