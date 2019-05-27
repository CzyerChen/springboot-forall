package com.aop.service;

import java.lang.reflect.Proxy;

public class ProxyServiceImpl implements ProxyService {
    public void testInfo() {
        System.out.print("proxy info");
    }

    public  static void main(String[] args){
        ProxyServiceImpl impl = new ProxyServiceImpl();
        ProxyService o = (ProxyService) Proxy.newProxyInstance(ProxyService.class.getClassLoader(),new Class<?>[]{ProxyService.class},new SimpleInvocationHandler(impl));
        o.testInfo();
    }
}
