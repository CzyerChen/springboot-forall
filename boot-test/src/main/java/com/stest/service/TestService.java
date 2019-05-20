package com.stest.service;

import com.stest.domain.TUser;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class TestService {

    public void test1() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class<?> aClass = Class.forName("com.stest.domain.TUser");
        TUser tUser = new TUser();
        Method[] declaredMethods = aClass.getDeclaredMethods();
        Field[] declaredFields = aClass.getDeclaredFields();
        for(Field f : declaredFields){
            Object o = f.get(tUser);
        }
        for(Method m : declaredMethods){
           Annotation[] declaredAnnotations = m.getDeclaredAnnotations();
           for(Annotation a : declaredAnnotations){

           }
       }
    }

    public  static void main(String[] args){
        ProxyServiceImpl impl = new ProxyServiceImpl();
        ProxyService o = (ProxyService) Proxy.newProxyInstance(ProxyService.class.getClassLoader(),new Class<?>[]{ProxyService.class},new SimpleInvocationHandler(impl));
        o.testInfo();
    }
}
