package com.example.java21sb3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Java21Sb3Application {

  public static void main(String[] args) {
    SpringApplication.run(Java21Sb3Application.class, args);
  }
}

//@Component
//class StartupApplicationListenerExample implements ApplicationListener<ContextRefreshedEvent> {
//
//  private static final Logger logger =
//      LoggerFactory.getLogger(StartupApplicationListenerExample.class);
//  private final RedisService redisService;
//  private final UserRepository userRepository;
//
//  @Autowired
//  public StartupApplicationListenerExample(
//      RedisService redisService, UserRepository userRepository) {
//    this.redisService = redisService;
//    this.userRepository = userRepository;
//  }
//
//  @Override
//  public void onApplicationEvent(ContextRefreshedEvent event) {
//    User user = new User("Fahim" + new Random().nextInt());
//    logger.info("Saving user {}", user.getName());
//    userRepository.save(user);
//
//    List<User> users = userRepository.findAll();
//
//    users.forEach(u -> logger.info(u.getId() + " " + u.getName()));
//
//    // redis
//    redisService.setData("user", user);
//    User userFromRedis = redisService.getData("user");
//    logger.info("Getting user from redis " + userFromRedis.toString());
//
//    // redisson
//    redisService.setDataRedisson("user2", user);
//    User userFromRedisson = redisService.getDataRedisson("user2");
//    logger.info("Getting user from redis " + userFromRedisson.toString());
//  }
//}
