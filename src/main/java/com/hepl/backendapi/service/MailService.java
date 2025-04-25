package com.hepl.backendapi.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;

@Service
public class MailService {

    @Value("${mailgun.api.key}")
    private String apiKey;

    @Value("${mailgun.domain}")
    private String domain;


    public void sendSimpleEmail(String to, String subject, String text) {
        HttpClient client = HttpClient.newHttpClient();
        String auth = Base64.getEncoder().encodeToString(("api:" + apiKey).getBytes());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.mailgun.net/v3/" + domain + "/messages"))
                .header("Authorization", "Basic " + auth)
                .POST(HttpRequest.BodyPublishers.ofString(
                        "from=Commande <mailgun@" + domain + ">&to=" + to + "&subject=" + subject + "&text=" + text
                ))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .build();

        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}
