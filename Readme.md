This is a template project for Java 21 and Spring Boot 3.

In this project, I am using Docker compose to prepare the local development environment.

1. Start local Docker
2. Go to the 'docker-compose' folder and run 'docker compose up'
3. Docker compose file will run MySQL and Redis instances.
4. Run the application.

**Testing:**
I added tests for MySQL, Redis and SQS using test container. We can execute each test separately.

**Note:** I added SQS test using Localstack. During the test, SQS test will work even if there is no SQS in AWS.
But we will need to have SQS in AWS if we want to run the application.

**Dockerfile:**
I added a Dockerfile to generate an optimized image for my application.
I used multiple stages to generate a smaller image for my application.
Note: This Docker file is not usable with the Docker compose environment. It can be used for production or UAT deployment.