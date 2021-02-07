/**
 * Author:   claire
 * Date:    2021-01-07 - 11:42
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2021-01-07 - 11:42          V1.14.0
 */
package com.learning.controller;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.learning.entity.Product;
import com.learning.entity.User;
import com.learning.service.BussinessService;
import com.learning.utils.generator.SnowflakeShardingKeyGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 功能简述 <br/> 
 * 〈〉
 *
 * @author claire
 * @date 2021-01-07 - 11:42
 */
@Slf4j
@RestController
public class BussinessController {
    @Autowired
    private BussinessService bussinessService;
    @Autowired
    private SnowflakeShardingKeyGenerator snowflakeIdGenerator;

    @InitBinder
    public void initBinder(WebDataBinder binder, WebRequest request) {
        //转换日期
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    @GetMapping("/buss/createProduct")
    public String createProduct() {
        Product product = new Product();
        product.setProductId(snowflakeIdGenerator.generateKey().longValue());
        product.setCode("66666666"+snowflakeIdGenerator.generateKey().longValue()+"202101" );
        product.setName("商品20210107");
        product.setDescription("商品描述20210107");
        product.setCreateTime(new Date());
        bussinessService.saveProducts(product);
//        for (int i = 1; i < 10; i++) {
//            Product product = new Product();
//            product.setProductId(snowflakeIdGenerator.generateKey().longValue());
//            product.setCode("P00" + i);
//            product.setName("商品" + i);
//            product.setDescription("商品描述" + i);
//            product.setCreateTime(new Date());
//            bussinessService.saveProducts(product);
//        }
        return "成功";
    }


    @GetMapping("/buss/queryProduct/{id}")
    public String queryProduct(@PathVariable String id) {
        return  JSON.toJSONString(bussinessService.queryProductById(Long.valueOf(id)));
    }

    /*mybatis-plus 不支持*/
    @GetMapping("/buss/queryProductPlus/{id}")
    public String queryProductPlus(@PathVariable String id) {
        return  JSON.toJSONString(bussinessService.queryProductByIdPlus(Long.valueOf(id)));
    }

    @GetMapping("/buss/create")
    public String create() {
        for (int i = 1; i <= 21; i++) {
            User user = new User();
            user.setName("testusera" + i);
            user.setGender(0);
            user.setAge(20 + i);
            user.setBirthDate(DateUtil.parseDate("1989-08-16"));
            user.setIdNumber("4101231989691" + i);

            bussinessService.saveAll(user);
        }

        for (int i = 1; i <= 21; i++) {
            User user = new User();
            user.setName("testuserb" + i);
            user.setGender(1);
            user.setAge(20 + i);
            user.setBirthDate(DateUtil.parseDate("1989-08-16"));
            user.setIdNumber("1101231989691" + i);

            bussinessService.saveAll(user);
        }

        return "成功";
    }

    @GetMapping("/buss/all")
    public String findAll(){
        Map<String,Object> result = new HashMap<>();
        result = bussinessService.findAll();
        return JSON.toJSONString(result);
    }

}
