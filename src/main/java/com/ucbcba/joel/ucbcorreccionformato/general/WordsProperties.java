package com.ucbcba.joel.ucbcorreccionformato.general;

import org.apache.pdfbox.text.TextPosition;

import java.util.List;

public class WordsProperties implements CharSequence{

    private List<TextPosition> textPositions;
    private int start, end;

    public WordsProperties(List<TextPosition> textPositions) {
        this.textPositions = textPositions;
        this.start = 0;
        this.end = textPositions.size();
    }

    public WordsProperties(List<TextPosition> textPositions, int start, int end) {
        this.textPositions = textPositions;
        this.start = start;
        this.end = end;
    }

    @Override
    public int length() {
        return end - start;
    }

    @Override
    public char charAt(int index) {
        TextPosition textPosition = textPositionAt(index);
        String text = textPosition.getUnicode();
        return text.charAt(0);
    }

    @Override
    public WordsProperties subSequence(int start, int end) {
        return new WordsProperties(textPositions, this.start + start, this.start + end);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(length());
        for (int i = 0; i < length(); i++) {
            builder.append(charAt(i));
        }
        return builder.toString();
    }

    public TextPosition textPositionAt(int index) {
        return textPositions.get(start + index);
    }

    public float getX() {
        return textPositions.get(start).getXDirAdj();
    }

    public float getXPlusWidth() {
        return textPositions.get(end-1).getEndX();
    }


    public float getY() {
        return textPositions.get(start).getYDirAdj();
    }

    public float getYPlusHeight() {
        return textPositions.get(start).getYDirAdj() - textPositions.get(start).getFontSizeInPt();
    }


    public String getFont() {
        TextPosition first = textPositions.get(start);
        return first.getFont().getName();
    }

    public float getFontSize() {
        TextPosition first = textPositions.get(start);
        return Math.round(first.getFontSize());
    }

    public String getFontBassic() {
        TextPosition first = textPositions.get((start+end)/2);
        return first.getFont().getName();
    }

    public float getFontSizeBasic() {
        TextPosition first = textPositions.get((start+end)/2);
        return Math.round(first.getFontSize());
    }
}
