/**
 * Author:   claire
 * Date:    2020-02-08 - 17:04
 * Description: db3-产品测试
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2020-02-08 - 17:04          V1.3.1           db3-产品测试
 */
package com.mybatis.multidb.service.db3;

import com.mybatis.multidb.AbstractTestMain;
import com.mybatis.multidb.entity.db3.TestProduct;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 功能简述 <br/> 
 * 〈db3-产品测试〉
 *
 * @author claire
 * @date 2020-02-08 - 17:04
 */
public class Db3ProductServiceTest extends AbstractTestMain {
    @Autowired
    private IDb3ProductService productService;
    @Test
    public void testDb3(){
        TestProduct product = productService.findById(1);
        Assert.assertNotNull(product);
    }
}
