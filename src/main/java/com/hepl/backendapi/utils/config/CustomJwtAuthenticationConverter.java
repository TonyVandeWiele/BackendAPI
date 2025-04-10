package com.hepl.backendapi.utils.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.core.Authentication;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CustomJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        // Extraire l'accountId du JWT
        String accountId = jwt.getClaimAsString("accountId");

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
