package com.therealtehu.discordbot.TehuBot.model.action.command.poll;

import com.therealtehu.discordbot.TehuBot.database.model.GuildData;
import com.therealtehu.discordbot.TehuBot.database.model.poll.PollAnswerData;
import com.therealtehu.discordbot.TehuBot.database.model.poll.PollData;
import com.therealtehu.discordbot.TehuBot.database.repository.GuildRepository;
import com.therealtehu.discordbot.TehuBot.database.repository.poll.PollRepository;
import com.therealtehu.discordbot.TehuBot.model.action.command.OptionName;
import com.therealtehu.discordbot.TehuBot.service.display.MessageSender;
import com.therealtehu.discordbot.TehuBot.service.poll.PollAnswerService;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreatePollCommandTest {
    private CreatePollCommand createPollCommand;
    @Mock
    private MessageSender messageSenderMock;
    @Mock
    private PollAnswerService pollAnswerServiceMock;
    @Mock
    private PollRepository pollRepositoryMock;
    @Mock
    private GuildRepository guildRepositoryMock;
    @Mock
    private SlashCommandInteractionEvent eventMock;
    @Mock
    private Member memberMock;
    @Mock
    private Guild guildMock;
    @Mock
    private GuildData guildDataMock;
    @Mock
    private PollAnswerData pollAnswerDataMock;

    @BeforeEach
    void setup() {
        createPollCommand = new CreatePollCommand(messageSenderMock, pollAnswerServiceMock,
                pollRepositoryMock, guildRepositoryMock);
    }

    @Test
    void executeCommandWhenMemberDoesNotHavePermissionDoesNothing() {
        when(eventMock.getMember()).thenReturn(memberMock);
        when(memberMock.hasPermission(Permission.MESSAGE_SEND_POLLS)).thenReturn(false);

        createPollCommand.executeCommand(eventMock);

        verifyNoInteractions(guildRepositoryMock);
        verifyNoInteractions(messageSenderMock);
        verifyNoInteractions(pollRepositoryMock);
        verifyNoInteractions(pollAnswerServiceMock);
    }

    @Test
    void executeCommandWhenMemberHasPermissionAndGuildNotInDbSendsErrorMessage() {
        when(eventMock.getMember()).thenReturn(memberMock);
        when(memberMock.hasPermission(Permission.MESSAGE_SEND_POLLS)).thenReturn(true);

        when(eventMock.getGuild()).thenReturn(guildMock);
        Long id = 1L;
        when(guildMock.getIdLong()).thenReturn(id);
        when(guildRepositoryMock.findById(id)).thenReturn(Optional.empty());

        createPollCommand.executeCommand(eventMock);

        verify(guildRepositoryMock).findById(id);
        verify(messageSenderMock).reply(eventMock, "DATABASE ERROR: Guild not found!");
        verifyNoInteractions(pollRepositoryMock);
        verifyNoInteractions(pollAnswerServiceMock);
    }

    @Test
    void executeCommandWhenMemberHasPermissionAndGuildIsInDbHasOneAnswerAndAllDefaultValuesSavesProperPollInDbAndSendsPollMessage() {
        when(eventMock.getMember()).thenReturn(memberMock);
        when(memberMock.hasPermission(Permission.MESSAGE_SEND_POLLS)).thenReturn(true);

        when(eventMock.getGuild()).thenReturn(guildMock);
        Long id = 1L;
        when(guildMock.getIdLong()).thenReturn(id);
        when(guildRepositoryMock.findById(id)).thenReturn(Optional.of(guildDataMock));

        when(eventMock.getOption(OptionName.POLL_TIME_LIMIT_OPTION.getOptionName())).thenReturn(null);
        when(eventMock.getOption(OptionName.POLL_NUMBER_OF_VOTES_OPTION.getOptionName())).thenReturn(null);
        when(eventMock.getOption(OptionName.POLL_MINIMUM_ROLE_OPTION.getOptionName())).thenReturn(null);
        when(eventMock.getOption(OptionName.POLL_ANONYMOUS_OPTION.getOptionName())).thenReturn(null);

        OptionMapping mockDescriptionOptionMapping = Mockito.mock(OptionMapping.class);
        when(mockDescriptionOptionMapping.getAsString()).thenReturn("Poll description");
        when(eventMock.getOption(OptionName.POLL_DESCRIPTION_OPTION.getOptionName())).thenReturn(mockDescriptionOptionMapping);

        when(guildDataMock.getId()).thenReturn(id);
        when(pollRepositoryMock.findLatestIdForGuild(id)).thenReturn(null);

        PollData expectedPollData = new PollData();
        expectedPollData.setPublicId("1-1");
        expectedPollData.setGuild(guildDataMock);
        expectedPollData.setPollDescription("Poll description");
        expectedPollData.setAnonymous(false);
        expectedPollData.setDeadLine(null);
        expectedPollData.setNumberOfVotesPerMember(1);
        expectedPollData.setMinimumRole(null);

        when(pollAnswerServiceMock.saveAnswers(eventMock, expectedPollData)).thenReturn(List.of(pollAnswerDataMock));
        when(pollAnswerDataMock.getAnswerEmoji()).thenReturn("Emoji");
        when(pollAnswerDataMock.getAnswerText()).thenReturn("Answer text");


        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("__poll id:__ ").append("1-1").append("\n")
                .append("**Rules: **").append("\n")
                .append("Number of votes per user: ").append("1").append("\n")
                .append("Minimum role to participate: ").append("Anyone").append("\n")
                .append("Is vote anonymous: ").append("No").append("\n")
                .append("Poll ends: ").append("when manually closed").append("\n").append("\n")
                .append("**__").append("Poll description").append("__**").append("\n").append("\n")
                .append("Emoji").append(" ").append("Answer text").append("\n");
        String expectedText = stringBuilder.toString();

        createPollCommand.executeCommand(eventMock);

        verify(guildRepositoryMock).findById(id);
        verify(pollRepositoryMock).findLatestIdForGuild(id);
        verify(pollRepositoryMock).save(expectedPollData);
        verify(pollAnswerServiceMock).saveAnswers(eventMock, expectedPollData);
        verify(messageSenderMock).reply(eventMock, expectedText);
    }

    @Test
    void executeCommandWhenMemberHasPermissionAndGuildIsInDbHasOneAnswerAndHasEndTimeSavesProperPollInDbAndSendsPollMessage() {
        when(eventMock.getMember()).thenReturn(memberMock);
        when(memberMock.hasPermission(Permission.MESSAGE_SEND_POLLS)).thenReturn(true);

        when(eventMock.getGuild()).thenReturn(guildMock);
        Long id = 1L;
        when(guildMock.getIdLong()).thenReturn(id);
        when(guildRepositoryMock.findById(id)).thenReturn(Optional.of(guildDataMock));

        OptionMapping mockTimeLimitOptionMapping = Mockito.mock(OptionMapping.class);
        when(mockTimeLimitOptionMapping.getAsString()).thenReturn("2024-05-05 15:00");
        when(eventMock.getTimeCreated())
                .thenReturn(OffsetDateTime.of(LocalDateTime.of(2024, 5, 5, 13, 14),
                        ZoneOffset.ofHours(1)));
        when(eventMock.getOption(OptionName.POLL_TIME_LIMIT_OPTION.getOptionName())).thenReturn(mockTimeLimitOptionMapping);

        when(eventMock.getOption(OptionName.POLL_NUMBER_OF_VOTES_OPTION.getOptionName())).thenReturn(null);
        when(eventMock.getOption(OptionName.POLL_MINIMUM_ROLE_OPTION.getOptionName())).thenReturn(null);
        when(eventMock.getOption(OptionName.POLL_ANONYMOUS_OPTION.getOptionName())).thenReturn(null);

        OptionMapping mockDescriptionOptionMapping = Mockito.mock(OptionMapping.class);
        when(mockDescriptionOptionMapping.getAsString()).thenReturn("Poll description");
        when(eventMock.getOption(OptionName.POLL_DESCRIPTION_OPTION.getOptionName())).thenReturn(mockDescriptionOptionMapping);

        when(guildDataMock.getId()).thenReturn(id);
        when(pollRepositoryMock.findLatestIdForGuild(id)).thenReturn(null);

        PollData expectedPollData = new PollData();
        expectedPollData.setPublicId("1-1");
        expectedPollData.setGuild(guildDataMock);
        expectedPollData.setPollDescription("Poll description");
        expectedPollData.setAnonymous(false);
        expectedPollData.setDeadLine(OffsetDateTime.of(LocalDateTime.of(2024, 5, 5, 15, 0),
                ZoneOffset.ofHours(1)));
        expectedPollData.setNumberOfVotesPerMember(1);
        expectedPollData.setMinimumRole(null);

        when(pollAnswerServiceMock.saveAnswers(eventMock, expectedPollData)).thenReturn(List.of(pollAnswerDataMock));
        when(pollAnswerDataMock.getAnswerEmoji()).thenReturn("Emoji");
        when(pollAnswerDataMock.getAnswerText()).thenReturn("Answer text");


        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("__poll id:__ ").append("1-1").append("\n")
                .append("**Rules: **").append("\n")
                .append("Number of votes per user: ").append("1").append("\n")
                .append("Minimum role to participate: ").append("Anyone").append("\n")
                .append("Is vote anonymous: ").append("No").append("\n")
                .append("Poll ends: ").append("2024-05-05 15:00").append("\n").append("\n")
                .append("**__").append("Poll description").append("__**").append("\n").append("\n")
                .append("Emoji").append(" ").append("Answer text").append("\n");
        String expectedText = stringBuilder.toString();

        createPollCommand.executeCommand(eventMock);

        verify(guildRepositoryMock).findById(id);
        verify(pollRepositoryMock).findLatestIdForGuild(id);
        verify(pollRepositoryMock).save(expectedPollData);
        verify(pollAnswerServiceMock).saveAnswers(eventMock, expectedPollData);
        verify(messageSenderMock).reply(eventMock, expectedText);
    }

    @Test
    void executeCommandWhenMemberHasPermissionAndGuildIsInDbHasOneAnswerAndHasEndTimeFormattedBadlyDoesntSavePollInDbAndSendsErrorMessage() {
        when(eventMock.getMember()).thenReturn(memberMock);
        when(memberMock.hasPermission(Permission.MESSAGE_SEND_POLLS)).thenReturn(true);

        when(eventMock.getGuild()).thenReturn(guildMock);
        Long id = 1L;
        when(guildMock.getIdLong()).thenReturn(id);
        when(guildRepositoryMock.findById(id)).thenReturn(Optional.of(guildDataMock));

        OptionMapping mockTimeLimitOptionMapping = Mockito.mock(OptionMapping.class);
        when(mockTimeLimitOptionMapping.getAsString()).thenReturn("2024.05.05 15:00");
        when(eventMock.getOption(OptionName.POLL_TIME_LIMIT_OPTION.getOptionName())).thenReturn(mockTimeLimitOptionMapping);

        createPollCommand.executeCommand(eventMock);

        verify(guildRepositoryMock).findById(id);
        verify(pollRepositoryMock, times(0)).save(any());
        verify(pollAnswerServiceMock, times(0)).saveAnswers(eq(eventMock), any());
        verify(messageSenderMock).reply(eventMock, "OPTION ERROR: Invalid time limit format!");
    }

    @Test
    void executeCommandWhenMemberHasPermissionAndGuildIsInDbHasOneAnswerAndHasNumberOfVotesOptionSavesProperPollInDbAndSendsPollMessage() {
        when(eventMock.getMember()).thenReturn(memberMock);
        when(memberMock.hasPermission(Permission.MESSAGE_SEND_POLLS)).thenReturn(true);

        when(eventMock.getGuild()).thenReturn(guildMock);
        Long id = 1L;
        when(guildMock.getIdLong()).thenReturn(id);
        when(guildRepositoryMock.findById(id)).thenReturn(Optional.of(guildDataMock));

        when(eventMock.getOption(OptionName.POLL_TIME_LIMIT_OPTION.getOptionName())).thenReturn(null);

        OptionMapping mockNumberOfVotesOption = Mockito.mock(OptionMapping.class);
        when(mockNumberOfVotesOption.getAsInt()).thenReturn(2);
        when(eventMock.getOption(OptionName.POLL_NUMBER_OF_VOTES_OPTION.getOptionName())).thenReturn(mockNumberOfVotesOption);

        when(eventMock.getOption(OptionName.POLL_MINIMUM_ROLE_OPTION.getOptionName())).thenReturn(null);
        when(eventMock.getOption(OptionName.POLL_ANONYMOUS_OPTION.getOptionName())).thenReturn(null);

        OptionMapping mockDescriptionOptionMapping = Mockito.mock(OptionMapping.class);
        when(mockDescriptionOptionMapping.getAsString()).thenReturn("Poll description");
        when(eventMock.getOption(OptionName.POLL_DESCRIPTION_OPTION.getOptionName())).thenReturn(mockDescriptionOptionMapping);

        when(guildDataMock.getId()).thenReturn(id);
        when(pollRepositoryMock.findLatestIdForGuild(id)).thenReturn(null);

        PollData expectedPollData = new PollData();
        expectedPollData.setPublicId("1-1");
        expectedPollData.setGuild(guildDataMock);
        expectedPollData.setPollDescription("Poll description");
        expectedPollData.setAnonymous(false);
        expectedPollData.setDeadLine(null);
        expectedPollData.setNumberOfVotesPerMember(2);
        expectedPollData.setMinimumRole(null);

        when(pollAnswerServiceMock.saveAnswers(eventMock, expectedPollData)).thenReturn(List.of(pollAnswerDataMock));
        when(pollAnswerDataMock.getAnswerEmoji()).thenReturn("Emoji");
        when(pollAnswerDataMock.getAnswerText()).thenReturn("Answer text");


        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("__poll id:__ ").append("1-1").append("\n")
                .append("**Rules: **").append("\n")
                .append("Number of votes per user: ").append("2").append("\n")
                .append("Minimum role to participate: ").append("Anyone").append("\n")
                .append("Is vote anonymous: ").append("No").append("\n")
                .append("Poll ends: ").append("when manually closed").append("\n").append("\n")
                .append("**__").append("Poll description").append("__**").append("\n").append("\n")
                .append("Emoji").append(" ").append("Answer text").append("\n");
        String expectedText = stringBuilder.toString();

        createPollCommand.executeCommand(eventMock);

        verify(guildRepositoryMock).findById(id);
        verify(pollRepositoryMock).findLatestIdForGuild(id);
        verify(pollRepositoryMock).save(expectedPollData);
        verify(pollAnswerServiceMock).saveAnswers(eventMock, expectedPollData);
        verify(messageSenderMock).reply(eventMock, expectedText);
    }

    @Test
    void executeCommandWhenMemberHasPermissionAndGuildIsInDbHasOneAnswerAndHasMinRoleSavesProperPollInDbAndSendsPollMessage() {
        when(eventMock.getMember()).thenReturn(memberMock);
        when(memberMock.hasPermission(Permission.MESSAGE_SEND_POLLS)).thenReturn(true);

        when(eventMock.getGuild()).thenReturn(guildMock);
        Long id = 1L;
        when(guildMock.getIdLong()).thenReturn(id);
        when(guildRepositoryMock.findById(id)).thenReturn(Optional.of(guildDataMock));

        when(eventMock.getOption(OptionName.POLL_TIME_LIMIT_OPTION.getOptionName())).thenReturn(null);
        when(eventMock.getOption(OptionName.POLL_NUMBER_OF_VOTES_OPTION.getOptionName())).thenReturn(null);

        Role mockRole = Mockito.mock(Role.class);
        OptionMapping mockMinRoleOption = Mockito.mock(OptionMapping.class);
        when(mockMinRoleOption.getAsRole()).thenReturn(mockRole);
        when(mockRole.getName()).thenReturn("Admin");

        when(eventMock.getOption(OptionName.POLL_MINIMUM_ROLE_OPTION.getOptionName())).thenReturn(mockMinRoleOption);
        when(eventMock.getOption(OptionName.POLL_ANONYMOUS_OPTION.getOptionName())).thenReturn(null);

        OptionMapping mockDescriptionOptionMapping = Mockito.mock(OptionMapping.class);
        when(mockDescriptionOptionMapping.getAsString()).thenReturn("Poll description");
        when(eventMock.getOption(OptionName.POLL_DESCRIPTION_OPTION.getOptionName())).thenReturn(mockDescriptionOptionMapping);

        when(guildDataMock.getId()).thenReturn(id);
        when(pollRepositoryMock.findLatestIdForGuild(id)).thenReturn(null);

        PollData expectedPollData = new PollData();
        expectedPollData.setPublicId("1-1");
        expectedPollData.setGuild(guildDataMock);
        expectedPollData.setPollDescription("Poll description");
        expectedPollData.setAnonymous(false);
        expectedPollData.setDeadLine(null);
        expectedPollData.setNumberOfVotesPerMember(1);
        expectedPollData.setMinimumRole("Admin");

        when(pollAnswerServiceMock.saveAnswers(eventMock, expectedPollData)).thenReturn(List.of(pollAnswerDataMock));
        when(pollAnswerDataMock.getAnswerEmoji()).thenReturn("Emoji");
        when(pollAnswerDataMock.getAnswerText()).thenReturn("Answer text");


        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("__poll id:__ ").append("1-1").append("\n")
                .append("**Rules: **").append("\n")
                .append("Number of votes per user: ").append("1").append("\n")
                .append("Minimum role to participate: ").append("Admin").append("\n")
                .append("Is vote anonymous: ").append("No").append("\n")
                .append("Poll ends: ").append("when manually closed").append("\n").append("\n")
                .append("**__").append("Poll description").append("__**").append("\n").append("\n")
                .append("Emoji").append(" ").append("Answer text").append("\n");
        String expectedText = stringBuilder.toString();

        createPollCommand.executeCommand(eventMock);

        verify(guildRepositoryMock).findById(id);
        verify(pollRepositoryMock).findLatestIdForGuild(id);
        verify(pollRepositoryMock).save(expectedPollData);
        verify(pollAnswerServiceMock).saveAnswers(eventMock, expectedPollData);
        verify(messageSenderMock).reply(eventMock, expectedText);
    }

    @Test
    void executeCommandWhenMemberHasPermissionAndGuildIsInDbHasOneAnswerAndIsAnonymousSavesProperPollInDbAndSendsPollMessage() {
        when(eventMock.getMember()).thenReturn(memberMock);
        when(memberMock.hasPermission(Permission.MESSAGE_SEND_POLLS)).thenReturn(true);

        when(eventMock.getGuild()).thenReturn(guildMock);
        Long id = 1L;
        when(guildMock.getIdLong()).thenReturn(id);
        when(guildRepositoryMock.findById(id)).thenReturn(Optional.of(guildDataMock));

        when(eventMock.getOption(OptionName.POLL_TIME_LIMIT_OPTION.getOptionName())).thenReturn(null);
        when(eventMock.getOption(OptionName.POLL_NUMBER_OF_VOTES_OPTION.getOptionName())).thenReturn(null);
        when(eventMock.getOption(OptionName.POLL_MINIMUM_ROLE_OPTION.getOptionName())).thenReturn(null);

        OptionMapping mockAnonymousOption = Mockito.mock(OptionMapping.class);
        when(mockAnonymousOption.getAsBoolean()).thenReturn(true);
        when(eventMock.getOption(OptionName.POLL_ANONYMOUS_OPTION.getOptionName())).thenReturn(mockAnonymousOption);

        OptionMapping mockDescriptionOptionMapping = Mockito.mock(OptionMapping.class);
        when(mockDescriptionOptionMapping.getAsString()).thenReturn("Poll description");
        when(eventMock.getOption(OptionName.POLL_DESCRIPTION_OPTION.getOptionName())).thenReturn(mockDescriptionOptionMapping);

        when(guildDataMock.getId()).thenReturn(id);
        when(pollRepositoryMock.findLatestIdForGuild(id)).thenReturn(null);

        PollData expectedPollData = new PollData();
        expectedPollData.setPublicId("1-1");
        expectedPollData.setGuild(guildDataMock);
        expectedPollData.setPollDescription("Poll description");
        expectedPollData.setAnonymous(true);
        expectedPollData.setDeadLine(null);
        expectedPollData.setNumberOfVotesPerMember(1);
        expectedPollData.setMinimumRole(null);

        when(pollAnswerServiceMock.saveAnswers(eventMock, expectedPollData)).thenReturn(List.of(pollAnswerDataMock));
        when(pollAnswerDataMock.getAnswerEmoji()).thenReturn("Emoji");
        when(pollAnswerDataMock.getAnswerText()).thenReturn("Answer text");

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("__poll id:__ ").append("1-1").append("\n")
                .append("**Rules: **").append("\n")
                .append("Number of votes per user: ").append("1").append("\n")
                .append("Minimum role to participate: ").append("Anyone").append("\n")
                .append("Is vote anonymous: ").append("Yes").append("\n")
                .append("Poll ends: ").append("when manually closed").append("\n").append("\n")
                .append("**__").append("Poll description").append("__**").append("\n").append("\n")
                .append("Emoji").append(" ").append("Answer text").append("\n");
        String expectedText = stringBuilder.toString();

        createPollCommand.executeCommand(eventMock);

        verify(guildRepositoryMock).findById(id);
        verify(pollRepositoryMock).findLatestIdForGuild(id);
        verify(pollRepositoryMock).save(expectedPollData);
        verify(pollAnswerServiceMock).saveAnswers(eventMock, expectedPollData);
        verify(messageSenderMock).reply(eventMock, expectedText);
    }

    @Test
    void executeCommandWhenMemberHasPermissionAndGuildIsInDbHasOneAnswerAndDbAlreadyHasPollsGivesIncrementedPollId() {
        when(eventMock.getMember()).thenReturn(memberMock);
        when(memberMock.hasPermission(Permission.MESSAGE_SEND_POLLS)).thenReturn(true);

        when(eventMock.getGuild()).thenReturn(guildMock);
        Long id = 1L;
        when(guildMock.getIdLong()).thenReturn(id);
        when(guildRepositoryMock.findById(id)).thenReturn(Optional.of(guildDataMock));

        when(eventMock.getOption(OptionName.POLL_TIME_LIMIT_OPTION.getOptionName())).thenReturn(null);
        when(eventMock.getOption(OptionName.POLL_NUMBER_OF_VOTES_OPTION.getOptionName())).thenReturn(null);
        when(eventMock.getOption(OptionName.POLL_MINIMUM_ROLE_OPTION.getOptionName())).thenReturn(null);
        when(eventMock.getOption(OptionName.POLL_ANONYMOUS_OPTION.getOptionName())).thenReturn(null);

        OptionMapping mockDescriptionOptionMapping = Mockito.mock(OptionMapping.class);
        when(mockDescriptionOptionMapping.getAsString()).thenReturn("Poll description");
        when(eventMock.getOption(OptionName.POLL_DESCRIPTION_OPTION.getOptionName())).thenReturn(mockDescriptionOptionMapping);

        when(guildDataMock.getId()).thenReturn(id);
        when(pollRepositoryMock.findLatestIdForGuild(id)).thenReturn(1L);

        PollData expectedPollData = new PollData();
        expectedPollData.setPublicId("1-2");
        expectedPollData.setGuild(guildDataMock);
        expectedPollData.setPollDescription("Poll description");
        expectedPollData.setAnonymous(false);
        expectedPollData.setDeadLine(null);
        expectedPollData.setNumberOfVotesPerMember(1);
        expectedPollData.setMinimumRole(null);

        when(pollAnswerServiceMock.saveAnswers(eventMock, expectedPollData)).thenReturn(List.of(pollAnswerDataMock));
        when(pollAnswerDataMock.getAnswerEmoji()).thenReturn("Emoji");
        when(pollAnswerDataMock.getAnswerText()).thenReturn("Answer text");


        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("__poll id:__ ").append("1-2").append("\n")
                .append("**Rules: **").append("\n")
                .append("Number of votes per user: ").append("1").append("\n")
                .append("Minimum role to participate: ").append("Anyone").append("\n")
                .append("Is vote anonymous: ").append("No").append("\n")
                .append("Poll ends: ").append("when manually closed").append("\n").append("\n")
                .append("**__").append("Poll description").append("__**").append("\n").append("\n")
                .append("Emoji").append(" ").append("Answer text").append("\n");
        String expectedText = stringBuilder.toString();

        createPollCommand.executeCommand(eventMock);

        verify(guildRepositoryMock).findById(id);
        verify(pollRepositoryMock).findLatestIdForGuild(id);
        verify(pollRepositoryMock).save(expectedPollData);
        verify(pollAnswerServiceMock).saveAnswers(eventMock, expectedPollData);
        verify(messageSenderMock).reply(eventMock, expectedText);
    }

    @Test
    void executeCommandWhenMemberHasPermissionAndGuildIsInDbHasTwoAnswerAndAllDefaultValuesSavesProperPollInDbAndSendsPollMessage() {
        when(eventMock.getMember()).thenReturn(memberMock);
        when(memberMock.hasPermission(Permission.MESSAGE_SEND_POLLS)).thenReturn(true);

        when(eventMock.getGuild()).thenReturn(guildMock);
        Long id = 1L;
        when(guildMock.getIdLong()).thenReturn(id);
        when(guildRepositoryMock.findById(id)).thenReturn(Optional.of(guildDataMock));

        when(eventMock.getOption(OptionName.POLL_TIME_LIMIT_OPTION.getOptionName())).thenReturn(null);
        when(eventMock.getOption(OptionName.POLL_NUMBER_OF_VOTES_OPTION.getOptionName())).thenReturn(null);
        when(eventMock.getOption(OptionName.POLL_MINIMUM_ROLE_OPTION.getOptionName())).thenReturn(null);
        when(eventMock.getOption(OptionName.POLL_ANONYMOUS_OPTION.getOptionName())).thenReturn(null);

        OptionMapping mockDescriptionOptionMapping = Mockito.mock(OptionMapping.class);
        when(mockDescriptionOptionMapping.getAsString()).thenReturn("Poll description");
        when(eventMock.getOption(OptionName.POLL_DESCRIPTION_OPTION.getOptionName())).thenReturn(mockDescriptionOptionMapping);

        when(guildDataMock.getId()).thenReturn(id);
        when(pollRepositoryMock.findLatestIdForGuild(id)).thenReturn(null);

        PollData expectedPollData = new PollData();
        expectedPollData.setPublicId("1-1");
        expectedPollData.setGuild(guildDataMock);
        expectedPollData.setPollDescription("Poll description");
        expectedPollData.setAnonymous(false);
        expectedPollData.setDeadLine(null);
        expectedPollData.setNumberOfVotesPerMember(1);
        expectedPollData.setMinimumRole(null);

        when(pollAnswerDataMock.getAnswerEmoji()).thenReturn("Emoji");
        when(pollAnswerDataMock.getAnswerText()).thenReturn("Answer text");

        PollAnswerData mockPollAnswerData2 = Mockito.mock(PollAnswerData.class);
        when(mockPollAnswerData2.getAnswerEmoji()).thenReturn("Emoji2");
        when(mockPollAnswerData2.getAnswerText()).thenReturn("Second answer");

        when(pollAnswerServiceMock.saveAnswers(eventMock, expectedPollData))
                .thenReturn(List.of(pollAnswerDataMock, mockPollAnswerData2));

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("__poll id:__ ").append("1-1").append("\n")
                .append("**Rules: **").append("\n")
                .append("Number of votes per user: ").append("1").append("\n")
                .append("Minimum role to participate: ").append("Anyone").append("\n")
                .append("Is vote anonymous: ").append("No").append("\n")
                .append("Poll ends: ").append("when manually closed").append("\n").append("\n")
                .append("**__").append("Poll description").append("__**").append("\n").append("\n")
                .append("Emoji").append(" ").append("Answer text").append("\n")
                .append("Emoji2").append(" ").append("Second answer").append("\n");
        String expectedText = stringBuilder.toString();

        createPollCommand.executeCommand(eventMock);

        verify(guildRepositoryMock).findById(id);
        verify(pollRepositoryMock).findLatestIdForGuild(id);
        verify(pollRepositoryMock).save(expectedPollData);
        verify(pollAnswerServiceMock).saveAnswers(eventMock, expectedPollData);
        verify(messageSenderMock).reply(eventMock, expectedText);
    }
}