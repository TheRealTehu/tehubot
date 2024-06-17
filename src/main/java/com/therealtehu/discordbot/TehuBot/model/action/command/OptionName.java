package com.therealtehu.discordbot.TehuBot.model.action.command;

public enum OptionName {
    DICE_ROLL_SIDES_OPTION("sides"),
    GET_WIKI_TITLE_OPTION("wikititle"),
    SEND_GIF_PROMPT_OPTION("gifprompt"),
    SEND_GIF_CHANNEL_OPTION("gifchannel");

    private final String optionName;

    OptionName(String optionName) {
        this.optionName = optionName;
    }

    public String getOptionName() {
        return optionName;
    }
}
