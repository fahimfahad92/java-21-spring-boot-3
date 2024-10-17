package com.example.java21sb3;

import com.example.java21sb3.db.UserRepository;import com.example.java21sb3.redis.RedisService;import com.example.java21sb3.user.User;import org.slf4j.Logger;import org.slf4j.LoggerFactory;import org.springframework.beans.factory.annotation.Autowired;import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationListener;import org.springframework.context.event.ContextRefreshedEvent;import org.springframework.stereotype.Component;import reactor.util.annotation.NonNull;
import java.util.List;import java.util.Random;

@SpringBootApplication
public class Java21Sb3Application {

  public static void main(String[] args) {
    SpringApplication.run(Java21Sb3Application.class, args);
  }
}

@Component
class StartupApplicationListenerExample implements ApplicationListener<ContextRefreshedEvent> {

  private static final Logger logger =
      LoggerFactory.getLogger(StartupApplicationListenerExample.class);
  private final RedisService redisService;
  private final UserRepository userRepository;

  @Autowired
  public StartupApplicationListenerExample(
      RedisService redisService, UserRepository userRepository) {
    this.redisService = redisService;
    this.userRepository = userRepository;
  }

  @Override
  public void onApplicationEvent(@NonNull ContextRefreshedEvent event) {
    User user = new User(0L, "Fahim" + new Random().nextInt());
    logger.info("Saving user {}", user.getName());
    userRepository.save(user);

    List<User> users = userRepository.findAll();

    users.forEach(u -> logger.info(u.getId() + " " + u.getName()));

    // redis
    redisService.setData("user", user);
    User userFromRedis = redisService.getData("user");
    logger.info("Getting user from redis " + userFromRedis.toString());

    // redisson
    redisService.setDataRedisson("user2", user);
    User userFromRedisson = redisService.getDataRedisson("user2");
    logger.info("Getting user from redis " + userFromRedisson.toString());
  }
}
