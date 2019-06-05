package com.mybatis.dao.mapper2;

import com.mybatis.domain.Person2;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

public interface Person2Mapper {

    @Select("select * from person where id = #{id} ")
    @Results(value = { @Result(id = true, column = "id", property = "id"),
            @Result(column = "person_id", property = "personId"),
            @Result(column = "person_name",property = "personName"),
            @Result(column = "sex",property = "sex"),
            @Result(column = "country",property = "country"),
            @Result(column = "address",property = "address")})
    public Person2 findbyId(int id);
}
