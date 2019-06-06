package com.mybatis.self;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

public class SelfMapperProxy implements InvocationHandler {
    private SelfConfiguration configuration;

    private SelfSqlSession sqlSession;

    public SelfMapperProxy(SelfConfiguration configuration,SelfSqlSession sqlSession){
     this.configuration = configuration;
     this.sqlSession = sqlSession;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        MapperBean mapperBean = configuration.readerMapper("mapper/PersonMapper.xml");
        if(!method.getDeclaringClass().getName().equals(mapperBean.getInterfaceName())){
            return  null;
        }
        List<Function> functions = mapperBean.getList();
        if(null != functions || 0!= functions.size()){
            for(Function f : functions){
                if(method.getName().equals(f.getFuncName())){
                    return  sqlSession.selectOne(f.getSql(),String.valueOf(args[0]));
                }
            }
        }

        return null;
    }
}
