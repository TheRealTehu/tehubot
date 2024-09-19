package com.therealtehu.discordbot.TehuBot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TehuBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(TehuBotApplication.class, args);
	}

}
