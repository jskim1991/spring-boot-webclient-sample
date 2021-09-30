package io.jay.springbootwebclientsample.config;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class CustomWebFilter implements WebFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange currentRequest, WebFilterChain chain) {
        String uuid = UUID.randomUUID().toString();
        System.out.println("Inside CustomWebFilter. Request UUID is " + uuid);

        ServerWebExchange outgoingExchange = currentRequest.mutate()
                .request(builder -> builder.header("custom-header", uuid))
                .build();
        return chain.filter(outgoingExchange);
    }
}
