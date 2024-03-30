package com.therealtehu.discordbot.TehuBot.listeners.event.guild;

public enum ButtonLabels {
    SET_SERVER_DEFAULT("setChannelsDefault"),
    CREATE_ONE_CHANNEL_FOR_ALL("createOneChannelForAll"),
    CREATE_CHANNELS_FOR_CATEGORIES("createChannelsForCategories"),
    SPECIFY_ONE_CHANNEL_FOR_ALL("specifyOneChannelForAll"),
    SPECIFY_CHANNELS_FOR_CATEGORIES("specifyChannelsForCategories");
    private final String buttonId;
    ButtonLabels(String buttonId) {
        this.buttonId = buttonId;
    }

    public String getButtonId() {
        return buttonId;
    }
}
