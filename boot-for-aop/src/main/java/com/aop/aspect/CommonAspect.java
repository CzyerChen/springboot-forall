package com.aop.aspect;


import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Aspect
@Order(10)
@Component
public class CommonAspect {
    private static final Logger log = Logger.getLogger("CommonAspect");

    @Before(value = "execution(* com.aop.controller.TestController.*(..))")
    public void before(){
        System.out.print("事前增强");
    }


    @After("execution(* com.aop.controller.TestController.*(..))")
    public void after(){
        System.out.print("事后增强");
    }


}
