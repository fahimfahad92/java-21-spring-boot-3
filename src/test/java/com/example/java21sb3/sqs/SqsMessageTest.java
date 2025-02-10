//package com.example.java21sb3.sqs;
//
//import static java.util.concurrent.TimeUnit.SECONDS;
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.awaitility.Awaitility.await;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//import com.example.java21sb3.ContainerInitializer;
//import com.example.java21sb3.db.UserRepository;
//import io.awspring.cloud.sqs.operations.SqsTemplate;
//import java.time.Duration;
//import java.util.UUID;
//import org.junit.jupiter.api.Disabled;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
//
//@Disabled
//@EnableConfigurationProperties(EventQueuesProperties.class)
//public class SqsMessageTest extends ContainerInitializer {
//
//  @Autowired UserRepository userRepository;
//  @Autowired private SqsTemplate sqsTemplate;
//  @Autowired private EventQueuesProperties eventQueuesProperties;
//
//  @Test
//  void isLocalstackRunning() {
//    assertTrue(localStack.isRunning());
//  }
//
//  @Test
//  void shouldConsumeThePayloadAndCreateDBEntry() {
//    var userName = "Fahim" + UUID.randomUUID();
//    sqsTemplate.send(
//        to ->
//            to.queue(eventQueuesProperties.getTestQueue())
//                .payload(userName)
//                .messageDeduplicationId(UUID.randomUUID().toString())
//                .messageGroupId(UUID.randomUUID().toString()));
//    await().atMost(Duration.ofSeconds(3)).until(() -> userRepository.findByName(userName) != null);
//  }
//
//  @Test
//  void shouldPublishMessageToSqs() {
//    Message message = new Message(UUID.randomUUID(), "Hello World");
//
//    var request =
//        SendMessageRequest.builder()
//            .messageBody(message.toString())
//            .messageGroupId(UUID.randomUUID().toString())
//            .messageDeduplicationId(UUID.randomUUID().toString())
//            .queueUrl(queueUrl)
//            .build();
//
//    sqsClient.sendMessage(request);
//
//    loadAttributes();
//
//    await()
//        .atMost(3, SECONDS)
//        .untilAsserted(
//            () -> {
//              assertThat(numberOfMessagesInQueue()).isEqualTo(1);
//              assertThat(numberOfMessagesNotVisibleInQueue()).isEqualTo(0);
//            });
//  }
//}
