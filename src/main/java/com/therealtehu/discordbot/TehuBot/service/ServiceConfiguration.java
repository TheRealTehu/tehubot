package com.therealtehu.discordbot.TehuBot.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ServiceConfiguration {
    @Bean
    public WebClient getWebClient() {
        return WebClient.create();
    }
}
