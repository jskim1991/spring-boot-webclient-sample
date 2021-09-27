package io.jay.springbootwebclientsample.config;

import io.jay.springbootwebclientsample.customer.Customer;
import io.jay.springbootwebclientsample.customer.CustomerService;
import io.jay.springbootwebclientsample.user.UserHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
public class HttpEndpointConfiguration {

    @Bean
    RouterFunction<ServerResponse> customerApis(CustomerService customerService) {
        return route()
                .GET("/customers/stream", req -> ok().contentType(MediaType.TEXT_EVENT_STREAM)
                        .body(customerService.getCustomersStream(), Customer.class))
                .build();
    }

    @Bean
    RouterFunction<ServerResponse> userApis(UserHandler userHandler) {
        return route()
                .nest(path("/users"), builder ->
                        builder.GET("/posts", userHandler::getPostsByUsers)
                                .GET("/{id}", userHandler::getUser)
                                .GET("", userHandler::getUsers)
                                .GET("/{id}/posts", userHandler::getPostsByUser)
                ).build();
    }
}
