package com.hepl.backendapi.controller.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JacksonController {

    private final ObjectMapper objectMapper;

    public JacksonController(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @GetMapping("/testJson")
    public String testJson() throws Exception {
        Person person = new Person("John", 30);
        Person.builder().name("John").age(30).build();
        return objectMapper.writeValueAsString(person);  // Serialize to JSON
    }

    @Setter
    @Getter
    @Builder
    static class Person {
        private String name;
        private int age;

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }
    }
}
