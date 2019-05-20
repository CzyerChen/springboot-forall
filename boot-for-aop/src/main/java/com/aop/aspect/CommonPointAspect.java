package com.aop.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Order(100)
@Component
public class CommonPointAspect {
    @Pointcut("execution(* com.aop.controller.CommonController.*(..))")
    public void point() {

    }

    @Before("point()")
    public void before(JoinPoint joinPoint) {
        System.out.println("Before增强：模拟权限检查");
        System.out.println("Before增强：被织入增强处理的目标目标方法为：" + joinPoint.getSignature().getName());
        System.out.println("Before增强：目标方法的参数为：" + Arrays.toString(joinPoint.getArgs()));
        joinPoint.getArgs()[0] = "除了Around其他的都是是不可以修改目标方法的参数的";
        System.out.println("joinPoint.getArgs()[0]:"+joinPoint.getArgs()[0]);
        System.out.println("Before增强：目标方法的参数为：" + Arrays.toString(joinPoint.getArgs()));
        System.out.println("Before增强：被织入增强处理的目标对象为：" + joinPoint.getTarget());
    }

    @Around("point()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        System.out.println("Around增强：执行方法之前，模拟开始事物");
        Object[] args = proceedingJoinPoint.getArgs();
        if(args != null && args.length > 0 && args[0].getClass() == String.class) {
            args[0] = "增加的前缀" + args[0];
        }
        Object rvt = proceedingJoinPoint.proceed();
        System.out.println("Around增强：执行方法之后，模拟结束事物");
        if(rvt != null && rvt instanceof Integer) {
            rvt = (Integer)rvt * (Integer)rvt;
        }
        return rvt;
    }

/**
 * 与After的区别在于AfterReturning只有在方法执行成功的之后才会被织入，如果After和
 * AfterReturning同时存在于一个文件中，谁写在前面谁先运行*/
@AfterReturning(pointcut = "execution(* com.aop.controller.CommonController.*(..))",returning = "rvt")
    public void afterReturning(JoinPoint joinPoint,Object rvt) {
    System.out.println("AfterReturning增强：获取目标方法的返回值：" + rvt);
    System.out.println("AfterReturning增强：模拟日志功能");
    System.out.println("AfterReturning增强：获织入增强的目标方法为：" + joinPoint.getSignature().getName());
    System.out.println("AfterReturning增强：目标方法的参数为：" + Arrays.toString(joinPoint.getArgs()));
    System.out.println("AfterReturning增强：被织入增强处理的目标对象为：" + joinPoint.getTarget());
    }
}
