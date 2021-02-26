package boot.mybatisplus;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by claire on 2019-09-16 - 09:44
 **/
@SpringBootApplication(exclude = DruidDataSourceAutoConfigure.class)
@ComponentScan(basePackages = {"boot.mybatisplus"})
@EntityScan(basePackages = {"boot.mybatisplus"})
@MapperScan(basePackages = {"boot.mybatisplus"})
@EnableEncryptableProperties
public class PlusApplication {
    public static void main(String[] args){
        SpringApplication.run(PlusApplication.class,args);
    }
}
