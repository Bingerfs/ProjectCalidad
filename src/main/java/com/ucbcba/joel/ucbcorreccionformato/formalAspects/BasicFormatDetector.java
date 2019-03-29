package com.ucbcba.joel.ucbcorreccionformato.formalAspects;

import com.ucbcba.joel.ucbcorreccionformato.General.GeneralSeeker;
import com.ucbcba.joel.ucbcorreccionformato.General.WordsProperties;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

public class BasicFormatDetector {
    private PDDocument pdfdocument;
    private GeneralSeeker seeker;
    private List<BasicFormatReport> basicFormatReports = new ArrayList<>();

    public BasicFormatDetector(PDDocument pdfdocument) {
        this.pdfdocument = pdfdocument;
        this.seeker = new GeneralSeeker(pdfdocument);
    }

    public void analyzeBasicFormat(String figureTableIndexPageEnd, String annexedPage) throws IOException {
        int indexPageEndI = Integer.parseInt(figureTableIndexPageEnd);
        int annexedPageI = Integer.parseInt(annexedPage);
        int midlePage = indexPageEndI+annexedPageI;
        midlePage = midlePage/2;
        basicFormatReports.addAll(getBasicFormatReport(midlePage));
    }

    public List<BasicFormatReport> getBasicFormatReport(int page) throws IOException {
        List<BasicFormatReport> resp = new ArrayList<>();

        String formatSize = "Tamaño de hoja carta";
        boolean isCorrectSize = false;
        float pageWidth = pdfdocument.getPage(page-1).getMediaBox().getWidth();
        float pageHeight = pdfdocument.getPage(page-1).getMediaBox().getHeight();
        if (pageWidth == 612.0 && pageHeight == 792.0){
            isCorrectSize = true;
        }
        resp.add(new BasicFormatReport(formatSize,isCorrectSize));


        String formatMargin = "Margen 3cm (derecho, inferior y superior) 3.5cm (izquierdo)";
        boolean isCorrectMargin = true;

        String formatFont = "Tipo de letra: Times New Roman 12";
        boolean isCorrectFont = true;

        String formatNumeration = "Numeración parte inferior";
        boolean isCorrectNumeration = false;

        // Recorre el PDF linea por linea
        PDFTextStripper pdfStripper = new PDFTextStripper();
        pdfStripper.setStartPage(page);
        pdfStripper.setEndPage(page);
        pdfStripper.setParagraphStart("\n");
        pdfStripper.setSortByPosition(true);
        for (String line : pdfStripper.getText(pdfdocument).split(pdfStripper.getParagraphStart())) {
            String arr[] = line.split(" ", 2);
            if (!arr[0].equals("")) {
                String wordLine = line.trim();
                // En caso que encuentre la numeración de la página
                if (wordLine.length() - wordLine.replaceAll(" ", "").length() >= 1) {
                    List<WordsProperties> words = seeker.findWordsFromAPage(page,wordLine);
                    // En caso que no se encuentre la linea del PDF la vuelve a buscar normalizandola
                    if (words.size() == 0) {
                        wordLine = Normalizer.normalize(wordLine, Normalizer.Form.NFD);
                        wordLine = wordLine.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
                        words = seeker.findWordsFromAPage(page, wordLine);
                        if (words.size() == 0) {
                            continue;
                        }
                    }
                    for(WordsProperties word:words){
                        if (word.getX() < 95 || word.getYPlusHeight() < 80 || word.getXPlusWidth() > 530 || word.getY() > 705){
                            isCorrectMargin = false;
                        }
                        if(!word.getFontBassic().contains("Times") || !word.getFontBassic().contains("New") || !word.getFontBassic().contains("Roman") || word.getFontSizeBasic()!=12){
                            isCorrectFont = false;
                        }
                    }
                }
                else {
                    List<WordsProperties> words = seeker.findWordsFromAPage( page,wordLine);
                    if (words.size() != 0){
                        if (words.get(words.size()-1).getY() > 720) {
                            isCorrectNumeration = true;
                        }
                    }
                }
            }
        }
        resp.add(new BasicFormatReport(formatMargin,isCorrectMargin));
        resp.add(new BasicFormatReport(formatFont,isCorrectFont));
        resp.add(new BasicFormatReport(formatNumeration,isCorrectNumeration));

        return resp;
    }

    public List<BasicFormatReport> getBasicFormatReports() {
        return basicFormatReports;
    }

    public void setBasicFormatReports(List<BasicFormatReport> basicFormatReports) {
        this.basicFormatReports = basicFormatReports;
    }
}
