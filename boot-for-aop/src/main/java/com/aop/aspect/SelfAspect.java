package com.aop.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

@Aspect
@Component
public class SelfAspect {

    /**
     * 切入点
     */
    @Pointcut("@annotation(com.aop.aspect.SelfAspectAnnotation)")
    public void selfAnnotation(){

    }

    /**
     * 切入方法
     */
    @Pointcut("execution(* com.aop.controller.SelfController.*(..))")
    public void point(){

    }

    @Before("execution(* findById*(..)) &&"+"args(id,..)")
    public void beforeMethod(int id){
        System.out.print("获取相应方法的增强");
    }

    @Around(value = "selfAnnotation() && point() && @annotation(selfAspectAnnotation)")
    public Object around(ProceedingJoinPoint joinPoint,SelfAspectAnnotation selfAspectAnnotation) throws Throwable {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

        String sessionId = requestAttributes.getSessionId();
        String value = selfAspectAnnotation.value();

        Object rvt = joinPoint.proceed();
        System.out.println("Around增强：执行方法之后，模拟结束事物 :"+rvt);
        if(rvt != null && rvt instanceof String) {
           return rvt;
        }
        return null;
    }
}
