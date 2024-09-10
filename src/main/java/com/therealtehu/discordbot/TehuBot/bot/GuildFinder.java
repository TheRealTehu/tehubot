package com.therealtehu.discordbot.TehuBot.bot;

import net.dv8tion.jda.api.entities.Guild;

import java.util.Optional;

public interface GuildFinder {
    Optional<Guild> findGuildBy(long guildId);
}
