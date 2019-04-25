package com.jpatest;

import com.forjpa.JpaTestApplication;
import com.forjpa.domain.User;
import com.forjpa.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -04 - 25 20:18
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = JpaTestApplication.class)
@WebAppConfiguration
public class ApplicationTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void test1(){
        User user = new User(0, "claire", 2, "11111111111", "hz", "xxxx@gmail.com");
        userRepository.save(user);
    }
}
