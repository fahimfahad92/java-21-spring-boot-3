package com.example.java21sb3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.java21sb3.redis.RedisService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Disabled
public class RedisServiceTest extends ContainerInitializer {

  @Autowired RedisService redisService;

  @Test
  void isRedisRunning() {
    assertTrue(redis.isRunning());
  }

  @Test
  void shouldPerformRedisOperations() {
    redisService.setData("key", "value");

    assertEquals("value", redisService.getData("key"));
  }

  @Test
  void shouldPerformRedissonOperations() {
    redisService.setDataRedisson("key", "value");

    assertEquals("value", redisService.getDataRedisson("key"));
  }
}
