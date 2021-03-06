package com.stest.service;

import org.springframework.cglib.proxy.Enhancer;

public class CGlibProxy {
    @SuppressWarnings("unchecked")
    public static <T> T getProxy(Class<T> clazz){
        Enhancer  enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(new SimpleInterceptor());
        return (T)enhancer.create();
    }
    public static void main(String[] args){
       /* ProxyService proxy = getProxy(ProxyServiceImpl.class);
        proxy.testInfo();
*/
        ProxyServiceImpl proxy1 = new SimpleInterceptor().getProxy(ProxyServiceImpl.class);
        proxy1.testInfo();
    }
}
