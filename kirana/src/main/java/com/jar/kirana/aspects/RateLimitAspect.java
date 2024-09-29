package com.jar.kirana.aspects;

import com.jar.kirana.exceptions.RateLimitException;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Aspect
@Component
public class RateLimitAspect {

    public static final String ERROR_MESSAGE = "Too many requests";
    private final ConcurrentHashMap<String, List<Long>> requestCounts = new ConcurrentHashMap<>();

    @Value("${spring.app.rateLimit}")
    private int rateLimit;

    @Value("${spring.app.rateDuration}")
    private int rateDuration;

    @Before("@annotation(com.jar.kirana.config.rateLimiter.WithRateLimitProtection)")
    public void rateLimit(){
        final ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        final String key = requestAttributes.getRequest().getRemoteAddr();
        final long currentTime = System.currentTimeMillis();

        requestCounts.putIfAbsent(key, new ArrayList<>());
        requestCounts.get(key).add(currentTime);
        cleanRequestCounts(currentTime);

        if(requestCounts.get(key).size() > rateLimit){
            throw new RateLimitException(ERROR_MESSAGE);
        }
    }

    private void cleanRequestCounts(long currentTime){
        requestCounts.values().forEach(x -> {
            x.removeIf(t -> timeIsOld(currentTime, t));
        });
    }

    private boolean timeIsOld(long currentTime, long timeToCheck){
        return currentTime - timeToCheck > rateDuration;
    }
}
