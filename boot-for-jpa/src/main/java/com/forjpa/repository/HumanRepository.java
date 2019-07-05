package com.forjpa.repository;

import com.forjpa.domain.Human;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -04 - 26 14:51
 */
public interface HumanRepository  extends JpaRepository<Human,Integer>, JpaSpecificationExecutor<Human>,HumanRepositoryCustom , QuerydslPredicateExecutor {
    @Query(value = "select id,name,email,phone from t_human where email like %:email% ",nativeQuery = true)
    Human findByEmailMatch(@Param("email") String email);


    @Query(value = "select * from t_human where name=:name",nativeQuery = true) //这边注意冒号等号和参数之间不能有空格，不然无法识别冒号
    Human findHumanByName(@Param("name")String name);


    @Query(value = "select * from t_human t where t.name=?1", nativeQuery = true) //这个映射简单粗暴
    List<Human> findByName(String name);

    @Query(value = "select * from #{#entityName} t where t.name=?1", nativeQuery = true)
    List<Human> findByName1(String name);
}
