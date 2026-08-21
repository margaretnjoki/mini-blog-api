package com.margaretnjoki.mini_blog_api.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI apiInfo() {
        final String schemeName = "bearerAuth";

        Server productionServer = new Server()
                .url("https://mini-blog-api-production-13c1.up.railway.app")
                .description("Production server (Railway)");

        Server localServer = new Server()
                .url("http://localhost:8080")
                .description("Local development server");

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
                .servers(List.of(productionServer, localServer))
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