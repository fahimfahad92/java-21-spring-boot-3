package com.example.java21sb3.ratelimit;

import static java.time.Duration.ofMinutes;

import io.github.bucket4j.Bucket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MultiRateLimiterService {

  @Value("${rateLimitPerToken}")
  private int rateLimitPerToken;

  private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

  /** This method will apply rate limiter based on token. Every token will have their own limit. */
  public Bucket resolveBucket(String jwtToken) {
    return cache.computeIfAbsent(jwtToken, this::newBucket);
  }

  private Bucket newBucket(String apiKey) {
    return Bucket.builder()
        .addLimit(
            limit ->
                limit.capacity(rateLimitPerToken).refillGreedy(rateLimitPerToken, ofMinutes(1)))
        .build();
  }
}
