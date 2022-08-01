package com.actionmonitor.interceptor;


import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static java.util.Optional.ofNullable;

@Service
@Slf4j
public class WebSocketChannelInterceptor implements ChannelInterceptor {
    static final String SESSION_KEY_HEADER = "simpSessionId";
    static final String WS_ID_HEADER = "ws-id";

    /**
     * Processes a message before sending it
     */
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = readHeaderAccessor(message);

        if (accessor.getCommand() == StompCommand.CONNECT) {

            String wsId = readWebSocketIdHeader(accessor);
            String sessionId = readSessionId(accessor);

            UsernamePasswordAuthenticationToken user = new UsernamePasswordAuthenticationToken(wsId, null);
            accessor.setHeader("connection-time", LocalDateTime.now().toString());
            accessor.setUser(user);
            log.info("User with authKey '{}', ws-id {} session {} make a WebSocket connection and generated the user {}", wsId, sessionId, user.toString());
        }

        return message;

    }

    /**
     * Instantiate an object for retrieving the STOMP headers
     */
    private StompHeaderAccessor readHeaderAccessor(Message<?> message) {
        StompHeaderAccessor accessor = getAccessor(message);
        if (accessor == null) {
            throw new AuthenticationCredentialsNotFoundException("Fail to read headers.");
        }
        return accessor;
    }

    private String readSessionId(StompHeaderAccessor accessor) {
        return ofNullable(accessor.getMessageHeaders().get(SESSION_KEY_HEADER))
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("Session header not found")).toString();
    }

    private String readWebSocketIdHeader(StompHeaderAccessor accessor) {
        String wsId = accessor.getFirstNativeHeader(WS_ID_HEADER);
        if (wsId == null || wsId.trim().isEmpty())
            throw new AuthenticationCredentialsNotFoundException("Web Socket ID Header not found");
        return wsId;
    }

    /*StompHeaderAccessor getAccessor(Message<?> message) {
        return MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
    }*/

    private StompHeaderAccessor getAccessor(Message<?> message) {
        MessageHeaderAccessor accessor = MessageHeaderAccessor.getMutableAccessor(message);
        accessor.setLeaveMutable(true);
        return (StompHeaderAccessor)accessor;
    }

}
