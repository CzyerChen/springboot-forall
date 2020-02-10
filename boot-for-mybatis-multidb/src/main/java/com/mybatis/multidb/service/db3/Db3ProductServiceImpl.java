/**
 * Author:   claire
 * Date:    2020-02-08 - 17:03
 * Description: db3-产品业务实现类
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2020-02-08 - 17:03          V1.3.1           db3-产品业务实现类
 */
package com.mybatis.multidb.service.db3;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mybatis.multidb.entity.db3.TestProduct;
import com.mybatis.multidb.mapper.db3.Db3ProductMapper;
import org.springframework.stereotype.Service;

/**
 * 功能简述 <br/> 
 * 〈db3-产品业务实现类〉
 *
 * @author claire
 * @date 2020-02-08 - 17:03
 */
@Service
public class Db3ProductServiceImpl extends ServiceImpl<Db3ProductMapper, TestProduct> implements IDb3ProductService {
    @Override
    public TestProduct findById(Integer id) {
        return baseMapper.selectById(id);
    }
}
