package com.therealtehu.discordbot.TehuBot.service;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TenorGifServiceTest {
    private TenorGifService tenorGifService;
    private final WebClient mockWebClient = Mockito.mock(WebClient.class);
    private final WebClient.RequestHeadersUriSpec mockRequestHeaderUriSpec =
            Mockito.mock(WebClient.RequestHeadersUriSpec.class);
    private final WebClient.ResponseSpec mockResponseSpec = Mockito.mock(WebClient.ResponseSpec.class);

    @BeforeEach
    void setup() {
        tenorGifService = new TenorGifService(mockWebClient);
    }

    @Test
    void getGifAsEmbedWhenNormalSearchTermIsGivenThenCorrectMessageIsSent() {
        when(mockWebClient.get()).thenReturn(mockRequestHeaderUriSpec);
        when(mockRequestHeaderUriSpec.uri(anyString())).thenReturn(mockRequestHeaderUriSpec);
        when(mockRequestHeaderUriSpec.retrieve()).thenReturn(mockResponseSpec);
        when(mockResponseSpec.bodyToMono(String.class)).thenReturn(Mono.just(exampleJsonResponse));

        MessageEmbed expected = new EmbedBuilder()
                .setImage("https://example.com/example.gif")
                .build();
        MessageEmbed actual = tenorGifService.getGifAsEmbed("Test");

        assertEquals(expected, actual);
    }

    @Test
    void getGifAsEmbedWhenUnsupportedSearchTermIsGivenThenErrorGifMessageIsSent() {
        when(mockWebClient.get()).thenReturn(mockRequestHeaderUriSpec);
        when(mockRequestHeaderUriSpec.uri(anyString())).thenReturn(mockRequestHeaderUriSpec);
        when(mockRequestHeaderUriSpec.retrieve()).thenReturn(mockResponseSpec);
        when(mockResponseSpec.bodyToMono(String.class)).thenReturn(Mono.just(exampleJsonResponse));

        MessageEmbed expected = new EmbedBuilder()
                .setImage("https://example.com/example.gif")
                .build();
        MessageEmbed actual = tenorGifService.getGifAsEmbed("-( )+");

        assertEquals(expected, actual);
        verify(mockRequestHeaderUriSpec).uri("https://tenor.googleapis.com/v2/search?q=Error&key=null&random=true&limit=1");
    }

    private final String exampleJsonResponse= """
            {
              "results": [
                {
                  "media_formats": {
                    "gif": {
                      "url": "https://example.com/example.gif"
                    },
                    "other_format": {
                      "url": "https://example.com/other_format.mp4"
                    }
                  }
                }
              ]
            }""";
}