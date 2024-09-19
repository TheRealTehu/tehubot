package com.therealtehu.discordbot.TehuBot.model.action.event.guild;

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
import org.springframework.stereotype.Service;

@Service
public class ServerNewMemberEvent extends EventHandler {
    private final TenorGifService tenorGifService;
    private final MemberService memberService;

    @Autowired
    public ServerNewMemberEvent(TenorGifService tenorGifService, MessageSender messageSender, MemberService memberService) {
        super(EventName.SERVER_NEW_MEMBER.getEventName(), messageSender);
        this.tenorGifService = tenorGifService;
        this.memberService = memberService;
    }

    @Override
    public void handle(Event event) {
        if (event instanceof GuildMemberJoinEvent guildMemberJoinEvent) {
            Member member = guildMemberJoinEvent.getMember();
            String memberAsMention = member.getAsMention();
            TextChannel channel = guildMemberJoinEvent.getGuild().getDefaultChannel().asTextChannel();
            String message = memberAsMention + " just joined the channel! Say hi everyone!";
            MessageEmbed gif = tenorGifService.getGifAsEmbed("Welcome");

            memberService.addNewMemberIfNotExists(member);

            messageSender.sendMessageWithMessageEmbed(channel, message, gif);
        }
    }
}
