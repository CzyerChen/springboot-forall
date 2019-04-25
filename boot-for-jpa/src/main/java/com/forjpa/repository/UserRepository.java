package com.forjpa.repository;

import com.forjpa.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -04 - 25 20:20
 */
public interface UserRepository extends JpaRepository<User,Integer> {
}
