package com.hepl.backendapi.utils.config.SpringSecurityConfig;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * CustomJwtAuthenticationConverter est un convertisseur personnalisé qui extrait les informations
 * pertinentes du JWT, telles que l'accountId et les rôles, et les transforme en un token d'authentification.
 *
 * Important :
 * - Ce convertisseur est utilisé après que le JWT ait été décodé et validé par le NimbusJwtDecoder.
 * - Le NimbusJwtDecoder se charge de valider la signature du JWT et de le décoder avant que ce
 *   convertisseur ne prenne le relais.
 * - Le convertisseur extrait l'accountId et les rôles du JWT et les transforme en une collection d'autorités
 *   (rôles) pour l'utilisateur.
 * - Le token d'authentification (JwtAuthenticationToken) est ensuite créé et injecté dans le contexte
 *   de sécurité de Spring, permettant à Spring Security de gérer l'authentification de l'utilisateur.
 */

@Component
public class CustomJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        // Extraire l'accountId du JWT
        String accountId = jwt.getClaimAsString("sub");

        // Extraire les rôles (en supposant qu'ils sont dans un champ "roles")
        List<String> roles = jwt.getClaimAsStringList("roles");

        // Convertir les rôles en autorités
        Collection<GrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());

        // Créer un JwtAuthenticationToken avec les autorités et l'accountId
        return new JwtAuthenticationToken(jwt, authorities, accountId);
    }
}
