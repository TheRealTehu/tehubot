package com.therealtehu.discordbot.TehuBot.model.action.command;

public enum OptionName {
    DICE_ROLL_SIDES_OPTION("sides"),
    GET_WIKI_TITLE_OPTION("wikititle"),
    SEND_GIF_PROMPT_OPTION("gifprompt"),
    SEND_GIF_CHANNEL_OPTION("gifchannel"),
    POLL_DESCRIPTION_OPTION("description"),
    POLL_TIME_LIMIT_OPTION("timelimit"),
    POLL_NUMBER_OF_VOTES_OPTION("numberofvotes"),
    POLL_MINIMUM_ROLE_OPTION("minrole"),
    POLL_ANONYMOUS_OPTION("anonymousvote"),
    POLL_ANSWER_1_OPTION("answer1"),
    POLL_ANSWER_2_OPTION("answer2"),
    POLL_ANSWER_3_OPTION("answer3"),
    POLL_ANSWER_4_OPTION("answer4"),
    POLL_ANSWER_5_OPTION("answer5"),
    POLL_ANSWER_6_OPTION("answer6"),
    POLL_ANSWER_7_OPTION("answer7"),
    POLL_ANSWER_8_OPTION("answer8"),
    POLL_ANSWER_9_OPTION("answer9"),
    POLL_ANSWER_10_OPTION("answer10"),
    POLL_ANSWER_11_OPTION("answer11"),
    POLL_ANSWER_12_OPTION("answer12"),
    POLL_ANSWER_13_OPTION("answer13"),
    POLL_ANSWER_14_OPTION("answer14"),
    POLL_ANSWER_15_OPTION("answer15"),
    POLL_ANSWER_16_OPTION("answer16"),
    POLL_ANSWER_17_OPTION("answer17"),
    POLL_ANSWER_18_OPTION("answer18"),
    POLL_ANSWER_19_OPTION("answer19"),
    POLL_ANSWER_20_OPTION("answer20"),
    POLL_ID_OPTION("pollid");

    private final String optionName;

    OptionName(String optionName) {
        this.optionName = optionName;
    }

    public String getOptionName() {
        return optionName;
    }
}
