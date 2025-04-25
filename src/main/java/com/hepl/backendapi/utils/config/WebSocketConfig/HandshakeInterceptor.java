package com.hepl.backendapi.utils.config.WebSocketConfig;

import com.hepl.backendapi.utils.config.SpringSecurityConfig.JwtConfig;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;

import java.util.Map;

/**
 * Intercepte la requête de handshake WebSocket pour extraire et valider le token JWT
 * dans les paramètres, puis stocke l'ID du compte utilisateur dans les attributs pour l'authentification.
 */
@Component
public class HandshakeInterceptor implements org.springframework.web.socket.server.HandshakeInterceptor {

    private final JwtConfig jwtService;

    public HandshakeInterceptor(JwtConfig jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) {
        if (request instanceof ServletServerHttpRequest servletRequest) {
            String token = servletRequest.getServletRequest().getParameter("Authorization");
            token = jwtService.stripBearerPrefix(token);
            try {
                String accountId = jwtService.extractAccountId(token);
                String role = jwtService.extractRole(token);
                attributes.put("accountId", accountId);
                attributes.put("role", role);

                return true;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {}
}

