package com.therealtehu.discordbot.TehuBot.model.action.command.poll;

import com.therealtehu.discordbot.TehuBot.model.action.command.OptionName;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class PollUtil {

    protected static List<OptionData> getOptions() {
        return options;
    }

    protected static DateTimeFormatter getDateFormatter() {
        return DateTimeFormatter.ofPattern(TIME_FORMAT);
    }
    protected static int getMaxNumberOfVoteOptions() {
        return MAX_NUMBER_OF_VOTE_OPTIONS;
    }

    protected static int getDefaultNumberOfVotes() {
        return DEFAULT_NUMBER_OF_VOTES;
    }

    protected static List<String> getEmojis() {
        return EMOJIS;
    }

    private static final int MIN_DESC_LENGTH = 3;
    private static final int MAX_DESC_LENGTH = 100;
    private static final String TIME_FORMAT = "yyyy-mm-dd HH:mm";
    private static final int TIME_OPTION_LENGTH = TIME_FORMAT.length();
    private static final int DEFAULT_NUMBER_OF_VOTES = 1;
    private static final int MAX_NUMBER_OF_VOTE_OPTIONS = 20;

    private static final OptionData DESCRIPTION_OPTION = new OptionData(
            OptionType.STRING,
            OptionName.POLL_DESCRIPTION_OPTION.getOptionName(),
            "The description of the vote",
            true).setMinLength(MIN_DESC_LENGTH).setMaxLength(MAX_DESC_LENGTH);

    private static final OptionData TIME_LIMIT_OPTION = new OptionData(
            OptionType.STRING,
            OptionName.POLL_TIME_LIMIT_OPTION.getOptionName(),
            "The end date of the poll in the following format: " + TIME_FORMAT,
            false).setMinLength(TIME_OPTION_LENGTH).setMaxLength(TIME_OPTION_LENGTH);

    private static final OptionData NUMBER_OF_VOTES_OPTION = new OptionData(
            OptionType.INTEGER,
            OptionName.POLL_NUMBER_OF_VOTES_OPTION.getOptionName(),
            "The number of votes one user can send. Default: " + DEFAULT_NUMBER_OF_VOTES,
            false).setMinValue(DEFAULT_NUMBER_OF_VOTES);

    private static final OptionData MINIMUM_ROLE_OPTION = new OptionData(
            OptionType.ROLE,
            OptionName.POLL_MINIMUM_ROLE_OPTION.getOptionName(),
            "The minimum role needed to vote.",
            false);

    private static final OptionData ANONYMOUS_OPTION = new OptionData(
            OptionType.BOOLEAN,
            OptionName.POLL_ANONYMOUS_OPTION.getOptionName(),
            "Make voting anonymous. False by default",
            false);

    private static final OptionData ANSWER_1_OPTION = new OptionData(
            OptionType.STRING,
            OptionName.POLL_ANSWER_1_OPTION.getOptionName(),
            "Answer 1",
            true);

    private static final OptionData ANSWER_2_OPTION = new OptionData(
            OptionType.STRING,
            OptionName.POLL_ANSWER_2_OPTION.getOptionName(),
            "Answer 2",
            false);

    private static final OptionData ANSWER_3_OPTION = new OptionData(
            OptionType.STRING,
            OptionName.POLL_ANSWER_3_OPTION.getOptionName(),
            "Answer 3",
            false);

    private static final OptionData ANSWER_4_OPTION = new OptionData(
            OptionType.STRING,
            OptionName.POLL_ANSWER_4_OPTION.getOptionName(),
            "Answer 4",
            false);

    private static final OptionData ANSWER_5_OPTION = new OptionData(
            OptionType.STRING,
            OptionName.POLL_ANSWER_5_OPTION.getOptionName(),
            "Answer 5",
            false);

    private static final OptionData ANSWER_6_OPTION = new OptionData(
            OptionType.STRING,
            OptionName.POLL_ANSWER_6_OPTION.getOptionName(),
            "Answer 6",
            false);

    private static final OptionData ANSWER_7_OPTION = new OptionData(
            OptionType.STRING,
            OptionName.POLL_ANSWER_7_OPTION.getOptionName(),
            "Answer 7",
            false);

    private static final OptionData ANSWER_8_OPTION = new OptionData(
            OptionType.STRING,
            OptionName.POLL_ANSWER_8_OPTION.getOptionName(),
            "Answer 8",
            false);

    private static final OptionData ANSWER_9_OPTION = new OptionData(
            OptionType.STRING,
            OptionName.POLL_ANSWER_9_OPTION.getOptionName(),
            "Answer 9",
            false);

    private static final OptionData ANSWER_10_OPTION = new OptionData(
            OptionType.STRING,
            OptionName.POLL_ANSWER_10_OPTION.getOptionName(),
            "Answer 10",
            false);

    private static final OptionData ANSWER_11_OPTION = new OptionData(
            OptionType.STRING,
            OptionName.POLL_ANSWER_11_OPTION.getOptionName(),
            "Answer 11",
            false);

    private static final OptionData ANSWER_12_OPTION = new OptionData(
            OptionType.STRING,
            OptionName.POLL_ANSWER_12_OPTION.getOptionName(),
            "Answer 12",
            false);

    private static final OptionData ANSWER_13_OPTION = new OptionData(
            OptionType.STRING,
            OptionName.POLL_ANSWER_13_OPTION.getOptionName(),
            "Answer 13",
            false);

    private static final OptionData ANSWER_14_OPTION = new OptionData(
            OptionType.STRING,
            OptionName.POLL_ANSWER_14_OPTION.getOptionName(),
            "Answer 14",
            false);

    private static final OptionData ANSWER_15_OPTION = new OptionData(
            OptionType.STRING,
            OptionName.POLL_ANSWER_15_OPTION.getOptionName(),
            "Answer 15",
            false);

    private static final OptionData ANSWER_16_OPTION = new OptionData(
            OptionType.STRING,
            OptionName.POLL_ANSWER_16_OPTION.getOptionName(),
            "Answer 16",
            false);

    private static final OptionData ANSWER_17_OPTION = new OptionData(
            OptionType.STRING,
            OptionName.POLL_ANSWER_17_OPTION.getOptionName(),
            "Answer 17",
            false);

    private static final OptionData ANSWER_18_OPTION = new OptionData(
            OptionType.STRING,
            OptionName.POLL_ANSWER_18_OPTION.getOptionName(),
            "Answer 18",
            false);

    private static final OptionData ANSWER_19_OPTION = new OptionData(
            OptionType.STRING,
            OptionName.POLL_ANSWER_19_OPTION.getOptionName(),
            "Answer 19",
            false);

    private static final OptionData ANSWER_20_OPTION = new OptionData(
            OptionType.STRING,
            OptionName.POLL_ANSWER_20_OPTION.getOptionName(),
            "Answer 20",
            false);

    private static final List<OptionData> options = List.of(
            DESCRIPTION_OPTION,
            ANSWER_1_OPTION,
            ANSWER_2_OPTION,
            ANSWER_3_OPTION,
            ANSWER_4_OPTION,
            ANSWER_5_OPTION,
            ANSWER_6_OPTION,
            ANSWER_7_OPTION,
            ANSWER_8_OPTION,
            ANSWER_9_OPTION,
            ANSWER_10_OPTION,
            ANSWER_11_OPTION,
            ANSWER_12_OPTION,
            ANSWER_13_OPTION,
            ANSWER_14_OPTION,
            ANSWER_15_OPTION,
            ANSWER_16_OPTION,
            ANSWER_17_OPTION,
            ANSWER_18_OPTION,
            ANSWER_19_OPTION,
            ANSWER_20_OPTION,
            TIME_LIMIT_OPTION,
            NUMBER_OF_VOTES_OPTION,
            MINIMUM_ROLE_OPTION,
            ANONYMOUS_OPTION
    );

    private static final List<String> EMOJIS = List.of(
            "😀", "😃", "😄", "😁", "😆", "😅", "😂", "🤣", "😊", "😇",
            "🙂", "🙃", "😉", "😌", "😍", "🥰", "😘", "😗", "😙", "😚",
            "😋", "😛", "😜", "🤪", "😝", "🤑", "🤗", "🤭", "🤫", "🤔",
            "🤐", "🤨", "😐", "😑", "😶", "😏", "😒", "🙄", "😬", "🤥",
            "😌", "😔", "😪", "🤤", "😴", "😷", "🤒", "🤕", "🤢", "🤮",
            "🥵", "🥶", "🥴", "😵", "🤯", "🤠", "🥳", "😎", "🤓", "🧐",
            "😕", "😟", "🙁", "☹️", "😮", "😯", "😲", "😳", "🥺", "😦",
            "😧", "😨", "😰", "😥", "😢", "😭", "😱", "😖", "😣", "😞",
            "😓", "😩", "😫", "🥱", "😤", "😡", "😠", "🤬", "😈", "👿",
            "💀", "☠️", "💩", "🤡", "👹", "👺", "👻", "👽", "👾", "🤖"
    );
}
