/**
 * Author:   claire
 * Date:    2021-02-25 - 16:57
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2021-02-25 - 16:57          V1.17.0
 */
package boot.mybatisplus;

import boot.mybatisplus.config.KerberosWebHDFSConnection;
import boot.mybatisplus.dao.AgeTypeMapper;
import boot.mybatisplus.dao.UserMapper;
import boot.mybatisplus.domain.AuthUser;
import boot.mybatisplus.domain.AgeTypeMapping;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.hadoop.security.authentication.client.AuthenticationException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

/**
 * 功能简述 
 * 〈〉
 *
 * @author claire
 * @date 2021-02-25 - 16:57
 */
@SpringBootTest(classes = PlusApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class UserMapperTest {
    KerberosWebHDFSConnection conn = null;

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AgeTypeMapper dimAgeTypeMapper;

    @Test
    public void testUser(){
        AuthUser user = userMapper.selectOne(Wrappers.<AuthUser>lambdaQuery().eq(AuthUser::getUserName, "xxxx"));
        Assert.assertNotNull(user);
    }

    @Test
    public void testAgeMapping(){
        AgeTypeMapping ageTypeMapping = dimAgeTypeMapper.selectOne(Wrappers.<AgeTypeMapping>lambdaQuery().eq(AgeTypeMapping::getAgeCode, 1));
        Assert.assertNotNull(ageTypeMapping);
    }


    @Before
    public void setUp() throws Exception {
        conn = new KerberosWebHDFSConnection("http://xxxx:14000", null, null);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getHomeDirectory() throws MalformedURLException, IOException, AuthenticationException {

        String json = conn.getHomeDirectory();
        System.out.println(json);
    }

    @Test
    public void listStatus() throws MalformedURLException, IOException, AuthenticationException {
        String path= "tmp";
        String json = conn.listStatus(path);
        System.out.println(json);
    }

    @Test
    public void open() throws MalformedURLException, IOException, AuthenticationException {
        String path="tmp/mktest/result.txt";
        FileOutputStream os = new  FileOutputStream(new File("/testupload3.txt"));
        String json = conn.open(path, os);
        System.out.println(json);
    }


    @Test
    public void create() throws MalformedURLException, IOException, AuthenticationException {
        FileInputStream is = new FileInputStream(new File("/testupload3.txt"));
        String path="tmp/mktest/result.txt";
        String json = conn.create(path, is);
        System.out.println(json);
    }

    //@Test
    public void delete() throws MalformedURLException, IOException, AuthenticationException {
        String path="w";
        String json = conn.delete(path);
        System.out.println(json);
    }

}
