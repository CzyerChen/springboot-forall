package com.forjpa.repository;

import com.forjpa.domain.Address;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -04 - 26 9:04
 */
public interface AddressRepository extends JpaRepository<Address,Integer> {
}
