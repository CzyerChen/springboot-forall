/**
 * Author:   claire
 * Date:    2021-01-07 - 11:37
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2021-01-07 - 11:37          V1.14.0
 */
package com.learning.service;

import com.learning.dao.ProductMapper;
import com.learning.dao.UserMapper;
import com.learning.entity.Product;
import com.learning.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 功能简述 <br/> 
 * 〈〉
 *
 * @author claire
 * @date 2021-01-07 - 11:37
 */
@Service
public class BussinessService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ProductMapper productMapper;

    @Transactional
    public void saveAll(User user) {
        userMapper.insertSelective(user);
    }

    @Transactional
    public void saveProducts(Product product) {
        productMapper.insert(product);
    }

    public Product queryProductById(Long id){
        return productMapper.queryById(id);
    }

    /*mybatis-plus 的不支持，会有报错*/
    public Product queryProductByIdPlus(Long id){
        return productMapper.selectById(id);
    }

    public Map<String,Object> findAll(){
        return Collections.emptyMap();
    }

}
