package com.forjpa.repository;

import com.forjpa.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store,Integer> {
    Store findBySname(String sname);
}
