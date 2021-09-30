package io.jay.springbootwebclientsample.websocket.chat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SignalType;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.*;

@Configuration
@RequiredArgsConstructor
public class ChatWebSocketConfiguration {

    private final Map<String, Connection> sessions = new ConcurrentHashMap<>();
    private final BlockingQueue<Message> messages = new LinkedBlockingQueue<>();

    @Bean
    HandlerMapping chatHandlerMapping() {
        return new SimpleUrlHandlerMapping(Map.of("/ws/chat", chatWebsocketHandler()), 2);
    }

    @Bean
    WebSocketHandler chatWebsocketHandler() {
        Flux<Message> messagesToBroadcast = Flux.<Message>create(sink -> {
            Future<Object> submit = Executors.newSingleThreadExecutor().submit(() -> {
                while (true) {
                    try {
                        sink.next(this.messages.take());
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            sink.onCancel(() -> submit.cancel(true));
        }).share();

        return session -> {
            String sessionId = session.getId();
            this.sessions.put(sessionId, new Connection(sessionId, session));

            Flux<Boolean> in = session.receive()
                    .map(WebSocketMessage::getPayloadAsText)
                    .map(msg -> {
                        Message message = new Message(sessionId, msg, new Date());
                        return this.messages.offer(message);
                    })
                    .doFinally(st -> {
                        if (SignalType.ON_COMPLETE.equals(st)) {
                            this.sessions.remove(sessionId);
                        }
                    });

            Flux<WebSocketMessage> out = messagesToBroadcast
                    .map(this::toJson)
                    .map(session::textMessage);

            return session.send(out).and(in);
        };
    }

    private String toJson(Message message) {
        try {
            return new ObjectMapper().writeValueAsString(message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
