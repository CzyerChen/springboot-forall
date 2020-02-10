/**
 * Author:   claire
 * Date:    2020-02-08 - 15:51
 * Description: db1 年龄接口实现类
 * History:
 * <author>          <time>                   <version>           <desc>
 * claire          2020-02-08 - 15:51          V1.3.1          db1 年龄接口实现类
 */
package com.mybatis.multidb.service.db2;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mybatis.multidb.entity.db2.TestAgeType;
import com.mybatis.multidb.mapper.db2.Db2AgeTypeMapper;
import org.springframework.stereotype.Service;

/**
 * 功能简述 <br/> 
 * 〈 db1 年龄接口实现类 〉
 *
 * @author claire
 * @date 2020-02-08 - 15:51
 */
@Service
public class Db2AgeTypeServiceImpl extends ServiceImpl<Db2AgeTypeMapper,TestAgeType> implements IDb2AgeTypeService {
    @Override
    public TestAgeType findByCode(Integer code) {
        LambdaQueryWrapper<TestAgeType> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TestAgeType::getAgeCode,code);
        return baseMapper.selectOne(wrapper);
    }
}
