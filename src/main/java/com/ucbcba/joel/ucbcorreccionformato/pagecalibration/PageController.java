package com.ucbcba.joel.ucbcorreccionformato.pagecalibration;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class PageController {

    @RequestMapping(value = "/api/getPages/{fileName:.+}", method = RequestMethod.GET)
    public List<String> getPages(@PathVariable String fileName)  {
        List<String> pages = new ArrayList<>();
        String dirPdfFile = "uploads/" + fileName;
        PDDocument pdfdocument = null;
        try {
            pdfdocument = PDDocument.load(new File(dirPdfFile));
            PdfDocument document = new PdfDocument(pdfdocument);
            pages.add(Integer.toString(document.getCoverPage()));
            pages.add(Integer.toString(document.getGeneralIndexPageStart()));
            pages.add(Integer.toString(document.getGeneralIndexPageEnd()));
            pages.add(Integer.toString(document.getFigureTableIndexPageEnd()));
            pages.add(Integer.toString(document.getBiographyPageStart()));
            pages.add(Integer.toString(document.getAnnexedPageStart()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pages;
    }
}
