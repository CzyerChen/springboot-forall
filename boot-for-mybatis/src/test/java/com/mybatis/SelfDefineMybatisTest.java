package com.mybatis;

import com.mybatis.dao.mapper1.Person1Mapper;
import com.mybatis.domain.Person1;
import com.mybatis.self.SelfSqlSession;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public class SelfDefineMybatisTest {

    @Test
    public void test(){
        SelfSqlSession sqlSession = new SelfSqlSession();
        Person1Mapper mapper  = sqlSession.getMapper(Person1Mapper.class);
        Person1 person1 = mapper.getUser(1);
        Assert.assertNotNull(person1);
    }
}
