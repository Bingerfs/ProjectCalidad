package com.ucbcba.joel.ucbcorreccionformato.formaterrors.formatrules;

import com.ucbcba.joel.ucbcorreccionformato.formaterrors.formatcontrol.GeneralIndexFormat;
import com.ucbcba.joel.ucbcorreccionformato.formaterrors.highlightsReport.FormatErrorReport;
import com.ucbcba.joel.ucbcorreccionformato.general.GeneralSeeker;
import com.ucbcba.joel.ucbcorreccionformato.general.ReportFormatError;
import com.ucbcba.joel.ucbcorreccionformato.general.WordsProperties;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class GeneralIndexPageFormat implements FormatRule {

    private PDDocument pdfdocument;
    private GeneralSeeker seeker;
    private AtomicLong counter;

    public GeneralIndexPageFormat(PDDocument pdfdocument, AtomicLong counter) {
        this.pdfdocument = pdfdocument;
        this.seeker = new GeneralSeeker(pdfdocument);
        this.counter = counter;
    }

    @Override
    public List<FormatErrorReport> getFormatErrors(int page) throws IOException {
        float pageWidth = pdfdocument.getPage(page-1).getMediaBox().getWidth();
        float pageHeight = pdfdocument.getPage(page-1).getMediaBox().getHeight();
        List<FormatErrorReport> formatErrors = new ArrayList<>();

        PDFTextStripper pdfStripper = new PDFTextStripper();
        pdfStripper.setStartPage(page);
        pdfStripper.setEndPage(page);
        pdfStripper.setParagraphStart("\n");
        pdfStripper.setSortByPosition(true);
        //Recorre la página linea por linea
        for (String line : pdfStripper.getText(pdfdocument).split(pdfStripper.getParagraphStart())) {
            String arr[] = line.split(" ", 2);
            // Condicional si encuentra una linea en blanco
            if (!arr[0].equals("")) {
                String wordLine = line.trim();
                //Condicional paara evitar el control en la paginación
                if (wordLine.length() - wordLine.replaceAll(" ", "").length() >= 1) {
                    List<WordsProperties> words = seeker.findWordsFromAPage(page, wordLine);
                    List<String> comments = new ArrayList<>();
                    if (words.size() == 0) {
                        wordLine = Normalizer.normalize(wordLine, Normalizer.Form.NFD);
                        wordLine = wordLine.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
                        words = seeker.findWordsFromAPage(page, wordLine);
                        if (words.size() == 0) {
                            continue;
                        }
                    }

                    if (wordLine.contains("ÍNDICE GENERAL") || wordLine.contains("Índice general") || wordLine.contains("Índice general")) {
                        comments = new GeneralIndexFormat(words.get(0),12,"Centrado",true,false,true,0).getFormatErrors(pageWidth);
                        reportFormatErrors(comments, words, formatErrors, pageWidth, pageHeight, page);
                        continue;
                    }
                    int numberOfPoints = countChar(arr[0], '.');
                    if (numberOfPoints == 0) {
                        if (!arr[0].equals("Anexo") && !arr[0].equals("ANEXO")) {
                            comments = new GeneralIndexFormat(words.get(0),12,"Izquierdo",true,false,true,0).getFormatErrors(pageWidth);
                            reportFormatErrors(comments, words, formatErrors, pageWidth, pageHeight, page);
                        }
                        continue;
                    }

                    if (numberOfPoints == 1) {
                        comments = new GeneralIndexFormat(words.get(0),12,"Izquierdo",true,false,true,0).getFormatErrors(pageWidth);
                        reportFormatErrors(comments, words, formatErrors, pageWidth, pageHeight, page);
                        continue;
                    }
                    if (numberOfPoints == 2) {
                        comments = new GeneralIndexFormat(words.get(0),12,"Izquierdo",true,false,false,1).getFormatErrors(pageWidth);
                        reportFormatErrors(comments, words, formatErrors, pageWidth, pageHeight, page);
                        continue;
                    }
                    if (numberOfPoints == 3) {
                        comments = new GeneralIndexFormat(words.get(0),12,"Izquierdo",true,true,false,2).getFormatErrors(pageWidth);
                        reportFormatErrors(comments, words, formatErrors, pageWidth, pageHeight, page);
                        continue;
                    }
                    if (numberOfPoints == 4) {
                        comments = new GeneralIndexFormat(words.get(0),12,"Izquierdo",false,true,false,3).getFormatErrors(pageWidth);
                        reportFormatErrors(comments, words, formatErrors, pageWidth, pageHeight, page);
                    }
                }

            }
        }
        return formatErrors;
    }

    private void reportFormatErrors(List<String> comments, List<WordsProperties> words, List<FormatErrorReport> formatErrors, float pageWidth, float pageHeight, int page) {
        if (comments.size() != 0) {
            formatErrors.add(new ReportFormatError(counter).reportFormatError(comments, words.get(0), pageWidth, pageHeight, page));
        }
    }

    public int countChar(String str, char c)
    {
        int count = 0;
        for(int i=0; i < str.length(); i++)
        {    if(str.charAt(i) == c)
            count++;
        }
        return count;
    }
}
