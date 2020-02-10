/**
 * Author:   claire
 * Date:    2020-02-08 - 17:02
 * Description: db3-产品业务接口类
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2020-02-08 - 17:02          V1.3.1           db3-产品业务接口类
 */
package com.mybatis.multidb.service.db3;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mybatis.multidb.entity.db3.TestProduct;

/**
 * 功能简述 <br/> 
 * 〈db3-产品业务接口类〉
 *
 * @author claire
 * @date 2020-02-08 - 17:02
 */
public interface IDb3ProductService extends IService<TestProduct> {
    TestProduct findById(Integer id);
}
