package com.ucbcba.joel.ucbcorreccionformato.formaterrors.formatrules;

import com.ucbcba.joel.ucbcorreccionformato.formaterrors.formatcontrol.PageFormat;
import com.ucbcba.joel.ucbcorreccionformato.formaterrors.highlightsreport.FormatErrorReport;
import com.ucbcba.joel.ucbcorreccionformato.general.GeneralSeeker;
import com.ucbcba.joel.ucbcorreccionformato.general.ReportFormatError;
import com.ucbcba.joel.ucbcorreccionformato.general.WordsProperties;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class PageNumerationFormat implements FormatRule {
    private PDDocument pdfdocument;
    private GeneralSeeker seeker;
    private AtomicLong counter;
    private int pageNumeration;

    public PageNumerationFormat(PDDocument pdfdocument, AtomicLong counter,int pageNumeration) {
        this.pdfdocument = pdfdocument;
        this.seeker = new GeneralSeeker(pdfdocument);
        this.counter = counter;
        this.pageNumeration = pageNumeration;
    }

    @Override
    public List<FormatErrorReport> getFormatErrors(int page) throws IOException {
        float pageWidth = pdfdocument.getPage(page-1).getMediaBox().getWidth();
        float pageHeight = pdfdocument.getPage(page-1).getMediaBox().getHeight();
        List<FormatErrorReport> formatErrors = new ArrayList<>();

        String lastLineWord = seeker.getLastWordsLine(page);
        if (!lastLineWord.equals("")) {
            List<WordsProperties> words = seeker.findWordsFromAPage(page, lastLineWord);
            WordsProperties numberPage = words.get(words.size()-1);
            if (numberPage.getY() > 720) {
                List<String> comments = new PageFormat(numberPage,12,"Derecho",false,false,pageNumeration).getFormatErrors(pageWidth);
                reportFormatErrors(comments, words, formatErrors, pageWidth, pageHeight, page);
            }
        }
        return formatErrors;
    }

    private void reportFormatErrors(List<String> comments, List<WordsProperties> words, List<FormatErrorReport> formatErrors, float pageWidth, float pageHeight, int page) {


        if (!comments.isEmpty()) {
            formatErrors.add(new ReportFormatError(counter).reportFormatError(comments, words.get(words.size()-1), pageWidth, pageHeight, page));
        }
    }
}
