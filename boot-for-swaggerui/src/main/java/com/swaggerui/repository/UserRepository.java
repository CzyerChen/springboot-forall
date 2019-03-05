package com.swaggerui.repository;

import com.swaggerui.domain.UserPO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<UserPO,Integer> {

    List<UserPO> findAll();

    UserPO findById(int id);
}
