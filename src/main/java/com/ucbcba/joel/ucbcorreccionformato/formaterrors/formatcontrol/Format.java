package com.ucbcba.joel.ucbcorreccionformato.formaterrors.formatcontrol;


import com.ucbcba.joel.ucbcorreccionformato.general.WordsProperties;

import java.util.ArrayList;
import java.util.List;

public class Format {
    protected WordsProperties word;
    protected String[] font;
    protected float fontSize;
    protected String alignment;
    protected boolean isBold;
    protected boolean isItalic;



    public Format(WordsProperties word, float fontSize,String alignment, boolean isBold, boolean isItalic) {
        this.word = word;
        this.font = new String[]{"Times", "New", "Roman"};
        this.fontSize = fontSize;
        this.alignment = alignment;
        this.isBold = isBold;
        this.isItalic = isItalic;
    }

    public List<String> getFormatErrors(float pageWidth){
        List<String> comments = new ArrayList<>();
        if (!word.getFont().contains(font[0]) || !word.getFont().contains(font[1]) || !word.getFont().contains(font[2])){
            comments.add("Fuente: "+font[0]+" "+font[1]+" "+font[2]);
        }

        if (word.getFontSize() != fontSize){
            comments.add("Tamaño de la letra: "+ (int) fontSize +" puntos");
        }

        if (isBold) {
            if (!word.getFont().contains("Bold")) {
                comments.add("Negrilla");
            }
        }else{
            if (word.getFont().contains("Bold")){
                comments.add("No tenga negrilla");
            }
        }

        if (isItalic) {
            if (!word.getFont().contains("Italic")) {
                comments.add("Cursiva");
            }
        }else{
            if (word.getFont().contains("Italic")){
                comments.add("No tenga cursiva");
            }
        }

        if(alignment.equals("Centrado")){
            if (Math.abs((pageWidth - word.getXPlusWidth()) - word.getX()) >= 100){
                comments.add("Alineado centrado");
            }
        }

        if(alignment.equals("Derecho")){
            if (Math.abs((pageWidth - word.getXPlusWidth()) - word.getX()) <= 20 || word.getXPlusWidth() < 500){
                comments.add("Alineado al margen derecho");
            }
        }

        return comments;
    }


}