package io.jay.springbootwebclientsample.websocket;

import io.jay.springbootwebclientsample.producer.IntervalMessageProducer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SignalType;

import java.util.Map;

@Configuration
public class EchoWebSocketConfiguration {

    @Bean
    HandlerMapping handlerMapping() {
        return new SimpleUrlHandlerMapping(Map.of("/ws/echo", webSocketHandler()), 10);
    }

    @Bean
    WebSocketHandler webSocketHandler() {
        return session -> {
            Flux<WebSocketMessage> out = IntervalMessageProducer
                    .produce(10)
                    .doOnNext(System.out::println)
                    .map(session::textMessage)
                    .doFinally(signalType -> System.out.println("outbound connection: " + signalType));

            Flux<String> in = session
                    .receive()
                    .map(WebSocketMessage::getPayloadAsText)
                    .doFinally(signalType -> {
                        System.out.println("inbound connection" + signalType);
                        if (SignalType.ON_COMPLETE.equals(signalType)) {
                            session.close().subscribe();
                        }
                    })
                    .doOnNext(System.out::println);
            return session.send(out).and(in);
        };
    }
}
