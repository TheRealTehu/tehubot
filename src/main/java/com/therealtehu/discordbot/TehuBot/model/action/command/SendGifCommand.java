package com.therealtehu.discordbot.TehuBot.model.action.command;

import com.therealtehu.discordbot.TehuBot.database.model.GuildData;
import com.therealtehu.discordbot.TehuBot.database.model.SendGifData;
import com.therealtehu.discordbot.TehuBot.database.repository.GuildRepository;
import com.therealtehu.discordbot.TehuBot.database.repository.SendGifRepository;
import com.therealtehu.discordbot.TehuBot.service.TenorGifService;
import com.therealtehu.discordbot.TehuBot.service.display.MessageSender;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Component
public class SendGifCommand extends CommandWithFunctionality {
    private static final int MIN_OPTION_LENGTH = 1;
    private static final int MAX_OPTION_LENGTH = 100;
    private static final OptionData PROMPT_OPTION = new OptionData(
            OptionType.STRING,
            OptionName.SEND_GIF_PROMPT_OPTION.getOptionName(),
            "The keyword you would like to use.",
            true).setMinLength(MIN_OPTION_LENGTH).setMaxLength(MAX_OPTION_LENGTH);

    private static final OptionData CHANNEL_OPTION = new OptionData(
            OptionType.CHANNEL,
            OptionName.SEND_GIF_CHANNEL_OPTION.getOptionName(),
            "The channel where the gif should be sent.",
            false).setChannelTypes(ChannelType.TEXT, ChannelType.NEWS);

    private static final String COMMAND_NAME = "sendgif";
    private static final String COMMAND_DESCRIPTION = "Send a gif from Tenor";
    private final TenorGifService tenorGifService;
    private final MessageCreateBuilder messageCreateBuilder;
    private final GuildRepository guildRepository;
    private final SendGifRepository sendGifRepository;

    @Autowired
    public SendGifCommand(TenorGifService tenorGifService, MessageSender messageSender,
                          MessageCreateBuilder messageCreateBuilder, GuildRepository guildRepository,
                          SendGifRepository sendGifRepository) {
        super(COMMAND_NAME, COMMAND_DESCRIPTION, List.of(PROMPT_OPTION, CHANNEL_OPTION), messageSender);
        this.tenorGifService = tenorGifService;
        this.messageCreateBuilder = messageCreateBuilder;
        this.guildRepository = guildRepository;
        this.sendGifRepository = sendGifRepository;
    }

    @Override
    public void executeCommand(SlashCommandInteractionEvent event) {
        event.deferReply().setEphemeral(true).queue();

        String prompt = event.getOption(OptionName.SEND_GIF_PROMPT_OPTION.getOptionName()).getAsString();
        OptionMapping channelOption = event.getOption(OptionName.SEND_GIF_CHANNEL_OPTION.getOptionName());

        TextChannel channel = event.getChannel().asTextChannel();

        if (channelOption != null) {
            channel = channelOption.getAsChannel().asTextChannel();
        }

        Member member = event.getMember();

        if (member.hasPermission(channel, Permission.MESSAGE_SEND)) {
            try {
                saveToDatabase(event, prompt);
                sendGifEmbed(event, prompt, channel);
            } catch (NoSuchElementException e) {
                messageSender.replyToEventEphemeral(event, e.getMessage());
            }
        } else {
            messageSender.replyToEventEphemeral(event, "ERROR: Doesn't have permission to send message to channel!");
        }

    }

    private void sendGifEmbed(SlashCommandInteractionEvent event, String prompt, TextChannel channel) {
        MessageEmbed messageEmbed = tenorGifService.getGifAsEmbed(prompt);
        MessageCreateData messageCreateData = messageCreateBuilder.addEmbeds(messageEmbed).build();
        messageCreateBuilder.clear();

        messageSender.sendMessage(channel, messageCreateData);

        messageSender.sendMessageOnHook(event.getHook(), "Gif sent");
    }

    private void saveToDatabase(SlashCommandInteractionEvent event, String searchedTerm) {
        SendGifData sendGifData = new SendGifData();
        sendGifData.setSearchTerm(searchedTerm);
        Optional<GuildData> guildData = guildRepository.findById(event.getGuild().getIdLong());
        if (guildData.isEmpty()) {
            throw new NoSuchElementException("DATABASE ERROR: Guild not found!");
        }
        sendGifData.setGuild(guildData.get());
        sendGifRepository.save(sendGifData);
    }
}
