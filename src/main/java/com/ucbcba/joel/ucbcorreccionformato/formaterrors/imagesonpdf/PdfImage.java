package com.ucbcba.joel.ucbcorreccionformato.formaterrors.imagesonpdf;

import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.util.Matrix;

public class PdfImage {
    private PDImageXObject pdImageXObject;
    private Matrix matrix;
    private Integer page;
    private float pageHeight;

    public PdfImage(PDImageXObject pdImageXObject,Matrix matrix,Integer page,float pageHeight){
        this.pdImageXObject = pdImageXObject;
        this.matrix = matrix;
        this.page = page;
        this.pageHeight = pageHeight;
    }

    public int getPage(){
        return page;
    }

    public float getX(){
        return matrix.getTranslateX();
    }

    public float getY(){
        return pageHeight - (matrix.getTranslateY() + getHeightDisplayed());
    }

    public float getEndX(){
        return getX() + getWidthDisplayed();
    }

    public float getEndY(){
        return pageHeight - matrix.getTranslateY();
    }

    public int getWidth(){
        return pdImageXObject.getWidth();
    }

    public int getHeight(){
        return pdImageXObject.getHeight();
    }

    public float getWidthDisplayed(){
        return matrix.getScalingFactorX();
    }

    public float getHeightDisplayed(){
        return matrix.getScalingFactorY();
    }
}
