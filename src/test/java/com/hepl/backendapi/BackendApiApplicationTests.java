package com.hepl.backendapi;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = "azure.keyvault.enabled=false")
class BackendApiApplicationTests {

    @Test
    void contextLoads() {
    }

}
