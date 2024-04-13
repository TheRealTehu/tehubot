package com.therealtehu.discordbot.TehuBot.utils;

import org.springframework.stereotype.Component;

@Component
public class RandomNumberGenerator {
    public int getRandomNumber(int upperBound) {
        return (int) (Math.random() * upperBound);
    }

    public int getRandomNumber(int lowerBound, int upperBound) {
        return (int) (Math.random() * upperBound) + lowerBound;
    }
}
