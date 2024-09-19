package com.therealtehu.discordbot.TehuBot.bot;

import com.therealtehu.discordbot.TehuBot.listeners.command.CommandManager;
import com.therealtehu.discordbot.TehuBot.listeners.event.EventListener;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TehuBot {
    private final ShardManager shardManager;
    @Autowired
    public TehuBot(ShardManager shardManager, EventListener eventListener, CommandManager commandManager) {
        this.shardManager = shardManager;
        shardManager.addEventListener(eventListener, commandManager);
    }
}
