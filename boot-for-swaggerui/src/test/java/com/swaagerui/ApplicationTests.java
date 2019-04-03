package com.swaagerui;

import com.swaggerui.SwaggerAppMain;
import com.swaggerui.controller.UserController;
import com.swaggerui.domain.UserPO;
import com.swaggerui.service.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.client.match.ContentRequestMatchers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -04 - 03 13:32
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SwaggerAppMain.class)
//较新版的Spring Boot取消了SpringApplicationConfiguration(classes = MockServletContext.class)这个注解，用@SpringBootTest就可以了
@WebAppConfiguration
@AutoConfigureMockMvc
@ActiveProfiles("dev")
public class ApplicationTests {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private UserService userService;
    //方法一：自动注入 @AutoConfigureMockMvc  @Autowired  private MockMvc mvc;
    //方法二：注入一个单例的上下文
   /*
   private
   @Before  MockMvc mvc;
    public void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(new UserController()).build();
    }*/

   //利用mvc调用接口
    @Test
    public void testUserController() throws Exception {
        RequestBuilder request = null;

        request = get("/user/1");
        mvc.perform(request)
                .andExpect(status().isOk());

        /*request =post("/user/save")
                .param("id","1");*/
    }


    //针对接口层部分功能进行测试
    @Test
    public void testUser() {
        UserPO userById = userService.findUserById(1);
        Assert.assertNotNull(userById);
    }
}
