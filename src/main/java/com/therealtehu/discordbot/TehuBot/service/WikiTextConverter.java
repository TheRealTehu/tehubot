package com.therealtehu.discordbot.TehuBot.service;

public class WikiTextConverter {
    public static String convertToPlainText(String wikitext) {
        // Replace bold text markup
        wikitext = wikitext.replaceAll("''([^']+)''", "*$1*");

        // Replace italic text markup
        wikitext = wikitext.replaceAll("'''([^']+)'''", "_$1_");

        // Replace internal links markup
        wikitext = wikitext.replaceAll("\\[\\[([^]|]+)\\|([^]]+)]]", "$2");

        // Replace external links markup
        wikitext = wikitext.replaceAll("\\[([^\\s]+)\\s([^]]+)]", "$2");

        // Replace headings markup
        wikitext = wikitext.replaceAll("==+\\s*([^=]+)\\s*==+", "**$1**");

        return wikitext;
    }
}
