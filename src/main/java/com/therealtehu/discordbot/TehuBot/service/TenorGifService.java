package com.therealtehu.discordbot.TehuBot.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Service
public class TenorGifService {
    private static final String TENOR_SEARCH_URL = "https://tenor.googleapis.com/v2/search?q=%s&key=%s&random=true&limit=1";
    private static final String LETTERS_NUMBERS_REGEX = ".*[a-zA-Z0-9].*";
    private static final String ERROR_SEARCH_TERM = "Error";
    @Value("${tenor.api.key}")
    private String API_KEY;
    private final WebClient webClient;

    public TenorGifService() {
        this.webClient = WebClient.create();
    }

    public MessageEmbed getGifAsEmbed(String searchTerm) {
        if(!searchTerm.matches(LETTERS_NUMBERS_REGEX)) {
            searchTerm = ERROR_SEARCH_TERM;
        }
        return new EmbedBuilder()
                .setImage(searchGifUrlOnTenor(searchTerm))
                .build();
    }
    private String searchGifUrlOnTenor(String searchTerm) {
        String URL = String.format(TENOR_SEARCH_URL, searchTerm, API_KEY);
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
