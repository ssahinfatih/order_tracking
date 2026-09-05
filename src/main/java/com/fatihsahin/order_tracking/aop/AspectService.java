package com.fatihsahin.order_tracking.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Service;

@Service
@Aspect
@Slf4j
public class AspectService {

    @Before("execution(* com.fatihsahin.order_tracking.service.*.*(..))")
    public void beforeMethod() {
        log.info("Before method execution");
    }
}
