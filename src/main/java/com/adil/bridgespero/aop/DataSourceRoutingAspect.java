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

        Transactional effectiveTx =
                method.getAnnotation(Transactional.class) != null
                        ? method.getAnnotation(Transactional.class)
                        : clazz.getAnnotation(Transactional.class);

        boolean readOnly = effectiveTx != null && effectiveTx.readOnly();
        String mode = readOnly ? "READ" : "WRITE";

        DataSourceContextHolder.setMode(mode);
    }

    @After("execution(* *(..)) && (@annotation(tx) || @within(tx))")
    public void clearContext(JoinPoint jp, Transactional tx) {
        DataSourceContextHolder.clear();
    }
}
