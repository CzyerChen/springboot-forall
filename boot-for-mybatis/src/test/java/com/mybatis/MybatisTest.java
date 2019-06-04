package com.mybatis;

import com.mybatis.dao.mapper1.Person1Mapper;
import com.mybatis.dao.mapper2.Person2Mapper;
import com.mybatis.domain.Person1;
import com.mybatis.domain.Person2;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.lang.annotation.Target;

@SpringBootTest(classes = MybatisTestApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class MybatisTest {

    @Autowired
    private Person1Mapper person1Mapper;
    @Autowired
    private Person2Mapper person2Mapper;

    @Test
    public void test1(){
        Person1 person1 = person1Mapper.findbyId(1);
        Person2 person2 = person2Mapper.findbyId(1);
        Assert.assertNotNull(person1);
        Assert.assertNotNull(person2);
    }
}


