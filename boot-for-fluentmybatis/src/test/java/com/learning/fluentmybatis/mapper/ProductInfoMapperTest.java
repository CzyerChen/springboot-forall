/**
 * Author:   claire
 * Date:    2023/8/8 - 6:09 下午
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2023/8/8 - 6:09 下午          V1.0.0
 */
package com.learning.fluentmybatis.mapper;

import com.learning.fluentmybatis.FluentMybatisApplication;
import com.learning.fluentmybatis.dao.base.ProductInfoBaseDao;
import com.learning.fluentmybatis.entity.ProductInfoEntity;
import com.learning.fluentmybatis.mapper.ProductInfoMapper;
//import org.junit.Test;
import com.learning.fluentmybatis.wrapper.ProductInfoUpdate;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

import java.util.Date;

/**
 * 功能简述 
 * 〈〉
 *
 * @author claire
 * @date 2023/8/8 - 6:09 下午
 * @since 1.0.0
 */
@SpringBootTest
public class ProductInfoMapperTest {
    @Qualifier("fmProductInfoMapper")
    @Autowired
    private ProductInfoMapper productInfoMapper;
    @Autowired
    private ProductInfoBaseDao productInfoBaseDao;

    @Test
    public void testInsert(){
        ProductInfoEntity entity = new ProductInfoEntity();
        entity.setName("test5");
        entity.setDisplayName("TEST5");
        productInfoMapper.insert(entity);
        System.out.println(entity.getId());
    }


    @Test
    public void testUpdate(){
        productInfoMapper.updateBy(productInfoMapper.updater().set.updateTime().is(new Date()).end()
                .where().id().eq(999L).end());
        ProductInfoEntity product = productInfoMapper.findOne(productInfoMapper.query().where().id().eq(999L).end());
        System.out.println(product.toString());
    }

    @Test
    public void testSelect(){
        ProductInfoEntity product = productInfoMapper.findOne(productInfoMapper.query().where().id().eq(999L).end());
        System.out.println(product.toString());
        
    }
}
