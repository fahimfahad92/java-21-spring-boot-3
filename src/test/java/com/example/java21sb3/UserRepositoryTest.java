package com.example.java21sb3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.java21sb3.db.UserRepository;
import com.example.java21sb3.user.User;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

@Disabled
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserRepositoryTest extends ContainerInitializer {

  @Autowired UserRepository userRepository;

  @Test
  @Order(1)
  void mysqlTestContainerRunning() {
    assertTrue(mySQLContainer.isRunning());
  }

  @Test
  @Order(2)
  void shouldCreateUser() {
    List<User> userList =
        List.of(
            new User(0L, "Fahim" + new Random().nextInt()),
            new User(0L, "Fahim" + new Random().nextInt()));
    userRepository.saveAll(userList);
  }

  @Test
  @Order(3)
  void shouldGetAllUser() {
    assertEquals(2, userRepository.findAll().size());
  }
}
