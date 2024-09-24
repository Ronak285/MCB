package com.mcb.ecommerce.order_management.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI configuration for customizing the API documentation.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Order Management API")
                        .version("1.0")
                        .description("API documentation for the Order Management System"))
                .addSecurityItem(new SecurityRequirement().addList("basicScheme"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("basicScheme",
                                new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("basic")));
    //    return new OpenAPI()
    //            .info(new Info()
    //                    .title("Order Management API")
    //                    .version("1.0")
    //                    .description("API for managing orders in an e-commerce application"));
    }
}