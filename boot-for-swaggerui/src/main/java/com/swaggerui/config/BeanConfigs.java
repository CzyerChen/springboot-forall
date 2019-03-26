package com.swaggerui.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -03 - 26 13:00
 */
@Configuration
public class BeanConfigs {
    @Bean
    public javax.validation.Validator getValidator(){
        return new LocalValidatorFactoryBean();
    }
}
