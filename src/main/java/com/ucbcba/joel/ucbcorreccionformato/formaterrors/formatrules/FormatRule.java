package com.ucbcba.joel.ucbcorreccionformato.formaterrors.formatrules;

import com.ucbcba.joel.ucbcorreccionformato.formaterrors.highlightsreport.FormatErrorReport;

import java.io.IOException;
import java.util.List;

public interface FormatRule {
    List<FormatErrorReport> getFormatErrors(int page) throws IOException;
}
