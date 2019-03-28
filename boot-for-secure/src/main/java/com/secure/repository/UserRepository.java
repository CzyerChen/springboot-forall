package com.secure.repository;

import com.secure.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -03 - 28 9:26
 */
public interface UserRepository extends JpaRepository<User,Integer> {
}
