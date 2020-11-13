package com.mpejcinovic.url.shortenurl.configuration;

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
 * @version 0.00.001
 * @since 13.11.2020.
 */
@Component
@ConfigurationProperties(prefix = "com/mpejcinovic/url/shortenurl")
public class Config {

    @Value("${shortenurl.language}")
    private String language;

}