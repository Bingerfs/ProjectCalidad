package com.ucbcba.joel.ucbcorreccionformato.formaterrors.FormatRules;

import com.ucbcba.joel.ucbcorreccionformato.formaterrors.formatControl.FigureNumerationFormat;
import com.ucbcba.joel.ucbcorreccionformato.formaterrors.formatControl.Format;
import com.ucbcba.joel.ucbcorreccionformato.formaterrors.HighlightsReport.*;
import com.ucbcba.joel.ucbcorreccionformato.formaterrors.ImagesOnPdf.ImageLocations;
import com.ucbcba.joel.ucbcorreccionformato.formaterrors.ImagesOnPdf.PdfImage;
import com.ucbcba.joel.ucbcorreccionformato.general.GeneralSeeker;
import com.ucbcba.joel.ucbcorreccionformato.general.ReportFormatError;
import com.ucbcba.joel.ucbcorreccionformato.general.WordsProperties;
import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.util.Matrix;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class FiguresFormat implements FormatRule {

    private PDDocument pdfdocument;
    private GeneralSeeker seeker;
    private PDPage page;
    private AtomicLong counter;
    private AtomicLong figureNumeration;

    public FiguresFormat(PDDocument pdfdocument, AtomicLong counter, PDPage page,AtomicLong figureNumeration){
        this.pdfdocument = pdfdocument;
        this.seeker = new GeneralSeeker(pdfdocument);
        this.page = page;
        this.counter = counter;
        this.figureNumeration = figureNumeration;
    }

    @Override
    public List<FormatErrorReport> getFormatErrors(int pageNum) throws IOException {
        float pageWidth = pdfdocument.getPage(pageNum-1).getMediaBox().getWidth();
        float pageHeight = pdfdocument.getPage(pageNum-1).getMediaBox().getHeight();
        List<FormatErrorReport> formatErrors = new ArrayList<>();
        final List<PdfImage> pdfImages = new ArrayList<PdfImage>();
        ImageLocations imageLocations = new ImageLocations(){
            @Override
            protected void processOperator(Operator operator, List<COSBase> operands) throws IOException
            {
                String operation = operator.getName();
                if( "Do".equals(operation) )
                {
                    COSName objectName = (COSName) operands.get( 0 );
                    PDXObject xobject = getResources().getXObject( objectName );
                    if( xobject instanceof PDImageXObject)
                    {
                        PDImageXObject image = (PDImageXObject)xobject;
                        Matrix ctmNew = getGraphicsState().getCurrentTransformationMatrix();
                        pdfImages.add(new PdfImage(image,ctmNew,pageNum,pageHeight));
                    }
                    else if(xobject instanceof PDFormXObject)
                    {
                        PDFormXObject form = (PDFormXObject)xobject;
                        showForm(form);
                    }
                }
                else
                {
                    super.processOperator( operator, operands);
                }
            }
        };
        imageLocations.processPage(page);

        List<String> comments = new ArrayList<>();
        if (pdfImages.size()<4) {
            // Sorting
            Collections.sort(pdfImages, new Comparator<PdfImage>() {
                @Override
                public int compare(PdfImage pdfImage1, PdfImage pdfImage2)
                {
                    return (int) (pdfImage1.getY() - pdfImage2.getY());
                }
            });
            for (PdfImage image : pdfImages) {
                List<String> commentsFigure = new ArrayList<>();
                WordsProperties figureNumerationWord = seeker.findFigureNumeration(image, pageNum);
                if (figureNumerationWord != null) {
                    comments = new FigureNumerationFormat(figureNumerationWord, 12, "Centrado", true, false, figureNumeration.get()).getFormatErrors(pageWidth);
                    reportFormatErrors(comments, figureNumerationWord, formatErrors, pageWidth, pageHeight, pageNum);
                } else {
                    commentsFigure.add("Tenga el título de la numeración = 'Figura " + figureNumeration + "'");
                }

                WordsProperties figureSource = seeker.findFigureSource(image, pageNum);
                if (figureSource != null) {
                    comments = new Format(figureSource, 12, "Centrado", false, false).getFormatErrors(pageWidth);
                    reportFormatErrors(comments, figureSource, formatErrors, pageWidth, pageHeight, pageNum);
                } else {
                    commentsFigure.add("Tenga la fuente de la figura");
                }

                if (commentsFigure.size() != 0) {
                    StringBuilder commentStr = new StringBuilder();
                    for (int i = 0; i < commentsFigure.size(); i++) {
                        if (i != 0) {
                            commentStr.append(" - ").append(commentsFigure.get(i));
                        } else {
                            commentStr.append(commentsFigure.get(i));
                        }
                    }
                    commentStr.append(".");
                    String comment = commentStr.toString();
                    reportFigureErrors(formatErrors, image, comment, pageWidth, pageHeight, pageNum);
                }
                figureNumeration.incrementAndGet();
            }
        }
        return formatErrors;
    }

    private void reportFigureErrors(List<FormatErrorReport> formatErrors, PdfImage image, String coment,float pageWidth, float pageHeight, int page) {
        Content content = new Content("Imagen");
        BoundingRect boundingRect = new BoundingRect(image.getX(), image.getY() , image.getEndX(),image.getEndY(),pageWidth,pageHeight);
        List<BoundingRect> boundingRects = new ArrayList<>();
        boundingRects.add(boundingRect);
        Position position = new Position(boundingRect,boundingRects,page);
        Comment comment = new Comment(coment,"");
        formatErrors.add(new FormatErrorReport(content,position,comment,String.valueOf(counter.incrementAndGet())));
    }

    private void reportFormatErrors(List<String> comments, WordsProperties words, List<FormatErrorReport> formatErrors, float pageWidth, float pageHeigh, int page) {
        if (comments.size() != 0) {
            formatErrors.add(new ReportFormatError(counter).reportFormatError(comments, words, pageWidth, pageHeigh, page));
        }
    }
}