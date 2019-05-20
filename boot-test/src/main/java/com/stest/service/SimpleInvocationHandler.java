package com.stest.service;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class SimpleInvocationHandler implements InvocationHandler {
    private Object impl;

    SimpleInvocationHandler(Object impl){
        this.impl = impl;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = method.invoke(impl, args);
        return result;
    }
}
