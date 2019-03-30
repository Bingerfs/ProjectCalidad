package com.ucbcba.joel.ucbcorreccionformato.formaterrors;

import com.ucbcba.joel.ucbcorreccionformato.formaterrors.formatrules.*;
import com.ucbcba.joel.ucbcorreccionformato.formaterrors.highlightsreport.FormatErrorReport;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class FormatErrorDetector {
    private PDDocument pdfdocument;
    private List<FormatErrorReport> formatErrorReports = new ArrayList<>();

    private final AtomicLong idHighlights = new AtomicLong();
    private final AtomicLong figureNumeration = new AtomicLong();


    public FormatErrorDetector(PDDocument pdfdocument) throws IOException {
        this.pdfdocument = pdfdocument;
        figureNumeration.incrementAndGet();
    }

    public List<FormatErrorReport> getFormatErrorReports() {
        return formatErrorReports;
    }

    public void analyzeFormatPdf(String coverPage, String generalIndexPageStart, String generalIndexPageEnd, String figureTableIndexPageEnd, String biographyPage, String annexedPage) throws IOException {
        int coverPageI = Integer.parseInt(coverPage);
        int generalIndexPageStartI = Integer.parseInt(generalIndexPageStart);
        int generalIndexPageEndI = Integer.parseInt(generalIndexPageEnd);
        int figureTableIndexPageEndI = Integer.parseInt(figureTableIndexPageEnd);
        int biographyPageI = Integer.parseInt(biographyPage);
        int annexedPageI = Integer.parseInt(annexedPage);

        int pageNumerationAnnexes = 1;
        int page = 0;
        for( PDPage pdfPage : pdfdocument.getPages() ) {
            page++;
            if (page == coverPageI){
                FormatRule coverPageFormat = new CoverPageFormat(pdfdocument, idHighlights);
                formatErrorReports.addAll(coverPageFormat.getFormatErrors(page));
                continue;
            }

            if (page >= generalIndexPageStartI && page <= generalIndexPageEndI){
                FormatRule generalIndexPageFormat = new GeneralIndexPageFormat(pdfdocument, idHighlights);
                formatErrorReports.addAll(generalIndexPageFormat.getFormatErrors(page));
                continue;
            }

            if (page <= figureTableIndexPageEndI){
                continue;
            }

            if (page >= biographyPageI && page <= annexedPageI-1 ){
                FormatRule generalIndexPageFormat = new BiographyPageFormat(pdfdocument, idHighlights);
                formatErrorReports.addAll(generalIndexPageFormat.getFormatErrors(page));
                continue;
            }

            if ( page < annexedPageI){
                FormatRule figuresFormat = new FiguresFormat(pdfdocument, idHighlights,pdfPage,figureNumeration);
                formatErrorReports.addAll(figuresFormat.getFormatErrors(page));


                FormatRule pageNumerationFormat = new PageNumerationFormat(pdfdocument, idHighlights,page);
                formatErrorReports.addAll(pageNumerationFormat.getFormatErrors(page));
            }else{
                FormatRule pageNumerationFormat = new PageNumerationFormat(pdfdocument, idHighlights,pageNumerationAnnexes);
                formatErrorReports.addAll(pageNumerationFormat.getFormatErrors(page));
                pageNumerationAnnexes++;
            }

        }
    }

}
