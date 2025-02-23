package com.example.java21sb3.ratelimit;

import io.github.bucket4j.Bucket;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.stereotype.Component;

@Component
public class RateLimitingFilter implements Filter {

  private final MultiRateLimiterService rateLimiterService;

  public RateLimitingFilter(MultiRateLimiterService rateLimiterService) {
    this.rateLimiterService = rateLimiterService;
  }

  @Override
  public void doFilter(
      ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
      throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) servletRequest;
    final String authHeader = request.getHeader("Authorization");

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      filterChain.doFilter(request, servletResponse);
      return;
    }

    final String jwtToken = authHeader.substring(7);

    Bucket bucket = rateLimiterService.resolveBucket(jwtToken);
    if (bucket.tryConsume(1)) {
      filterChain.doFilter(servletRequest, servletResponse);
    } else {
      HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
      httpResponse.setStatus(429);
      httpResponse.setContentType("application/json");
      httpResponse.getWriter().write("{\"message\":\"Too many requests\"}");
    }
  }
}
