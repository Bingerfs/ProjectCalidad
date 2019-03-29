package com.ucbcba.joel.ucbcorreccionformato.FormatErrors;

import com.ucbcba.joel.ucbcorreccionformato.FormatErrors.HighlightsReport.FormatErrorReport;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class FormatErrorController {

    @RequestMapping(value = "/api/formatErrors/{fileName:.+}",method = RequestMethod.GET)
    public List<FormatErrorReport> greeting(@PathVariable String fileName, @RequestParam(value="coverPage") String coverPage
            , @RequestParam(value="generalIndexPageStart") String generalIndexPageStart, @RequestParam(value="generalIndexPageEnd") String generalIndexPageEnd
            , @RequestParam(value="figureTableIndexPageEnd") String figureTableIndexPageEnd, @RequestParam(value="biographyPage") String biographyPage
            , @RequestParam(value="annexedPage") String annexedPage)  {
        List<FormatErrorReport> formatErrors = new ArrayList<>();
        String dirPdfFile = "uploads/" + fileName;
        PDDocument pdfdocument = null;
        try {
            pdfdocument = PDDocument.load(new File(dirPdfFile));
            FormatErrorDetector formatErrorDetector = new FormatErrorDetector(pdfdocument);
            formatErrorDetector.analyzeFormatPdf(coverPage,generalIndexPageStart,generalIndexPageEnd,figureTableIndexPageEnd,biographyPage,annexedPage);
            formatErrors = formatErrorDetector.getFormatErrorReports();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return formatErrors;
    }
}
