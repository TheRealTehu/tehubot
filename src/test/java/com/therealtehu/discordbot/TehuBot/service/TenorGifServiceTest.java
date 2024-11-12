package com.therealtehu.discordbot.TehuBot.service;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TenorGifServiceTest {
    private TenorGifService tenorGifService;
    @Mock
    private WebClient webClientMock;
    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpecMock;
    @Mock
    private WebClient.ResponseSpec responseSpecMock;

    @BeforeEach
    void setup() {
        tenorGifService = new TenorGifService(webClientMock);
    }

    @Test
    void getGifAsEmbedWhenNormalSearchTermIsGivenThenCorrectMessageIsSent() {
        when(webClientMock.get()).thenReturn(requestHeadersUriSpecMock);
        when(requestHeadersUriSpecMock.uri(anyString())).thenReturn(requestHeadersUriSpecMock);
        when(requestHeadersUriSpecMock.retrieve()).thenReturn(responseSpecMock);
        when(responseSpecMock.bodyToMono(String.class)).thenReturn(Mono.just(exampleJsonResponse));

        MessageEmbed expected = new EmbedBuilder()
                .setImage("https://example.com/example.gif")
                .build();
        MessageEmbed actual = tenorGifService.getGifAsEmbed("Test");

        assertEquals(expected, actual);
    }

    @Test
    void getGifAsEmbedWhenUnsupportedSearchTermIsGivenThenErrorGifMessageIsSent() {
        when(webClientMock.get()).thenReturn(requestHeadersUriSpecMock);
        when(requestHeadersUriSpecMock.uri(anyString())).thenReturn(requestHeadersUriSpecMock);
        when(requestHeadersUriSpecMock.retrieve()).thenReturn(responseSpecMock);
        when(responseSpecMock.bodyToMono(String.class)).thenReturn(Mono.just(exampleJsonResponse));

        MessageEmbed expected = new EmbedBuilder()
                .setImage("https://example.com/example.gif")
                .build();
        MessageEmbed actual = tenorGifService.getGifAsEmbed("-( )+");

        assertEquals(expected, actual);
        verify(requestHeadersUriSpecMock).uri("https://tenor.googleapis.com/v2/search?q=Error&key=null&random=true&limit=1");
    }

    @Test
    void getGifAsEmbedWhenMonoThrowsExceptionReturnsPredefinedErrorGif() {
        when(webClientMock.get()).thenReturn(requestHeadersUriSpecMock);
        when(requestHeadersUriSpecMock.uri(anyString())).thenReturn(requestHeadersUriSpecMock);
        when(requestHeadersUriSpecMock.retrieve()).thenReturn(responseSpecMock);
        when(responseSpecMock.bodyToMono(String.class))
                .thenReturn(Mono.error(new WebClientResponseException(500, "Internal Server Error", null, null, null)));

        MessageEmbed expected = new EmbedBuilder()
                .setImage("https://media.tenor.com/FOzbM2mVKG0AAAAC/error-windows-xp.gif")
                .build();

        MessageEmbed actual = tenorGifService.getGifAsEmbed("anything");

        assertEquals(expected, actual);
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