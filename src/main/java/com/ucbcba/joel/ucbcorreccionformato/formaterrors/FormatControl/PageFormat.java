package com.ucbcba.joel.ucbcorreccionformato.formaterrors.FormatControl;

import com.ucbcba.joel.ucbcorreccionformato.General.WordsProperties;

import java.util.List;

public class PageFormat extends Format {
    private int pageNumeration;
    public PageFormat(WordsProperties word, float fontSize, String alignment, boolean isBold, boolean isItalic, int pageNumeration) {
        super(word, fontSize, alignment, isBold, isItalic);
        this.pageNumeration = pageNumeration;
    }

    @Override
    public List<String> getFormatErrors(float pageWidth) {
        List<String> comments =  super.getFormatErrors(pageWidth);
        if (!word.toString().equals(Integer.toString(pageNumeration))){
            comments.add("Número de página debería ser "+ pageNumeration);
        }
        return comments;
    }
}
