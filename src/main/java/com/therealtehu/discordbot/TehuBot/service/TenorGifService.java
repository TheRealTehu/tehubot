package com.therealtehu.discordbot.TehuBot.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Service
public class TenorGifService {
    @Value("${tenor.api.key}")
    private String API_KEY;
    private final WebClient webClient;

    public TenorGifService() {
        this.webClient = WebClient.create();
    }
    public String searchGifUrlOnTenor(String searchTerm) {
        String URL = String.format("https://tenor.googleapis.com/v2/search?q=%s&key=%s&random=true&limit=1", searchTerm, API_KEY);
        String response = webClient
                .get()
                .uri(URL)
                .retrieve()
                .bodyToMono(String.class)
                .onErrorResume(Exception.class, e -> Mono.empty())
                .block();
        return extractGifUrl(response);
    }
    private String extractGifUrl(String response) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response);
            JsonNode resultsNode = rootNode.get("results");
            if (resultsNode.isArray() && resultsNode.size() > 0) {
                JsonNode gifNode = resultsNode.get(0).get("media_formats").get("gif");
                return gifNode.get("url").asText();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
