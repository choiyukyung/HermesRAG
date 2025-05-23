package com.kayla.HermesRAG.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    // Controller 시작!
    @Before("execution(* com.kayla.HermesRAG.controller..*(..))")
    public void logBeforeController(JoinPoint joinPoint) {
        logger.info("Start Controller: {}", joinPoint.getSignature().toShortString());
    }

    // Service 실행 시간 측정
    @Around("execution(* com.kayla.HermesRAG.service..*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        Object proceed = joinPoint.proceed();

        long executionTime = System.currentTimeMillis() - start;
        logger.info("{} execution time: {} ms", joinPoint.getSignature(), executionTime);

        return proceed;
    }

    // 예외
    @AfterThrowing(pointcut = "execution(* com.kayla.HermesRAG..*(..))", throwing = "ex")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable ex) {
        logger.error("error: {} - {}", joinPoint.getSignature(), ex.getMessage());
    }
}

