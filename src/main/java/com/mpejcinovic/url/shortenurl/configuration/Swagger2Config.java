package com.mpejcinovic.url.shortenurl.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static com.mpejcinovic.url.shortenurl.configuration.SwaggerSettingsConstants.*;

/**
 * Class that configures the Swagger for
 * documenting the service.
 *
 * @author Matea Pejcinovic
 * @version 0.00.002
 * @since 13.11.2020.
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors
                        .basePackage("com.mpejcinovic.url.shortenurl.controllers"))
                .paths(PathSelectors.any())
                .build().apiInfo(apiEndPointsInfo())
                //.securityContexts(Lists.newArrayList(securityContext()))
                .tags(new Tag(STATUS_CONTROLLER, STATUS_CONTROLLER_DESCRIPTION))
                .tags(new Tag(URL_CONTROLLER, URL_CONTROLLER_DESCRIPTION));
    }

    private ApiInfo apiEndPointsInfo() {
        return new ApiInfoBuilder().title(TITLE)
                .description(API_DESCRIPTION)
                .contact(new Contact(CONTACT_NAME, CONTACT_URL, CONTACT_EMAIL))
                .license(LICENCE)
                .licenseUrl(LICENCE_URL)
                .build();
    }


/*    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .build();
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope
                = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Lists.newArrayList(
                new SecurityReference("JWT", authorizationScopes));
    }*/
}
