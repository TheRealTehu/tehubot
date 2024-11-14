package com.therealtehu.discordbot.TehuBot.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WikiArticleServiceTest {
    private WikiArticleService wikiArticleService;
    @Mock
    private WebClient webClientMock;
    @Mock
    private WebClient.Builder webClientBuilderMock;
    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpecMock;
    @Mock
    private WebClient.ResponseSpec responseSpecMock;


    @BeforeEach
    void setup() {
        when(webClientMock.mutate()).thenReturn(webClientBuilderMock);
        when(webClientBuilderMock.baseUrl("https://en.wikipedia.org/w/api.php")).thenReturn(webClientBuilderMock);
        when(webClientBuilderMock.defaultHeader(HttpHeaders.USER_AGENT, "TehuBotDiscord/1.0 (https://github.com/TheRealTehu)"))
                .thenReturn(webClientBuilderMock);
        when(webClientBuilderMock.build()).thenReturn(webClientMock);
        wikiArticleService = new WikiArticleService(webClientMock);
    }

    @Test
    void getWikiArticleWhenTitleIsGivenAnArticleTextIsReturned() {
        when(webClientMock.get()).thenReturn(requestHeadersUriSpecMock);
        when(requestHeadersUriSpecMock.uri("?action=query&format=json&prop=extracts&explaintext=false&titles=Test Title"))
                .thenReturn(requestHeadersUriSpecMock);
        when(requestHeadersUriSpecMock.retrieve()).thenReturn(responseSpecMock);
        when(responseSpecMock.bodyToMono(String.class)).thenReturn(Mono.just(exampleJsonResponse));

        String expected = "This is the Wiki article for the given topic.";
        String actual = wikiArticleService.getWikiArticle("Test Title");

        assertEquals(expected, actual);
    }

    @Test
    void getWikiArticleWhenApiErrorOccursErrorTextIsReturned() {
        when(webClientMock.get()).thenReturn(requestHeadersUriSpecMock);
        when(requestHeadersUriSpecMock.uri("?action=query&format=json&prop=extracts&explaintext=false&titles=Test Title"))
                .thenReturn(requestHeadersUriSpecMock);
        when(requestHeadersUriSpecMock.retrieve()).thenReturn(responseSpecMock);
        when(responseSpecMock.bodyToMono(String.class))
                .thenReturn(Mono.error(new WebClientResponseException(500, "Internal Server Error", null, null, null)));

        String expected = "ERROR: Could not reach Wikipedia!";
        String actual = wikiArticleService.getWikiArticle("Test Title");

        assertEquals(expected, actual);
    }

    private final String exampleJsonResponse = """
            {
              "query": {
                "pages": {
                  "12345": {
                    "extract": "This is the Wiki article for the given topic."
                  },
                  "67890": {
                    "extract": "Other info"
                  }
                }
              }
            }
            """;
}