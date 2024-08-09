package com.therealtehu.discordbot.TehuBot.model.action.command.poll;

import com.therealtehu.discordbot.TehuBot.database.model.GuildData;
import com.therealtehu.discordbot.TehuBot.database.model.poll.PollAnswerData;
import com.therealtehu.discordbot.TehuBot.database.model.poll.PollData;
import com.therealtehu.discordbot.TehuBot.database.repository.GuildRepository;
import com.therealtehu.discordbot.TehuBot.database.repository.poll.PollRepository;
import com.therealtehu.discordbot.TehuBot.model.action.command.OptionName;
import com.therealtehu.discordbot.TehuBot.service.display.MessageSender;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

class CreatePollCommandTest {
    private CreatePollCommand createPollCommand;
    private final MessageSender mockMessageSender = Mockito.mock(MessageSender.class);
    private final PollAnswerHandler mockPollAnswerHandler = Mockito.mock(PollAnswerHandler.class);
    private final PollRepository mockPollRepository = Mockito.mock(PollRepository.class);
    private final GuildRepository mockGuildRepository = Mockito.mock(GuildRepository.class);
    private final SlashCommandInteractionEvent mockEvent = Mockito.mock(SlashCommandInteractionEvent.class);
    private final Guild mockGuild = Mockito.mock(Guild.class);
    private final GuildData mockGuildData = Mockito.mock(GuildData.class);

    private final PollAnswerData mockPollAnswerData = Mockito.mock(PollAnswerData.class);

    @BeforeEach
    void setup() {
        createPollCommand = new CreatePollCommand(mockMessageSender, mockPollAnswerHandler,
                mockPollRepository, mockGuildRepository);
    }

    @Test
    void executeCommandWhenGuildNotInDbSendsErrorMessage() {
        when(mockEvent.getGuild()).thenReturn(mockGuild);
        Long id = 1L;
        when(mockGuild.getIdLong()).thenReturn(id);
        when(mockGuildRepository.findById(id)).thenReturn(Optional.empty());

        createPollCommand.executeCommand(mockEvent);

        verify(mockGuildRepository).findById(id);
        verify(mockMessageSender).replyToEvent(mockEvent, "DATABASE ERROR: Guild not found!");
    }

    @Test
    void executeCommandWhenGuildIsInDbHasOneAnswerAndAllDefaultValuesSavesProperPollInDbAndSendsPollMessage() {
        when(mockEvent.getGuild()).thenReturn(mockGuild);
        Long id = 1L;
        when(mockGuild.getIdLong()).thenReturn(id);
        when(mockGuildRepository.findById(id)).thenReturn(Optional.of(mockGuildData));

        when(mockEvent.getOption(OptionName.POLL_TIME_LIMIT_OPTION.getOptionName())).thenReturn(null);
        when(mockEvent.getOption(OptionName.POLL_NUMBER_OF_VOTES_OPTION.getOptionName())).thenReturn(null);
        when(mockEvent.getOption(OptionName.POLL_MINIMUM_ROLE_OPTION.getOptionName())).thenReturn(null);
        when(mockEvent.getOption(OptionName.POLL_ANONYMOUS_OPTION.getOptionName())).thenReturn(null);

        OptionMapping mockDescriptionOptionMapping = Mockito.mock(OptionMapping.class);
        when(mockDescriptionOptionMapping.getAsString()).thenReturn("Poll description");
        when(mockEvent.getOption(OptionName.POLL_DESCRIPTION_OPTION.getOptionName())).thenReturn(mockDescriptionOptionMapping);

        when(mockGuildData.getId()).thenReturn(id);
        when(mockPollRepository.findLatestIdForGuild(id)).thenReturn(null);

        PollData expectedPollData = new PollData();
        expectedPollData.setPublicId("1-1");
        expectedPollData.setGuild(mockGuildData);
        expectedPollData.setPollDescription("Poll description");
        expectedPollData.setAnonymous(false);
        expectedPollData.setDeadLine(null);
        expectedPollData.setNumberOfVotesPerMember(1);
        expectedPollData.setMinimumRole(null);

        when(mockPollAnswerHandler.saveAnswers(mockEvent, expectedPollData)).thenReturn(List.of(mockPollAnswerData));
        when(mockPollAnswerData.getAnswerEmoji()).thenReturn("Emoji");
        when(mockPollAnswerData.getAnswerText()).thenReturn("Answer text");


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

        createPollCommand.executeCommand(mockEvent);

        verify(mockGuildRepository).findById(id);
        verify(mockPollRepository).findLatestIdForGuild(id);
        verify(mockPollRepository).save(expectedPollData);
        verify(mockPollAnswerHandler).saveAnswers(mockEvent, expectedPollData);
        verify(mockMessageSender).replyToEvent(mockEvent, expectedText);
    }

    @Test
    void executeCommandWhenGuildIsInDbHasOneAnswerAndHasEndTimeSavesProperPollInDbAndSendsPollMessage() {
        when(mockEvent.getGuild()).thenReturn(mockGuild);
        Long id = 1L;
        when(mockGuild.getIdLong()).thenReturn(id);
        when(mockGuildRepository.findById(id)).thenReturn(Optional.of(mockGuildData));

        OptionMapping mockTimeLimitOptionMapping = Mockito.mock(OptionMapping.class);
        when(mockTimeLimitOptionMapping.getAsString()).thenReturn("2024-05-05 15:00");
        when(mockEvent.getTimeCreated())
                .thenReturn(OffsetDateTime.of(LocalDateTime.of(2024, 5, 5, 13, 14),
                        ZoneOffset.ofHours(1)));
        when(mockEvent.getOption(OptionName.POLL_TIME_LIMIT_OPTION.getOptionName())).thenReturn(mockTimeLimitOptionMapping);

        when(mockEvent.getOption(OptionName.POLL_NUMBER_OF_VOTES_OPTION.getOptionName())).thenReturn(null);
        when(mockEvent.getOption(OptionName.POLL_MINIMUM_ROLE_OPTION.getOptionName())).thenReturn(null);
        when(mockEvent.getOption(OptionName.POLL_ANONYMOUS_OPTION.getOptionName())).thenReturn(null);

        OptionMapping mockDescriptionOptionMapping = Mockito.mock(OptionMapping.class);
        when(mockDescriptionOptionMapping.getAsString()).thenReturn("Poll description");
        when(mockEvent.getOption(OptionName.POLL_DESCRIPTION_OPTION.getOptionName())).thenReturn(mockDescriptionOptionMapping);

        when(mockGuildData.getId()).thenReturn(id);
        when(mockPollRepository.findLatestIdForGuild(id)).thenReturn(null);

        PollData expectedPollData = new PollData();
        expectedPollData.setPublicId("1-1");
        expectedPollData.setGuild(mockGuildData);
        expectedPollData.setPollDescription("Poll description");
        expectedPollData.setAnonymous(false);
        expectedPollData.setDeadLine(OffsetDateTime.of(LocalDateTime.of(2024, 5, 5, 15, 0),
                ZoneOffset.ofHours(1)));
        expectedPollData.setNumberOfVotesPerMember(1);
        expectedPollData.setMinimumRole(null);

        when(mockPollAnswerHandler.saveAnswers(mockEvent, expectedPollData)).thenReturn(List.of(mockPollAnswerData));
        when(mockPollAnswerData.getAnswerEmoji()).thenReturn("Emoji");
        when(mockPollAnswerData.getAnswerText()).thenReturn("Answer text");


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

        createPollCommand.executeCommand(mockEvent);

        verify(mockGuildRepository).findById(id);
        verify(mockPollRepository).findLatestIdForGuild(id);
        verify(mockPollRepository).save(expectedPollData);
        verify(mockPollAnswerHandler).saveAnswers(mockEvent, expectedPollData);
        verify(mockMessageSender).replyToEvent(mockEvent, expectedText);
    }

    @Test
    void executeCommandWhenGuildIsInDbHasOneAnswerAndHasEndTimeFormattedBadlyDoesntSavePollInDbAndSendsErrorMessage() {
        when(mockEvent.getGuild()).thenReturn(mockGuild);
        Long id = 1L;
        when(mockGuild.getIdLong()).thenReturn(id);
        when(mockGuildRepository.findById(id)).thenReturn(Optional.of(mockGuildData));

        OptionMapping mockTimeLimitOptionMapping = Mockito.mock(OptionMapping.class);
        when(mockTimeLimitOptionMapping.getAsString()).thenReturn("2024.05.05 15:00");
        when(mockEvent.getTimeCreated())
                .thenReturn(OffsetDateTime.of(LocalDateTime.of(2024, 5, 5, 13, 14),
                        ZoneOffset.ofHours(1)));
        when(mockEvent.getOption(OptionName.POLL_TIME_LIMIT_OPTION.getOptionName())).thenReturn(mockTimeLimitOptionMapping);

        createPollCommand.executeCommand(mockEvent);

        verify(mockGuildRepository).findById(id);
        verify(mockPollRepository, times(0)).save(any());
        verify(mockPollAnswerHandler, times(0)).saveAnswers(eq(mockEvent), any());
        verify(mockMessageSender).replyToEvent(mockEvent, "OPTION ERROR: Invalid time limit format!");
    }

    @Test
    void executeCommandWhenGuildIsInDbHasOneAnswerAndHasNumberOfVotesOptionSavesProperPollInDbAndSendsPollMessage() {
        when(mockEvent.getGuild()).thenReturn(mockGuild);
        Long id = 1L;
        when(mockGuild.getIdLong()).thenReturn(id);
        when(mockGuildRepository.findById(id)).thenReturn(Optional.of(mockGuildData));

        when(mockEvent.getOption(OptionName.POLL_TIME_LIMIT_OPTION.getOptionName())).thenReturn(null);

        OptionMapping mockNumberOfVotesOption = Mockito.mock(OptionMapping.class);
        when(mockNumberOfVotesOption.getAsInt()).thenReturn(2);
        when(mockEvent.getOption(OptionName.POLL_NUMBER_OF_VOTES_OPTION.getOptionName())).thenReturn(mockNumberOfVotesOption);

        when(mockEvent.getOption(OptionName.POLL_MINIMUM_ROLE_OPTION.getOptionName())).thenReturn(null);
        when(mockEvent.getOption(OptionName.POLL_ANONYMOUS_OPTION.getOptionName())).thenReturn(null);

        OptionMapping mockDescriptionOptionMapping = Mockito.mock(OptionMapping.class);
        when(mockDescriptionOptionMapping.getAsString()).thenReturn("Poll description");
        when(mockEvent.getOption(OptionName.POLL_DESCRIPTION_OPTION.getOptionName())).thenReturn(mockDescriptionOptionMapping);

        when(mockGuildData.getId()).thenReturn(id);
        when(mockPollRepository.findLatestIdForGuild(id)).thenReturn(null);

        PollData expectedPollData = new PollData();
        expectedPollData.setPublicId("1-1");
        expectedPollData.setGuild(mockGuildData);
        expectedPollData.setPollDescription("Poll description");
        expectedPollData.setAnonymous(false);
        expectedPollData.setDeadLine(null);
        expectedPollData.setNumberOfVotesPerMember(2);
        expectedPollData.setMinimumRole(null);

        when(mockPollAnswerHandler.saveAnswers(mockEvent, expectedPollData)).thenReturn(List.of(mockPollAnswerData));
        when(mockPollAnswerData.getAnswerEmoji()).thenReturn("Emoji");
        when(mockPollAnswerData.getAnswerText()).thenReturn("Answer text");


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

        createPollCommand.executeCommand(mockEvent);

        verify(mockGuildRepository).findById(id);
        verify(mockPollRepository).findLatestIdForGuild(id);
        verify(mockPollRepository).save(expectedPollData);
        verify(mockPollAnswerHandler).saveAnswers(mockEvent, expectedPollData);
        verify(mockMessageSender).replyToEvent(mockEvent, expectedText);
    }

    @Test
    void executeCommandWhenGuildIsInDbHasOneAnswerAndHasMinRoleSavesProperPollInDbAndSendsPollMessage() {
        when(mockEvent.getGuild()).thenReturn(mockGuild);
        Long id = 1L;
        when(mockGuild.getIdLong()).thenReturn(id);
        when(mockGuildRepository.findById(id)).thenReturn(Optional.of(mockGuildData));

        when(mockEvent.getOption(OptionName.POLL_TIME_LIMIT_OPTION.getOptionName())).thenReturn(null);
        when(mockEvent.getOption(OptionName.POLL_NUMBER_OF_VOTES_OPTION.getOptionName())).thenReturn(null);

        Role mockRole = Mockito.mock(Role.class);
        OptionMapping mockMinRoleOption = Mockito.mock(OptionMapping.class);
        when(mockMinRoleOption.getAsRole()).thenReturn(mockRole);
        when(mockRole.getName()).thenReturn("Admin");

        when(mockEvent.getOption(OptionName.POLL_MINIMUM_ROLE_OPTION.getOptionName())).thenReturn(mockMinRoleOption);
        when(mockEvent.getOption(OptionName.POLL_ANONYMOUS_OPTION.getOptionName())).thenReturn(null);

        OptionMapping mockDescriptionOptionMapping = Mockito.mock(OptionMapping.class);
        when(mockDescriptionOptionMapping.getAsString()).thenReturn("Poll description");
        when(mockEvent.getOption(OptionName.POLL_DESCRIPTION_OPTION.getOptionName())).thenReturn(mockDescriptionOptionMapping);

        when(mockGuildData.getId()).thenReturn(id);
        when(mockPollRepository.findLatestIdForGuild(id)).thenReturn(null);

        PollData expectedPollData = new PollData();
        expectedPollData.setPublicId("1-1");
        expectedPollData.setGuild(mockGuildData);
        expectedPollData.setPollDescription("Poll description");
        expectedPollData.setAnonymous(false);
        expectedPollData.setDeadLine(null);
        expectedPollData.setNumberOfVotesPerMember(1);
        expectedPollData.setMinimumRole("Admin");

        when(mockPollAnswerHandler.saveAnswers(mockEvent, expectedPollData)).thenReturn(List.of(mockPollAnswerData));
        when(mockPollAnswerData.getAnswerEmoji()).thenReturn("Emoji");
        when(mockPollAnswerData.getAnswerText()).thenReturn("Answer text");


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

        createPollCommand.executeCommand(mockEvent);

        verify(mockGuildRepository).findById(id);
        verify(mockPollRepository).findLatestIdForGuild(id);
        verify(mockPollRepository).save(expectedPollData);
        verify(mockPollAnswerHandler).saveAnswers(mockEvent, expectedPollData);
        verify(mockMessageSender).replyToEvent(mockEvent, expectedText);
    }

    @Test
    void executeCommandWhenGuildIsInDbHasOneAnswerAndIsAnonymousSavesProperPollInDbAndSendsPollMessage() {
        when(mockEvent.getGuild()).thenReturn(mockGuild);
        Long id = 1L;
        when(mockGuild.getIdLong()).thenReturn(id);
        when(mockGuildRepository.findById(id)).thenReturn(Optional.of(mockGuildData));

        when(mockEvent.getOption(OptionName.POLL_TIME_LIMIT_OPTION.getOptionName())).thenReturn(null);
        when(mockEvent.getOption(OptionName.POLL_NUMBER_OF_VOTES_OPTION.getOptionName())).thenReturn(null);
        when(mockEvent.getOption(OptionName.POLL_MINIMUM_ROLE_OPTION.getOptionName())).thenReturn(null);

        OptionMapping mockAnonymousOption = Mockito.mock(OptionMapping.class);
        when(mockAnonymousOption.getAsBoolean()).thenReturn(true);
        when(mockEvent.getOption(OptionName.POLL_ANONYMOUS_OPTION.getOptionName())).thenReturn(mockAnonymousOption);

        OptionMapping mockDescriptionOptionMapping = Mockito.mock(OptionMapping.class);
        when(mockDescriptionOptionMapping.getAsString()).thenReturn("Poll description");
        when(mockEvent.getOption(OptionName.POLL_DESCRIPTION_OPTION.getOptionName())).thenReturn(mockDescriptionOptionMapping);

        when(mockGuildData.getId()).thenReturn(id);
        when(mockPollRepository.findLatestIdForGuild(id)).thenReturn(null);

        PollData expectedPollData = new PollData();
        expectedPollData.setPublicId("1-1");
        expectedPollData.setGuild(mockGuildData);
        expectedPollData.setPollDescription("Poll description");
        expectedPollData.setAnonymous(true);
        expectedPollData.setDeadLine(null);
        expectedPollData.setNumberOfVotesPerMember(1);
        expectedPollData.setMinimumRole(null);

        when(mockPollAnswerHandler.saveAnswers(mockEvent, expectedPollData)).thenReturn(List.of(mockPollAnswerData));
        when(mockPollAnswerData.getAnswerEmoji()).thenReturn("Emoji");
        when(mockPollAnswerData.getAnswerText()).thenReturn("Answer text");

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

        createPollCommand.executeCommand(mockEvent);

        verify(mockGuildRepository).findById(id);
        verify(mockPollRepository).findLatestIdForGuild(id);
        verify(mockPollRepository).save(expectedPollData);
        verify(mockPollAnswerHandler).saveAnswers(mockEvent, expectedPollData);
        verify(mockMessageSender).replyToEvent(mockEvent, expectedText);
    }

    @Test
    void executeCommandWhenGuildIsInDbHasOneAnswerAndDbAlreadyHasPollsGivesIncrementedPollId() {
        when(mockEvent.getGuild()).thenReturn(mockGuild);
        Long id = 1L;
        when(mockGuild.getIdLong()).thenReturn(id);
        when(mockGuildRepository.findById(id)).thenReturn(Optional.of(mockGuildData));

        when(mockEvent.getOption(OptionName.POLL_TIME_LIMIT_OPTION.getOptionName())).thenReturn(null);
        when(mockEvent.getOption(OptionName.POLL_NUMBER_OF_VOTES_OPTION.getOptionName())).thenReturn(null);
        when(mockEvent.getOption(OptionName.POLL_MINIMUM_ROLE_OPTION.getOptionName())).thenReturn(null);
        when(mockEvent.getOption(OptionName.POLL_ANONYMOUS_OPTION.getOptionName())).thenReturn(null);

        OptionMapping mockDescriptionOptionMapping = Mockito.mock(OptionMapping.class);
        when(mockDescriptionOptionMapping.getAsString()).thenReturn("Poll description");
        when(mockEvent.getOption(OptionName.POLL_DESCRIPTION_OPTION.getOptionName())).thenReturn(mockDescriptionOptionMapping);

        when(mockGuildData.getId()).thenReturn(id);
        when(mockPollRepository.findLatestIdForGuild(id)).thenReturn(1L);

        PollData expectedPollData = new PollData();
        expectedPollData.setPublicId("1-2");
        expectedPollData.setGuild(mockGuildData);
        expectedPollData.setPollDescription("Poll description");
        expectedPollData.setAnonymous(false);
        expectedPollData.setDeadLine(null);
        expectedPollData.setNumberOfVotesPerMember(1);
        expectedPollData.setMinimumRole(null);

        when(mockPollAnswerHandler.saveAnswers(mockEvent, expectedPollData)).thenReturn(List.of(mockPollAnswerData));
        when(mockPollAnswerData.getAnswerEmoji()).thenReturn("Emoji");
        when(mockPollAnswerData.getAnswerText()).thenReturn("Answer text");


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

        createPollCommand.executeCommand(mockEvent);

        verify(mockGuildRepository).findById(id);
        verify(mockPollRepository).findLatestIdForGuild(id);
        verify(mockPollRepository).save(expectedPollData);
        verify(mockPollAnswerHandler).saveAnswers(mockEvent, expectedPollData);
        verify(mockMessageSender).replyToEvent(mockEvent, expectedText);
    }

    //TODO: Question has two answers
    @Test
    void executeCommandWhenGuildIsInDbHasTwoAnswerAndAllDefaultValuesSavesProperPollInDbAndSendsPollMessage() {
        when(mockEvent.getGuild()).thenReturn(mockGuild);
        Long id = 1L;
        when(mockGuild.getIdLong()).thenReturn(id);
        when(mockGuildRepository.findById(id)).thenReturn(Optional.of(mockGuildData));

        when(mockEvent.getOption(OptionName.POLL_TIME_LIMIT_OPTION.getOptionName())).thenReturn(null);
        when(mockEvent.getOption(OptionName.POLL_NUMBER_OF_VOTES_OPTION.getOptionName())).thenReturn(null);
        when(mockEvent.getOption(OptionName.POLL_MINIMUM_ROLE_OPTION.getOptionName())).thenReturn(null);
        when(mockEvent.getOption(OptionName.POLL_ANONYMOUS_OPTION.getOptionName())).thenReturn(null);

        OptionMapping mockDescriptionOptionMapping = Mockito.mock(OptionMapping.class);
        when(mockDescriptionOptionMapping.getAsString()).thenReturn("Poll description");
        when(mockEvent.getOption(OptionName.POLL_DESCRIPTION_OPTION.getOptionName())).thenReturn(mockDescriptionOptionMapping);

        when(mockGuildData.getId()).thenReturn(id);
        when(mockPollRepository.findLatestIdForGuild(id)).thenReturn(null);

        PollData expectedPollData = new PollData();
        expectedPollData.setPublicId("1-1");
        expectedPollData.setGuild(mockGuildData);
        expectedPollData.setPollDescription("Poll description");
        expectedPollData.setAnonymous(false);
        expectedPollData.setDeadLine(null);
        expectedPollData.setNumberOfVotesPerMember(1);
        expectedPollData.setMinimumRole(null);

        when(mockPollAnswerData.getAnswerEmoji()).thenReturn("Emoji");
        when(mockPollAnswerData.getAnswerText()).thenReturn("Answer text");

        PollAnswerData mockPollAnswerData2 = Mockito.mock(PollAnswerData.class);
        when(mockPollAnswerData2.getAnswerEmoji()).thenReturn("Emoji2");
        when(mockPollAnswerData2.getAnswerText()).thenReturn("Second answer");

        when(mockPollAnswerHandler.saveAnswers(mockEvent, expectedPollData))
                .thenReturn(List.of(mockPollAnswerData, mockPollAnswerData2));

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

        createPollCommand.executeCommand(mockEvent);

        verify(mockGuildRepository).findById(id);
        verify(mockPollRepository).findLatestIdForGuild(id);
        verify(mockPollRepository).save(expectedPollData);
        verify(mockPollAnswerHandler).saveAnswers(mockEvent, expectedPollData);
        verify(mockMessageSender).replyToEvent(mockEvent, expectedText);
    }
}