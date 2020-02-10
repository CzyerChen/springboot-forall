/**
 * Author:   claire
 * Date:    2020-02-08 - 15:50
 * Description: db1 年龄业务接口类
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2020-02-08 - 15:50          V1.3.1           db1 年龄业务接口类
 */
package com.mybatis.multidb.service.db2;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mybatis.multidb.entity.db2.TestAgeType;

/**
 * 功能简述 <br/> 
 * 〈db1 年龄业务接口类〉
 *
 * @author claire
 * @date 2020-02-08 - 15:50
 */
public interface IDb2AgeTypeService extends IService<TestAgeType> {
    TestAgeType findByCode(Integer code);
}
