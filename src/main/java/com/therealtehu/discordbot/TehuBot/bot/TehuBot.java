package com.therealtehu.discordbot.TehuBot.bot;

import com.therealtehu.discordbot.TehuBot.listeners.command.CommandManager;
import com.therealtehu.discordbot.TehuBot.listeners.event.EventListener;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TehuBot {
    private final ShardManager shardManager;
    @Autowired
    public TehuBot(@Value("${discord.bot.token}") String token, EventListener eventListener, CommandManager commandManager) {
        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(token);
        builder.enableIntents(GatewayIntent.GUILD_MEMBERS);
        builder.addEventListeners(eventListener, commandManager);
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.playing("Useful Little Bot"));
        shardManager = builder.build();
    }
}
