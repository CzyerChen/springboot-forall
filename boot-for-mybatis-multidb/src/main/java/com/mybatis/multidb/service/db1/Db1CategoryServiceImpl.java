/**
 * Author:   claire
 * Date:    2020-02-08 - 16:52
 * Description: 分组业务接口实现类
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2020-02-08 - 16:52          V1.3.1           分组业务接口实现类
 */
package com.mybatis.multidb.service.db1;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mybatis.multidb.entity.db1.TestCategory;
import com.mybatis.multidb.mapper.db1.Db1CategoryMapper;
import org.springframework.stereotype.Service;

/**
 * 功能简述 <br/> 
 * 〈分组业务接口实现类〉
 *
 * @author claire
 * @date 2020-02-08 - 16:52
 */
@Service
public class Db1CategoryServiceImpl extends ServiceImpl<Db1CategoryMapper, TestCategory> implements IDb1CategoryService {
    @Override
    public TestCategory findById(Integer id) {
        return baseMapper.selectById(id);
    }
}
