package com.aop.service;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodProxy;

public class CGlibProxy {
    @SuppressWarnings("unchecked")
    public static <T> T getProxy(Class<T> clazz){
        Enhancer  enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(new SimpleInterceptor());
        return (T)enhancer.create();
    }


    public static void main(String[] args){
        ProxyServiceImpl proxy = (ProxyServiceImpl)getProxy(ProxyServiceImpl.class);
        proxy.testInfo();
    }
}
