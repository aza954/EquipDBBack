package ru.SevertsovDmitry.EquipmentMaintenance.config;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut("execution(public * ru.SevertsovDmitry.EquipmentMaintenance.Service.*.*(..))")
    public void serviceMethods() {}

    @Before("serviceMethods()")
    public void logBefore(JoinPoint joinPoint) {
        logger.info("Вызов метода: {} с параметрами: {}",
                joinPoint.getSignature().toShortString(), joinPoint.getArgs());
    }

    @AfterReturning(pointcut = "serviceMethods()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        logger.info("Метод {} вернул: {}",
                joinPoint.getSignature().toShortString(), result);
    }

    @AfterThrowing(pointcut = "serviceMethods()", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
        logger.error("В методе {} возникло исключение: {}",
                joinPoint.getSignature().toShortString(), exception.getMessage());
    }

    @Around("serviceMethods()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            long duration = System.currentTimeMillis() - startTime;
            logger.info("Метод {} выполнен за {} мс",
                    joinPoint.getSignature().toShortString(), duration);
            return result;
        } catch (Throwable ex) {
            logger.error("Метод {} выбросил исключение: {}",
                    joinPoint.getSignature().toShortString(), ex.getMessage());
            throw ex;
        }
    }
}
