package com.mappers.repository;

import com.mappers.domain.ProfilePO;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -03 - 25 15:15
 */
public  interface ProfileRepository extends JpaRepository<ProfilePO,Integer> {
    ProfilePO findById(int id);
}
