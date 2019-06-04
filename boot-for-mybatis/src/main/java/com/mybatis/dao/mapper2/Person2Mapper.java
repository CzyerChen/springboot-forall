package com.mybatis.dao.mapper2;

import com.mybatis.domain.Person2;
import org.apache.ibatis.annotations.Select;

public interface Person2Mapper {

    @Select("select * from person where id = #{id} ")
    public Person2 findbyId(int id);
}
