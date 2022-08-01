package com.actionmonitor.config;

import com.actionmonitor.interceptor.WebSocketChannelInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@Slf4j
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Autowired
    WebSocketChannelInterceptor channelInterceptor;

    @Value("${broker.host}")
    private String brokerHost;

    @Value("${broker.port}")
    private int brokerPort;

    @Value("${broker.username}")
    private String brokerUser;

    @Value("${broker.password}")
    private String brokerPass;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/action-monitor")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        //Simple Broker
        //registry.enableSimpleBroker("/topic");

        //ActiveMQ broker
        registry.enableStompBrokerRelay("/topic")
                .setRelayHost(brokerHost)
                .setRelayPort(brokerPort)
                .setClientLogin(brokerUser)
                .setClientPasscode(brokerPass)
                .setSystemLogin(brokerUser)
                .setSystemPasscode(brokerPass)
                .setUserDestinationBroadcast("/topic/unresolved-user")
                .setUserRegistryBroadcast("/topic/log-user-registry");

        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(channelInterceptor);
    }
}
