package com.mybatis.service;


import com.mybatis.constant.DSType;
import com.mybatis.constant.DataSourceTypeEnum;
import com.mybatis.dao.mapper1.Person1Mapper;
import com.mybatis.dao.mapper2.Person2Mapper;
import com.mybatis.domain.Person1;
import com.mybatis.domain.Person2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DynamicService {
    @Autowired
    private Person1Mapper person1Mapper;
    @Autowired
    private Person2Mapper person2Mapper;

    @DSType(value = DataSourceTypeEnum.DB1)
    public Person1 getEntitySwitchDB1(){
       return person1Mapper.findbyId(1);
    }

    @DSType(value = DataSourceTypeEnum.DB2)
    public Person2 getEntitySwitchDB2(){
        return person2Mapper.findbyId(1);
    }
}
