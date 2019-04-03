package com.mvc.config;

import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.List;

/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -04 - 03 15:34
 */
//@Configuration
public class MessageConverterConfig {/*extends WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter {

    public  void configureMessageConverters(List<MappingJackson2HttpMessageConverter> converters){
        Jackson2ObjectMapperBuilder builder = Jackson2ObjectMapperBuilder.xml();
        builder.indentOutput(true);
        converters.add(new MappingJackson2HttpMessageConverter(builder.build()));
    }*/

}
