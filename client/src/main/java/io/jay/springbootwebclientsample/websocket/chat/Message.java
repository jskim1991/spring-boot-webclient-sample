package io.jay.springbootwebclientsample.websocket.chat;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Data
@RequiredArgsConstructor
public class Message {
    private final String clientId;
    private final String text;
    private final Date when;
}
