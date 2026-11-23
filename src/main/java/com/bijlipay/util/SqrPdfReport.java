package com.bijlipay.util;

import com.bijlipay.config.Constants;
import com.bijlipay.model.AgreementDetails;
import com.bijlipay.model.LeadInformation;
import com.bijlipay.model.dto.MdrChargesDto;
import com.bijlipay.model.dto.PdfEsignData;
import com.bijlipay.repository.AgreementDetailsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.FontSelector;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class SqrPdfReport {

    private static final Logger logger = LoggerFactory.getLogger(PdfReport.class);
    //    private static final String FONT = "FreeSans.ttf";
    private static final String FONT = "Arial Unicode MS.ttf";
    @Autowired
    private AgreementDetailsRepository agreementDetailsRepository;

    public ByteArrayOutputStream createSqrAgreementPDF(LeadInformation leadInfo,AgreementDetails agreementDetails) {

        try {

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            Document document = new Document();
            document.setPageSize(PageSize.A4);
            PdfWriter writer = PdfWriter.getInstance(document, byteArrayOutputStream);
            writer.setFullCompression();
            document.open();

            PdfPTable pdfPTable = new PdfPTable(1);
            pdfPTable.setWidthPercentage(100);
            pdfPTable.setWidths(new float[]{1});
            pdfPTable.setSpacingBefore(5);
            pdfPTable.addCell(getHeaderCell("FIRST ADDENDUM AGREEMENT"));

            document.add(pdfPTable);
            Paragraph paragraph = new Paragraph();
            paragraph.setSpacingBefore(10);
            Chunk chunk = new Chunk("This First Addendum Agreement (\"First Addendum”) dated "+DateUtil.currentDateAndMonth()+"-2024 (the “First Addendum Effective Date”) is " +
                    "executed by and between SKILWORTH TECHNOLOGIES PVT. LTD, a company incorporated under the " +
                    "Companies Act, 1956 and having its registered office at No. 20C/2A, Sarangapani Street, T. Nagar, Chennai, " +
                    "Tamil Nadu- 600017 and its corporate office at Chaitanya “EXOTICA” 9th Floor, New No 51, Venkatnarayana " +
                    "Road, T. Nagar,Chennai, Tamil Nadu - 600017 (hereinafter referred to as the “STPL” which expression shall " +
                    "mean and include its successors-in-interest and permitted assigns) of the FIRST PART; AND "+leadInfo.getMerchantName()+"," +
                    "" + "a company incorporated under the Companies Act, 1956/2013, and having its registered " +
                    "office at "+leadInfo.getContactAddress()+", (hereinafter referred to as the “Merchant” which " +
                    "expression shall mean and include its successors-in-interest and permitted assigns) of the SECOND PART. " +
                    "STPL and the Merchant may hereinafter be individually referred to as a “Party” and collectively as “Parties”.",
                   new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.NORMAL));

            paragraph.add(chunk);
            paragraph.setAlignment(Element.ALIGN_JUSTIFIED);
            paragraph.setSpacingAfter(10);
            document.add(paragraph);

            Paragraph paragraph1 = new Paragraph();
            Chunk chunk1 = new Chunk("RECITALS", new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD));
            paragraph1.add(chunk1);
            paragraph1.setAlignment(Element.ALIGN_LEFT);
            paragraph1.setSpacingAfter(10);
            document.add(paragraph1);

            Paragraph paragraph2 = new Paragraph();
            logger.info("lead-Id ---- :{}", leadInfo.getLeadId());
//            AgreementDetails agreementDetails = agreementDetailsRepository.findLead(leadInfo.getLeadId());
//            logger.info("agreement --- : {}", agreementDetails.getUpdatedAt());
            if (agreementDetails != null) {
                Chunk chunk2 = new Chunk("A.   WHEREAS, STPL and the Merchant had entered into a POS agreement dated "+agreementDetails.getUpdatedAt()+"" +
                        "(“Principal Agreement”), under which \"POS + QR Services\".", new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.NORMAL));
                paragraph2.add(chunk2);
            } else {
                Chunk chunk27 = new Chunk("A.   WHEREAS, STPL and the Merchant had entered into a POS agreement dated ____________ " +
                        "(“Principal Agreement”), under which \"POS + QR Services\".", new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.NORMAL));
                paragraph2.add(chunk27);
            }
            paragraph2.setAlignment(Element.ALIGN_JUSTIFIED);
            paragraph2.setSpacingAfter(10);
            document.add(paragraph2);

            Paragraph paragraph12 = new Paragraph();
            Chunk chunk12 = new Chunk("B.   Subsequent to certain discussions between the Parties, the Parties have decided to revise their understanding in" +
                    "Principal Agreement and are therefore, entering into this First Addendum to capture the revised understanding" +
                    "between the Parties.", new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.NORMAL));
            paragraph12.add(chunk12);
            paragraph12.setAlignment(Element.ALIGN_JUSTIFIED);;
            paragraph12.setSpacingAfter(10);
            document.add(paragraph12);

            Paragraph paragraph3 = new Paragraph();
            Chunk chunk3 = new Chunk("NOW THIS FIRST ADDENDUM WITNESSETH AS FOLLOWS:",
                    new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD));
            paragraph3.add(chunk3);
            paragraph3.setAlignment(Element.ALIGN_LEFT);
            paragraph3.setSpacingAfter(10);
            document.add(paragraph3);

            Paragraph paragraph4 = new Paragraph();
            Chunk chunk4 = new Chunk("1.    INTERPRETATIONS.", new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD));
            paragraph4.add(chunk4);
            paragraph4.setAlignment(Element.ALIGN_LEFT);
            paragraph4.setSpacingAfter(10);
            document.add(paragraph4);

            Paragraph paragraph5 = new Paragraph();
            Chunk chunk5 = new Chunk("1.1. The terms and conditions of this First Addendum shall be read and interpreted in conjunction with those of the" +
                    "Principal Agreement.", new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.NORMAL));
            paragraph5.add(chunk5);
            paragraph5.setAlignment(Element.ALIGN_JUSTIFIED);
            paragraph5.setSpacingAfter(10);
            document.add(paragraph5);

            Paragraph paragraph13 = new Paragraph();
            Chunk chunk13 = new Chunk("1.2. Capitalized terms used herein but not specifically defined shall have the meaning assigned to it in the Principal" +
                    "Agreement.", new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.NORMAL));
            paragraph13.add(chunk13);
            paragraph13.setAlignment(Element.ALIGN_JUSTIFIED);
            paragraph13.setSpacingAfter(10);
            document.add(paragraph13);

            Paragraph paragraph14 = new Paragraph();
            Chunk chunk14 = new Chunk("1.3. The term “this Agreement” and “the Agreement” in Principal Agreement shall be deemed to mean and include" +
                    "this First Addendum to the extent the terms are added by this First Addendum.",
                    new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.NORMAL));
            paragraph14.add(chunk14);
            paragraph14.setAlignment(Element.ALIGN_JUSTIFIED);
            paragraph14.setSpacingAfter(10);
            document.add(paragraph14);

            Paragraph paragraph15 = new Paragraph();
            Chunk chunk15 = new Chunk("1.4. The First Addendum and all the terms and provisions herein contained shall form part of the Principal Agreement" +
                    "as fully and with the same effect as if all such terms and provisions had been set forth in the Principal Agreement." +
                    "The Parties reiterate and re-affirm that the Principal Agreement shall continue to remain valid, binding and" +
                    "enforceable except to the extent added by this First Addendum and the Principal Agreement, and this First" +
                    "Addendum shall be read, taken and construed together as one instrument.",
                    new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.NORMAL));
            paragraph15.add(chunk15);
            paragraph15.setAlignment(Element.ALIGN_JUSTIFIED);
            paragraph15.setSpacingAfter(10);
            document.add(paragraph15);

            Paragraph paragraph6 = new Paragraph();
            Chunk chunk6 = new Chunk("2.    AMENDMENTS.", new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD));
            paragraph6.add(chunk6);
            paragraph6.setAlignment(Element.ALIGN_LEFT);
            paragraph6.setSpacingAfter(10);
            document.add(paragraph6);

            Paragraph paragraph7 = new Paragraph();
            Chunk chunk7 = new Chunk("2.1. The Parties have mutually agreed that sub-clause 25 of Clause 1 (Definitions) in the Principal Agreement shall be " +
                    "amended with effect from the First Amendment Effective Date and shall read as follows:",
                    new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.NORMAL));
            paragraph7.add(chunk7);
            paragraph7.setAlignment(Element.ALIGN_JUSTIFIED);
            paragraph7.setSpacingAfter(10);
            document.add(paragraph7);

            Paragraph paragraph16 = new Paragraph();
            Chunk chunk16 = new Chunk("“QR Services: shall mean the QR services to be provided by STPL to ME, which is dynamic that shall be " +
                    "included in the POS Terminal or as static which shall be a sticker based QR Service or as sound box solution that " +
                    "provides voice alert in multiple languages for each successful payment receipt, and sound box shall also facilitate " +
                    "receipt of payments by scanning the QR Code from any UPI linked mobile app;”",
                    new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.NORMAL));
            paragraph16.add(chunk16);
            paragraph16.setAlignment(Element.ALIGN_JUSTIFIED);
            paragraph16.setSpacingAfter(10);
            document.add(paragraph16);

            Paragraph paragraph17 = new Paragraph();
            Chunk chunk17 = new Chunk("2.2. The Parties have mutually agreed that Clause 6.4 (QR Services) in the Principal Agreement shall be amended " +
                    "with effect from the First Amendment Effective Date and shall read as follows:",
                    new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.NORMAL));
            paragraph17.add(chunk17);
            paragraph17.setAlignment(Element.ALIGN_JUSTIFIED);
            paragraph17.setSpacingAfter(10);
            document.add(paragraph17);

            Paragraph paragraph18 = new Paragraph();
            Chunk chunk18 = new Chunk("“6.4 QR Services:", new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.NORMAL));
            paragraph18.add(chunk18);
            paragraph18.setAlignment(Element.ALIGN_JUSTIFIED);
            paragraph18.setSpacingAfter(10);
            document.add(paragraph18);

            Paragraph paragraph19 = new Paragraph();
            Chunk chunk19 = new Chunk("a)   At the request of the Merchant, STPL has enabled the QR Services facility to the Merchant. The " +
                    "rentals/charges mentioned in Schedule A (as revised from time to time) shall be applicable, and the ME" +
                    "agrees that STPL shall debit ME account to recover the applicable charges for the said service.",
                    new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.NORMAL));
            paragraph19.add(chunk19);
            paragraph19.setAlignment(Element.ALIGN_JUSTIFIED);
            paragraph19.setSpacingAfter(1);
            document.add(paragraph19);

            Paragraph paragraph8 = new Paragraph();
            Chunk chunk8 = new Chunk("b)   The Merchant agrees that all the Transactions will be done during the normal business hours as shared " +
                    "in the Application Form.", new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.NORMAL));
            paragraph8.add(chunk8);
            paragraph8.setAlignment(Element.ALIGN_JUSTIFIED);
            paragraph8.setSpacingAfter(1);
            document.add(paragraph8);

            Paragraph paragraph20 = new Paragraph();
            Chunk chunk20 = new Chunk("c)   The Merchant agrees that STPL shall only be responsible in respect of the payment files and the " +
                    "Transaction files.", new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.NORMAL));
            paragraph20.add(chunk20);
            paragraph20.setAlignment(Element.ALIGN_JUSTIFIED);
            paragraph20.setSpacingAfter(1);
            document.add(paragraph20);

            Paragraph paragraph21 = new Paragraph();
            Chunk chunk21 = new Chunk("d)   If in event the Merchant fails to comply with the conditions stated herein, Merchant understands that the " +
                    "terminals will be de-activated by STPL and the settlement will be released only upon investigation and proper documentation done with STPL and corresponding " +
                    "bank partner. There will be no incentive provided for TID’s in case the Transactions are not done in accordance to the rules stated above.",
                    new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.NORMAL));
            paragraph21.add(chunk21);
            paragraph21.setAlignment(Element.ALIGN_JUSTIFIED);
            paragraph21.setSpacingAfter(1);
            document.add(paragraph21);

            Paragraph paragraph22 = new Paragraph();
            Chunk chunk22 = new Chunk("e)   In addition to the covenants set out in this Clause 6, all other Merchant terms and covenants stated in the " +
                    "Agreement (as applicable) shall apply to QR Services.”",
                    new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.NORMAL));
            paragraph22.add(chunk22);
            paragraph22.setAlignment(Element.ALIGN_JUSTIFIED);
            paragraph22.setSpacingAfter(10);
            document.add(paragraph22);

            Paragraph paragraph9 = new Paragraph();
            Chunk chunk9 = new Chunk("2.3. The Parties have agreed that the entirety of Schedule A of the Principal Agreement shall be replaced with the " +
                    "details of the fees and the payment terms as specified in Exhibit A of this First Addendum, with effect from the" +
                    "First Addendum Effective Date.", new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.NORMAL));
            paragraph9.add(chunk9);
            paragraph9.setSpacingAfter(10);
            paragraph9.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(paragraph9);

            Paragraph paragraph10 = new Paragraph();
            Chunk chunk10 = new Chunk("3.   CONTINUING OBLIGATIONS.", new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD));
            paragraph10.add(chunk10);
            paragraph10.setSpacingAfter(10);
            paragraph10.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(paragraph10);

            Paragraph paragraph11 = new Paragraph();
            Chunk chunk11 = new Chunk("3.1. This First Addendum shall co - exist and co-terminus with the Principal Agreement and shall terminate along with " +
                    "the Principal Agreement, unless otherwise terminated earlier by the Parties in writing.",
                    new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.NORMAL));
            paragraph11.add(chunk11);
            paragraph11.setAlignment(Element.ALIGN_JUSTIFIED);
            paragraph11.setSpacingAfter(10);
            document.add(paragraph11);

            Paragraph paragraph23 = new Paragraph();
            Chunk chunk23 = new Chunk("3.2. Save as the provisions contained in this First Addendum, all other terms and conditions of the Principal " +
                    "Agreement shall continue in full force and effect. In the event of any conflict between the terms of this First " +
                    "Addendum and the Principal Agreement, the terms of this First Addendum shall prevail with respect to the matters " +
                    "specifically mentioned herein and for all other terms and conditions, the terms of the Principal Agreement " +
                    "shall prevail.",
                    new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.NORMAL));
            paragraph23.add(chunk23);
            paragraph23.setAlignment(Element.ALIGN_JUSTIFIED);
            paragraph23.setSpacingAfter(10);
            document.add(paragraph23);

            Paragraph paragraph24 = new Paragraph();
            Chunk chunk24 = new Chunk("3.3. No modification to this First Addendum shall be considered valid, unless mutually agreed in writing and signed " +
                    "by both Parties.", new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.NORMAL));
            paragraph24.add(chunk24);
            paragraph24.setAlignment(Element.ALIGN_JUSTIFIED);
            paragraph24.setSpacingAfter(10);
            document.add(paragraph24);

            document.newPage();

            PdfPTable pdfPTable1 = new PdfPTable(1);
            pdfPTable1.setWidthPercentage(100);
            pdfPTable1.setWidths(new float[]{1});
            pdfPTable1.setSpacingBefore(5);
            pdfPTable1.addCell(getHeaderCell1("Exhibit A"));

            document.add(pdfPTable1);

            Paragraph paragraph25 = new Paragraph();
            Chunk chunk25 = new Chunk("A) Merchant Discount Rate " +
                    "The Merchant Discount Rate (MDR) as provided below shall be recovered for every Transactions and the settlement " +
                    "of the net proceeds will be done in line with the conditions enumerated in the Agreement.",
                    new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.NORMAL));
            paragraph25.add(chunk25);
            paragraph25.setAlignment(Element.ALIGN_JUSTIFIED);
            paragraph25.setSpacingAfter(10);
            document.add(paragraph25);

            PdfPTable pdfPTable2 = new PdfPTable(3);
            pdfPTable2.setSpacingBefore(10);
            pdfPTable2.setWidths(new float[]{1, 3, 1});
            pdfPTable2.addCell(getCellwithLeftAlignWithLeftBottomtBorderWithBold("SNO"));
            pdfPTable2.addCell(getCellwithLeftAlignWithLeftBottomtBorderWithBold("Particulars"));
            pdfPTable2.addCell(getCellwithLeftAlignWithRightBottomtBorderWithBold("MDR (% of the transaction amount +\n" +
                    "Applicable taxes)"));
            document.add(pdfPTable2);

            ObjectMapper mapper = new ObjectMapper();

            PdfPTable pdfPTable3 = new PdfPTable(3);
            pdfPTable3.setWidths(new float[]{1,3,1});
            pdfPTable3.addCell(getCellwithLeftAlignWithLeftBottomtBorder("1"));
            pdfPTable3.addCell(getCellwithLeftAlignWithLeftBottomtBorder("QR - Standard/classic credit card domestic\n" +
                    "transactions"));
            if (leadInfo.isMDRSubventionRequested()) {
                if (leadInfo.getAppliedStdCC() != 0.0) {
                    pdfPTable3.addCell(getCellwithRightAlignWithRightBottomtBorder(leadInfo.getAppliedStdCC() + "\t\t\t\t+GST %"));
                } else {
                    pdfPTable3.addCell(getCellwithRightAlignWithRightBottomtBorder("\t\t\t\t+GST %"));
                }
            } else {
                if (leadInfo.getMarsMdrCharges() != null && leadInfo.getMarsMdrCharges().getStandardOrClassic() != null) {
                    MdrChargesDto stdCC = mapper.readValue(leadInfo.getMarsMdrCharges().getStandardOrClassic(), MdrChargesDto.class);
                    logger.info("std cc ---------- : {}", new Gson().toJson(stdCC));
                    pdfPTable3.addCell(getCellwithRightAlignWithRightBottomtBorder(stdCC.getPercentage() + "\t\t\t\t+GST %"));
                } else {
                    pdfPTable3.addCell(getCellwithRightAlignWithRightBottomtBorder("\t\t\t\t+GST %"));
                }
            }
            document.add(pdfPTable3);

            PdfPTable pdfPTable4 = new PdfPTable(3);
            pdfPTable4.setWidths(new float[]{1, 3, 1});
            pdfPTable4.addCell(getCellwithLeftAlignWithLeftBottomtBorder("2"));
            pdfPTable4.addCell(getCellwithLeftAlignWithLeftBottomtBorder("QR - Premium/Platinum credit card domestic\n" +
                    "transactions"));
            if (leadInfo.isMDRSubventionRequested()) {
                if (leadInfo.getAppliedPremiumCC() != 0.0) {
                    pdfPTable4.addCell(getCellwithRightAlignWithRightBottomtBorder(leadInfo.getAppliedPremiumCC() + "\t\t\t\t+GST %"));
                } else {
                    pdfPTable4.addCell(getCellwithRightAlignWithRightBottomtBorder("\t\t\t\t+GST %"));
                }
            } else {
                if (leadInfo.getMarsMdrCharges() != null && leadInfo.getMarsMdrCharges().getPremiumOrPlatinum() != null) {
                    MdrChargesDto premiumCC = mapper.readValue(leadInfo.getMarsMdrCharges().getPremiumOrPlatinum(), MdrChargesDto.class);
                    logger.info("premium or platinum -------------- : {}", new Gson().toJson(premiumCC));
                    pdfPTable4.addCell(getCellwithRightAlignWithRightBottomtBorder(premiumCC.getPercentage() + "\t\t\t\t+GST %"));
                } else {
                    pdfPTable4.addCell(getCellwithRightAlignWithRightBottomtBorder("\t\t\t\t+GST %"));
                }
            }
            document.add(pdfPTable4);

            PdfPTable pdfPTable5 = new PdfPTable(3);
            pdfPTable5.setWidths(new float[]{1, 3, 1});
            pdfPTable5.addCell(getCellwithLeftAlignWithLeftBottomtBorder("3"));
            pdfPTable5.addCell(getCellwithLeftAlignWithLeftBottomtBorder("QR - SuperPremium/Signature credit card domestic\n" +
                    "transactions"));
            if (leadInfo.isMDRSubventionRequested()) {
                if (leadInfo.getAppliedSuperPremiumCC() != 0.0) {
                    pdfPTable5.addCell(getCellwithRightAlignWithRightBottomtBorder(leadInfo.getAppliedSuperPremiumCC() + "\t\t\t\t+GST %"));
                } else {
                    pdfPTable5.addCell(getCellwithRightAlignWithRightBottomtBorder("\t\t\t\t+GST %"));
                }
            } else {
                if (leadInfo.getMarsMdrCharges() != null && leadInfo.getMarsMdrCharges().getSuperPremiumOrSignature() != null) {
                    MdrChargesDto superPremiumCC = mapper.readValue(leadInfo.getMarsMdrCharges().getSuperPremiumOrSignature(), MdrChargesDto.class);
                    logger.info("super premium cc ----------: {}", new Gson().toJson(superPremiumCC));
                    pdfPTable5.addCell(getCellwithRightAlignWithRightBottomtBorder(superPremiumCC.getPercentage() + "\t\t\t\t+GST %"));
                } else {
                    pdfPTable5.addCell(getCellwithRightAlignWithRightBottomtBorder("\t\t\t\t+GST %"));
                }
            }
            document.add(pdfPTable5);

            PdfPTable pdfPTable6 = new PdfPTable(3);
            pdfPTable6.setWidths(new float[]{1, 3, 1});
            pdfPTable6.addCell(getCellwithLeftAlignWithLeftBottomtBorder("4"));
            pdfPTable6.addCell(getCellwithLeftAlignWithLeftBottomtBorder("QR - Commercial/Corporate credit card domestic\n" +
                    "transactions"));
            if (leadInfo.isMDRSubventionRequested()) {
                if (leadInfo.getAppliedCorpCC() != 0.0) {
                    pdfPTable6.addCell(getCellwithRightAlignWithRightBottomtBorder(leadInfo.getAppliedCorpCC() + "\t\t\t\t+GST %"));
                } else {
                    pdfPTable6.addCell(getCellwithRightAlignWithRightBottomtBorder("\t\t\t\t+GST %"));
                }
            } else {
                if (leadInfo.getMarsMdrCharges() != null && leadInfo.getMarsMdrCharges().getCommercialOrCorporate() != null) {
                    MdrChargesDto corpCC = mapper.readValue(leadInfo.getMarsMdrCharges().getCommercialOrCorporate(), MdrChargesDto.class);
                    logger.info("Corp CC ------------ : {}", new Gson().toJson(corpCC));
                    pdfPTable6.addCell(getCellwithRightAlignWithRightBottomtBorder(corpCC.getPercentage() + "\t\t\t\t+GST %"));
                } else {
                    pdfPTable6.addCell(getCellwithRightAlignWithRightBottomtBorder("\t\t\t\t+GST %"));
                }
            }

            document.add(pdfPTable6);


            PdfPTable pdfPTable7 = new PdfPTable(3);
            pdfPTable7.setSpacingBefore(15);
            pdfPTable7.setWidthPercentage(100);
            pdfPTable7.setWidths(new float[] {0.4f, 1.2f, 1.2f});

            PdfPTable nestedTable4 = new PdfPTable(1);
            nestedTable4.setWidthPercentage(100);
            nestedTable4.setWidths(new float[] {1});
            nestedTable4.addCell(getRowCellWithoutBorder("Sound box payment details"));
            pdfPTable7.addCell(nestedTable4);

            PdfPTable nestedTable = new PdfPTable(6);
            nestedTable.setWidthPercentage(100);
            nestedTable.setWidths(new float[]{1, 1, 1, 1, 1, 1});
            nestedTable.addCell(getRowCellWithoutBorder("Rental"));
            nestedTable.addCell(getCheckBox2());
            nestedTable.addCell(getRowCellWithoutBorder("INR"));
            nestedTable.addCell(getRowCellWithoutBorder(""));
            nestedTable.addCell(getRowCellWithoutBorder(""));
            nestedTable.addCell(getRowCellWithoutBorder("+GST"));

            pdfPTable7.addCell(nestedTable);

            PdfPTable nestedTable2 = new PdfPTable(6);
            nestedTable2.setWidthPercentage(100);
            nestedTable2.setWidths(new float[]{1, 1, 1, 1, 1, 1});
            nestedTable2.addCell(getRowCellWithoutBorder("Sale"));
            nestedTable2.addCell(getCheckBox2());
            nestedTable2.addCell(getRowCellWithoutBorder("INR"));
            nestedTable2.addCell(getRowCellWithoutBorder(""));
            nestedTable2.addCell(getRowCellWithoutBorder(""));
            nestedTable2.addCell(getRowCellWithoutBorder("+GST"));

            pdfPTable7.addCell(nestedTable2);
            document.add(pdfPTable7);

//            PdfPTable pdfPTable8 = new PdfPTable(1);
//            pdfPTable8.setWidthPercentage(100);
//            pdfPTable8.setWidths(new float[] {1});
//
//            PdfPTable nestedTable3 = new PdfPTable(12);
//            nestedTable3.setWidthPercentage(100);
//            nestedTable3.setWidths(new float[]{1,1,1,1,1,1,1,1,1,1,1,1});
//            nestedTable3.addCell(getRowCellWithoutBorder("Adv rent No.of years"));
//            nestedTable3.addCell(getRowCellWithoutBorder("1"));
//            nestedTable3.addCell(getCheckBox2());
//            nestedTable3.addCell(getRowCellWithoutBorder("2"));
//            nestedTable3.addCell(getCheckBox2());
//            nestedTable3.addCell(getRowCellWithoutBorder(""));
//            nestedTable3.addCell(getRowCellWithoutBorder("EMI No. of Months"));
//            nestedTable3.addCell(getRowCellWithoutBorder(""));
//            nestedTable3.addCell(getRowCellWithoutBorder("EMI Amount"));
//            nestedTable3.addCell(getRowCellWithoutBorder("INR"));
//            nestedTable3.addCell(getRowCellWithoutBorder(""));
//            nestedTable3.addCell(getRowCellWithoutBorder(""));
//
//            pdfPTable8.addCell(nestedTable3);
//            document.add(pdfPTable8);
//
//            PdfPTable pdfPTable9 = new PdfPTable(1);
//            pdfPTable9.setWidthPercentage(100);
//            pdfPTable9.setWidths(new float[] {1});
//
//            PdfPTable nestedTable5 = new PdfPTable(4);
//            nestedTable5.setWidthPercentage(100);
//            nestedTable5.setWidths(new float[]{1,1,1,1});
//            nestedTable5.addCell(getRowCellWithoutBorder("2nd year INR"));
//            nestedTable5.addCell(getRowCellWithoutBorder(""));
//            nestedTable5.addCell(getRowCellWithoutBorder("3rd year onwards AMC INR"));
//            nestedTable5.addCell(getRowCellWithoutBorder(""));
//
//            pdfPTable9.addCell(nestedTable5);
//            document.add(pdfPTable9);


            PdfPTable pTable16 = new PdfPTable(4);
            pTable16.setWidthPercentage(100);
            pTable16.setWidths(new float[]{0.5f, 0.5f, 1, 1});
            pTable16.addCell(getCellwithLeftAlignWithLeftBottomtBorderSize45("Adv rent No of years: "));   //need to add value

            PdfPTable nestedTable5 = new PdfPTable(4);
            nestedTable5.setWidthPercentage(100);
            nestedTable5.setWidths(new float[]{0.1f, 0.2f, 0.1f, 0.2f});
            nestedTable5.addCell(getCheckBox2());
            nestedTable5.addCell(getRowCellWithoutBorder("1"));
            nestedTable5.addCell(getCheckBox2());
            nestedTable5.addCell(getRowCellWithoutBorder("2"));

            pTable16.addCell(nestedTable5);

            pTable16.addCell(getCellwithLeftAlignWithLeftBottomtBorderSize45("EMI No of months: "));     //need to add value
            pTable16.addCell(getCellwithLeftAlignWithRightBottomtBorderSize45("EMI Amount   INR"));      //need to add value

            document.add(pTable16);


            PdfPTable pTable17 = new PdfPTable(2);
            pTable17.setWidthPercentage(100);
            pTable17.setWidths(new float[]{1, 2});
            pTable17.addCell(getCellwithLeftAlignWithLeftBottomtBorderSize45("2nd year INR"));
            pTable17.addCell(getCellwithLeftAlignWithRightBottomtBorderSize45("3rd year Onwards AMC INR:"));

            document.add(pTable17);


            PdfPTable pTable10 = new PdfPTable(1);
            pTable10.setWidthPercentage(100);
            pTable10.setWidths(new float[]{1});
            pTable10.addCell(getHeaderCellColumns("Swipe"));

            document.add(pTable10);

            PdfPTable pTable19 = new PdfPTable(3);
            pTable19.setWidthPercentage(100);
            pTable19.setWidths(new float[]{0.8f, 0.8f, 1.4f});
            pTable19.addCell(getCellwithLeftAlignWithLeftBottomtBorderSize45("Amount: ")); //need to add value
            pTable19.addCell(getCellwithLeftAlignWithLeftBottomtBorderSize45("Date: ")); //need to add value
            pTable19.addCell(getCellwithLeftAlignWithRightBottomtBorderSize45("Swiped in the terminal of: ")); //need to add value

            document.add(pTable19);

            PdfPTable pTable20 = new PdfPTable(1);

            pTable20.setWidthPercentage(100);
            pTable20.setWidths(new float[]{1});
            pTable20.addCell(getHeaderCellColumns("Cheque/DD"));

            document.add(pTable20);

            PdfPTable pTable21 = new PdfPTable(2);
            pTable21.setWidthPercentage(100);
            pTable21.setWidths(new float[]{2, 2});
            pTable21.addCell(getCellwithLeftAlignWithLeftBottomtBorderSize45("Cheque/DD No:"));
            pTable21.addCell(getCellwithLeftAlignWithRightBottomtBorderSize45("Amount:"));

            document.add(pTable21);


            PdfPTable pTable22 = new PdfPTable(2);
            pTable22.setWidthPercentage(100);
            pTable22.setWidths(new float[]{2, 2});
            pTable22.addCell(getCellwithLeftAlignWithLeftBottomtBorderSize45("Cheque/DD Date: "));
            pTable22.addCell(getCellwithLeftAlignWithRightBottomtBorderSize45("Cheque Deposit Date: "));

            document.add(pTable22);



            PdfPTable pTable23 = new PdfPTable(1);
            pTable23.setWidthPercentage(100);
            pTable23.setWidths(new float[]{1});
            pTable23.addCell(getHeaderCellColumns("Bank Name:"));  //need to value

            document.add(pTable23);

            PdfPTable pTable24 = new PdfPTable(1);
            pTable24.setWidthPercentage(100);
            pTable24.setWidths(new float[]{1});
            pTable24.addCell(getHeaderCellColumns("NEFT/IMPS/PG REFS NO:"));  //need to add value
            document.add(pTable24);

            PdfPTable pdfPTable25 = new PdfPTable(3);
            pdfPTable25.setWidthPercentage(100);
            pdfPTable25.setWidths(new float[] {0.4f,1.2f,1.2f});

            PdfPTable nestedTable10 = new PdfPTable(1);
            nestedTable10.setWidthPercentage(100);
            nestedTable10.setWidths(new float[]{1});
            nestedTable10.addCell(getRowCellWithoutBorder("Monthly Rent"));
            pdfPTable25.addCell(nestedTable10);


            PdfPTable nestedTable8 = new PdfPTable(6);
            nestedTable8.setWidthPercentage(100);
            nestedTable8.setWidths(new float[]{0.9f, 1, 1, 1, 1, 1});
            nestedTable8.addCell(getRowCellWithoutBorder("Soundbox"));
            nestedTable8.addCell(getRowCellWithoutBorder(""));
            nestedTable8.addCell(getRowCellWithoutBorder("INR"));
            nestedTable8.addCell(getRowCellWithoutBorder(""));
            nestedTable8.addCell(getRowCellWithoutBorder("+GST"));
            nestedTable8.addCell(getRowCellWithoutBorder(""));

            pdfPTable25.addCell(nestedTable8);

            PdfPTable nestedTable9 = new PdfPTable(4);
            nestedTable9.setWidthPercentage(100);
            nestedTable9.setWidths(new float[]{1, 1, 1, 1});
            nestedTable9.addCell(getRowCellWithoutBorder("No. of Terminals"));
            nestedTable9.addCell(getRowCellWithoutBorder(""));
            nestedTable9.addCell(getRowCellWithoutBorder("Soundbox"));
            nestedTable9.addCell(getRowCellWithoutBorder(""));

            pdfPTable25.addCell(nestedTable9);
            document.add(pdfPTable25);

            Paragraph paragraph26 = new Paragraph();
            paragraph26.setSpacingBefore(10);
            Chunk chunk26 = new Chunk("IN WITNESS THEREOF the Parties have hereunto executed through their duly authorized persons these presents on " +
                    "the day, month & year first above written", new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.NORMAL));
            paragraph26.add(chunk26);
            paragraph26.setAlignment(Element.ALIGN_JUSTIFIED);
            paragraph26.setSpacingAfter(10);
            document.add(paragraph26);

            PdfPTable table22 = new PdfPTable(2);
            table22.setSpacingBefore(15);
            table22.setWidths(new float[]{1, 1});
            table22.addCell(getCellLeftAlignWithBold("for and on behalf of"));
            table22.addCell(getCellLeftAlignWithBold("for and on behalf of"));
            document.add(table22);

            PdfPTable table23 = new PdfPTable(2);
            table23.setWidths(new float[]{1, 1});
            table23.addCell(getCellwithLeftAlignWithLeftRightBorder("SKILWORTH TECHNOLOGIES PRIVATE LIMITED"));
            table23.addCell(getCellwithLeftAlignWithLeftRightBorder("MERCHANT"));
            document.add(table23);

            PdfPTable table24 = new PdfPTable(2);
            table24.setWidths(new float[]{1, 1});
//            table24.addCell(getCellwithLeftAlignWithoutTopBorder(" "));
            table24.addCell(getSignedImage());
            table24.addCell(getCellwithLeftAlignWithoutTopBorder(" "));
            document.add(table24);

            PdfPTable table25 = new PdfPTable(2);
            table25.setWidths(new float[]{1, 1});
            table25.addCell(getCellwithLeftAlignWithLeftRightBorder("Name :   M. R. BHANU PRATHAP"));
            table25.addCell(getCellwithLeftAlignWithLeftRightBorder("Name :   " + leadInfo.getMerchantName()));
            document.add(table25);

            PdfPTable table26 = new PdfPTable(2);
            table26.setWidths(new float[]{1, 1});
            table26.addCell(getCellwithLeftAlignWithLeftRightBorder("Designation : Chief Operating Officer"));
            table26.addCell(getCellwithLeftAlignWithLeftRightBorder("Designation : Director"));
            document.add(table26);

            PdfPTable table27 = new PdfPTable(2);
            table27.setWidths(new float[]{1, 1});
            table27.addCell(getCellwithLeftAlignWithoutTopBorder("Date :" + DateUtil.currentDate()));
            table27.addCell(getCellwithLeftAlignWithoutTopBorder("Date :" + DateUtil.currentDate()));
            document.add(table27);

            document.close();
            return byteArrayOutputStream;
        } catch (Exception e) {
            logger.info("provide the error -- : {}", e.getMessage());
            return null;
        }
    }

    private static PdfPCell getHeaderCell(String text) {
        Font font = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD|Font.UNDERLINE);
        FontSelector fontSelector = new FontSelector();
        fontSelector.addFont(font);
        Phrase phrase = fontSelector.process(text);
        PdfPCell cell = new PdfPCell(phrase);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(5.0f);
        cell.setBorderWidthBottom(0);
        cell.setBorderWidthTop(0);
        cell.setBorderWidthRight(0);
        cell.setBorderWidthLeft(0);
        return cell;
    }

    private static PdfPCell getHeaderCell1(String text) {
        Font font = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
        FontSelector fontSelector = new FontSelector();
        fontSelector.addFont(font);
        Phrase phrase = fontSelector.process(text);
        PdfPCell cell = new PdfPCell(phrase);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(5.0f);
        cell.setBorderWidthBottom(0);
        cell.setBorderWidthTop(0);
        cell.setBorderWidthRight(0);
        cell.setBorderWidthLeft(0);
        return cell;
    }

    private static PdfPCell getCellwithLeftAlignWithLeftBottomtBorderSize45(String text) {

        Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL);
        FontSelector fs = new FontSelector();
        fs.addFont(catFont);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell(phrase);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setPadding(5.0f);
        cell.setBorderWidthTop(0);
        cell.setBorderWidthRight(0);
        return cell;
    }

    private static PdfPCell getCellwithLeftAlignWithLeftBottomtBorder(String text) {

        Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL);
        FontSelector fs = new FontSelector();
        fs.addFont(catFont);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell(phrase);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setPadding(4.0f);
        cell.setBorderWidthTop(1);
        cell.setBorderWidthRight(1);
        return cell;
    }

    private static PdfPCell getCellwithLeftAlignWithLeftBottomtBorderWithBold(String text) {

        Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.BOLD);
        FontSelector fs = new FontSelector();
        fs.addFont(catFont);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell(phrase);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setPadding(4.0f);
        cell.setBorderWidthTop(1);
        cell.setBorderWidthRight(1);
//        cell.setBorderWidthLeft(0);
//        cell.setBorderWidthBottom(0);
        return cell;
    }

    private static PdfPCell getCellwithLeftAlignWithRightBottomtBorderWithBold(String text) {

        Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.BOLD);
        FontSelector fs = new FontSelector();
        fs.addFont(catFont);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell(phrase);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setPadding(4.0f);
        cell.setBorderWidthTop(1);
        return cell;
    }

    private static PdfPCell getCellwithRightAlignWithRightBottomtBorder(String text) {

        Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL);
        FontSelector fs = new FontSelector();
        fs.addFont(catFont);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell(phrase);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setPadding(4.0f);
        cell.setBorderWidthTop(1);
        return cell;
    }

    private PdfPCell getCheckBox1() throws BadElementException, IOException {

        Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 7, Font.NORMAL);
        FontSelector fs = new FontSelector();
        fs.addFont(catFont);
        PdfPCell pdfPCell = new PdfPCell();

        String imagePath = Constants.CHECKBOX_SELECTED_IMAGE;
        Image image12 = Image.getInstance(imagePath);
        image12.scaleToFit(10, 10); // Adjust the image size as needed

        pdfPCell.addElement(image12);

        pdfPCell.setPadding(4.0f);
        pdfPCell.setBorderWidthBottom(0);
        pdfPCell.setBorderWidthTop(0);
        pdfPCell.setBorderWidthRight(0);
        pdfPCell.setBorderWidthLeft(0);

        return pdfPCell;
    }


    public static PdfPCell getRowCellWithoutBorder(String text) {
        Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL);
        FontSelector fs = new FontSelector();
        fs.addFont(catFont);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell(phrase);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setPadding(5.0f);
        cell.setBorderWidthBottom(0);
        cell.setBorderWidthTop(0);
        cell.setBorderWidthRight(0);
        cell.setBorderWidthLeft(0);
        return cell;
    }

    private PdfPCell getCheckBox2() throws BadElementException, IOException {

        Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 7, Font.NORMAL);
        FontSelector fs = new FontSelector();
        fs.addFont(catFont);
        PdfPCell pdfPCell = new PdfPCell();

        String imagePath = Constants.CHECKBOX_UNSELECTED_IMAGE;
        Image image12 = Image.getInstance(imagePath);
        image12.scaleToFit(10, 10); // Adjust the image size as needed

        pdfPCell.addElement(image12);

        pdfPCell.setPadding(4.0f);
        pdfPCell.setBorderWidthBottom(0);
        pdfPCell.setBorderWidthTop(0);
        pdfPCell.setBorderWidthRight(0);
        pdfPCell.setBorderWidthLeft(0);

        return pdfPCell;
    }

    private static PdfPCell getHeaderCellColumns(String text) {
        Font font = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD);
        FontSelector fontSelector = new FontSelector();
        fontSelector.addFont(font);
        Phrase phrase = fontSelector.process(text);
        PdfPCell cell = new PdfPCell(phrase);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setPadding(4.0f);
        return cell;
    }

    private static PdfPCell getCellwithLeftAlignWithRightBottomtBorderSize45(String text) {

        Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL);
        FontSelector fs = new FontSelector();
        fs.addFont(catFont);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell(phrase);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setPadding(4.0f);
        cell.setBorderWidthTop(0);
        return cell;
    }

    private static PdfPCell getCellLeftAlignWithBold(String text) {
        Font font = new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.BOLD);
        FontSelector fontSelector = new FontSelector();
        fontSelector.addFont(font);
        Phrase phrase = fontSelector.process(text);
        PdfPCell cell = new PdfPCell(phrase);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setPadding(4.0f);
//        cell.setBorderWidthTop(0);
//        cell.setBorderWidthRight(0);
//        cell.setBorderWidthLeft(0);
        cell.setBorderWidthBottom(0);
        return cell;
    }

    private static PdfPCell getCellwithLeftAlignWithLeftRightBorder(String text) {

        Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL);
        FontSelector fs = new FontSelector();
        fs.addFont(catFont);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell(phrase);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setPadding(4.0f);
        cell.setBorderWidthTop(0);
//        cell.setBorderWidthRight(0);
//        cell.setBorderWidthLeft(0);
        cell.setBorderWidthBottom(0);
        return cell;
    }

    private static PdfPCell getCellwithLeftAlignWithoutTopBorder(String text) {

        Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL);
        FontSelector fs = new FontSelector();
        fs.addFont(catFont);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell(phrase);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setPadding(4.0f);
        cell.setBorderWidthTop(0);
//        cell.setBorderWidthRight(0);
//        cell.setBorderWidthLeft(0);
//        cell.setBorderWidthBottom(0);
        return cell;
    }

    private PdfPCell getSignedImage() throws DocumentException, IOException {

        Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 7, Font.NORMAL);
        FontSelector fs = new FontSelector();
        fs.addFont(catFont);
        PdfPCell pdfPCell = new PdfPCell();

        String imagePath = Constants.SIGNED_IMAGE;

        Image image = Image.getInstance(imagePath);
        image.scaleToFit(75, 45); // Adjust the image size as needed
        image.setCompressionLevel(9);
        pdfPCell.addElement(image);


        pdfPCell.setBorderWidthTop(0);
        pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        pdfPCell.setPaddingLeft(50.0f);


        return pdfPCell;
    }

}
