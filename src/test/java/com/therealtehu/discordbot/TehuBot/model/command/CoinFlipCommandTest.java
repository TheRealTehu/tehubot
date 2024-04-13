package com.therealtehu.discordbot.TehuBot.model.command;

import com.therealtehu.discordbot.TehuBot.service.display.MessageSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CoinFlipCommandTest {

    private CoinFlipCommand coinFlipCommand;

    private final MessageSender mockMessageSender = Mockito.mock(MessageSender.class);

    @BeforeEach
    void setup() {
        coinFlipCommand = new CoinFlipCommand(mockMessageSender);
    }

    @Test
    void executeCommand() {
    }
}