package com.ucbcba.joel.ucbcorreccionformato.formatErrors.ImagesOnPdf;

import org.apache.pdfbox.contentstream.PDFStreamEngine;
import org.apache.pdfbox.contentstream.operator.DrawObject;
import org.apache.pdfbox.contentstream.operator.state.*;

import java.io.IOException;

public class ImageLocations extends PDFStreamEngine {

    public ImageLocations() throws IOException {
        addOperator(new Concatenate());
        addOperator(new DrawObject());
        addOperator(new SetGraphicsStateParameters());
        addOperator(new Save());
        addOperator(new Restore());
        addOperator(new SetMatrix());
    }
}