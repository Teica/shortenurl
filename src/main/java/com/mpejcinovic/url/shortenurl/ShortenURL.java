package com.mpejcinovic.url.shortenurl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Initial class for application.
 *
 * @author Matea Pejcinovic
 * @version 0.00.001
 * @since 13.11.2020.
 */
@SpringBootApplication
public class ShortenURL extends SpringBootServletInitializer {
    /**
     * Configure the application.
     *
     * @param application a builder for the application context
     * @return the application builder
     * @see SpringApplicationBuilder
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ShortenURL.class);
    }

    /**
     * /**
     * Runs a Spring application {@link ShortenURL}
     *
     * @param args an absolute URL giving the base location of the image
     * @return nothing
     */
    public static void main(String[] args) {
        SpringApplication.run(ShortenURL.class, args);
    }
}