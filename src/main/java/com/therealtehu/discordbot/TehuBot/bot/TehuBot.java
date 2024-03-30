package com.therealtehu.discordbot.TehuBot.bot;

import com.therealtehu.discordbot.TehuBot.listeners.event.EventListener;
import com.therealtehu.discordbot.TehuBot.listeners.event.guild.ServerJoinEvent;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TehuBot {
    private final ShardManager shardManager;
    @Autowired
    public TehuBot(@Value("${discord.bot.token}") String token, ServerJoinEvent serverJoinEvent) {
        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(token);
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.playing("Useful Little Bot"));
        shardManager = builder.build();
        shardManager.addEventListener(new EventListener(serverJoinEvent));
    }

    public ShardManager getShardManager() {
        return shardManager;
    }
}
