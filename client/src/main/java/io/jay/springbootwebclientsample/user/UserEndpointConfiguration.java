package io.jay.springbootwebclientsample.user;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class UserEndpointConfiguration {
    @Bean
    public RouterFunction<ServerResponse> userApis(UserHandler userHandler) {
        return route()
                .path("/users", builder ->
                        builder.GET("/posts", userHandler::getPostsByUsers)
                                .GET("/error", userHandler::dummyHandler)
                                .GET("/{id}", userHandler::getUser)
                                .GET("", userHandler::getUsers)
                                .GET("/{id}/posts", userHandler::getPostsByUser)
                                .filter((req, next) -> {
                                    System.out.println(".filter(): before");
                                    Mono<ServerResponse> reply = next.handle(req)
                                            .onErrorResume(CustomUserException.class, e -> {
                                                String customHeaderValue = req.headers().firstHeader("custom-header");
                                                return ServerResponse
                                                        .status(600)
                                                        .bodyValue("This endpoint is prohibited " + customHeaderValue);
                                            });
                                    System.out.println(".filter(): after");
                                    return reply;
                                })
                                .before(request -> {
                                    String customHeaderValue = request.headers().firstHeader("custom-header");
                                    System.out.println(".before() - " + customHeaderValue);
                                    return request;
                                })
                                .after(((request, serverResponse) -> {
                                    String customHeaderValue = request.headers().firstHeader("custom-header");
                                    System.out.println(".after() - " + customHeaderValue);
                                    return serverResponse;
                                }))
                )
                .build();
    }
}
