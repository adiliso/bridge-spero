package com.adil.bridgespero.aop;

import com.adil.bridgespero.datasource.DataSourceContextHolder;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;

@Aspect
@Component
public class DataSourceRoutingAspect {

    @Before("execution(* *(..)) && (@annotation(tx) || @within(tx))")
    public void markTx(JoinPoint jp, Transactional tx) {
        Method method = ((MethodSignature) jp.getSignature()).getMethod();
        Class<?> clazz = jp.getTarget().getClass();

        // Determine effective @Transactional (method-level overrides class-level)
        Transactional effectiveTx =
                method.getAnnotation(Transactional.class) != null
                        ? method.getAnnotation(Transactional.class)
                        : clazz.getAnnotation(Transactional.class);

        boolean readOnly = effectiveTx != null && effectiveTx.readOnly();

        String mode = readOnly ? "READ" : "WRITE";

        System.err.printf(
                "[TX-ROUTING] %s → %s.%s()%n",
                mode,
                clazz.getSimpleName(),
                method.getName()
        );

        DataSourceContextHolder.setMode(mode);
    }

    @After("execution(* *(..)) && (@annotation(tx) || @within(tx))")
    public void clearContext(JoinPoint jp, Transactional tx) {
        Method method = ((MethodSignature) jp.getSignature()).getMethod();
        Class<?> clazz = jp.getTarget().getClass();

        System.err.printf(
                "[TX-CLEAR] %s.%s()%n",
                clazz.getSimpleName(),
                method.getName()
        );

        DataSourceContextHolder.clear();
    }
}
