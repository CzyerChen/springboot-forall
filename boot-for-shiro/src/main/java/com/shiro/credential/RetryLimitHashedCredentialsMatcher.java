package com.shiro.credential;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -04 - 17 14:20
 */
public class RetryLimitHashedCredentialsMatcher extends HashedCredentialsMatcher {

    private Cache<String, AtomicInteger> retryCount;
    private int retryMax ;

    public RetryLimitHashedCredentialsMatcher(CacheManager cacheManager){
       retryCount = cacheManager.getCache("login_retry_count");
    }


    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        String username = (String)token.getPrincipal();

        AtomicInteger count = retryCount.get(username);
        if(count == null){
            retryCount.put(username,new AtomicInteger(0));
        }
        if(count.incrementAndGet() >retryMax){
            //超出次数，可以抛出异常
        }

        boolean match = super.doCredentialsMatch(token, info);
        if(match){
            retryCount.remove(username);
        }
        return  match;
    }

    public int getRetryMax() {
        return retryMax;
    }

    public void setRetryMax(int retryMax) {
        this.retryMax = retryMax;
    }
}
