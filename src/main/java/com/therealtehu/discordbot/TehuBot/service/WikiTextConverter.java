package com.therealtehu.discordbot.TehuBot.service;

public class WikiTextConverter {
    public static String convertToPlainText(String wikitext) {
        // Replace internal links markup
        wikitext = wikitext.replaceAll("\\[\\[([^\\|]+)\\|([^\\]]+)\\]\\]", "[$2]($1)");

        // Replace external links markup with negative lookahead for '('
        wikitext = wikitext.replaceAll("\\[(https?://[^\\s]+)\\s([^\\]]+)\\]", "[$2]($1)");

        // Replace italic text markup
        wikitext = wikitext.replaceAll("'''([^']+)'''", "*$1*");

        // Replace bold text markup
        wikitext = wikitext.replaceAll("''([^']+)''", "**$1**");

        // Replace headings markup
        wikitext = wikitext.replaceAll("==+\\s*([^=]+)\\s*==+", "# $1");

        return wikitext;
    }
}
