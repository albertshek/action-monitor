package com.actionmonitor.interceptor;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

class WebSocketChannelInterceptorTest {

    @Mock
    private MessageChannel messageChannel;
    private StompHeaderAccessor stompHeaderAccessor;
    private final String WS_ID_HEADER = "ws-id";
    private final String SESSION_KEY_HEADER = "simpSessionId";
    String userFrom = "Client001";
    String sessionId = "12345678";

    WebSocketChannelInterceptor webSocketChannelInterceptor = new WebSocketChannelInterceptor();

    @BeforeEach
    void setUp() {
        stompHeaderAccessor = StompHeaderAccessor.create(StompCommand.CONNECT);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testPreSend() {
        stompHeaderAccessor.setNativeHeader(WS_ID_HEADER, userFrom);
        stompHeaderAccessor.setHeader(SESSION_KEY_HEADER, sessionId);
        Message<byte[]> message = MessageBuilder.createMessage(new byte[0], stompHeaderAccessor.getMessageHeaders());

        Message<?> expectedMessage = webSocketChannelInterceptor.preSend(message, messageChannel);

        assertEquals(expectedMessage, message);
    }

    @Test
     void testPreSendSessionHeaderNotFoundThrowException() {
        stompHeaderAccessor.setNativeHeader(WS_ID_HEADER, userFrom);
        Message<byte[]> message = MessageBuilder.createMessage(new byte[0], stompHeaderAccessor.getMessageHeaders());

        RuntimeException exception =
                assertThrows(AuthenticationCredentialsNotFoundException.class,
                        () -> webSocketChannelInterceptor.preSend(message, messageChannel));
        assertEquals("Session header not found", exception.getMessage());
    }

    @Test
    void testPreSendWebsocketIdHeaderNotFoundThrowException() {
        stompHeaderAccessor.setHeader(SESSION_KEY_HEADER, sessionId);
        Message<byte[]> message = MessageBuilder.createMessage(new byte[0], stompHeaderAccessor.getMessageHeaders());

        RuntimeException exception =
                assertThrows(AuthenticationCredentialsNotFoundException.class,
                        () -> webSocketChannelInterceptor.preSend(message, messageChannel));
        assertEquals("Web Socket ID Header not found", exception.getMessage());
    }
}