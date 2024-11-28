package com.therealtehu.discordbot.TehuBot.model.action.command;

import com.therealtehu.discordbot.TehuBot.database.model.GetWikiData;
import com.therealtehu.discordbot.TehuBot.database.model.GuildData;
import com.therealtehu.discordbot.TehuBot.database.repository.GetWikiRepository;
import com.therealtehu.discordbot.TehuBot.database.repository.GuildRepository;
import com.therealtehu.discordbot.TehuBot.service.WikiArticleService;
import com.therealtehu.discordbot.TehuBot.service.WikiTextConverter;
import com.therealtehu.discordbot.TehuBot.service.display.MessageSender;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.awt.*;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetWikiCommandTest {
    private GetWikiCommand getWikiCommand;
    @Mock
    private MessageSender messageSenderMock;
    @Mock
    private WikiArticleService wikiArticleServiceMock;
    @Mock
    private GetWikiRepository getWikiRepositoryMock;
    @Mock
    private GuildRepository guildRepositoryMock;
    @Mock
    private SlashCommandInteractionEvent eventMock;
    @Mock
    private Member memberMock;
    @Mock
    private OptionMapping optionMappingMock;
    @Mock
    private InteractionHook interactionHookMock;
    @Mock
    private ReplyCallbackAction replyCallbackActionMock;
    @Mock
    private Guild guildMock;
    @Mock
    private GuildData guildDataMock;

    @BeforeEach
    void setup() {
        getWikiCommand = new GetWikiCommand(wikiArticleServiceMock, messageSenderMock,
                getWikiRepositoryMock, guildRepositoryMock);
    }

    @Test
    void executeCommandWhenMemberHasPermissionAndNormalLengthArticleFoundAndGuildIsInDbSendsArticleMessageEmbedAndSavesToDb() {
        when(eventMock.getMember()).thenReturn(memberMock);
        when(memberMock.hasPermission(Permission.MESSAGE_SEND)).thenReturn(true);

        when(eventMock.deferReply()).thenReturn(replyCallbackActionMock);

        when(eventMock.getOption(OptionName.GET_WIKI_TITLE_OPTION.getOptionName())).thenReturn(optionMappingMock);
        when(optionMappingMock.getAsString()).thenReturn("Wiki Title");
        when(wikiArticleServiceMock.getWikiArticle("Wiki Title")).thenReturn("Wiki text from the API");

        when(eventMock.getGuild()).thenReturn(guildMock);
        when(guildMock.getIdLong()).thenReturn(1L);
        when(guildRepositoryMock.findById(1L)).thenReturn(Optional.of(guildDataMock));

        when(eventMock.getHook()).thenReturn(interactionHookMock);

        try (MockedStatic<WikiTextConverter> mockWikiTextConverter = Mockito.mockStatic(WikiTextConverter.class)) {
            mockWikiTextConverter.when(() -> WikiTextConverter.convertToPlainText(anyString()))
                    .thenReturn("Wiki text from the API");

            GetWikiData expectedWikiData = new GetWikiData();
            expectedWikiData.setSearchedTopic("Wiki Title");
            expectedWikiData.setGuild(guildDataMock);

            MessageEmbed expectedMessage = new EmbedBuilder()
                    .setTitle("Wiki article: " + "Wiki Title")
                    .setColor(Color.BLUE)
                    .setDescription("Wiki text from the API")
                    .build();

            getWikiCommand.executeCommand(eventMock);

            verify(eventMock.deferReply()).queue();
            verify(getWikiRepositoryMock).save(expectedWikiData);
            mockWikiTextConverter.verify(() -> WikiTextConverter.convertToPlainText(anyString()), times(1));
            verify(messageSenderMock).sendMessageEmbedOnHook(eventMock.getHook(), expectedMessage);
        }
    }

    @Test
    void executeCommandWhenMemberHasPermissionAndTooLongArticleFoundAndGuildIsInDbSendsShortenedMessageAndSavesToDb() {
        when(eventMock.getMember()).thenReturn(memberMock);
        when(memberMock.hasPermission(Permission.MESSAGE_SEND)).thenReturn(true);

        when(eventMock.deferReply()).thenReturn(replyCallbackActionMock);

        when(eventMock.getOption(OptionName.GET_WIKI_TITLE_OPTION.getOptionName())).thenReturn(optionMappingMock);
        when(optionMappingMock.getAsString()).thenReturn("Wiki Title");
        when(wikiArticleServiceMock.getWikiArticle("Wiki Title")).thenReturn(textOf4096Characters);

        when(eventMock.getGuild()).thenReturn(guildMock);
        when(guildMock.getIdLong()).thenReturn(1L);
        when(guildRepositoryMock.findById(1L)).thenReturn(Optional.of(guildDataMock));

        try (MockedStatic<WikiTextConverter> mockWikiTextConverter = Mockito.mockStatic(WikiTextConverter.class)) {
            mockWikiTextConverter.when(() -> WikiTextConverter.convertToPlainText(anyString()))
                    .thenReturn(textOf4096Characters);

            GetWikiData expectedWikiData = new GetWikiData();
            expectedWikiData.setSearchedTopic("Wiki Title");
            expectedWikiData.setGuild(guildDataMock);

            String shortenedText = textOf4096Characters.substring(0, 4093) + "...";

            MessageEmbed expectedMessage = new EmbedBuilder()
                    .setTitle("Wiki article: " + "Wiki Title")
                    .setColor(Color.BLUE)
                    .setDescription(shortenedText)
                    .build();

            getWikiCommand.executeCommand(eventMock);

            verify(eventMock.deferReply()).queue();
            verify(getWikiRepositoryMock).save(expectedWikiData);
            mockWikiTextConverter.verify(() -> WikiTextConverter.convertToPlainText(anyString()), times(1));
            verify(messageSenderMock).sendMessageEmbedOnHook(eventMock.getHook(), expectedMessage);
        }
    }

    @Test
    void executeCommandWhenMemberHasPermissionAndApiTooBusySendsPleaseWaitMessageAndDoesNotSaveToDb() {
        when(eventMock.getMember()).thenReturn(memberMock);
        when(memberMock.hasPermission(Permission.MESSAGE_SEND)).thenReturn(true);

        when(eventMock.deferReply()).thenReturn(replyCallbackActionMock);

        when(eventMock.getOption(OptionName.GET_WIKI_TITLE_OPTION.getOptionName())).thenReturn(optionMappingMock);
        when(optionMappingMock.getAsString()).thenReturn("Wiki Title");
        when(wikiArticleServiceMock.getWikiArticle("Wiki Title")).thenReturn("<!-- API NOT AVAILABLE -->");

        try (MockedStatic<WikiTextConverter> mockWikiTextConverter = Mockito.mockStatic(WikiTextConverter.class)) {
            mockWikiTextConverter.when(() -> WikiTextConverter.convertToPlainText(anyString()))
                    .thenReturn("<!-- API NOT AVAILABLE -->");

            String expectedMessage = "Wiki API too busy, please try again later!";

            getWikiCommand.executeCommand(eventMock);

            verify(eventMock.deferReply()).queue();
            mockWikiTextConverter.verifyNoInteractions();
            verifyNoInteractions(guildRepositoryMock);
            verifyNoInteractions(getWikiRepositoryMock);
            verify(messageSenderMock).reply(eventMock, expectedMessage);
        }
    }

    @Test
    void executeCommandWhenMemberHasPermissionAndApiErrorOccursSendsErrorMessageAndDoesNotSaveToDb() {
        when(eventMock.getMember()).thenReturn(memberMock);
        when(memberMock.hasPermission(Permission.MESSAGE_SEND)).thenReturn(true);

        when(eventMock.deferReply()).thenReturn(replyCallbackActionMock);

        when(eventMock.getOption(OptionName.GET_WIKI_TITLE_OPTION.getOptionName())).thenReturn(optionMappingMock);
        when(optionMappingMock.getAsString()).thenReturn("Wiki Title");
        when(wikiArticleServiceMock.getWikiArticle("Wiki Title")).thenReturn("ERROR: Could not reach Wikipedia!");

        try (MockedStatic<WikiTextConverter> mockWikiTextConverter = Mockito.mockStatic(WikiTextConverter.class)) {

            getWikiCommand.executeCommand(eventMock);

            verify(eventMock.deferReply()).queue();
            mockWikiTextConverter.verifyNoInteractions();
            verifyNoInteractions(guildRepositoryMock);
            verifyNoInteractions(getWikiRepositoryMock);
            verify(messageSenderMock).reply(eventMock, "ERROR: Could not reach Wikipedia!");
        }
    }

    @Test
    void executeCommandWhenMemberHasPermissionAndNormalLengthArticleFoundAndGuildIsNotInDbSendsErrorMessageAndDoesNotSaveToDb() {
        when(eventMock.getMember()).thenReturn(memberMock);
        when(memberMock.hasPermission(Permission.MESSAGE_SEND)).thenReturn(true);

        when(eventMock.deferReply()).thenReturn(replyCallbackActionMock);

        when(eventMock.getOption(OptionName.GET_WIKI_TITLE_OPTION.getOptionName())).thenReturn(optionMappingMock);
        when(optionMappingMock.getAsString()).thenReturn("Wiki Title");
        when(wikiArticleServiceMock.getWikiArticle("Wiki Title")).thenReturn("Wiki text from the API");

        when(eventMock.getGuild()).thenReturn(guildMock);
        when(guildMock.getIdLong()).thenReturn(1L);
        when(guildRepositoryMock.findById(1L)).thenReturn(Optional.empty());

        try (MockedStatic<WikiTextConverter> mockWikiTextConverter = Mockito.mockStatic(WikiTextConverter.class)) {
            mockWikiTextConverter.when(() -> WikiTextConverter.convertToPlainText(anyString()))
                    .thenReturn("Wiki text from the API");

            getWikiCommand.executeCommand(eventMock);

            verify(eventMock.deferReply()).queue();
            verify(guildRepositoryMock).findById(1L);
            verifyNoMoreInteractions(guildRepositoryMock);
            verifyNoMoreInteractions(getWikiRepositoryMock);
            mockWikiTextConverter.verify(() -> WikiTextConverter.convertToPlainText(anyString()), times(1));
            verify(messageSenderMock).reply(eventMock, "DATABASE ERROR: Guild not found!");
        }
    }

    @Test
    void executeCommandWhenMemberDoesNotHavePermissionDoesNothing() {
        when(eventMock.getMember()).thenReturn(memberMock);
        when(memberMock.hasPermission(Permission.MESSAGE_SEND)).thenReturn(false);

        getWikiCommand.executeCommand(eventMock);

        verifyNoInteractions(getWikiRepositoryMock);
        verifyNoInteractions(guildRepositoryMock);
        verifyNoInteractions(wikiArticleServiceMock);
        verifyNoInteractions(messageSenderMock);
    }

    private final String textOf4096Characters = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis ultrices diam " +
            "a pretium pellentesque. Morbi at egestas libero. Curabitur pulvinar ipsum ipsum, quis eleifend turpis " +
            "sagittis sit amet. In et felis nec dui pulvinar tempor vitae sit amet libero. Donec nulla ipsum, pretium " +
            "vel dictum ut, venenatis ut odio. Proin eget libero sed diam consequat semper. Proin eget sapien tortor. " +
            "Nunc tempor condimentum nisi, vel maximus ante pharetra at. Integer vitae egestas erat. Pellentesque dictum" +
            " et felis sed condimentum. Donec a condimentum tortor. Maecenas massa est, ornare quis sagittis a, vestibulum" +
            " at nisl. Proin posuere et diam vel ultricies. Nulla aliquam ac augue eget pulvinar. Proin eget neque et ex" +
            " scelerisque fringilla ut eget nisl. Pellentesque habitant morbi tristique senectus et netus et malesuada " +
            "fames ac turpis egestas. Proin bibendum enim vitae consectetur facilisis. Suspendisse vestibulum gravida " +
            "ultricies. Ut feugiat felis sed ultricies ornare. Aenean vehicula sapien vel tortor iaculis rutrum. " +
            "Pellentesque efficitur eros vitae diam facilisis faucibus. Nulla lacinia cursus mauris quis luctus. Nam " +
            "eget lacinia felis, in elementum augue. Aenean aliquet tristique justo vitae tincidunt. In bibendum eu dui " +
            "ac convallis. Nulla euismod mi sed aliquet tincidunt. Nullam sodales, arcu eget sodales placerat, nulla " +
            "magna dapibus leo, in cursus lectus tortor id ante. Mauris placerat, felis eget malesuada mollis, lorem sem" +
            " eleifend felis, a dictum lacus magna nec nunc. Curabitur viverra ut magna ac rutrum. Maecenas sit amet " +
            "lacus eu urna malesuada tempus. Proin ac lorem erat. Praesent at fermentum mauris. Praesent vitae tincidunt" +
            " tortor. Curabitur aliquet libero nec sapien blandit interdum. Morbi id urna elementum lorem placerat " +
            "dictum. Sed feugiat auctor lectus ac molestie. Vestibulum mi velit, accumsan in elit suscipit, varius " +
            "pulvinar tortor. Vestibulum euismod augue et elit lacinia consectetur. Quisque pulvinar in dolor sed " +
            "fermentum. Suspendisse potenti. Orci varius natoque penatibus et magnis dis parturient montes, nascetur " +
            "ridiculus mus. Vivamus ut libero sed dolor suscipit porttitor ut non sem. Duis malesuada rutrum sem lobortis" +
            " porta. Aenean eu nunc eleifend neque fermentum luctus. Donec venenatis, turpis non auctor posuere, velit " +
            "lacus sollicitudin lectus, eget semper erat justo in elit. Vestibulum vitae quam quis felis lobortis " +
            "vulputate. Etiam rutrum turpis urna, pellentesque malesuada dui dictum in. Donec nulla metus, venenatis ac " +
            "felis quis, pharetra vestibulum dolor. Morbi nec lectus et mi fringilla molestie. Integer vel mi non magna " +
            "pretium congue. Nam pretium tortor in augue tincidunt, non placerat mauris vulputate. Aenean vel dolor diam." +
            " Etiam ut dapibus dui. Proin condimentum quam eget tempus hendrerit. Orci varius natoque penatibus et " +
            "magnis dis parturient montes, nascetur ridiculus mus. Proin sit amet enim sem. Pellentesque venenatis porta" +
            " scelerisque. Aenean pellentesque sapien nec pulvinar pellentesque. Maecenas ultricies lectus libero, et " +
            "viverra odio bibendum sit amet. Praesent sit amet eros vel purus pulvinar interdum. Praesent quis lorem mi. " +
            "Mauris semper ante lacus, consequat fringilla nibh condimentum sed. Etiam risus ipsum, elementum et mattis " +
            "vel, pretium sit amet nisl. Integer quis tristique lacus. Nullam vitae mauris facilisis, blandit ante ac, " +
            "lacinia metus. Aenean ullamcorper pretium tempus. Duis et hendrerit erat. Fusce dolor felis, luctus non " +
            "blandit a, tincidunt vel magna. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per " +
            "inceptos himenaeos. Nulla at mi nec erat pulvinar efficitur condimentum ut dui. Suspendisse condimentum " +
            "neque nec tortor scelerisque finibus. Sed eleifend maximus iaculis. Quisque finibus, lorem in tincidunt " +
            "accumsan, neque arcu pulvinar magna, ut pharetra libero libero in ipsum. Nulla facilisi. Morbi cursus semper" +
            " enim vitae viverra. Phasellus ac risus ac lacus tempus posuere. Class aptent taciti sociosqu ad litora " +
            "torquent per conubia nostra, per inceptos himenaeos. Integer sit amet nibh ultricies, tincidunt lorem sit " +
            "amet, sodales arcu. Etiamb";
}