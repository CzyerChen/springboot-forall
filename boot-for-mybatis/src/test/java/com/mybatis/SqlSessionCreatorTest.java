package com.mybatis;

import com.mybatis.dao.mapper1.Person1Mapper;
import com.mybatis.domain.Person1;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.InputStream;

@RunWith(SpringJUnit4ClassRunner.class)
public class SqlSessionCreatorTest {

    @Test
    public void testSqlSession(){
        String file = "config.xml";
        InputStream asStream = this.getClass().getClassLoader().getResourceAsStream(file);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(asStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();

        Person1Mapper mapper = sqlSession.getMapper(Person1Mapper.class);
        Person1 user = mapper.getUser(1);
        Assert.assertNotNull(user);
    }
}
