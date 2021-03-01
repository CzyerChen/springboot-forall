/**
 * Author:   claire
 * Date:    2021-02-25 - 16:57
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2021-02-25 - 16:57          V1.17.0
 */
package boot.mybatisplus;

import boot.mybatisplus.dao.AgeTypeMapper;
import boot.mybatisplus.dao.UserMapper;
import boot.mybatisplus.domain.AuthUser;
import boot.mybatisplus.domain.AgeTypeMapping;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AgeTypeMapper dimAgeTypeMapper;

    @Test
    public void testUser(){
        AuthUser user = userMapper.selectOne(Wrappers.<AuthUser>lambdaQuery().eq(AuthUser::getUserName, "dmpowner"));
        Assert.assertNotNull(user);
    }

    @Test
    public void testAgeMapping(){
        AgeTypeMapping ageTypeMapping = dimAgeTypeMapper.selectOne(Wrappers.<AgeTypeMapping>lambdaQuery().eq(AgeTypeMapping::getAgeCode, 1));
        Assert.assertNotNull(ageTypeMapping);
    }

}
