package com.example.java21sb3;

import com.redis.testcontainers.RedisContainer;
import org.junit.jupiter.api.AfterAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
@SpringBootTest
public abstract class ContainerInitializer {

  @Container
  public static RedisContainer redis =
      new RedisContainer(DockerImageName.parse("redis:7.4")).withExposedPorts(6379);

  @Container
  static MySQLContainer mySQLContainer =
      new MySQLContainer<>(DockerImageName.parse("mysql:5.7")).withExposedPorts(3306);

  static {
    redis.start();
    mySQLContainer.start();
  }

  @DynamicPropertySource
  static void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
    registry.add("spring.datasource.username", mySQLContainer::getUsername);
    registry.add("spring.datasource.password", mySQLContainer::getPassword);

    registry.add("spring.data.redis.host", redis::getHost);
    registry.add("spring.data.redis.port", () -> redis.getMappedPort(6379).toString());
  }

  @AfterAll
  public static void afterAll() {
    redis.stop();
    mySQLContainer.stop();
  }
}
