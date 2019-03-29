package com.ucbcba.joel.ucbcorreccionformato.general;

import com.ucbcba.joel.ucbcorreccionformato.formaterrors.HighlightsReport.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class ReportFormatError {
    private AtomicLong counter;

    public ReportFormatError(AtomicLong counter) {
        this.counter = counter;
    }

    public FormatErrorReport reportFormatError(List<String> comments, WordsProperties word, float pageWidth, float pageHeight, int page){
        StringBuilder commentStr = new StringBuilder();
        for (int i = 0; i < comments.size(); i++) {
            if (i != 0) {
                commentStr.append(" - ").append(comments.get(i));
            } else {
                commentStr.append(comments.get(i));
            }
        }
        commentStr.append(".");
        String comment = commentStr.toString();
        String content = word.toString();
        return createFormatError(word,content,comment,pageWidth,pageHeight,page,String.valueOf(counter.incrementAndGet()));
    }

    public FormatErrorReport createFormatError(WordsProperties word, String contentText, String commentText, float pageWidth, float pageHeight, int page, String id  ){
        Content content = new Content(contentText);
        BoundingRect boundingRect = new BoundingRect(word.getX(), word.getYPlusHeight(), word.getXPlusWidth(),word.getY(),pageWidth,pageHeight);
        List<BoundingRect> boundingRects = new ArrayList<>();
        boundingRects.add(boundingRect);
        Position position = new Position(boundingRect,boundingRects,page);
        Comment comment = new Comment(commentText,"");
        return new FormatErrorReport(content,position,comment,id);
    }
}
