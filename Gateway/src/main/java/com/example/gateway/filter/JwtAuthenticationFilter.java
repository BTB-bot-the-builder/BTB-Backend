package com.example.gateway.filter;

import com.example.gateway.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationFilter implements GatewayFilter {

    @Autowired
    JwtUtil jwtUtil;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        ServerWebExchange mutateServerWebExchange = exchange;
        if(isApiSecured(request)){
            if(!request.getHeaders().containsKey("Authorization")){
                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.setComplete();
            }

            String token = request.getHeaders().getOrEmpty("Authorization").get(0);
            if(token.startsWith("Bearer")){
                token = token.substring(7);
            }
            else{
                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.BAD_REQUEST);
                return response.setComplete();
            }
            try{
                jwtUtil.validateToken(token);
                String username = jwtUtil.extractUsername(token);
                ServerHttpRequest mutatedRequest = request.mutate().header("email", username).build();
                mutateServerWebExchange = exchange.mutate().request(mutatedRequest).build();
            }
            catch(Exception e){
                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.BAD_REQUEST);
                return response.setComplete();
            }
        }

        return chain.filter(mutateServerWebExchange);
    }

    boolean isApiSecured(ServerHttpRequest request){
        String url = request.getURI().getPath();

        System.out.println(url);

        if(url.startsWith("/login") || url.startsWith("/api")){
            return false;
        }
        return true;
    }
}
