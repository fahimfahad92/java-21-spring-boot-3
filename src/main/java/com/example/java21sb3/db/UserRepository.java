package com.example.java21sb3.db;

import com.example.java21sb3.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
  User findByName(String name);
}
