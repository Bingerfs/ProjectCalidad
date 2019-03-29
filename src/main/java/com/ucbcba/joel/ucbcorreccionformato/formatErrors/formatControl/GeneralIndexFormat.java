package com.ucbcba.joel.ucbcorreccionformato.formatErrors.formatControl;

import com.ucbcba.joel.ucbcorreccionformato.general.WordsProperties;

import java.util.List;

public class GeneralIndexFormat extends Format {
    private int nroBleeding;
    private boolean isAllUpperCase;

    public GeneralIndexFormat(WordsProperties word, float fontSize, String alignment, boolean isBold, boolean isItalic, boolean isAllUpperCase, int nroBleeding) {
        super(word, fontSize, alignment, isBold, isItalic);
        this.nroBleeding = nroBleeding;
        this.isAllUpperCase = isAllUpperCase;
    }

    @Override
    public List<String> getFormatErrors(float pageWidth) {
        List<String> comments = super.getFormatErrors(pageWidth);
        int indexFirstCharacter = 0;

        while (!Character.isLetter(word.charAt(indexFirstCharacter)) && word.length() > indexFirstCharacter){
            indexFirstCharacter++;
        }

        if(indexFirstCharacter != word.length()) {
            if (isAllUpperCase) {
                if (!Character.isUpperCase(word.charAt(indexFirstCharacter + 1))) {
                    comments.add("Todo en mayúscula");
                }
            } else {
                if (Character.isUpperCase(word.charAt(indexFirstCharacter + 1))) {
                    comments.add("No todo en mayúscula");
                }
            }
        }

        if (alignment.equals("Izquierdo")) {
            if (nroBleeding == 0) {
                if (word.getX() < 95 || word.getX() > 105) {
                    comments.add("Alineado al margen izquierdo");
                }
            }

            if (nroBleeding == 1) {
                if (word.getX() < 106 || word.getX() > 116) {
                    comments.add("Tenga una sangría pequeña");
                }
            }

            if (nroBleeding == 2) {
                if (word.getX() < 117 || word.getX() > 127) {
                    comments.add("Tenga dos sangrías pequeñas");
                }
            }

            if (nroBleeding == 3) {
                if (word.getX() < 129 || word.getX() > 139) {
                    comments.add("Tenga tres sangrías pequeñas");
                }
            }
        }
        return comments;
    }
}
