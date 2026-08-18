package com.margaretnjoki.mini_blog_api.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI apiInfo() {
        final String schemeName = "bearerAuth";

        return new OpenAPI()
                .info(new Info()
                        .title("Mini Blog API")
                        .version("v1")
                        .description(
                                "A RESTful API for creating and managing blog posts, " +
                                        "comments, tags, and user authentication. " +
                                        "The API supports JWT-based authentication, " +
                                        "PostgreSQL persistence, and Redis caching for improved performance."
                        ))
                .addSecurityItem(
                        new SecurityRequirement().addList(schemeName)
                )
                .components(
                        new Components()
                                .addSecuritySchemes(
                                        schemeName,
                                        new SecurityScheme()
                                                .name(schemeName)
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                )
                );
    }
}