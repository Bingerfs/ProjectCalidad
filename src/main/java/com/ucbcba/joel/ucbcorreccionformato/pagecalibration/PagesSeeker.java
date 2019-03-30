package com.ucbcba.joel.ucbcorreccionformato.pagecalibration;

import com.ucbcba.joel.ucbcorreccionformato.general.GeneralSeeker;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.IOException;
import java.text.Normalizer;

public class PagesSeeker {

    private PDDocument pdfdocument;
    private GeneralSeeker generalSeeker;

    public PagesSeeker(PDDocument pdfdocument){
        this.pdfdocument = pdfdocument;
        this.generalSeeker = new GeneralSeeker(pdfdocument);
    }

    public boolean isTheCoverInThisPage(int page) throws IOException {
        boolean bool1,bool2, bool3, bool4, bool5, bool6,bool7, bool8, bool9, bool10,bool11,bool12, bool13, bool14, bool15;
        bool1 = generalSeeker.isTheWordInThePage(page,"Boliviana");
        bool2 = generalSeeker.isTheWordInThePage(page,"boliviana");
        bool3 = generalSeeker.isTheWordInThePage(page,"BOLIVIANA");
        bool4 = generalSeeker.isTheWordInThePage(page,"Regional");
        bool5 = generalSeeker.isTheWordInThePage(page,"regional");
        bool6 = generalSeeker.isTheWordInThePage(page,"REGIONAL");
        bool7 = generalSeeker.isTheWordInThePage(page,"Departamento");
        bool8 = generalSeeker.isTheWordInThePage(page,"departamento");
        bool9 = generalSeeker.isTheWordInThePage(page,"DEPARTAMENTO");
        bool10 = generalSeeker.isTheWordInThePage(page,"Carrera");
        bool11 = generalSeeker.isTheWordInThePage(page,"carrera");
        bool12 = generalSeeker.isTheWordInThePage(page,"CARRERA");
        bool13 = generalSeeker.isTheWordInThePage(page,"– Bolivia");
        bool14 = generalSeeker.isTheWordInThePage(page,"– bolivia");
        bool15 = generalSeeker.isTheWordInThePage(page,"– BOLIVIA");
        return getNumberOfTrues(bool1,bool2, bool3, bool4, bool5, bool6,bool7, bool8, bool9, bool10,bool11,bool12, bool13, bool14, bool15) >= 3;
    }

    public int getCoverPage() throws IOException {
        int resp = 0;
        for (int page = 1; page <= pdfdocument.getNumberOfPages(); page++) {
            if ( isTheCoverInThisPage(page) ){
                return page;
            }
        }
        return resp;
    }


    public boolean isTheGeneralIndexInThisPage(int page) throws IOException {
        return generalSeeker.isTheWordInThePage(page,"..........");
    }

    public int getFirstGeneralIndexPage() throws IOException {
        int resp = 0;
        for (int page = 1; page <= pdfdocument.getNumberOfPages(); page++) {
            if ( isTheGeneralIndexInThisPage(page) ){
                return page;
            }
        }
        return resp;
    }


    public boolean isTheFigureTableIndexInThisPage(int page) throws IOException {
        boolean bool1,bool2,bool3,bool4;
        bool1 = generalSeeker.isTheWordInThePage(page,"Figura");
        bool2 = generalSeeker.isTheWordInThePage(page,"FIGURA");
        bool3 = generalSeeker.isTheWordInThePage(page,"TABLA");
        bool4 = generalSeeker.isTheWordInThePage(page,"Tabla");
        return getNumberOfTrues(bool1,bool2,bool3,bool4)>=1;
    }

    public int getLastGeneralIndexPage(int generalIndexPageStart) throws IOException {
        int resp = 0;
        if(generalIndexPageStart == 0){
            return resp;
        }
        for (int page = generalIndexPageStart; page <= pdfdocument.getNumberOfPages(); page++) {
            if ( isTheGeneralIndexInThisPage(page) && !isTheFigureTableIndexInThisPage(page)){
                resp = page;
            }else{
                return resp;
            }
        }
        return resp;
    }


    public int getLastFigureTableIndexPage (int generalIndexPageStart) throws IOException {
        int resp = 0;
        if(generalIndexPageStart == 0){
            return resp;
        }
        for (int page = generalIndexPageStart; page <= pdfdocument.getNumberOfPages(); page++) {
            if ( isTheGeneralIndexInThisPage(page) ){
                resp = page;
            }else{
                return resp;
            }
        }
        return resp;
    }

    public boolean isTheFirsBiographyInThisPage(int page) throws IOException {
        boolean bool1,bool2,bool3,bool4;
        bool1 = generalSeeker.isTheWordInThePage(page,"BIBLIOGRAFÍA");
        bool2 = generalSeeker.isTheWordInThePage(page,"Bibliografía");
        String word = Normalizer.normalize("BIBLIOGRAFÍA", Normalizer.Form.NFD);
        word = word.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        bool3 = generalSeeker.isTheWordInThePage(page,word);
        word = Normalizer.normalize("Bibliografía", Normalizer.Form.NFD);
        word = word.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        bool4 = generalSeeker.isTheWordInThePage(page,word);
        return getNumberOfTrues(bool1,bool2,bool3,bool4) >= 1;
    }

    public int getFirstBiographyPage() throws IOException {
        int resp = pdfdocument.getNumberOfPages()+1;
        for (int page = pdfdocument.getNumberOfPages(); page >= 1; page--) {
            if ( isTheFirsBiographyInThisPage(page) ){
                return page;
            }
        }
        return resp;
    }


    public boolean isTheFirstAnnexInThisPage(int page) throws IOException {
        boolean bool1,bool2,bool3,bool4,bool5,bool6,bool7,bool8,bool9,bool10,bool11,bool12;
        bool1 = generalSeeker.isTheWordInThePage(page,"Anexo 1 ");
        bool2 = generalSeeker.isTheWordInThePage(page,"anexo 1 ");
        bool3 = generalSeeker.isTheWordInThePage(page,"ANEXO 1 ");
        bool4 = generalSeeker.isTheWordInThePage(page,"Anexo 1:");
        bool5 = generalSeeker.isTheWordInThePage(page,"anexo 1:");
        bool6 = generalSeeker.isTheWordInThePage(page,"ANEXO 1:");
        bool7 = generalSeeker.isTheWordInThePage(page,"Anexo 1,");
        bool8 = generalSeeker.isTheWordInThePage(page,"anexo 1,");
        bool9 = generalSeeker.isTheWordInThePage(page,"ANEXO 1,");
        bool10 = generalSeeker.isTheWordInThePage(page,"Anexo 1.");
        bool11 = generalSeeker.isTheWordInThePage(page,"anexo 1.");
        bool12 = generalSeeker.isTheWordInThePage(page,"ANEXO 1.");
        return getNumberOfTrues(bool1,bool2,bool3,bool4,bool5,bool6,bool7,bool8,bool9,bool10,bool11,bool12) >= 1;
    }


    public int getFirstAnnexedPage() throws IOException {
        int resp = pdfdocument.getNumberOfPages()+1;
        for (int page = pdfdocument.getNumberOfPages(); page >= 1; page--) {
            if ( isTheFirstAnnexInThisPage(page) ){
                return page;
            }
        }
        return resp;
    }



    public int getNumberOfTrues(boolean... vars) {
        int count = 0;
        for (boolean var : vars) {
            count += (var ? 1 : 0);
        }
        return count;
    }
}
