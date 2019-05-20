package com.notes.config;

public class SingletonTest {
    //volatile防止指令重排
    private  volatile static SingletonTest instance;

    public SingletonTest(){

    }

    public SingletonTest getInstance(){
        if(instance == null){
            synchronized (SingletonTest.class){
                if(instance == null){
                    //1.创建一个对象，jvm分配内存
                    //2.初始化对象
                    //3.指向内存区域
                    //如果不使用volatile就可能造成2，3指令调整，那么就会出现导致对象未初始化
                 instance = new SingletonTest();
                }
            }
        }
        return instance;
    }

}
