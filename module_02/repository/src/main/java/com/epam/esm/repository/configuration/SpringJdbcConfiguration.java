package com.epam.esm.repository.configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@ComponentScan("com.epam.esm.repository")
@PropertySource("classpath:application.properties")
public class SpringJdbcConfiguration {

    @Value("${spring.datasource.driverClassName}")
    private String dbDriverClassName;
    @Value("${spring.datasource.url}")
    private String dbUrl;
    @Value("${spring.datasource.login}")
    private String dbLogin;
    @Value("${spring.datasource.password}")
    private String dbPassword;
    @Value("${spring.datasource.maxPoolSize}")
    private Integer maxPoolSize;
    @Value("${spring.datasource.source_property.cache}")
    private String cacheProperty;
    @Value("${spring.datasource.source_property.cache_value}")
    private String cachePropertyValue;
    @Value("${spring.datasource.source_property.cache_size}")
    private String cacheSizeProperty;
    @Value("${spring.datasource.source_property.cache_size_value}")
    private String cacheSizeValue;
    @Value("${spring.datasource.source_property.cache_limit}")
    private String cacheLimitProperty;
    @Value("${spring.datasource.source_property.cache_limit_value}")
    private String cacheLimitValue;

    @Bean
    public DataSource dataSource() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(dbDriverClassName);
        hikariConfig.setJdbcUrl(dbUrl);
        hikariConfig.setUsername(dbLogin);
        hikariConfig.setPassword(dbPassword);

        hikariConfig.setMaximumPoolSize(maxPoolSize);
        hikariConfig.addDataSourceProperty(cacheProperty, cachePropertyValue);
        hikariConfig.addDataSourceProperty(cacheSizeProperty, cacheSizeValue);
        hikariConfig.addDataSourceProperty(cacheLimitProperty, cacheLimitValue);

        return new HikariDataSource(hikariConfig);
    }

    @Bean
    public NamedParameterJdbcTemplate jdbcTemplate() {
        return new NamedParameterJdbcTemplate(dataSource());
    }

    @Bean
    public DataSourceTransactionManager transactionManager() {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource());
        return transactionManager;
    }
}
