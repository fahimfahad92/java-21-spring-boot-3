package com.example.java21sb3.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.micrometer.common.util.StringUtils;
import java.util.Properties;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSourceConfig {

  @Value("${spring.datasource.hikari.jdbc-url}")
  private String dataSourceJdbcUrl;

  @Value("${database.username}")
  private String userName;

  @Value("${database.password}")
  private String password;

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

  /**
   * username and password should come from external secret manager like parameter store. if we use
   * Hikari to configure database and test container to test it, we need to allow a way to inject
   * username, password and test database url. For this reason I added a code to load username and
   * password from properties if setup from test container.
   *
   * @return DataSource with all the necessary configuration
   */
  @Bean
  public DataSource primaryDataSource() {
    String userName = StringUtils.isNotBlank(this.userName) ? this.userName : "fahim";
    String password = StringUtils.isNotBlank(this.password) ? this.password : "test";

    Properties dsProps = new Properties();
    dsProps.put("url", dataSourceJdbcUrl);
    dsProps.put("user", userName);
    dsProps.put("password", password);
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
