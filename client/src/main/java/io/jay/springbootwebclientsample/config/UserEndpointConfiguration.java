package io.jay.springbootwebclientsample.config;

import io.jay.springbootwebclientsample.user.UserHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class UserEndpointConfiguration {
    @Bean
    public RouterFunction<ServerResponse> userApis(UserHandler userHandler) {
        return route()
                .path("/users", builder ->
                        builder.GET("/posts", userHandler::getPostsByUsers)
                                .GET("/{id}", userHandler::getUser)
                                .GET("", userHandler::getUsers)
                                .GET("/{id}/posts", userHandler::getPostsByUser)
                )
                .build();
    }
}
