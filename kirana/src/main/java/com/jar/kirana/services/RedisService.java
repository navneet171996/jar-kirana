package com.jar.kirana.services;


public interface RedisService {
    public <T> T get(String key, Class<T> entityClass);
    public void set(String key, Object o, Long expiry);
}
