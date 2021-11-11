package com.example.gateway.config;

import com.example.gateway.filter.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Autowired
    JwtAuthenticationFilter filter;


    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder){
        return builder.routes()
                .route("login", r->r.path("/login").filters(f->f.filter(filter)).uri("lb://authentication"))
                .route("user", r->r.path("/user/**").filters(f->f.filter(filter)).uri("lb://user-project"))
                .route("python-main-server", r->r.path("/api/**").filters(f->f.filter(filter)).uri("http://localhost:8990"))
                .route("python-main-server-deploy", r->r.path("/chatbot/**").filters(f->f.filter(filter)).uri("http://localhost:8990")).build();


//

    }

}
