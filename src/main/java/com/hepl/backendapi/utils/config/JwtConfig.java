package com.hepl.backendapi.utils.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.security.KeyFactory;

@Configuration
public class JwtConfig {

    // Clé publique au format PEM (sans les "BEGIN PUBLIC KEY" / "END PUBLIC KEY")
    String PUBLIC_KEY_PEM = """
    -----BEGIN PUBLIC KEY-----
    MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApPaxu23nGp4gPYxx0G1W
    C8RxExB8lYmFSVOP2GEtgZ1Z5xIHDH8KqQjEim9Qu4fLGN1TL2AX5IFVEYeZyt2A
    KXK0p6EUB4fpNzfNVN8kWjCw0tLZhYlSL0ikfp9YZ5DdWfnGzAfCNvRmF1vJ5+Sl
    mM+M3+zI/NsmDDyGngcSe+tnPr0W+08X3S5TzI8TyaVmD4qVgF/5Dy8ZmyAu1DCe
    zL6Vk5an2M2onmZcRghP+2zF6fvU20PTZ3cGUM+8NRUuQqJ3oH1J3M4XONrpsQKv
    Wkig82avD2O/5x8fGswz1qU8YVxayE3pjF/OGMyZx9DCIqR7q9Y0XP7dNRAE5E/I
    iwIDAQAB
    -----END PUBLIC KEY-----
    """;

    String cleanedKey = PUBLIC_KEY_PEM
            .replace("-----BEGIN PUBLIC KEY-----", "")
            .replace("-----END PUBLIC KEY-----", "")
            .replaceAll("\\s", "");

    @Bean
    public JwtDecoder jwtDecoder() throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(cleanedKey);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        RSAPublicKey publicKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(spec);
        return NimbusJwtDecoder.withPublicKey(publicKey).build();
    }
}
