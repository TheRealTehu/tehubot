package com.therealtehu.discordbot.TehuBot.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class WikiArticleServiceTest {
    private WikiArticleService wikiArticleService;
    private final WebClient mockWebClient = Mockito.mock(WebClient.class);
    private final WebClient.Builder mockWebClientBuilder = Mockito.mock(WebClient.Builder.class);
    private final WebClient.RequestHeadersUriSpec mockRequestHeaderUriSpec =
            Mockito.mock(WebClient.RequestHeadersUriSpec.class);
    private final WebClient.ResponseSpec mockResponseSpec = Mockito.mock(WebClient.ResponseSpec.class);


    @BeforeEach
    void setup() {
        when(mockWebClient.mutate()).thenReturn(mockWebClientBuilder);
        when(mockWebClientBuilder.baseUrl("https://en.wikipedia.org/w/api.php")).thenReturn(mockWebClientBuilder);
        when(mockWebClientBuilder.defaultHeader(HttpHeaders.USER_AGENT, "TehuBotDiscord/1.0 (https://github.com/TheRealTehu)"))
                .thenReturn(mockWebClientBuilder);
        when(mockWebClientBuilder.build()).thenReturn(mockWebClient);
        wikiArticleService = new WikiArticleService(mockWebClient);
    }

    @Test
    void getWikiArticleWhenTitleIsGivenAnArticleTextIsReturned() {

        when(mockWebClient.get()).thenReturn(mockRequestHeaderUriSpec);
        when(mockRequestHeaderUriSpec.uri("?action=query&format=json&prop=extracts&explaintext=false&titles=Test Title"))
                .thenReturn(mockRequestHeaderUriSpec);
        when(mockRequestHeaderUriSpec.retrieve()).thenReturn(mockResponseSpec);
        when(mockResponseSpec.bodyToMono(String.class)).thenReturn(Mono.just(exampleJsonResponse));

        String expected = "This is the Wiki article for the given topic.";
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