package com.actionmonitor.controller;

import com.actionmonitor.model.WebSocketRequestMessage;
import com.actionmonitor.service.WebSocketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@Slf4j
public class WebSocketController {

    @Autowired
    private final WebSocketService webSocketService;

    @MessageMapping("message/{userTo}")
    public void sendMessage(
                            Principal principal,
                            @DestinationVariable String userTo,
                            @RequestBody WebSocketRequestMessage message) {
        log.info("Send message from user {} to user {}.", principal.getName(), userTo);
        webSocketService.sendMessage(principal.getName(), userTo, message.getMessageContent());
    }
}
