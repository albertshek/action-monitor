package com.actionmonitor.service;

import com.actionmonitor.model.MessageHistory;
import com.actionmonitor.model.WebSocketResponseMessage;
import com.actionmonitor.repository.WebSocketRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.sql.Timestamp;

@Service
@Slf4j
@RequiredArgsConstructor
public class WebSocketService {
    @Autowired
    private final SimpMessagingTemplate messageTemplate;

    @Autowired
    private final WebSocketRepository webSocketRepository;

    private final String DESTINATION = "/topic/messages";

    @SneakyThrows
    public void sendMessage(String userFrom, String userTo, String message) {
        String json = (new ObjectMapper()).writeValueAsString(new WebSocketResponseMessage(message));
        messageTemplate.convertAndSendToUser(userTo, DESTINATION, json);
        log.info("Convert and send message from user {} to user {}, with topic: {}.", userFrom, userTo, message);

        saveMessageHistory(userFrom, userTo, message);
    }

    public void saveMessageHistory(String userFrom, String userTo, String message) {
        MessageHistory messageHistory = MessageHistory.builder()
                .message(message)
                .userFrom(userFrom)
                .userTo(userTo)
                .timestamp(new Timestamp(System.currentTimeMillis()))
                .build();

        webSocketRepository.saveAndFlush(messageHistory);
        log.info("Save Message History: " + messageHistory.toString());
    }
}
