package com.bijlipay.util;

import com.bijlipay.config.Constants;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.IOException;


public class HeaderFooterPageEvent extends PdfPageEventHelper {
//    String line1 = "End Page Line 1";
//    String line2 = "End Page Line 2";
//    Font ffont = new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL);
//
//    public void onEndPage(PdfWriter writer, Document document) {
//        int pageNumber = writer.getPageNumber();
//
//        if (pageNumber == 1 || pageNumber == 2 || pageNumber == 3) {
//            PdfContentByte cb = writer.getDirectContent();
////        Phrase header = new Phrase("this is a header", ffont);
//            Phrase footer = new Phrase("Please do not pay cash - Transfer only to company account", ffont);
////        ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
////                header,
////                (document.right() - document.left()) / 2 + document.leftMargin(),
////                document.top() + 10, 0);
//            ColumnText.showTextAligned(cb, Element.ALIGN_RIGHT,
//                    footer,
//                    (document.right() - document.left()) / 2.5f + document.leftMargin(),
//                    document.bottom() - 10, 0);
//        }else {
//            PdfContentByte cb = writer.getDirectContent();
////        Phrase header = new Phrase("this is a header", ffont);
//            Phrase footer = new Phrase("Accepted,\n.......................(Sign and stamp of the Merchant)", ffont);
////        ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
////                header,
////                (document.right() - document.left()) / 2 + document.leftMargin(),
////                document.top() + 10, 0);
//            ColumnText.showTextAligned(cb, Element.ALIGN_RIGHT,
//                    footer,
//                    (document.right() - document.left()) / 2.5f + document.leftMargin(),
//                    document.bottom() - 10, 0);
//        }
//    }

    private Image soSign;

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

            Phrase phrase1 = new Phrase("Please do not pay cash - Transfer only to company account", FontFactory.getFont(FontFactory.TIMES_ROMAN, 8));

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
            float x = document.leftMargin();
            float y = document.top() + 5;
            try {
                writer.getDirectContent().addImage(image, 100f, 0, 0, 25f, x, y);
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        }
    }

    //import com.itextpdf.text.*;
    //import com.itextpdf.text.pdf.*;
    //
    //import java.io.FileNotFoundException;
    //import java.io.FileOutputStream;
    //import java.io.IOException;
    //import java.net.URL;
    //
    //public class ImageHeaderEvent extends PdfPageEventHelper {
    //
    //    private Image image;
    //
    //    public ImageHeaderEvent(String imagePath) throws IOException, BadElementException {
    //        // Load the image from the file path or URL
    //        this.image = Image.getInstance(imagePath);
    //        // You can adjust the scale of the image if needed
    //        this.image.scaleToFit(100, 100);
    //    }
    //
    //    @Override
    //    public void onEndPage(PdfWriter writer, Document document) {
    //        // Add the image to the header at the top-left corner of each page
    //        float x = document.leftMargin();
    //        float y = document.top() + 10;
    //        try {
    //            writer.getDirectContent().addImage(image, image.getWidth(), 0, 0, image.getHeight(), x, y);
    //        } catch (DocumentException e) {
    //            e.printStackTrace();
    //        }
    //    }
    //
    //    public static void generatePdf(String filePath) throws DocumentException, FileNotFoundException, IOException {
    //        Document document = new Document();
    //        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filePath));
    //
    //        // Specify the path or URL of the image you want to use in the header
    //        String imagePath = "path/to/your/image.png";
    //
    //        ImageHeaderEvent event = new ImageHeaderEvent(imagePath);
    //        writer.setPageEvent(event);
    //
    //        document.open();
    //
    //        // Add content to the document
    //        for (int i = 0; i < 5; i++) {
    //            document.add(new Paragraph("Content on Page " + (i + 1)));
    //            document.newPage(); // Start a new page for each iteration
    //        }
    //
    //        document.close();
    //        writer.close();
    //    }
    //}
}
