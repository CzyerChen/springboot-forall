package com.shiro.repository;

import com.shiro.domain.UrlFilter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UrlFilterRepository extends JpaRepository<UrlFilter,Long> {
   /* public UrlFilter createUrlFilter(UrlFilter urlFilter);
    public UrlFilter updateUrlFilter(UrlFilter urlFilter);
    public void deleteUrlFilter(Long urlFilterId);

    public UrlFilter findOne(Long urlFilterId);
    public List<UrlFilter> findAll();*/
}
