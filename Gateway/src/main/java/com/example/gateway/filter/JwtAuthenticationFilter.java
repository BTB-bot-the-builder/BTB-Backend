package com.example.gateway.filter;

import com.example.gateway.util.JwtUtil;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationFilter implements GatewayFilter {

    @Autowired
    JwtUtil jwtUtil;

    public Mono<Void> badRequestResponse(ServerHttpResponse response, ServerWebExchange exchange, GatewayFilterChain chain, HttpStatus code){
        response.setStatusCode(code);

        DataBufferFactory dataBufferFactory = response.bufferFactory();

        ServerHttpResponseDecorator decorator = new ServerHttpResponseDecorator(response){

            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body){
                Flux<? extends DataBuffer> flux = (Flux<? extends DataBuffer>) body;

                Flux<? extends DataBuffer> f = flux.flatMap( dataBuffer  -> {

                    byte[] origRespContent = new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(origRespContent);

                    System.out.println("content::: " + (new String(origRespContent)));

                    //alocating a new buffer size does not help.
                    DataBuffer b = dataBufferFactory.allocateBuffer(256);
                    if(code == HttpStatus.UNAUTHORIZED){
                        b.write(Byte.parseByte("{\"status\":\""+ code.toString() + "\",\"msg\":\"Unauthorized\"}".getBytes()));
                    }
                    else{
                        b.write(Byte.parseByte("{\"status\":\""+ code.toString() + "\",\"msg\":\"Invalid jwt token\"}".getBytes()));
                    }

                    return Flux.just(b);
                });

                return super.writeWith(f);
            }

        };

        ServerWebExchange swe = exchange.mutate().response(decorator).build();

        return response.setComplete();
//        return chain.filter(swe);
    }


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        ServerWebExchange mutateServerWebExchange = exchange;
        if(isApiSecured(request)){
            if(!request.getHeaders().containsKey("Authorization")){
                ServerHttpResponse response = exchange.getResponse();
                return badRequestResponse(response, exchange, chain, HttpStatus.UNAUTHORIZED);
            }

            String token = request.getHeaders().getOrEmpty("Authorization").get(0);
            if(token.startsWith("Bearer")){
                token = token.substring(7);
            }
            else{
                ServerHttpResponse response = exchange.getResponse();
                return badRequestResponse(response, exchange, chain, HttpStatus.BAD_REQUEST);
            }
            try{
                if(!jwtUtil.validateToken(token)){
                    throw new Exception();
                }
                String username = jwtUtil.extractUsername(token);
                ServerHttpRequest mutatedRequest = request.mutate().header("email", username).build();
                mutateServerWebExchange = exchange.mutate().request(mutatedRequest).build();
            }
            catch(Exception e){
                ServerHttpResponse response = exchange.getResponse();
                return badRequestResponse(response, exchange, chain , HttpStatus.BAD_REQUEST);
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
