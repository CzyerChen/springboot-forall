package com.notes.service;

import com.mongodb.BasicDBObject;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.hibernate.validator.internal.util.logging.LoggerFactory;
import org.jboss.logging.Logger;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;


/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -03 - 28 17:50
 */
@Aspect
@Component
@Order(1)
public class LogAscept {
    private Logger log = Logger.getLogger(LogAscept.class);
    ThreadLocal<Long> startTime = new ThreadLocal<Long>();


    @Pointcut("execution(public * com.notes.web..*.*(..))")
    public  void webLog(){

    }


    @Before("webLog()")
    public void doBeforeAction(JoinPoint joinPoint){
        startTime.set(System.currentTimeMillis());
        ServletRequestAttributes requestAttributes =(ServletRequestAttributes)RequestContextHolder.getRequestAttributes();

        HttpServletRequest request = requestAttributes.getRequest();

        //记录下请求的内容，或者参数等，进行请求次数的统计等
        log.info("URL : " + request.getRequestURL().toString());
        log.info("HTTP_METHOD : " + request.getMethod());
        log.info("IP : " + request.getRemoteAddr());
        log.info("CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        log.info("ARGS : " + Arrays.toString(joinPoint.getArgs()));

        BasicDBObject logInfo = getBasicDBObject(request, joinPoint);
        log.info(logInfo);

    }

    private BasicDBObject getBasicDBObject(HttpServletRequest request, JoinPoint joinPoint) {
// 基本信息
        BasicDBObject r = new BasicDBObject();
        r.append("requestURL", request.getRequestURL().toString());
        r.append("requestURI", request.getRequestURI());
        r.append("queryString", request.getQueryString());
        r.append("remoteAddr", request.getRemoteAddr());
        r.append("remoteHost", request.getRemoteHost());
        r.append("remotePort", request.getRemotePort());
        r.append("localAddr", request.getLocalAddr());
        r.append("localName", request.getLocalName());
        r.append("method", request.getMethod());
        r.append("headers", getHeadersInfo(request));
        r.append("parameters", request.getParameterMap());
        r.append("classMethod", joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
                r.append("args", Arrays.toString(joinPoint.getArgs()));
        return r;
    }


    private Map<String, String> getHeadersInfo(HttpServletRequest request) {
        Map<String, String> map = new HashMap<String, String>();
        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }
        return map;
    }

    @AfterReturning(returning = "obj",pointcut = "webLog()")
    public void doAfterAction(Object obj){
        //记录请求返回的对象，比如对controller请求的返回JSON值
       log.info("after returning :"+obj);
       //记录接口请求时间
       long time = System.currentTimeMillis() - startTime.get();
       log.info("request time :"+time);
    }
}
