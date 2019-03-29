package com.ucbcba.joel.ucbcorreccionformato.formaterrors.HighlightsReport;

public class FormatErrorReport {
    private Content content;
    private Position position;
    private Comment comment;
    private String id;

    public FormatErrorReport(Content content, Position position, Comment comment, String id) {
        this.content = content;
        this.position = position;
        this.comment = comment;
        this.id = id;
    }


    public String getId() {
        return id;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }
}
