/**
 * Author:   claire
 * Date:    2020-02-08 - 16:53
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2020-02-08 - 16:53          V1.3.1
 */
package com.mybatis.multidb.service.db1;

import com.mybatis.multidb.AbstractTestMain;
import com.mybatis.multidb.entity.db1.TestCategory;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 功能简述 <br/> 
 * 〈db1 test〉
 *
 * @author claire
 * @date 2020-02-08 - 16:53
 */
public class Db1CategoryServiceTest extends AbstractTestMain {
    @Autowired
    private IDb1CategoryService categoryService;

    @Test
    public void testDb1(){
        TestCategory category = categoryService.findById(1);
        Assert.assertNotNull(category);
    }
}
