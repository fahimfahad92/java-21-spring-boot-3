package com.example.java21sb3.redis;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {

  @Value("${spring.data.redis.port}")
  String redisPort;

  @Value("${spring.data.redis.host}")
  String redisHost;

  @Bean
  public RedissonClient redissonClient() {
    String redisUrl = "redis://" + redisHost + ":" + redisPort;
    Config config = new Config();
    config.useSingleServer().setAddress(redisUrl);
    return Redisson.create(config);
  }
}
