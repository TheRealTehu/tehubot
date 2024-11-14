package com.therealtehu.discordbot.TehuBot.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

@Service
public class TenorGifService {
    private static final String TENOR_SEARCH_URL = "https://tenor.googleapis.com/v2/search?q=%s&key=%s&random=true&limit=1";
    private static final String LETTERS_NUMBERS_REGEX = ".*[a-zA-Z0-9].*";
    private static final String ERROR_SEARCH_TERM = "Error";
    @Value("${tenor.api.key}")
    private String TENOR_API_KEY;
    private final WebClient webClient;
    private static final String ERROR_ANSWER =
            """
                            {
                              "results": [
                                {
                                  "id": "1507820989123471469",
                                  "title": "",
                                  "media_formats": {
                                    "gif": {
                                      "url": "https://media.tenor.com/FOzbM2mVKG0AAAAC/error-windows-xp.gif",
                                      "duration": 4.2,
                                      "preview": "",
                                      "dims": [
                                        498,
                                        466
                                      ],
                                      "size": 425560
                                    }
                                  }
                                }
                              ]
                            }
                    """;

    @Autowired
    public TenorGifService(WebClient webClient) {
        this.webClient = webClient;
    }

    public MessageEmbed getGifAsEmbed(String searchTerm) {
        if (!searchTerm.matches(LETTERS_NUMBERS_REGEX)) {
            searchTerm = ERROR_SEARCH_TERM;
        }
        return new EmbedBuilder()
                .setImage(searchGifUrlOnTenor(searchTerm))
                .build();
    }

    private String searchGifUrlOnTenor(String searchTerm) {
        String URL = String.format(TENOR_SEARCH_URL, searchTerm, TENOR_API_KEY);
        String response = webClient
                .get()
                .uri(URL)
                .retrieve()
                .bodyToMono(String.class)
                .onErrorReturn(ERROR_ANSWER)
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
