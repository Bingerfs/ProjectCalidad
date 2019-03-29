package com.ucbcba.joel.ucbcorreccionformato.formaterrors.FormatControl;

import com.ucbcba.joel.ucbcorreccionformato.General.WordsProperties;

import java.util.List;

public class CoverFormat  extends  Format{
    private boolean isAllUpperCase;

    public CoverFormat(WordsProperties word, float fontSize, String alignment, boolean isBold, boolean isItalic, boolean isAllUpperCase) {
        super(word, fontSize, alignment, isBold, isItalic);
        this.isAllUpperCase = isAllUpperCase;
    }

    @Override
    public List<String> getFormatErrors(float pageWidth){
        List<String> comments = super.getFormatErrors(pageWidth);;
        if (isAllUpperCase) {
            if (!Character.isUpperCase(word.charAt(1))) {
                comments.add("Todo en mayúscula");
            }
        } else {
            if (Character.isUpperCase(word.charAt(1))) {
                comments.add("No todo en mayúscula");
            }
        }

        return comments;
    }
}
