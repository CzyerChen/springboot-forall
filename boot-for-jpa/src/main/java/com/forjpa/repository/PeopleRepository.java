package com.forjpa.repository;

import com.forjpa.domain.People;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -04 - 26 9:24
 */
public interface PeopleRepository extends JpaRepository<People,Integer> {
    People findByPname(String pname);
}
