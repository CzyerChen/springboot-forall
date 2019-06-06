package com.mybatis.self;

import java.lang.reflect.Proxy;

public class SelfSqlSession {

    private Executor executor = new SelfExecutor();

    private SelfConfiguration configuration = new SelfConfiguration();

    public <T> T selectOne(String statement,Object parameter){
        return executor.query(statement, parameter);
    }

    @SuppressWarnings("unchecked")
    public <T> T getMapper(Class<T> clas){
        //动态代理调用
        return (T) Proxy.newProxyInstance(clas.getClassLoader(),new Class[]{clas},
                new SelfMapperProxy(configuration,this));
    }

}
