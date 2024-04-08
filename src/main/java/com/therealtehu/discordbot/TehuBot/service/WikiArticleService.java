package com.therealtehu.discordbot.TehuBot.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

@Service
public class WikiArticleService {
    private static final String MEDIAWIKI_USER_AGENT = "TehuBotDiscord/1.0 (https://github.com/TheRealTehu)";
    private static final String BASE_URL = "https://en.wikipedia.org/w/api.php";
    private static final String URL_QUERY = "?action=query&format=json&prop=extracts&explaintext=false&titles=";
    private final WebClient webClient;

    public WikiArticleService() {
        this.webClient = WebClient.builder()
                .baseUrl(BASE_URL)
                .defaultHeader(HttpHeaders.USER_AGENT, MEDIAWIKI_USER_AGENT)
                .build();
    }

    public String getWikiArticle(String title) {
        String response = webClient.get()
                .uri(URL_QUERY + title)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return extractDescription(response);
    }

    private String extractDescription(String response) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response);
            JsonNode queryNode = rootNode.path("query");
            JsonNode pagesNode = queryNode.path("pages");
            JsonNode firstPageNode = pagesNode.elements().next();
            JsonNode extractNode = firstPageNode.path("extract");
            return extractNode.asText();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
