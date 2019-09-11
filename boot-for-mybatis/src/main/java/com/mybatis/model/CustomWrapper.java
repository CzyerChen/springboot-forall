package com.mybatis.model;

import com.google.common.base.CaseFormat;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.wrapper.MapWrapper;

import java.util.Map;

/**
 * Created by claire on 2019-09-11 - 15:44
 **/
public class CustomWrapper extends MapWrapper {
    public CustomWrapper(MetaObject metaObject, Map<String, Object> map) {
        super(metaObject, map);
    }

    @Override
    public String findProperty(String name, boolean useCamelCaseMapping) {
        if(useCamelCaseMapping){
            //CaseFormat是引用的 guava库,里面有转换驼峰的,免得自己重复造轮子,pom添加
            /**
             **         <dependency>
             <groupId>com.google.guava</groupId>
             <artifactId>guava</artifactId>
             <version>27.1-jre</version>
             </dependency>
             **/
            return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL,name);
        }
        return name;
    }
}
