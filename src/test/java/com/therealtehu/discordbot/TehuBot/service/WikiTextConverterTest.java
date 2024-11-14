package com.therealtehu.discordbot.TehuBot.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WikiTextConverterTest {

    @Test
    void convertToPlainTextWhenPlainTextIsGivenReturnsInputString() {
        String expected = "Normal plain text.";
        String actual = WikiTextConverter.convertToPlainText(expected);

        assertEquals(expected, actual);
    }

    @Test
    void convertToPlainTextWhenContainsBoldTextChangesBoldTextMarker() {
        String boldText = "Contains ''bold'' text.";
        String expected = "Contains **bold** text.";
        String actual = WikiTextConverter.convertToPlainText(boldText);

        assertEquals(expected, actual);
    }

    @Test
    void convertToPlainTextWhenContainsItalicTextChangesItalicTextMarker() {
        String italicText = "Contains '''italic''' text.";
        String expected = "Contains *italic* text.";
        String actual = WikiTextConverter.convertToPlainText(italicText);

        assertEquals(expected, actual);
    }

    @Test
    void convertToPlainTextWhenContainsInternalLinkChangesInternalLinkMarker() {
        String internalLinkText = "Contains [[Link|Text]].";
        String expected = "Contains [Text](Link).";
        String actual = WikiTextConverter.convertToPlainText(internalLinkText);

        assertEquals(expected, actual);
    }

    @Test
    void convertToPlainTextWhenContainsExternalLinkChangesExternalLinkMarker() {
        String externalLinkText = "Contains [https://www.example.com link].";
        String expected = "Contains [link](https://www.example.com).";
        String actual = WikiTextConverter.convertToPlainText(externalLinkText);

        assertEquals(expected, actual);
    }

    @Test
    void convertToPlainTextWhenContainsHeadingsChangesHeadingsMarker() {
        String headingsText = "Contains == Heading ==.";
        String expected = "Contains # Heading .";
        String actual = WikiTextConverter.convertToPlainText(headingsText);

        assertEquals(expected, actual);
    }

    @Test
    void convertToPlainTextWhenContainsMultipleKindsOfMarkersChangesAllMarkersProperly() {
        String textWithBoldAndHeadingMarkers = "Contains ''bold'', also contains == heading ==.";
        String textWithInternalAndExternalLinkMarkers = "Contains [[Link|Text]], also contains [https://www.example.com link].";
        String textWithItalicAndBoldMarkers = "Contains '''italic''', also contains ''bold''.";
        String textWithItalicAllMarkers = "Contains ''bold'', '''italic''', [[Link|Text]], [https://www.example.com link] and == heading ==.";

        String textAfterBoldAndHeadingMarkers = "Contains **bold**, also contains # heading .";
        String textAfterInternalAndExternalMarkers = "Contains [Text](Link), also contains [link](https://www.example.com).";
        String textAfterItalicAndBoldMarkers = "Contains *italic*, also contains **bold**.";
        String textAfterAllMarkers = "Contains **bold**, *italic*, [Text](Link), [link](https://www.example.com) and # heading .";

        assertEquals(textAfterBoldAndHeadingMarkers, WikiTextConverter.convertToPlainText(textWithBoldAndHeadingMarkers));
        assertEquals(textAfterInternalAndExternalMarkers, WikiTextConverter.convertToPlainText(textWithInternalAndExternalLinkMarkers));
        assertEquals(textAfterItalicAndBoldMarkers, WikiTextConverter.convertToPlainText(textWithItalicAndBoldMarkers));
        assertEquals(textAfterAllMarkers, WikiTextConverter.convertToPlainText(textWithItalicAllMarkers));
    }
}
