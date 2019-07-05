package com.forjpa.repository;

import com.forjpa.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -04 - 26 10:01
 */
public interface ProductRepository extends JpaRepository<Product,Integer> {
    Product findByPname(String pname);
}
