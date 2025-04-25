package com.hepl.backendapi.controller;

import com.hepl.backendapi.dto.generic.PrivateMessageDTO;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
/**
 * Ce contrôleur gère les messages privés envoyés par les clients via WebSocket.
 * Lorsqu’un utilisateur envoie un message à `/app/private-message`, ce contrôleur est invoqué.
 * Il utilise le `SimpMessagingTemplate` pour envoyer ce message vers `/user/{toAccountId}/private`.
 * Grâce à l'identité définie via le `Principal`, Spring STOMP sait exactement quel client est concerné,
 * et le message est reçu uniquement par cet utilisateur (et non diffusé à tous).
 */
@Controller
public class PrivateChatController {

    private final SimpMessagingTemplate messagingTemplate;

    public PrivateChatController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/private-message")
    public PrivateMessageDTO handlePrivateMessage(@Payload PrivateMessageDTO message, Principal principal) {
        message.setFromAccountId(principal.getName());
        messagingTemplate.convertAndSendToUser(message.getToAccountId(),"/private", message);
        return message;
    }
}
