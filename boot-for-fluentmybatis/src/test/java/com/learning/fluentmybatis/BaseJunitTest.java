/**
 * Author:   claire
 * Date:    2023/8/8 - 5:39 下午
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2023/8/8 - 5:39 下午          V1.0.0
 */
package com.learning.fluentmybatis;

import com.learning.fluentmybatis.FluentMybatisApplication;
import com.learning.fluentmybatis.entity.ProductInfoEntity;
import com.learning.fluentmybatis.mapper.ProductInfoMapper;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 功能简述 
 * 〈〉
 *
 * @author claire
 * @date 2023/8/8 - 5:39 下午
 * @since 1.0.0
 */
@SpringBootTest(classes = FluentMybatisApplication.class)
public class BaseJunitTest {
    @Qualifier("fmProductInfoMapper")
    @Autowired
    private ProductInfoMapper productInfoMapper;
    
    @Test
    public void test(){
        ProductInfoEntity entity = new ProductInfoEntity();
        entity.setName("test4");
        entity.setDisplayName("TEST4");
        productInfoMapper.insert(entity);
        System.out.println(entity.getId());
    }
}
