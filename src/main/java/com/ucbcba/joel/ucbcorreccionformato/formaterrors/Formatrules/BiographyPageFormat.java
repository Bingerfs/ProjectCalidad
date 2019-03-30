package com.ucbcba.joel.ucbcorreccionformato.formaterrors.Formatrules;

import com.ucbcba.joel.ucbcorreccionformato.formaterrors.Bibliographies.PatternBibliographyReferences;
import com.ucbcba.joel.ucbcorreccionformato.formaterrors.highlightsreport.*;
import com.ucbcba.joel.ucbcorreccionformato.General.GeneralSeeker;
import com.ucbcba.joel.ucbcorreccionformato.General.WordsProperties;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BiographyPageFormat implements  FormatRule {

    private PDDocument pdfdocument;
    private GeneralSeeker seeker;
    private AtomicLong counter;

    public BiographyPageFormat(PDDocument pdfdocument, AtomicLong counter){
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
        List<String> ref_bibliografy = new ArrayList<>();
        boolean end=false;
        //Recorre la página linea por linea
        for (String line : pdfStripper.getText(pdfdocument).split(pdfStripper.getParagraphStart())) {
            String arr[] = line.split(" ", 2);
            // Condicional si encuentra una linea en blanco
            if (!arr[0].equals("")) {
                String wordLine = line.trim();
                if (!wordLine.contains("BIBLIOGRAFÍA") && !wordLine.contains("Bibliografía") && !wordLine.contains("BIBLIOGRAFÍA")) {
                    //Condicional paara evitar el control en la paginación
                    if ((wordLine.length() - wordLine.replaceAll(" ", "").length() >= 1) || wordLine.length() > 4) {
                        if (wordLine.charAt(0) == '[') {
                            StringBuilder bibliographic = new StringBuilder();
                            for (String lines : ref_bibliografy) {
                                bibliographic.append(lines).append(" ");
                            }
                            if(bibliographic.length()!=0){
                                List<String> comments = new ArrayList<>();
                                PatternBibliographyReferences pattern = getpattern(bibliographic.toString());
                                if (pattern!=null) {
                                    Matcher matcher = pattern.getMatcher(bibliographic.toString());
                                    if (!matcher.find()) {
                                        comments.add("La referencia en "+pattern.getName()+".");
                                    }
                                }else{
                                    comments.add("Consultar la Guía para la presentación de trabajos académicos.");
                                }
                                reportFormatErrors(comments, ref_bibliografy, formatErrors, pageWidth, pageHeight, page);
                            }
                            ref_bibliografy = new ArrayList<>();
                            ref_bibliografy.add(wordLine);
                        } else {
                            ref_bibliografy.add(wordLine);
                        }
                    }
                }
            }
        }
        StringBuilder bibliographic = new StringBuilder();
        for (String lines : ref_bibliografy) {
            bibliographic.append(lines).append(" ");
        }
        if(bibliographic.length()!=0){
            List<String> comments = new ArrayList<>();
            PatternBibliographyReferences pattern = getpattern(bibliographic.toString());
            if (pattern!=null) {
                Matcher matcher = pattern.getMatcher(bibliographic.toString());
                if (!matcher.find()) {
                    comments.add("La referencia en "+pattern.getName()+".");
                }
            }else{
                comments.add("Consultar la Guía para la presentación de trabajos académicos.");
            }
            reportFormatErrors(comments, ref_bibliografy, formatErrors, pageWidth, pageHeight, page);
        }
        return formatErrors;
    }

    private void reportFormatErrors(List<String> comments, List<String> ref_bibliografy, List<FormatErrorReport> formatErrors, float pageWidth, float pageHeight, int page) throws IOException {
        if (comments.size() != 0) {
            List<BoundingRect> boundingRects = new ArrayList<>();
            String contentText = "";
            float x = 0,y=0,endX=0,upperY=0;
            for (int i = 0;i<ref_bibliografy.size();i++){
                String lineWord = ref_bibliografy.get(i);
                List<WordsProperties> lineWordWithProperties = seeker.findWordsFromAPage(page,lineWord);
                int linewordwithpropertiessize = lineWordWithProperties.size();
                if (linewordwithpropertiessize == 0) {
                    lineWord = Normalizer.normalize(lineWord, Normalizer.Form.NFD);
                    lineWord = lineWord.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
                    lineWordWithProperties = seeker.findWordsFromAPage(page, lineWord);
                }
                if (linewordwithpropertiessize !=0){
                    WordsProperties word = lineWordWithProperties.get(0);
                    BoundingRect boundingRect = new BoundingRect(word.getX(), word.getYPlusHeight(), word.getXPlusWidth(),word.getY(),pageWidth,pageHeight);
                    boundingRects.add(boundingRect);
                    if (i==0){
                        x = word.getX();
                        upperY = word.getYPlusHeight();
                        contentText = lineWord;
                    }
                    if (i==ref_bibliografy.size()-1){
                        endX = word.getXPlusWidth();
                        y = word.getY();
                    }
                }
            }
            BoundingRect mainBoundingRect = new BoundingRect(x, upperY, endX,y,pageWidth,pageHeight);
            Position position = new Position(mainBoundingRect,boundingRects,page);
            Content content = new Content(contentText);
            Comment comment = new Comment(comments.get(0),"");
            String id = String.valueOf(counter.incrementAndGet());
            formatErrors.add(new FormatErrorReport(content,position,comment,id));
        }
    }


    public PatternBibliographyReferences getpattern(String lineWord){

        Pattern compile = Pattern.compile("([^(]+\\([^)]+\\)\\.|[^(]+\\([dir.compe]+\\)[^(]*\\([^)]+\\)\\.)[^“]*“[^”]+”\\.[^<]*<[^>]+>[^<]*<[^>]+>[^(]*\\(fecha de consulta.+", Pattern.CASE_INSENSITIVE);
        Pattern discussionsBibliographies = compile;
        PatternBibliographyReferences discussionList = new PatternBibliographyReferences("Listas de discusión",discussionsBibliographies);

        Pattern pageWebBibliography = Pattern.compile("([^(]+\\([^)]+\\)\\.|[^(]+\\([dir.compe]+\\)[^(]*\\([^)]+\\)\\.)[^“]*“[^”]+”\\.[^E]*En:[^<]*<[^>]+>[^,]*,[^(]*\\(fecha de consulta.+", Pattern.CASE_INSENSITIVE);
        PatternBibliographyReferences pageWeb = new PatternBibliographyReferences("Página web",pageWebBibliography);

        Pattern emailBibliography = Pattern.compile("[^(]+\\([^)]+\\)[^(]*\\([^)]+\\)\\.[^“]*“[^”]+”\\.[^(]*\\([^)]+\\)[^(]*\\(fecha del mensaje.+", Pattern.CASE_INSENSITIVE);
        PatternBibliographyReferences email = new PatternBibliographyReferences("Correo electrónico",emailBibliography);

        Pattern radioBibliography = Pattern.compile("([^(]+\\([^)]+\\)\\.|[^(]+\\([dir.compe]+\\)[^(]*\\([^)]+\\)\\.)[^“]*“[^”]+”\\.[^(]*\\([^)]+\\).+", Pattern.CASE_INSENSITIVE);
        PatternBibliographyReferences radio = new PatternBibliographyReferences("Programa de radio ",radioBibliography);

        Pattern cdRomDVDBibliography = Pattern.compile("([^(]+\\([^)]+\\)\\.|[^(]+\\([dir.compe]+\\)[^(]*\\([^)]+\\)\\.)[^:]+:.+", Pattern.CASE_INSENSITIVE);
        PatternBibliographyReferences cdRomDVD = new PatternBibliographyReferences("Libro en soporte CD-ROM/DVD",cdRomDVDBibliography);

        Pattern thesisBibliography = Pattern.compile("[^(]+\\([^)]+\\)\\..+", Pattern.CASE_INSENSITIVE);
        PatternBibliographyReferences thesis = new PatternBibliographyReferences("Tesis/Trabajo de titulación",thesisBibliography);

        Pattern articleMagazineBibliography = Pattern.compile("([^(]+\\([^)]+\\)\\.|[^(]+\\([dir.compe]+\\)[^(]*\\([^)]+\\)\\.)[^“]*“[^”]+”\\.[^E]*En:.+Año.+N.+", Pattern.CASE_INSENSITIVE);
        PatternBibliographyReferences articleMagazine = new PatternBibliographyReferences("Artículo de revista",articleMagazineBibliography);

        Pattern chapterBookBibliography = Pattern.compile("([^(]+\\([^)]+\\)\\.|[^(]+\\([dir.compe]+\\)[^(]*\\([^)]+\\)\\.)[^“]*“[^”]+”\\.[^E]*En:[^:]+:.+", Pattern.CASE_INSENSITIVE);
        PatternBibliographyReferences chapterBook = new PatternBibliographyReferences("Capítulo de libro",chapterBookBibliography);

        Pattern articleNewspaperBibliography = Pattern.compile("([^(]+\\([^)]+\\)\\.|[^(]+\\([dir.compe]+\\)[^(]*\\([^)]+\\)\\.)[^“]*“[^”]+”\\.[^E]*En:[^(]+\\([^)]+\\).+", Pattern.CASE_INSENSITIVE);
        PatternBibliographyReferences articleNewspaper = new PatternBibliographyReferences("Artículo de periódico",articleNewspaperBibliography);

        Pattern conferenceArtworksBibliography = Pattern.compile("([^(]+\\([^)]+\\)\\.|[^(]+\\([dir.compe]+\\)[^(]*\\([^)]+\\)\\.)[^“]*“[^”]+”\\.[^E]*En:.+", Pattern.CASE_INSENSITIVE);
        PatternBibliographyReferences conferenceArtworks = new PatternBibliographyReferences("Congreso/Conferencia",conferenceArtworksBibliography);

        Pattern moviesBibliography = Pattern.compile("([^(]+\\([^)]+\\)\\.|[^(]+\\([dir.compe]+\\)[^(]*\\([^)]+\\)\\.)[^“]*“[^”]+”\\.[^:]+:.+", Pattern.CASE_INSENSITIVE);
        PatternBibliographyReferences movies = new PatternBibliographyReferences("Película",moviesBibliography);

        Pattern bookBibliography = Pattern.compile("([^(]+\\([^)]+\\)\\.|[^(]+\\([dir.compe]+\\)[^(]*\\([^)]+\\)\\.)[^:]+:.+", Pattern.CASE_INSENSITIVE);
        PatternBibliographyReferences book = new PatternBibliographyReferences("Libro",bookBibliography);


        if (lineWord.contains("http")){
            if( lineWord.contains("@")){
                return discussionList;
            }else{
                return pageWeb;
            }
        }

        if (lineWord.contains("@")){
            return email;
        }

        if (lineWord.contains("FM,") || lineWord.contains("AM,")){
            return radio;
        }

        if (lineWord.contains("CD-ROM") || lineWord.contains("DVD")){
            return cdRomDVD;
        }

        if (lineWord.contains("licenciatura") || lineWord.contains("Licenciatura") || lineWord.contains("titulación") || lineWord.contains("Titulación")){
            return thesis;
        }

        if (lineWord.contains("En:")){
            if( lineWord.contains("N°") || lineWord.contains(", Año")){
                return articleMagazine;
            }
            Matcher matcher = chapterBookBibliography.matcher(lineWord);
            if (matcher.find()){
                return chapterBook;
            }
            matcher = articleNewspaperBibliography.matcher(lineWord);
            if (matcher.find()){
                return articleNewspaper;
            }
            matcher = conferenceArtworksBibliography.matcher(lineWord);
            if (matcher.find()){
                return conferenceArtworks;
            }
            return null;
        }

        if (lineWord.contains(":")){
            if( lineWord.contains("“")){
                return movies;
            }else{
                return book;
            }
        }

        return null;
    }
}