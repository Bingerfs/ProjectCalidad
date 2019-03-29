package com.ucbcba.joel.ucbcorreccionformato.formatErrors.bibliographies;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternBibliographyReferences {
    private String name;
    private Pattern pattern;

    public PatternBibliographyReferences(String name, Pattern pattern) {
        this.name = name;
        this.pattern = pattern;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }

    public Matcher getMatcher(String lineWord){
        return pattern.matcher(lineWord);
    }


}
