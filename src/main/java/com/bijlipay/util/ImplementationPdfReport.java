package com.bijlipay.util;

import com.bijlipay.config.Constants;
import com.bijlipay.model.CounterManagement;
import com.bijlipay.model.LeadInformation;
import com.bijlipay.model.MarsDeviceDetails;
import com.bijlipay.model.UploadedDocuments;
import com.bijlipay.model.dto.ImplementationSignDto;
import com.bijlipay.model.dto.MdrChargesDto;
import com.bijlipay.model.dto.PdfEsignData;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class ImplementationPdfReport {
    private static final Logger logger = LoggerFactory.getLogger(ImplementationPdfReport.class);

    public static final int TARGET_PAGE = 2;

    // Keys used to identify hidden positional markers in the PDF.
    public static final String KEY_DEVICE_MODEL = "deviceModel";
    public static final String KEY_LAT_LONG     = "latLong";
    public static final String KEY_IP_ADDRESS   = "ipAddress";
    public static final String KEY_DATE_TIME    = "dateTime";

    // Legacy fallback coordinates (used ONLY if updating an old PDF missing the new hidden markers)
    public static final float[] DEFAULT_DEVICE_MODEL_XY = {170f, 380f};
    public static final float[] DEFAULT_LATLONG_XY      = {430f, 380f};
    public static final float[] DEFAULT_IP_ADDRESS_XY   = {170f, 390f};
    public static final float[] DEFAULT_DATE_TIME_XY    = {430f, 390f};

    // Kept for backward compatibility with other services
    private Map<String, float[]> lastCapturedCoordinates = new HashMap<>();

    public Map<String, float[]> getLastCapturedCoordinates() {
        return lastCapturedCoordinates;
    }

    /**
     * Embeds a completely HIDDEN positional marker in the cell.
     * This prevents the blue "editable" box from showing up for the user,
     * while saving the exact dynamic X/Y coordinates for the overlay API to use later.
     */
    private static class HiddenMarkerCellEvent implements PdfPCellEvent {
        private final String fieldName;

        HiddenMarkerCellEvent(String fieldName) {
            this.fieldName = fieldName;
        }

        @Override
        public void cellLayout(PdfPCell cell, Rectangle position, PdfContentByte[] canvases) {
            try {
                PdfWriter writer = canvases[0].getPdfWriter();
                // Create a bounding box matching the cell's inner padding
                Rectangle rect = new Rectangle(
                        position.getLeft() + 2f,
                        position.getBottom() + 2f,
                        position.getRight() - 2f,
                        position.getTop() - 2f
                );

                TextField tf = new TextField(writer, rect, fieldName);
                // CRITICAL FIX: Make the field completely invisible to the user viewing the PDF
                tf.setVisibility(TextField.HIDDEN);

                PdfFormField field = tf.getTextField();
                writer.addAnnotation(field);
            } catch (Exception e) {
                logger.error("Error creating hidden marker: {}", fieldName, e);
            }
        }
    }

    public ByteArrayOutputStream createImplementationFormPDF(LeadInformation leadInformation, List<MarsDeviceDetails> marsDeviceDetails, String tids, String mids, String serialNos,String sim,String deviceType) {
        try {
            String formattedDate = "";
            if (marsDeviceDetails.get(0) != null) {
                if (marsDeviceDetails.get(0).getScheduleDate() != null) {
                    Date date = marsDeviceDetails.get(0).getScheduleDate();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    formattedDate = sdf.format(date);
                }
            }
//            String sim = "";
//            if (marsDeviceDetails.size() > 0) {
//                for (MarsDeviceDetails m : marsDeviceDetails) {
//                    String sim1 = m.getSim();
//                    if (sim1 != null) {
//                        sim = sim.concat("," + sim1);
//                    } else {
//                        logger.info("Sim is empty--{}");
//                    }
//                }
//            }
//            sim = sim.replaceFirst(",", "");

            this.lastCapturedCoordinates = new HashMap<>(); // Reset safely

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            LocalDate now = LocalDate.now();

            Document document = new Document(PageSize.A4, 42, 42, 30, 30);
            PdfWriter.getInstance(document, outputStream);

            document.open();

            //FONT STYLE AND FONTSIZE...

            Font title = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD);
            Font heading = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD);
            Font text = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.NORMAL);
            Font italc = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.ITALIC);
            Font bulletFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
            Font page = new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.NORMAL);

            Image image1 = Image.getInstance(Constants.LOGO_IMAGE);
            image1.scaleToFit(80, 80);

            PdfPTable logo = new PdfPTable(2);
            logo.setWidthPercentage(100);
            logo.setWidths(new float[]{100, 50});

            PdfPCell logoLeftCell = new PdfPCell(new Phrase(""));
            logoLeftCell.setBorder(Rectangle.NO_BORDER);   // for empty

            PdfPCell logoRightCell = new PdfPCell(image1);
            logoRightCell.setBorder(Rectangle.NO_BORDER);
            logoRightCell.setHorizontalAlignment(Element.ALIGN_RIGHT);  //for image

            logo.addCell(logoLeftCell);
            logo.addCell(logoRightCell);

            document.add(logo);

            // ======= INSTALLATION/REPLACEMENT/RECOVERY FORM  ========

            Paragraph form = new Paragraph(" INSTALLATION/REPLACEMENT/RECOVERY FORM \n\n", heading);
            form.setAlignment(Element.ALIGN_CENTER);
            document.add(form);
            document.add(new Chunk());

            // ===== Paragraph started  =====

            addPara(document, new Chunk("We/I confirm that the following equipment ", text),
                    new Chunk("TID: ", title),
                    new Chunk(tids + "  ", title),
                    new Chunk("mapped with ", text),
                    new Chunk("MID: ", title),
                    new Chunk(mids + " ", title),
                    new Chunk("has been installed/Replaced/Recovered by BIJLIPAY or its representative at our Establishment on: ", text),
                    new Chunk(formattedDate + " ", title),
                    new Chunk("at " + "\n", text),
                    new Chunk("“ ", text),
                    new Chunk(leadInformation.getLegalName() + "\n", title),
                    new Chunk("" + "\n", title)

            );

            Paragraph p1 = new Paragraph("We further acknowledge that the above equipment belongs to Bijlipay and will be returned to Bijlipay, immediately on demand. We confirm that the equipment will be handled with utmost care while possession and will be responsible for damage to the equipment due to improper handling or lost /theft from our establishment, while in our possession. In such an event we will reimburse Bijlipay the full cost of the equipment (Rs.10,000/-). The set up or advance rental collected from us will not under any circumstance or acts, shall entitle the ownership of Bijlipay to us. Under all circumstance the ownership/rights of the machine shall remain exclusive with Bijlipay.\n", text);
            p1.setAlignment(Element.ALIGN_JUSTIFIED);
            p1.setSpacingAfter(5);
            p1.setLeading(18);
            document.add(p1);

            Paragraph device = new Paragraph();
            device.setSpacingAfter(5);
            device.setLeading(18);
            device.add(new Chunk("We confirm having received a comprehensive card acceptance, POS and DQR training program and understand that the below are the Do’s and Don’ts Do’s:", title));
            document.add(device);

            // Do and Do not  in the agreement

            PdfPTable pdfPTable1 = new PdfPTable(2);
            pdfPTable1.setWidthPercentage(100);
            pdfPTable1.setWidths(new float[]{10, 10});

            PdfPCell dos = new PdfPCell(new Phrase("Do’s:", title));
            dos.setBorder(Rectangle.NO_BORDER);
            dos.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell donot = new PdfPCell(new Phrase("Dont’s:", title));
            donot.setHorizontalAlignment(Element.ALIGN_CENTER);
            donot.setBorder(Rectangle.NO_BORDER);

            PdfPCell dos_1 = new PdfPCell(new Phrase("• Inspect the card for any abnormalities before initiating a transaction. Verify that it carries the Visa / MasterCard / Maestro / RuPay / Amex logo/ other valid card brand logo, a pre-printed bank name, and is within the validity period.\n", page));
            dos_1.setBorder(Rectangle.NO_BORDER);

            PdfPCell donot_1 = new PdfPCell(new Phrase("• Do not use the device for any other purpose other than the one informed during device activation.\n", page));
            donot_1.setBorder(Rectangle.NO_BORDER);

            PdfPCell dos_2 = new PdfPCell(new Phrase("• For transactions exceeding ₹50,000, obtain a signed identity proof of the cardholder/customer, in case of UPI transactions, obtain signed identity proof from the cardholder/customer for transactions exceeding ₹20,000.\n", page));
            dos_2.setBorder(Rectangle.NO_BORDER);

            PdfPCell donot_2 = new PdfPCell(new Phrase("• Do not process your own or immediate relatives credit/debit cards on your own GPRS / Android / Soundbox / GeeneePOS terminals/QR.\n", page));
            donot_2.setBorder(Rectangle.NO_BORDER);

            PdfPCell dos_3 = new PdfPCell(new Phrase("• Prepare an invoice with all relevant terms and conditions, get it signed by the cardholder/customer. Verify and retain all supporting cardholder documents for every transaction.\n", page));
            dos_3.setBorder(Rectangle.NO_BORDER);

            PdfPCell donot_3 = new PdfPCell(new Phrase("• Do not complete the transaction on your terminal/QR if you are doubtful of the authenticity of the cardholder/customer, reach out to Bijlipay service desk for further assistance.\n", page));
            donot_3.setBorder(Rectangle.NO_BORDER);

            PdfPCell dos_4 = new PdfPCell(new Phrase("• Submit the required documents to Bijlipay promptly upon request whenever an amount is withheld due to risk assessment, to facilitate release of such payments.\n", page));
            dos_4.setBorder(Rectangle.NO_BORDER);

            PdfPCell donot_4 = new PdfPCell(new Phrase("• Do not split transactions or encourage multiple swipes for a single purchase amount.\n", page));
            donot_4.setBorder(Rectangle.NO_BORDER);

            PdfPCell dos_5 = new PdfPCell(new Phrase("• Inform Bijlipay immediately of any changes in business model, ownership, or registered business address to ensure seamless operations.\n", page));
            dos_5.setBorder(Rectangle.NO_BORDER);

            PdfPCell donot_5 = new PdfPCell(new Phrase("• Do not process repeated transactions using the same card/VPA in quick succession.\n", page));
            donot_5.setBorder(Rectangle.NO_BORDER);

            PdfPCell dos_6 = new PdfPCell(new Phrase("• Update Bijlipay without delay if there are changes to your email ID, mobile number, or bank account details to maintain uninterrupted service and smooth settlements.\n", page));
            dos_6.setBorder(Rectangle.NO_BORDER);

            PdfPCell donot_6 = new PdfPCell(new Phrase("• Do not accept or share sensitive credit card/customer details through email, phone, or any other electronic communication (e.g., chat windows).\n", page));
            donot_6.setBorder(Rectangle.NO_BORDER);

            PdfPCell dos_7 = new PdfPCell(new Phrase("• Return the POS terminal/sound box/ GeeNeePOS Pro to the Bijlipay office/Bijlipay representative immediately upon termination of the Merchant Agreement.\n", page));
            dos_7.setBorder(Rectangle.NO_BORDER);

            PdfPCell donot_7 = new PdfPCell(new Phrase("• Do not process refunds through cash, cheque, online transfer, NEFT, or RTGS. Refunds must be processed only to the original source used for the purchase through the aggregator. (Any violation may lead to disputes.)\n", page));
            donot_7.setBorder(Rectangle.NO_BORDER);

            PdfPCell dos_8 = new PdfPCell(new Phrase("• Ensure that the transaction is completed and successful before delivering goods or services to the customer. Also, regularly review your statements for any settlement details.\n", page));
            dos_8.setBorder(Rectangle.NO_BORDER);

            PdfPCell donot_8 = new PdfPCell(new Phrase("• Do not hand over your POS terminal/sound box to any unauthorized person for use.\n", page));
            donot_8.setBorder(Rectangle.NO_BORDER);

            PdfPCell dos_9 = new PdfPCell(new Phrase("• If the device stops working or displays a tamper screen, please inform Bijlipay immediately and return the device.\n", page));
            dos_9.setBorder(Rectangle.NO_BORDER);

            PdfPCell donot_9 = new PdfPCell(new Phrase("• Do not provide cash against credit/debit/prepaid/gift cards, unless specifically authorized to do so under your registration.\n", page));
            donot_9.setBorder(Rectangle.NO_BORDER);

            PdfPCell dos_10 = new PdfPCell(new Phrase("• Please read the important updates sent through SMS/messaging app/e-mail/physical letter and reach out to Bijlipay service desk for any clarification.\n", page));
            dos_10.setBorder(Rectangle.NO_BORDER);

            PdfPCell donot_10 = new PdfPCell(new Phrase("• Do not charge customers any MDR or service fee for using cards/UPI for payment.\n", page));
            donot_10.setBorder(Rectangle.NO_BORDER);

            PdfPCell dos_11 = new PdfPCell(new Phrase("• As per RBI mandate we will reach out to you on a periodic basis for Re-KYC as per your business categorization, please share your updated KYC again with us for a seamless acceptance of digital transactions.\n", page));
            dos_11.setBorder(Rectangle.NO_BORDER);

            PdfPCell donot_11 = new PdfPCell(new Phrase("• Do not try to open the terminal/sound box.\n", page));
            donot_11.setBorder(Rectangle.NO_BORDER);

            PdfPCell dos_12 = new PdfPCell(new Phrase("• The Merchant shall not engage in any fraudulent, misleading, or non-genuine transactions and shall not process any transaction that lacks a valid economic purpose.\n", page));
            dos_12.setBorder(Rectangle.NO_BORDER);

            PdfPCell donot_12 = new PdfPCell(new Phrase("• Promptly report any fraud attempts or security issues to the Bijlipay.\n", page));
            donot_12.setBorder(Rectangle.NO_BORDER);

            PdfPCell dos_13 = new PdfPCell(new Phrase("• Merchant shall retain all invoices for a minimum period of 18 months from the transaction date.\n", page));
            dos_13.setBorder(Rectangle.NO_BORDER);

            PdfPCell donot_13 = new PdfPCell(new Phrase("• Never Ignore security/software updates for the POS device.\n", page));
            donot_13.setBorder(Rectangle.NO_BORDER);

            PdfPCell dos_14 = new PdfPCell(new Phrase("• Secure your Wi-Fi connection (if applicable) and never use public networks for your POS system.\n", page));
            dos_14.setBorder(Rectangle.NO_BORDER);

            PdfPCell donot_14 = new PdfPCell(new Phrase("• Don’t leave POS terminals or devices unattended for extended periods. Always monitor and secure them when in use.\n", page));
            donot_14.setBorder(Rectangle.NO_BORDER);

            PdfPCell dos_15 = new PdfPCell(new Phrase("• Ensure employees are properly trained on how to use the POS device securely and to communicated to you in case of errors and issues.\n", page));
            dos_15.setBorder(Rectangle.NO_BORDER);

            PdfPCell donot_15 = new PdfPCell(new Phrase("• QR images generated from the POS device must not be shared with customers through screenshots, photographs, or any other external medium. Customers should perform the scan directly from the live QR displayed on the device.\n", page));
            donot_15.setBorder(Rectangle.NO_BORDER);

            PdfPCell dos_16 = new PdfPCell(new Phrase("• Perform daily balancing and reconciliation of card transactions against settlement reports. Make sure to provide a receipt to every customer.\n", page));
            dos_16.setBorder(Rectangle.NO_BORDER);

            PdfPCell donot_16 = new PdfPCell(new Phrase("• Do not accept plain white cards that do not display any recognizable Bank or Card Network logo, such as Visa, Mastercard, Maestro, RuPay, American Express (Amex), or other authorized card-network branding.\n", page));
            donot_16.setBorder(Rectangle.NO_BORDER);

            PdfPCell dos_17 = new PdfPCell(new Phrase("• Do report any lost, damaged, POS machine to Bijlipay in any of the mode available (Reach out Bijlipay staffs, Raise ticket online, email, Toll-free number, etc.,).\n", page));
            dos_17.setBorder(Rectangle.NO_BORDER);

            PdfPCell donot_17 = new PdfPCell(new Phrase(""));
            donot_17.setBorder(Rectangle.NO_BORDER);

            PdfPCell dos_18 = new PdfPCell(new Phrase("• Use only live QR scans generated directly from the device.\n", page));
            dos_18.setBorder(Rectangle.NO_BORDER);

            PdfPCell donot_18 = new PdfPCell(new Phrase(""));
            donot_18.setBorder(Rectangle.NO_BORDER);

            PdfPCell dos_19 = new PdfPCell(new Phrase("• Ensure QR codes are freshly generated for every transaction.\n", page));
            dos_19.setBorder(Rectangle.NO_BORDER);

            PdfPCell donot_19 = new PdfPCell(new Phrase(""));
            donot_19.setBorder(Rectangle.NO_BORDER);

            PdfPCell dos_20 = new PdfPCell(new Phrase("• Customers must scan the QR themselves; third-party handling is strictly prohibited.\n", page));
            dos_20.setBorder(Rectangle.NO_BORDER);

            PdfPCell donot_20 = new PdfPCell(new Phrase(""));
            donot_20.setBorder(Rectangle.NO_BORDER);

            PdfPCell dos_21 = new PdfPCell(new Phrase("• Verify transaction amount and reference details before and after every scan.\n", page));
            dos_21.setBorder(Rectangle.NO_BORDER);

            PdfPCell donot_21 = new PdfPCell(new Phrase("\n\n"));
            donot_21.setBorder(Rectangle.NO_BORDER);

            //For add the table
            pdfPTable1.addCell(dos);
            pdfPTable1.addCell(donot);
            pdfPTable1.addCell(dos_1);
            pdfPTable1.addCell(donot_1);
            pdfPTable1.addCell(dos_2);
            pdfPTable1.addCell(donot_2);
            pdfPTable1.addCell(dos_3);
            pdfPTable1.addCell(donot_3);
            pdfPTable1.addCell(dos_4);
            pdfPTable1.addCell(donot_4);
            pdfPTable1.addCell(dos_5);
            pdfPTable1.addCell(donot_5);
            pdfPTable1.addCell(dos_6);
            pdfPTable1.addCell(donot_6);
            pdfPTable1.addCell(dos_7);
            pdfPTable1.addCell(donot_7);
            pdfPTable1.addCell(dos_8);
            pdfPTable1.addCell(donot_8);
            pdfPTable1.addCell(dos_9);
            pdfPTable1.addCell(donot_9);
            pdfPTable1.addCell(dos_10);
            pdfPTable1.addCell(donot_10);
            pdfPTable1.addCell(dos_11);
            pdfPTable1.addCell(donot_11);
            pdfPTable1.addCell(dos_12);
            pdfPTable1.addCell(donot_12);
            pdfPTable1.addCell(dos_13);
            pdfPTable1.addCell(donot_13);
            pdfPTable1.addCell(dos_14);
            pdfPTable1.addCell(donot_14);
            pdfPTable1.addCell(dos_15);
            pdfPTable1.addCell(donot_15);
            pdfPTable1.addCell(dos_16);
            pdfPTable1.addCell(donot_16);
            pdfPTable1.addCell(dos_17);
            pdfPTable1.addCell(donot_17);
            pdfPTable1.addCell(dos_18);
            pdfPTable1.addCell(donot_18);
            pdfPTable1.addCell(dos_19);
            pdfPTable1.addCell(donot_19);
            pdfPTable1.addCell(dos_20);
            pdfPTable1.addCell(donot_20);
            pdfPTable1.addCell(dos_21);
            pdfPTable1.addCell(donot_21);

            document.add(pdfPTable1);

            //New line
            document.add(Chunk.NEWLINE);

            // =========================================================
            // FOR THE MERCHANT SIGNATURE (UPDATED TO 2-COLUMN VERTICAL)
            // =========================================================

            PdfPTable sign = new PdfPTable(2); // Changed to 2 columns for vertical stacking
            sign.setWidthPercentage(100);
            sign.setWidths(new float[]{1, 1}); // 50% width per column gives lots of space for text

            // Row 1
            PdfPCell merchantNmae = new PdfPCell(new Phrase("Merchant Name :", text));
            merchantNmae.setBorder(Rectangle.NO_BORDER);
            PdfPCell merchantNmae_1 = new PdfPCell(new Phrase(leadInformation.getMerchantName(), title));
            merchantNmae_1.setBorder(Rectangle.NO_BORDER);

            // Row 2
            PdfPCell merchantNmaeEstablishment = new PdfPCell(new Phrase("Merchant establishment Name :", text));
            merchantNmaeEstablishment.setBorder(Rectangle.NO_BORDER);
            PdfPCell merchantNmaeEstablishment_1 = new PdfPCell(new Phrase(leadInformation.getMerchantName(), title));
            merchantNmaeEstablishment_1.setBorder(Rectangle.NO_BORDER);

            // Row 3
            // Removed the \n here because the cell is wider now and it won't need to be forced to wrap
            PdfPCell merchantEmail = new PdfPCell(new Phrase("Merchant contact number & email ID :", text));
            merchantEmail.setBorder(Rectangle.NO_BORDER);
            PdfPCell merchantEmail_1 = new PdfPCell(new Phrase(leadInformation.getContactNumber() + " & " + leadInformation.getContactEmail(), title));
            merchantEmail_1.setBorder(Rectangle.NO_BORDER);

            // Row 4
            PdfPCell merchantSerial = new PdfPCell(new Phrase("Device serial number(s):", text));
            merchantSerial.setBorder(Rectangle.NO_BORDER);
            PdfPCell merchantSerial_1 = new PdfPCell(new Phrase(serialNos, title));
            merchantSerial_1.setBorder(Rectangle.NO_BORDER);

            // Row 5
            PdfPCell installedBy = new PdfPCell(new Phrase("Installed by :", text));
            installedBy.setBorder(Rectangle.NO_BORDER);
            PdfPCell installedBy_1 = new PdfPCell(new Phrase((leadInformation.getCreatedBy() != null ? leadInformation.getCreatedBy().getName() : "") + " & " + (leadInformation.getCreatedBy() != null ? leadInformation.getCreatedBy().getEmployeeID() : ""), title));
            installedBy_1.setBorder(Rectangle.NO_BORDER);

            // Row 6
            PdfPCell DeviceType = new PdfPCell(new Phrase("Device Type :", text));
            DeviceType.setBorder(Rectangle.NO_BORDER);
            PdfPCell DeviceType_1 = new PdfPCell(new Phrase(deviceType, title));
            DeviceType_1.setBorder(Rectangle.NO_BORDER);

            // Row 7
            PdfPCell signatureAuth_2 = new PdfPCell(new Phrase("Device Model:", text));
            signatureAuth_2.setBorder(Rectangle.NO_BORDER);

            PdfPCell signatureAuth_3 = new PdfPCell(new Phrase(" "));
            signatureAuth_3.setBorder(Rectangle.NO_BORDER);
            signatureAuth_3.setCellEvent(new HiddenMarkerCellEvent(KEY_DEVICE_MODEL));

            // Row 8
            PdfPCell signatureAuth = new PdfPCell(new Phrase("Sim Number :", text));
            signatureAuth.setBorder(Rectangle.NO_BORDER);
            PdfPCell installedBy_2 = new PdfPCell(new Phrase(sim, title));
            installedBy_2.setBorder(Rectangle.NO_BORDER);

            // Row 9
            PdfPCell signatureAuth_6 = new PdfPCell(new Phrase("Merchant IP Address:", text));
            signatureAuth_6.setBorder(Rectangle.NO_BORDER);
            PdfPCell signatureAuth_7 = new PdfPCell(new Phrase(" "));
            signatureAuth_7.setBorder(Rectangle.NO_BORDER);
            signatureAuth_7.setCellEvent(new HiddenMarkerCellEvent(KEY_IP_ADDRESS));

            // Row 10
            PdfPCell signatureAuth_4 = new PdfPCell(new Phrase("Merchant Lat&Long:", text));
            signatureAuth_4.setBorder(Rectangle.NO_BORDER);
            PdfPCell signatureAuth_5 = new PdfPCell(new Phrase(" "));
            signatureAuth_5.setBorder(Rectangle.NO_BORDER);
            signatureAuth_5.setCellEvent(new HiddenMarkerCellEvent(KEY_LAT_LONG));

            // Row 11
            PdfPCell signatureAuth_8 = new PdfPCell(new Phrase("DateTime:", text));
            signatureAuth_8.setBorder(Rectangle.NO_BORDER);
            PdfPCell signatureAuth_9 = new PdfPCell(new Phrase(" "));
            signatureAuth_9.setBorder(Rectangle.NO_BORDER);
            signatureAuth_9.setCellEvent(new HiddenMarkerCellEvent(KEY_DATE_TIME));

            // Add all rows vertically
            sign.addCell(merchantNmae);
            sign.addCell(merchantNmae_1);

            sign.addCell(merchantNmaeEstablishment);
            sign.addCell(merchantNmaeEstablishment_1);

            sign.addCell(merchantEmail);
            sign.addCell(merchantEmail_1);

            sign.addCell(merchantSerial);
            sign.addCell(merchantSerial_1);

            sign.addCell(installedBy);
            sign.addCell(installedBy_1);

            sign.addCell(DeviceType);
            sign.addCell(DeviceType_1);

            sign.addCell(signatureAuth_2);
            sign.addCell(signatureAuth_3);

            sign.addCell(signatureAuth);
            sign.addCell(installedBy_2);

            sign.addCell(signatureAuth_6);
            sign.addCell(signatureAuth_7);

            sign.addCell(signatureAuth_4);
            sign.addCell(signatureAuth_5);

            sign.addCell(signatureAuth_8);
            sign.addCell(signatureAuth_9);

            //add the document
            document.add(sign);

            //New line
            document.add(Chunk.NEWLINE);
            document.add(Chunk.NEWLINE);

            // ====== FOR THE QUERY AND MAIL ========

            Paragraph query = new Paragraph("For queries, please contact: 18004200235 / e-mail: service@bijlipay.co.in ", title);
            query.setAlignment(Element.ALIGN_CENTER);
            document.add(query);
            document.close();

            return outputStream;
        } catch (DocumentException | IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static void addPara(Document document, Chunk... chunks) throws DocumentException {
        Paragraph paragraph = new Paragraph();
        paragraph.setAlignment(Element.ALIGN_JUSTIFIED);

        for (Chunk chunk : chunks) {
            paragraph.add(chunk);
        }
        document.add(paragraph);
    }

    public static void overlayGrid(String inputPdfPath, String outputPdfPath) throws IOException {
        PdfReader reader = new PdfReader(inputPdfPath);
        try (FileOutputStream fos = new FileOutputStream(outputPdfPath)) {
            PdfStamper stamper = new PdfStamper(reader, fos);

            int totalPages = reader.getNumberOfPages();
            for (int page = 1; page <= totalPages; page++) {
                com.itextpdf.text.Rectangle pageSize = reader.getPageSize(page);
                PdfContentByte canvas = stamper.getOverContent(page);

                canvas.setColorStroke(BaseColor.RED);
                canvas.setLineWidth(0.3f);

                for (float x = 0; x < pageSize.getWidth(); x += 50) {
                    canvas.moveTo(x, 0);
                    canvas.lineTo(x, pageSize.getHeight());
                    canvas.stroke();
                    canvas.beginText();
                    canvas.setFontAndSize(com.itextpdf.text.pdf.BaseFont
                            .createFont(com.itextpdf.text.pdf.BaseFont.HELVETICA,
                                    com.itextpdf.text.pdf.BaseFont.CP1252,
                                    com.itextpdf.text.pdf.BaseFont.NOT_EMBEDDED), 6);
                    canvas.showTextAligned(com.itextpdf.text.Element.ALIGN_LEFT,
                            String.valueOf((int) x), x + 1, pageSize.getHeight() - 10, 0);
                    canvas.endText();
                }

                for (float y = 0; y < pageSize.getHeight(); y += 50) {
                    canvas.moveTo(0, y);
                    canvas.lineTo(pageSize.getWidth(), y);
                    canvas.stroke();
                    canvas.beginText();
                    canvas.setFontAndSize(com.itextpdf.text.pdf.BaseFont
                            .createFont(com.itextpdf.text.pdf.BaseFont.HELVETICA,
                                    com.itextpdf.text.pdf.BaseFont.CP1252,
                                    com.itextpdf.text.pdf.BaseFont.NOT_EMBEDDED), 6);
                    canvas.showTextAligned(com.itextpdf.text.Element.ALIGN_LEFT,
                            String.valueOf((int) y), 2, y + 1, 0);
                    canvas.endText();
                }
            }
            stamper.close();
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
        reader.close();
    }

    public void updateStoredPdfWithExtraData(String filePath, ImplementationSignDto extraData) throws IOException {
        updateStoredPdfWithExtraData(filePath, extraData, this.lastCapturedCoordinates);
    }

    public void updateStoredPdfWithExtraData(String filePath, ImplementationSignDto extraData, Map<String, float[]> coordinates) throws IOException {
        Path originalPath = Paths.get(filePath);
        if (!Files.exists(originalPath)) {
            throw new IOException("PDF not found at path: " + filePath);
        }

        byte[] originalPdfBytes = Files.readAllBytes(originalPath);
        logger.info("Read existing PDF ({} bytes) from {}", originalPdfBytes.length, filePath);

        byte[] updatedPdfBytes = this.overlayExtraData(originalPdfBytes, extraData, coordinates);

        Path tempPath = Files.createTempFile(originalPath.getParent(), "implform_", ".tmp");
        Files.write(tempPath, updatedPdfBytes);
        Files.move(tempPath, originalPath, StandardCopyOption.REPLACE_EXISTING);

        logger.info("Overwrote PDF at {} with extra data ({} bytes)", filePath, updatedPdfBytes.length);
    }

    public byte[] overlayExtraData(byte[] originalPdfBytes, ImplementationSignDto extraData) throws IOException {
        return overlayExtraData(originalPdfBytes, extraData, this.lastCapturedCoordinates);
    }

    public byte[] overlayExtraData(byte[] originalPdfBytes, ImplementationSignDto extraData, Map<String, float[]> coordinates) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfReader reader = new PdfReader(originalPdfBytes);

        try {
            PdfStamper stamper = new PdfStamper(reader, outputStream);
            try {
                AcroFields form = stamper.getAcroFields();
                BaseFont baseFont = BaseFont.createFont(BaseFont.COURIER, BaseFont.CP1250, BaseFont.NOT_EMBEDDED);

                // Use the new dynamic wrapping text overlay
                stampWrappedDynamicText(stamper, form, baseFont, KEY_DEVICE_MODEL, extraData.getDeviceName(), getXY(coordinates, KEY_DEVICE_MODEL, DEFAULT_DEVICE_MODEL_XY));
                stampWrappedDynamicText(stamper, form, baseFont, KEY_LAT_LONG, extraData.getLatLong(), getXY(coordinates, KEY_LAT_LONG, DEFAULT_LATLONG_XY));
                stampWrappedDynamicText(stamper, form, baseFont, KEY_IP_ADDRESS, extraData.getIpAddress(), getXY(coordinates, KEY_IP_ADDRESS, DEFAULT_IP_ADDRESS_XY));
                stampWrappedDynamicText(stamper, form, baseFont, KEY_DATE_TIME, extraData.getDateTime(), getXY(coordinates, KEY_DATE_TIME, DEFAULT_DATE_TIME_XY));

                // Completely clean up the hidden markers from the final document
                stamper.setFormFlattening(true);

            } finally {
                stamper.close();
            }
        } catch (com.itextpdf.text.DocumentException e) {
            throw new IOException("Failed to overlay data onto PDF", e);
        } finally {
            reader.close();
        }

        return outputStream.toByteArray();
    }

    /**
     * Finds the hidden marker field, extracts its X/Y location, and uses ColumnText
     * to draw standard text that naturally wraps downwards if the string is long.
     */
    private void stampWrappedDynamicText(PdfStamper stamper, AcroFields form, BaseFont baseFont, String fieldName, String text, float[] fallbackXY) {
        if (text == null || text.isBlank()) return;

        // Fallback variables in case the hidden field isn't in the PDF
        float llx = fallbackXY[0];
        float lly = fallbackXY[1] - 50f;
        float urx = fallbackXY[0] + 130f;
        float ury = fallbackXY[1] + 10.5f;
        int targetPage = TARGET_PAGE;

        try {
            List<AcroFields.FieldPosition> positions = form.getFieldPositions(fieldName);
            if (positions != null && !positions.isEmpty()) {
                AcroFields.FieldPosition pos = positions.get(0);
                Rectangle rect = pos.position;
                llx = rect.getLeft();
                urx = rect.getRight() + 10f; // Give a slight 10pt bleed allowance for wrapping
                ury = rect.getTop();
                lly = rect.getTop() - 80f;   // Allow up to 80pt height for text to flow downwards
                targetPage = pos.page;
            }
        } catch (Exception e) {
            logger.warn("Error locating hidden marker {}, falling back to fixed coordinates", fieldName);
        }

        if (targetPage > stamper.getReader().getNumberOfPages()) {
            targetPage = stamper.getReader().getNumberOfPages();
        }

        PdfContentByte canvas = stamper.getOverContent(targetPage);
        ColumnText ct = new ColumnText(canvas);
        ct.setSimpleColumn(llx, lly, urx, ury);

        Font font = new Font(baseFont, 11, Font.BOLD, BaseColor.BLACK);
        Paragraph paragraph = new Paragraph(text, font);
        paragraph.setLeading(12f); // Tight line spacing so wrapped lines fit nicely

        ct.addElement(paragraph);
        try {
            ct.go();
        } catch (DocumentException e) {
            logger.error("Failed to wrap text overlay for {}", fieldName, e);
        }
    }

    private float[] getXY(Map<String, float[]> coordinates, String key, float[] fallback) {
        if (coordinates != null && coordinates.containsKey(key)) {
            return coordinates.get(key);
        }
        return fallback;
    }
}
