package com.mpejcinovic.url.shortenurl.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Wildfly configuration class that sets
 * variables using configuration file's
 * values and than sets the variables for
 * main configuration class in core module.
 *
 * @author Matea Pejcinovic
 * @version 0.00.002
 * @since 13.11.2020.
 */
@Component
@ConfigurationProperties(prefix = "shortenurl")
public class Config {

    @Getter
    @Value("${shortenurl.language}")
    private String language;

    @Getter
    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Getter
    @Value("${spring.datasource.username}")
    private String dbUsername;

    @Getter
    @Value("${spring.datasource.password}")
    private String dbPassword;

}