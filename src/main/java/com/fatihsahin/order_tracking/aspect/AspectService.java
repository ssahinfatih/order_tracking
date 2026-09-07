package com.fatihsahin.order_tracking.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Service;

@Service
@Aspect
@Slf4j
public class AspectService {

    @Pointcut("execution(* com.fatihsahin.order_tracking.service.*.*(..))")
    public void serviceMethods() {
    }

    @Before("serviceMethods()")
    public void beforeMethod(JoinPoint joinPoint ) {
        log.info("Before method execution");
        log.info("Method declared in: {}", joinPoint.getSignature().getDeclaringType());//Metodun bulunduğu class
        log.info("Method name: {}", joinPoint.getSignature().getName());//Metot adı
        log.info("Method arguments: {}", joinPoint.getArgs());//Metoda gönderilen parametreler
        log.info("Method class name: {}", joinPoint.getSignature().getDeclaringType().getSimpleName());//Sadece class'ın kısa adı
    }
}
