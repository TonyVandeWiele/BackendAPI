package com.hepl.backendapi.utils.config.websocketconfig;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.security.Principal;

@Slf4j
@Component
public class WebSocketEventListener {

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        Principal user = event.getUser();
        log.info("🟢 Connexion STOMP établie pour user: {}", user != null ? user.getName() : "anonyme");
    }

    @EventListener
    public void handleWebSocketSubscribeListener(SessionSubscribeEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        log.info("📥 Abonnement à destination: {} par session: {}",
                headerAccessor.getDestination(),
                headerAccessor.getSessionId());
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        String sessionId = event.getSessionId();
        log.info("🔴 Déconnexion de session WebSocket: {}", sessionId);

        Principal user = event.getUser();
        if (user != null) {
            log.info("👋 User {} s’est déconnecté", user.getName());
        }
    }
}
