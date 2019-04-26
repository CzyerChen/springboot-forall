package com.forjpa.repository;

import com.forjpa.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -04 - 26 9:05
 */
public interface CustomerRepository extends JpaRepository<Customer,Integer> {
    Customer findByCname(String cname);
}
