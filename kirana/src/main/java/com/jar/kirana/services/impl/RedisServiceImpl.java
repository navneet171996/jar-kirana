package com.jar.kirana.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jar.kirana.dto.CurrencyConverterResponseDTO;
import com.jar.kirana.services.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisServiceImpl implements RedisService {

    private static final Logger logger = LoggerFactory.getLogger(RedisServiceImpl.class);

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisServiceImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public <T> T get(String key, Class<T> entityClass) {
        try{
            Object o = redisTemplate.opsForValue().get(key);
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(o.toString(), entityClass);
        }catch (RedisConnectionFailureException e){
            logger.error("Redis is not connected with reason :{}", e.getMessage());
            return null;
        }catch (JsonProcessingException e){
            logger.info("Value of Currency API not available with reason :{}", e.getMessage());
            return null;
        }catch (Exception e){
            logger.error("Exception occurred while getting from Redis with reason:{}", e.getMessage());
            return null;
        }
    }

    @Override
    public void set(String key, Object o, Long expiry) {
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            String value = objectMapper.writeValueAsString(o);
            redisTemplate.opsForValue().set(key, value, expiry, TimeUnit.SECONDS);
        } catch (Exception e) {
            logger.error("Exception occurred while setting to Redis with reason :{}", e.getMessage());
        }
    }
}
