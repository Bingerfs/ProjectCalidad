package com.ucbcba.joel.ucbcorreccionformato.formaterrors.formatrules;

import com.ucbcba.joel.ucbcorreccionformato.formaterrors.formatcontrol.CoverFormat;
import com.ucbcba.joel.ucbcorreccionformato.formaterrors.highlightsreport.FormatErrorReport;
import com.ucbcba.joel.ucbcorreccionformato.general.GeneralSeeker;
import com.ucbcba.joel.ucbcorreccionformato.general.ReportFormatError;
import com.ucbcba.joel.ucbcorreccionformato.general.WordsProperties;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;


public class CoverPageFormat implements FormatRule {

    private PDDocument pdfdocument;
    private GeneralSeeker seeker;
    private AtomicLong counter;


    private String align = "Centrado";


    public CoverPageFormat(PDDocument pdfdocument, AtomicLong counter){
        this.pdfdocument = pdfdocument;
        this.seeker = new GeneralSeeker(pdfdocument);
        this.counter = counter;
    }


    List<String> generateComments(List<WordsProperties> words, int fontSize, String alignment, Boolean isBold, Boolean isItalic, Boolean allUppercase, float pageWidth){
        List<String> comments;
        comments=new CoverFormat(words.get(0), fontSize, alignment, isBold, isItalic, allUppercase).getFormatErrors(pageWidth);
        return comments;
    }

    public void verifyLine(int line, List<WordsProperties> words, List<FormatErrorReport> formatErrors, float pageWidth, float pageHeight, int page, int numberOfLines){
        List<String> comments= new ArrayList<>();
        if (line == 1) {
            generateComments( words, 18, align, true, false, true, pageWidth);
            reportFormatErrors(comments, words, formatErrors, pageWidth, pageHeight, page);
        }
        if (line == 2) {
            generateComments( words, 16, align, true, false, true, pageWidth);
            reportFormatErrors(comments, words, formatErrors, pageWidth, pageHeight, page);
        }
        if (line == 3 || line == 4) {
            generateComments(words, 14, align, true, false, false, pageWidth);
            reportFormatErrors(comments, words, formatErrors, pageWidth, pageHeight, page);
        }

        if (line == 5) {
            generateComments(words, 16, align, true, false, false, pageWidth);
            reportFormatErrors(comments, words, formatErrors, pageWidth, pageHeight, page);
        }

        if (line > 5 && line <= numberOfLines - 4) {
            generateComments(words, 16, align, true, false, false, pageWidth);
            reportFormatErrors(comments, words, formatErrors, pageWidth, pageHeight, page);
        }

        if (line == numberOfLines - 3) {
            align="Derecho";
            generateComments(words, 12, align, false, true, false, pageWidth);
            reportFormatErrors(comments, words, formatErrors, pageWidth, pageHeight, page);
        }

        if (line == numberOfLines - 2) {
            generateComments(words, 14, align, true, false, false, pageWidth);
            reportFormatErrors(comments, words, formatErrors, pageWidth, pageHeight, page);
        }

        if (line == numberOfLines || line == numberOfLines - 1) {
            generateComments(words, 12, align, false, false, false, pageWidth);
            reportFormatErrors(comments, words, formatErrors, pageWidth, pageHeight, page);
        }
        align="Centrado";
    }

    @Override
    public List<FormatErrorReport> getFormatErrors(int page) throws IOException {
        float pageWidth = pdfdocument.getPage(page-1).getMediaBox().getWidth();
        float pageHeight = pdfdocument.getPage(page-1).getMediaBox().getHeight();
        List<FormatErrorReport> formatErrors = new ArrayList<>();
        int numberOfLines = getNumberOfLines(page);
        int cont=1;

        PDFTextStripper pdfStripper = new PDFTextStripper();
        pdfStripper.setStartPage(page);
        pdfStripper.setEndPage(page);
        pdfStripper.setParagraphStart("\n");
        pdfStripper.setSortByPosition(true);

        //Recorre la página linea por linea
        for (String line : pdfStripper.getText(pdfdocument).split(pdfStripper.getParagraphStart())) {

            String[] arr = line.split(" ", 2);
            // Condicional si encuentra una linea en blanco
            if (!arr[0].equals("")) {
                String wordLine = line.trim();
                List<WordsProperties> words = seeker.findWordsFromAPage(page, wordLine);

                if (!words.isEmpty())
                    verifyLine(cont, words, formatErrors, pageWidth, pageHeight, page, numberOfLines);
                cont++;
            }
        }
        return formatErrors;
    }


    public int getNumberOfLines(int page) throws IOException {
        int cont=0;
        PDFTextStripper pdfStripper = new PDFTextStripper();
        pdfStripper.setStartPage(page);
        pdfStripper.setEndPage(page);
        pdfStripper.setParagraphStart("\n");
        pdfStripper.setSortByPosition(true);
        for (String line : pdfStripper.getText(pdfdocument).split(pdfStripper.getParagraphStart())) {

            String[] arr = line.split(" ", 2);

            if (!arr[0].equals("")) {
                cont++;
            }
        }
        return cont;
    }

    private void reportFormatErrors(List<String> comments, List<WordsProperties> words, List<FormatErrorReport> formatErrors, float pageWidth, float pageHeight, int page) {


        if (!comments.isEmpty()) {
            formatErrors.add(new ReportFormatError(counter).reportFormatError(comments, words.get(0), pageWidth, pageHeight, page));
        }
    }
}