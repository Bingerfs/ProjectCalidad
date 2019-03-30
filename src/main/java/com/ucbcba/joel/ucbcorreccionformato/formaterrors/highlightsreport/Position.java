package com.ucbcba.joel.ucbcorreccionformato.formaterrors.highlightsreport;

import java.util.List;

public class Position {
    private BoundingRect boundingRect;
    private List<BoundingRect> rects;
    private int pageNumber;

    public Position(BoundingRect boundingRect, List<BoundingRect> rects, int pageNumber) {
        this.boundingRect = boundingRect;
        this.rects = rects;
        this.pageNumber = pageNumber;
    }


    public BoundingRect getBoundingRect() {
        return boundingRect;
    }

    public void setBoundingRect(BoundingRect boundingRect) {
        this.boundingRect = boundingRect;
    }



    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public List<BoundingRect> getRects() {
        return rects;
    }

    public void setRects(List<BoundingRect> rects) {
        this.rects = rects;
    }
}
