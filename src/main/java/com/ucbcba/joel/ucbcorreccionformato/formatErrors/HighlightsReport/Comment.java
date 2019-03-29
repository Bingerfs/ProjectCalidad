package com.ucbcba.joel.ucbcorreccionformato.formatErrors.HighlightsReport;

public class Comment {
    private String text;
    private String emoji;

    public Comment(String text, String emoji) {
        this.text = text;
        this.emoji = emoji;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getEmoji() {
        return emoji;
    }

    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }
}
