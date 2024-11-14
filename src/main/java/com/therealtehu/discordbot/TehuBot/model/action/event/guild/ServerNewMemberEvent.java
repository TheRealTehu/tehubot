package com.therealtehu.discordbot.TehuBot.model.action.event.guild;

import com.therealtehu.discordbot.TehuBot.database.model.GuildData;
import com.therealtehu.discordbot.TehuBot.database.repository.GuildRepository;
import com.therealtehu.discordbot.TehuBot.model.action.event.EventHandler;
import com.therealtehu.discordbot.TehuBot.model.action.event.EventName;
import com.therealtehu.discordbot.TehuBot.service.MemberService;
import com.therealtehu.discordbot.TehuBot.service.TenorGifService;
import com.therealtehu.discordbot.TehuBot.service.display.MessageSender;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ServerNewMemberEvent extends EventHandler {
    private final TenorGifService tenorGifService;
    private final MemberService memberService;
    private final GuildRepository guildRepository;

    @Autowired
    public ServerNewMemberEvent(TenorGifService tenorGifService, MessageSender messageSender,
                                MemberService memberService, GuildRepository guildRepository) {
        super(EventName.SERVER_NEW_MEMBER.getEventName(), messageSender);
        this.tenorGifService = tenorGifService;
        this.memberService = memberService;
        this.guildRepository = guildRepository;
    }

    @Override
    public void handle(Event event) {
        if (event instanceof GuildMemberJoinEvent guildMemberJoinEvent) {
            TextChannel channel = guildMemberJoinEvent.getGuild().getDefaultChannel().asTextChannel();

            Optional<GuildData> optionalGuildData = guildRepository.findById(guildMemberJoinEvent.getGuild().getIdLong());
            if (optionalGuildData.isPresent()) {
                Member member = guildMemberJoinEvent.getMember();
                String memberAsMention = member.getAsMention();
                channel = guildMemberJoinEvent.getGuild().getTextChannelById(optionalGuildData.get().getBotChatChannelId());
                String message = memberAsMention + " just joined the channel! Say hi everyone!";
                MessageEmbed gif = tenorGifService.getGifAsEmbed("Welcome");

                memberService.addNewMemberIfNotExists(member);

                messageSender.sendMessageWithMessageEmbed(channel, message, gif);
            } else {
                messageSender.sendMessage(channel, "ERROR: Guild not found in database!");
            }
        }
    }
}
