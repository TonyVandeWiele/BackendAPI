package com.hepl.backendapi.controller;

import com.hepl.backendapi.dto.generic.UserDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/me")
public class UserInfoController {

    @GetMapping
    public ResponseEntity<String> getCurrentUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not authenticated");
        }

        String accountId = authentication.getName();

        // Récupérer toutes les infos de l'User connecté depuis les détails
        UserDTO user = (UserDTO) authentication.getDetails();

        return ResponseEntity.ok("AccountId: " + accountId +  " User email: " + user.getEmail() + "\nConnecté en tant que: " + user.getRole());
    }
}
