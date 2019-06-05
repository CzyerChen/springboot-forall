package com.mybatis.aspect;

import com.mybatis.config.DatabaseContextHolder;
import com.mybatis.constant.DSType;
import com.mybatis.constant.DataSourceTypeEnum;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;


@Aspect
@Component
public class DynamicDataSourceAspect {

    @Pointcut(value = "@annotation(com.mybatis.constant.DSType)")
    public void point(){

    }

    @Before(value = "point()")
    public void switchDB(JoinPoint joinPoint){
        //获取当前访问的class
        Class<?> aClass = joinPoint.getTarget().getClass();
        //获取访问的方法名
        String methodName = joinPoint.getSignature().getName();
        Class[] parameterTypes = ((MethodSignature) joinPoint.getSignature()).getParameterTypes();
        DataSourceTypeEnum db = DatabaseContextHolder.DEFAULT_DB;
        try{
            Method method = aClass.getMethod(methodName, parameterTypes);
            if(method.isAnnotationPresent(DSType.class)){
                //获取这个注解中的属性
                DSType annotation = method.getAnnotation(DSType.class);
                db = annotation.value();
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        //切换数据源
        DatabaseContextHolder.setDbs(db);

    }

    @After(value = "point()")
    public void afterDBChange(JoinPoint joinPoint){
        DatabaseContextHolder.clearDB();
    }
}
