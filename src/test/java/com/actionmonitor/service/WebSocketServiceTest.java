package com.actionmonitor.service;

import com.actionmonitor.model.MessageHistory;
import com.actionmonitor.model.WebSocketResponseMessage;
import com.actionmonitor.repository.WebSocketRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.sql.Timestamp;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class WebSocketServiceTest {

    @Mock
    private SimpMessagingTemplate messageTemplate;
    @Mock
    private WebSocketRepository webSocketRepository;
    private WebSocketService webSocketService;

    private final String DESTINATION = "/topic/messages";

    String userFrom = "Client001";
    String userTo = "Client002";
    String message = "Hello!";

    @BeforeEach
    void setUp() {
        webSocketService = new WebSocketService(messageTemplate, webSocketRepository);
    }

    @AfterEach
    void tearDown() throws Exception {

    }

    @Test
    void testSendMessage() throws JsonProcessingException {
        String json = (new ObjectMapper()).writeValueAsString(new WebSocketResponseMessage(message));
        webSocketService.sendMessage(userFrom, userTo, message);
        verify(messageTemplate).convertAndSendToUser(userTo, DESTINATION, json);
    }

    @Test
    void testSaveMessageHistory() {
        MessageHistory messageHistory = MessageHistory.builder()
                .message(message)
                .userFrom(userFrom)
                .userTo(userTo)
                .timestamp(new Timestamp(System.currentTimeMillis()))
                .build();

        webSocketService.saveMessageHistory(userFrom, userTo, message);
        ArgumentCaptor<MessageHistory> messageHistoryArgumentCaptor =
                ArgumentCaptor.forClass(MessageHistory.class);
        verify(webSocketRepository).saveAndFlush(messageHistoryArgumentCaptor.capture());

        MessageHistory capturedMessageHistory = messageHistoryArgumentCaptor.getValue();
        assertThat(capturedMessageHistory).isEqualTo(messageHistory);
    }
}