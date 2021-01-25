package com.epam.esm.repository.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

// FIXME
@Configuration
@EnableTransactionManagement
@PropertySource("classpath:application-test.properties")
@ComponentScan(basePackages = {"com.epam.esm.repository.impl", "com.epam.esm.repository.mapper"})
public class TestRepositoryConfiguration {

    @Value("${spring.datasource.script_encoding}")
    private String scriptEncoding;
    @Value("${spring.datasource.init_script_path}")
    private String initScriptPath;
    @Value("${spring.datasource.fill_script_path}")
    private String fillScriptPath;

    @Bean
    public DataSource testDataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .setName("testDB;MODE=MySQL")
                .setScriptEncoding(scriptEncoding)
                .addScript(initScriptPath)
                .addScript(fillScriptPath)
                .build();
    }

    @Bean
    public NamedParameterJdbcTemplate jdbcTemplate() {
        return new NamedParameterJdbcTemplate(testDataSource());
    }

    @Bean
    public DataSourceTransactionManager transactionManager() {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(testDataSource());
        return transactionManager;
    }
}
