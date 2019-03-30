package com.ucbcba.joel.ucbcorreccionformato.formalaspects;

public class BasicFormatReport {
    private String format;
    private boolean isCorrect;

    public BasicFormatReport(String format, boolean isCorrect) {
        this.format = format;
        this.isCorrect = isCorrect;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }
}
