/**
 * Author:   claire
 * Date:    2021-01-07 - 14:00
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2021-01-07 - 14:00          V1.14.0
 */
package com.learning.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.learning.entity.Product;
import org.apache.ibatis.annotations.Param;

/**
 * 功能简述 <br/> 
 * 〈〉
 *
 * @author claire
 * @date 2021-01-07 - 14:00
 */
public interface ProductMapper extends BaseMapper<Product> {
    int insert(Product record);

    Product queryById(@Param("id") Long id);
}
