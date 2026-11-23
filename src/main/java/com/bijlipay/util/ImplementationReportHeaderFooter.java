package com.bijlipay.util;

import com.bijlipay.config.Constants;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.IOException;

public class ImplementationReportHeaderFooter extends PdfPageEventHelper {
    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        // Add custom footer for the end of the page
        String line1 = "Accepted,";
        String line2 = ".......................(Sign and stamp of the Merchant";
        addFooter(writer, document, line1, line2);
    }

    private void addFooter(PdfWriter writer, Document document, String line1, String line2) {
        int pageNumber = writer.getPageNumber();

        if (pageNumber == 1 || pageNumber == 2 || pageNumber == 3 || pageNumber ==4){
            PdfContentByte canvas = writer.getDirectContent();

            Phrase phrase1 = new Phrase("STPL/OPS/IRR/V1.5_03/2026", FontFactory.getFont(FontFactory.TIMES_ROMAN, 8));

            float x = document.leftMargin();
            float y = document.bottomMargin() - 10;

            ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, phrase1, x, y, 0);
        }else {
            PdfContentByte canvas = writer.getDirectContent();

            Phrase phrase1 = new Phrase(line1, FontFactory.getFont(FontFactory.TIMES_ROMAN, 8));
            Phrase phrase2 = new Phrase(line2, FontFactory.getFont(FontFactory.TIMES_ROMAN, 8));

            float x1 = document.right() - 40;
            float x2 = document.right() - 160;

            float y = document.bottomMargin() - 10;

            ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, phrase1, x1, y, 0);
            ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, phrase2, x2, y - 12, 0);
        }
    }


    private Image image;
    @Override
    public void onStartPage(PdfWriter writer, Document document) {

        int pageNumber = writer.getPageNumber();
        if (pageNumber == 1){
            try {
                image = Image.getInstance(Constants.LOGO_IMAGE);
                image.scaleToFit(100, 100);
            } catch (BadElementException | IOException e) {
                e.printStackTrace();
            }
            // You can adjust the scale of the image if needed
            // Add the image to the header at the top-left corner of each page
            float x = document.top() + 5;
            float y = document.rightMargin();
            try {
                writer.getDirectContent().addImage(image, 100f, 0, 0, 25f, x, y);
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        }
    }
}
