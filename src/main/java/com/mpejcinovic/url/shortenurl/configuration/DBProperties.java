package com.mpejcinovic.url.shortenurl.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * This class comprises all relevant properties
 * for establishing a connection to the database.
 *
 * @author Matea Pejcinovic
 * @version 0.00.003
 * @since 14.11.2020.
 */
@Component
@ConfigurationProperties(prefix = "spring")
@Getter
public class DBProperties {

    @Value("${spring.datasource.url}")
    private String jdbcURL;

    @Value("${spring.datasource.username}")
    private String jdbcUsername;

    @Value("${spring.datasource.password}")
    private String jdbcPassword;
}
