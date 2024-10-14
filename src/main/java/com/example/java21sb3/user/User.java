package com.example.java21sb3.user;

import jakarta.persistence.*;

@Entity
@Table(name = "user")
public class User {

  @Id
  Long id;

  @Column(name = "name")
  String name;

  public User(String name) {
    this.id = 0L;
    this.name = name;
  }

  public User(Long id, String name) {
    this.id = id;
    this.name = name;
  }
  
  public User() {
    
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }
}
