package com.mybatis.self;

public interface Executor {
    public <T> T query(String statement,Object parameter);
}
