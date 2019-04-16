package com.shiro.service.impl;

import com.shiro.domain.UrlFilter;
import com.shiro.repository.UrlFilterRepository;
import com.shiro.service.ShiroFilerChainManager;
import com.shiro.service.UrlFilterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -04 - 15 18:13
 */
@Service
public class UrlFilterServiceImpl implements UrlFilterService {
    @Autowired
    private UrlFilterRepository urlFilterRepository;

    //@Autowired
    private ShiroFilerChainManager shiroFilerChainManager;

    @Override
    public UrlFilter createUrlFilter(UrlFilter urlFilter) {
        urlFilterRepository.save(urlFilter);
        initFilterChain();
        return urlFilter;
    }



    @Override
    public UrlFilter updateUrlFilter(UrlFilter urlFilter) {
        urlFilterRepository.save(urlFilter);
        initFilterChain();
        return urlFilter;
    }

    @Override
    public void deleteUrlFilter(Long urlFilterId) {
        urlFilterRepository.deleteById(urlFilterId);
        initFilterChain();
    }

    @Override
    public UrlFilter findOne(Long urlFilterId) {
        Optional<UrlFilter> byId = urlFilterRepository.findById(urlFilterId);
        if(byId.isPresent()){
            return byId.get();
        }
        return null;
    }

    @Override
    public List<UrlFilter> findAll() {
        return urlFilterRepository.findAll();
    }

    @PostConstruct
    public void initFilterChain() {
        //shiroFilerChainManager.initFilterChains(findAll());
    }
}
