package com.hepl.backendapi.utils.config.WebSocketConfig;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

/**
 * Ce gestionnaire extrait l'ID utilisateur depuis les attributs de session (définis par l’intercepteur),
 * et l'utilise pour créer un objet `Principal`. Cet objet représente l'utilisateur connecté,
 * et est utilisé par Spring pour router les messages privés vers ce client. Ainsi,
 * chaque utilisateur est associé à un nom unique (ici : son accountId), et seuls
 * les messages lui étant destinés lui seront délivrés.
 */
@Component
public class HandshakeHandler extends DefaultHandshakeHandler {
    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler,
                                      Map<String, Object> attributes) {
        String accountId = (String) attributes.get("accountId");
        String role = (String) attributes.get("role");
        logger.info("👤 Nouvel utilisateur connecté avec ID: {"+ accountId + "}");

        return () -> accountId + " : " + role ;
    }
}
