package io.jay.springbootwebclientsample.sse;

import io.jay.springbootwebclientsample.producer.IntervalMessageProducer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
public class SseConfiguration {
    private final String countPathVariable = "count";

    @Bean
    RouterFunction<ServerResponse> routes() {
        return route()
                .GET("/sse/{" + this.countPathVariable + "}", this::handleSse)
                .build();
    }

    Mono<ServerResponse> handleSse(ServerRequest r) {
        int count = Integer.parseInt(r.pathVariable(this.countPathVariable));
        Flux<String> publisher = IntervalMessageProducer.produce(count)
                .doOnComplete(() -> System.out.println("completed"));

        return ok().contentType(MediaType.TEXT_EVENT_STREAM)
                .body(publisher, String.class);
    }
}
