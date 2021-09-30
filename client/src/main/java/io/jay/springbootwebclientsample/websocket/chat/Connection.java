package io.jay.springbootwebclientsample.websocket.chat;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.socket.WebSocketSession;

@Data
@RequiredArgsConstructor
public class Connection {
    private final String id;
    private final WebSocketSession session;
}
