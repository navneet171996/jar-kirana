package com.jar.kirana.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jar.kirana.dto.CurrencyConverterResponseDTO;
import com.jar.kirana.services.RedisService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisServiceImpl implements RedisService {

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
        }catch (Exception e){
            e.printStackTrace();
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
            e.printStackTrace();
        }
    }
}
