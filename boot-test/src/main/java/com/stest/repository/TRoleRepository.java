package com.stest.repository;


import com.stest.domain.TRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -04 - 08 17:25
 */
public interface TRoleRepository extends JpaRepository<TRole,Integer> {
}
