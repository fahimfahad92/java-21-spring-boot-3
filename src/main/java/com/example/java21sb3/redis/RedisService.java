package com.example.java21sb3.redis;

import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

  private final RedisTemplate redisTemplate;
  private final RedissonClient redissonClient;

  @Autowired
  public RedisService(RedisTemplate redisTemplate, RedissonClient redissonClient) {
    this.redisTemplate = redisTemplate;
    this.redissonClient = redissonClient;
  }

  //  @Autowired
  //  public RedisService(RedisTemplate redisTemplate) {
  //    this.redisTemplate = redisTemplate;
  //  }

  public <T> boolean setDataRedisson(String key, T data) {
    redissonClient.getBucket(key).set(data);
    return true;
  }

  public <T> T getDataRedisson(String key) {
    return (T) redissonClient.getBucket(key).get();
  }

  public <T> void setData(String key, T data) {
    redisTemplate.opsForValue().set(key, data);
  }

  public <T> T getData(String key) {
    return (T) redisTemplate.opsForValue().get(key);
  }
}
