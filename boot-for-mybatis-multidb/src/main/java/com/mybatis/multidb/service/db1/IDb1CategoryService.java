/**
 * Author:   claire
 * Date:    2020-02-08 - 16:51
 * Description: 分组服务接口类
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2020-02-08 - 16:51          V1.3.1           分组服务接口类
 */
package com.mybatis.multidb.service.db1;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mybatis.multidb.entity.db1.TestCategory;

/**
 * 功能简述 <br/> 
 * 〈分组服务接口类〉
 *
 * @author claire
 * @date 2020-02-08 - 16:51
 */
public interface IDb1CategoryService extends IService<TestCategory> {
    TestCategory findById(Integer id);
}
