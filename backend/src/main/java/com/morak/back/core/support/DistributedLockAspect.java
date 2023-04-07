package com.morak.back.core.support;

import java.lang.reflect.Method;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Aspect
@Order(value = 1)
public class DistributedLockAspect {

    private final RedissonClient redissonClient;

    @Pointcut("@annotation(com.morak.back.core.support.DistributedLock)")
    private void distributedLockPointCut() {

    }

    @Around("distributedLockPointCut()")
    public Object lock(final ProceedingJoinPoint joinPoint) throws Throwable {
        String code = (String) joinPoint.getArgs()[2];

        RLock rLock = redissonClient.getLock(String.format("lock:%s", code));

        DistributedLock lock = getDistributedLock(joinPoint);
        try {
            if (!rLock.tryLock(lock.waitTime(), lock.leaseTime(), lock.timeUnit())) {
                return false;
            }
            return joinPoint.proceed();
        } finally {
            rLock.unlock();
        }
    }

    private DistributedLock getDistributedLock(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        return method.getAnnotation(DistributedLock.class);
    }
}
