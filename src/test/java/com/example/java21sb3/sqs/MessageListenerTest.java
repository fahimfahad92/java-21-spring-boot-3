package com.example.java21sb3.sqs;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.SQS;

import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;

@SpringBootTest
@Testcontainers
class MessageListenerTest {

  private static final String SQS_QUEUE = "Test_Queue.fifo";

  @Container
  static LocalStackContainer localStack =
      new LocalStackContainer(DockerImageName.parse("localstack/localstack:3.0"))
          .withServices(LocalStackContainer.Service.SQS)
          .withReuse(true);

  private static SqsClient sqsClient;
  private static String queueUrl;
  private static GetQueueAttributesResponse attributes;

  static {
    localStack.start();
  }

  @AfterAll
  public static void cleanup() {
    localStack.stop();
  }

  @DynamicPropertySource
  static void overrideProperties(DynamicPropertyRegistry registry) {
    registry.add("aws.accessKeyId", localStack::getAccessKey);
    registry.add("aws.secretAccessKey", localStack::getSecretKey);
    registry.add("aws.endpointOverride.sqs", () -> localStack.getEndpointOverride(SQS).toString());
  }

  @BeforeAll
  static void beforeAll() {
    createSqsClient();
    createSqsQueues();
  }

  static void createSqsClient() {
    sqsClient =
        SqsClient.builder()
            .region(Region.US_EAST_1)
            .endpointOverride(localStack.getEndpointOverride(SQS))
            .credentialsProvider(
                StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(
                        localStack.getAccessKey(), localStack.getSecretKey())))
            .build();
  }

  static void createSqsQueues() {
    CreateQueueResponse response =
        sqsClient.createQueue(
            CreateQueueRequest.builder()
                .queueName(SQS_QUEUE)
                .attributes(
                    Map.of(
                        QueueAttributeName.FIFO_QUEUE,
                        "true",
                        QueueAttributeName.CONTENT_BASED_DEDUPLICATION,
                        "true"))
                .build());

    queueUrl = response.queueUrl();
  }

  @Test
  void shouldPublishMessageToSqs() {
    Message message = new Message(UUID.randomUUID(), "Hello World");

    var request =
        SendMessageRequest.builder()
            .messageBody(message.toString())
            .messageGroupId(UUID.randomUUID().toString())
            .messageDeduplicationId(UUID.randomUUID().toString())
            .queueUrl(queueUrl)
            .build();

    sqsClient.sendMessage(request);

    loadAttributes();

    await()
        .atMost(3, SECONDS)
        .untilAsserted(
            () -> {
              assertThat(numberOfMessagesInQueue()).isEqualTo(1);
              assertThat(numberOfMessagesNotVisibleInQueue()).isEqualTo(0);
            });
  }

  private void loadAttributes() {
    attributes =
        sqsClient.getQueueAttributes(
            GetQueueAttributesRequest.builder()
                .queueUrl(queueUrl)
                .attributeNamesWithStrings("All")
                .build());
  }

  private Integer numberOfMessagesInQueue() {
    return Integer.parseInt(
        attributes.attributes().get(QueueAttributeName.APPROXIMATE_NUMBER_OF_MESSAGES));
  }

  private Integer numberOfMessagesNotVisibleInQueue() {
    return Integer.parseInt(
        attributes.attributes().get(QueueAttributeName.APPROXIMATE_NUMBER_OF_MESSAGES_NOT_VISIBLE));
  }
}
