/**
 * Author:   claire
 * Date:    2020-02-08 - 16:05
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2020-02-08 - 16:05          V1.3.1
 */
package com.mybatis.multidb.service.db2;

import com.mybatis.multidb.AbstractTestMain;
import com.mybatis.multidb.entity.db2.TestAgeType;
import com.mybatis.multidb.service.db2.IDb2AgeTypeService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 功能简述 <br/> 
 * 〈Db2 测试〉
 *
 * @author claire
 * @date 2020-02-08 - 16:05
 */
public class Db2AgeTypeServiceTest extends AbstractTestMain {
    @Autowired
    private IDb2AgeTypeService ageTypeService;

    @Test
    public void testDb2(){
        TestAgeType ageType = ageTypeService.findByCode(1);
        Assert.assertNotNull(ageType);
    }
}
