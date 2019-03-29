package com.ucbcba.joel.ucbcorreccionformato.formaterrors.formatControl;

import com.ucbcba.joel.ucbcorreccionformato.general.WordsProperties;

import java.util.List;

public class FigureNumerationFormat extends Format {

    private long figureNumeration;

    public FigureNumerationFormat(WordsProperties word, float fontSize, String alignment, boolean isBold, boolean isItalic, long figureNumeration) {
        super(word, fontSize, alignment, isBold, isItalic);
        this.figureNumeration = figureNumeration;
    }

    @Override
    public List<String> getFormatErrors(float pageWidth) {
        List<String> comments =  super.getFormatErrors(pageWidth);
        if (!word.toString().contains(Long.toString(figureNumeration))){
            comments.add("Número de figura debería ser "+ figureNumeration);
        }
        return comments;
    }
}