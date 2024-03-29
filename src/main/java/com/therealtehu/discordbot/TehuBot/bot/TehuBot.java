package com.therealtehu.discordbot.TehuBot.bot;

import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TehuBot {
    private final ShardManager shardManager;
    public TehuBot(@Value("${discord.bot.token}") String token) {
        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(token);
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.playing("Useful Little Bot"));
        shardManager = builder.build();
    }

    public ShardManager getShardManager() {
        return shardManager;
    }
}
