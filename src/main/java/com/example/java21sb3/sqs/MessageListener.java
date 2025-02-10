// package com.example.java21sb3.sqs;
//
// import com.example.java21sb3.db.UserRepository;
// import com.example.java21sb3.user.User;
// import io.awspring.cloud.sqs.annotation.SqsListener;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Component;
//
// @Component
// public class MessageListener {
//
//  private static final Logger logger = LoggerFactory.getLogger(MessageListener.class);
//
//  UserRepository userRepository;
//
//  @Autowired
//  public MessageListener(UserRepository userRepository) {
//    this.userRepository = userRepository;
//  }
//
//  @SqsListener("${events.queues.testQueue}")
//  public void testConsumer(String username) {
//
//    logger.info("Received message: {}", username);
//    userRepository.save(new User(0L, username));
//  }
// }
