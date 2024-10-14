package com.example.java21sb3.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.util.Properties;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSourceConfig {

  @Value("${spring.datasource.hikari.jdbc-url}")
  private String dataSourceJdbcUrl;

  @Value("${spring.datasource.poolName}")
  private String poolName;

  @Value("${spring.datasource.connectionTimeout}")
  private int connectionTimeout;

  @Value("${spring.datasource.maximumPoolSize}")
  private int maximumPoolSize;

  @Value("${spring.datasource.minimumIdle}")
  private int minimumIdle;

  @Value("${spring.datasource.maxLifetime}")
  private int idleTimeout;

  @Value("${spring.datasource.idleTimeout}")
  private int maxLifetime;

  @Bean
  public DataSource primaryDataSource() {
    Properties dsProps = new Properties();
    dsProps.put("url", dataSourceJdbcUrl);
    dsProps.put("user", "fahim");
    dsProps.put("password", "test");
    dsProps.put("prepStmtCacheSize", 250);
    dsProps.put("prepStmtCacheSqlLimit", 2048);
    dsProps.put("cachePrepStmts", Boolean.TRUE);
    dsProps.put("useServerPrepStmts", Boolean.TRUE);

    Properties configProps = new Properties();
    configProps.put("jdbcUrl", dataSourceJdbcUrl);
    configProps.put("poolName", poolName);
    configProps.put("maximumPoolSize", maximumPoolSize);
    configProps.put("minimumIdle", minimumIdle);
    configProps.put("connectionTimeout", connectionTimeout);
    configProps.put("idleTimeout", idleTimeout);
    configProps.put("maxLifetime", maxLifetime);
    configProps.put("dataSourceProperties", dsProps);

    HikariConfig hc = new HikariConfig(configProps);
    return new HikariDataSource(hc);
  }
}
