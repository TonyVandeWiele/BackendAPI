package com.hepl.backendapi.utils.config.WebSocketConfig;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

/**
 * Configure le serveur WebSocket avec STOMP en définissant les préfixes des messages,
 * le point d'accès `/ws`, un `HandshakeHandler` personnalisé pour identifier les utilisateurs,
 * et un `HandshakeInterceptor` pour vérifier le token JWT.
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final HandshakeInterceptor handshakeInterceptor;
    private final HandshakeHandler handshakeHandler;

    public WebSocketConfig(HandshakeInterceptor handshakeInterceptor, HandshakeHandler handshakeHandler) {
        this.handshakeInterceptor = handshakeInterceptor;
        this.handshakeHandler = handshakeHandler;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/private");
        registry.setApplicationDestinationPrefixes("/app");       // routes de messages entrants
        registry.setUserDestinationPrefix("/user");               // routes de messages sortants privés
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setHandshakeHandler(handshakeHandler)
                .addInterceptors(handshakeInterceptor)
                .setAllowedOriginPatterns("*") // pour tests locaux
                .withSockJS(); // fallback navigateur
    }
}
