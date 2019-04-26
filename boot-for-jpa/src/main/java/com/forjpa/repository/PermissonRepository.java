package com.forjpa.repository;

import com.forjpa.domain.Permisson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -04 - 26 10:24
 */
public interface PermissonRepository extends JpaRepository<Permisson,Integer> {
    List<Permisson> findByTab(String tab);
}
