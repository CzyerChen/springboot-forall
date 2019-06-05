package com.mybatis;

import com.mybatis.domain.Person1;
import com.mybatis.domain.Person2;
import com.mybatis.service.DynamicService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@SpringBootTest(classes = MybatisTestApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class DynamicDbTest {

    @Autowired
    private DynamicService dynamicService;

    @Test
    public void test1(){
        Person1 entitySwitchDB1 = dynamicService.getEntitySwitchDB1();
        Assert.assertNotNull(entitySwitchDB1);
        Person2 entitySwitchDB2 = dynamicService.getEntitySwitchDB2();
        Assert.assertNotNull(entitySwitchDB2);
    }
}
