package com.hepl.backendapi.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class MailServiceTest {

    private MailService mailService;

    private final String apiKey = "test-api-key";
    private final String domain = "test.domain.com";

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        mailService = new MailService();

        // Injecter les valeurs de apiKey et domain via réflexion
        Field apiKeyField = MailService.class.getDeclaredField("apiKey");
        apiKeyField.setAccessible(true);
        apiKeyField.set(mailService, apiKey);

        Field domainField = MailService.class.getDeclaredField("domain");
        domainField.setAccessible(true);
        domainField.set(mailService, domain);
    }

    @Test
    void sendSimpleEmail_validParameters_doesNotThrow() {
        // Arrange
        String to = "test@example.com";
        String subject = "Test Subject";
        String text = "Test Message";

        // Act & Assert
        assertDoesNotThrow(() -> mailService.sendSimpleEmail(to, subject, text));
    }

    @Test
    void sendSimpleEmail_ioException_doesNotThrow() {
        // Arrange
        String to = "test@example.com";
        String subject = "Test Subject";
        String text = "Test Message";

        // Act & Assert
        assertDoesNotThrow(() -> mailService.sendSimpleEmail(to, subject, text));
    }

    @Test
    void sendSimpleEmail_interruptedException_doesNotThrow() {
        // Arrange
        String to = "test@example.com";
        String subject = "Test Subject";
        String text = "Test Message";

        // Act & Assert
        assertDoesNotThrow(() -> mailService.sendSimpleEmail(to, subject, text));
    }
}
