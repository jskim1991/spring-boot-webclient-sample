package io.jay.springbootwebclientsample.customer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
public class CustomerEndpointConfiguration {

    @Bean
    public RouterFunction<ServerResponse> customerApis(CustomerService customerService) {
        return route()
                .GET("/customers/stream", req -> ok().contentType(MediaType.TEXT_EVENT_STREAM)
                        .body(customerService.getCustomersStream(), Customer.class))
                .build();
    }
}
