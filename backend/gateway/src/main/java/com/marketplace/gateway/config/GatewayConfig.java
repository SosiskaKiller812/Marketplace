package com.marketplace.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth_service_route", r -> r.path("/api/auth/**")
                        .uri("http://localhost:8081"))
                .route("users_service_route", r -> r.path("/api/user/**")
                        .uri("http://localhost:8082"))
                .route("products_service_route", r -> r.path("/api/product/**")
                        .uri("http://localhost:8083"))
                .build();
    }
}
