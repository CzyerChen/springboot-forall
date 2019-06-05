package com.mybatis.dao.mapper1;

import com.mybatis.domain.Person1;
import org.apache.ibatis.annotations.Select;

public interface Person1Mapper {
    @Select("select * from t_people where pid = #{id} ")
    public Person1 findbyId(int id);

    public Person1 getUser(int id);
}
