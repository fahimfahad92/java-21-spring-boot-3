package com.example.java21sb3;

import com.redis.testcontainers.RedisContainer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.localstack.LocalStackContainer;import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;import software.amazon.awssdk.regions.Region;import software.amazon.awssdk.services.sqs.SqsClient;import software.amazon.awssdk.services.sqs.model.*;import java.util.Map;import static org.testcontainers.containers.localstack.LocalStackContainer.Service.SQS;

@Testcontainers
@SpringBootTest
public abstract class ContainerInitializer {

  private static final String SQS_QUEUE = "Test_Queue.fifo";
  private static final String SQS_QUEUE_FOR_PRODUCER = "Test_Queue_2.fifo";
  private static final String LOCAL_STACK_VERSION = "localstack/localstack:3.0";
  protected static SqsClient sqsClient;
  protected static String queueUrl;

  @Container
  static LocalStackContainer localStack =
          new LocalStackContainer(DockerImageName.parse(LOCAL_STACK_VERSION))
                  .withServices(SQS)
                  .withReuse(true);

  private static GetQueueAttributesResponse attributes;

  @Container
  public static RedisContainer redis =
      new RedisContainer(DockerImageName.parse("redis:7.4")).withExposedPorts(6379);

  @Container
  static MySQLContainer mySQLContainer =
      new MySQLContainer<>(DockerImageName.parse("mysql:5.7")).withExposedPorts(3306);

  static {
    redis.start();
    mySQLContainer.start();
    localStack.start();
  }

  @DynamicPropertySource
  static void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
    registry.add("spring.datasource.username", mySQLContainer::getUsername);
    registry.add("spring.datasource.password", mySQLContainer::getPassword);

    registry.add("spring.data.redis.host", redis::getHost);
    registry.add("spring.data.redis.port", () -> redis.getMappedPort(6379).toString());

    registry.add("spring.cloud.aws.region.static", () -> localStack.getRegion());
    registry.add("spring.cloud.aws.credentials.access-key", () -> localStack.getAccessKey());
    registry.add("spring.cloud.aws.credentials.secret-key", () -> localStack.getSecretKey());
    registry.add(
            "spring.cloud.aws.sqs.endpoint", () -> localStack.getEndpointOverride(SQS).toString());
  }

  @BeforeAll
  static void beforeAll() {
    createSqsClient();
    createSqsQueues();
  }

  @AfterAll
  public static void afterAll() {
    redis.stop();
    mySQLContainer.stop();
    localStack.stop();
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

    CreateQueueResponse response =
            sqsClient.createQueue(
                    CreateQueueRequest.builder()
                            .queueName(SQS_QUEUE_FOR_PRODUCER)
                            .attributes(
                                    Map.of(
                                            QueueAttributeName.FIFO_QUEUE,
                                            "true",
                                            QueueAttributeName.CONTENT_BASED_DEDUPLICATION,
                                            "true"))
                            .build());

    queueUrl = response.queueUrl();
  }

  protected void loadAttributes() {
    attributes =
            sqsClient.getQueueAttributes(
                    GetQueueAttributesRequest.builder()
                            .queueUrl(queueUrl)
                            .attributeNamesWithStrings("All")
                            .build());
  }

  protected Integer numberOfMessagesInQueue() {
    return Integer.parseInt(
            attributes.attributes().get(QueueAttributeName.APPROXIMATE_NUMBER_OF_MESSAGES));
  }

  protected Integer numberOfMessagesNotVisibleInQueue() {
    return Integer.parseInt(
            attributes.attributes().get(QueueAttributeName.APPROXIMATE_NUMBER_OF_MESSAGES_NOT_VISIBLE));
  }
}
