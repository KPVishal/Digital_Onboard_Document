package com.bijlipay.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.util.Pair;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import static com.bijlipay.config.Constants.*;


@Component
public class MailHandler {
    @Autowired
    private  JavaMailSender javaMailSender;

    public Pair<Integer,String> sendAgreementUrlToMerchant(String email , String url) throws MessagingException {
        try {

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,true,"utf-8");
            message.setFrom("reports@bijlipay.co.in");
            message.setRecipients(Message.RecipientType.TO,email);
            message.setSubject("AGREEMENT URL");
            String text = "Hi,\n\nPlease find the agreement url :\t"+url+"\n\nIt will expires within few mins.";
            message.setText(text);
            javaMailSender.send(message);
            return Pair.of(1,"mail send successfully");
        }catch ( MessagingException e){
            e.printStackTrace();
            return Pair.of(0,"mail not sent");
        }
    }
    public void sendEmail(String email, String signedFile) throws Exception {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(email);
        helper.setSubject("Agreement Signed Copy-" + DateUtil.currentDate());
        helper.setText(String.format("Please find the Attachment"));
        FileSystemResource file = new FileSystemResource(AGREEMENT_SIGNED_DOCUMENT_PATH + signedFile);
        helper.addAttachment(file.getFilename(), file);
        javaMailSender.send(message);
    }

    public void sendDocumentRejectedEmail(String email, String leadNumber, String documentType,String leadSource) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        message.setFrom("reports@bijlipay.co.in");
        MimeMessageHelper helper = new MimeMessageHelper(message,true);
        if(leadSource.equalsIgnoreCase(LEAD_SOURCE_PET_POOJA)){
            InternetAddress[] internetAddresses=InternetAddress.parse("tapaswini.behera@petpooja.com"+","+"muskan.batham@petpooja.com",true);
//            InternetAddress[] internetAddresses=InternetAddress.parse("mrithun.r@bijlipay.co.in"+","+"senthamizhselvan.s@bijlipay.co.in"+","+"arshath.a@bijlipay.co.in",true);
            message.setRecipients(Message.RecipientType.TO,internetAddresses);
        }else if (leadSource.equalsIgnoreCase(LEAD_SOURCE_FIDY_PAY)){
            InternetAddress[] internetAddresses=InternetAddress.parse("mrithun.r@bijlipay.co.in"+","+"senthamizhselvan.s@bijlipay.co.in"+","+"arshath.a@bijlipay.co.in",true);
            message.setRecipients(Message.RecipientType.TO,internetAddresses);
        }else if (leadSource.equalsIgnoreCase(LEAD_SOURCE_VIYONA_FINTECH)){
            InternetAddress[] internetAddresses=InternetAddress.parse("mrithun.r@bijlipay.co.in"+","+"senthamizhselvan.s@bijlipay.co.in"+","+"arshath.a@bijlipay.co.in",true);
            message.setRecipients(Message.RecipientType.TO,internetAddresses);
        }else if (leadSource.equalsIgnoreCase(LEAD_SOURCE_KVB)){
            InternetAddress[] internetAddresses=InternetAddress.parse("mrithun.r@bijlipay.co.in"+","+"senthamizhselvan.s@bijlipay.co.in"+","+"arshath.a@bijlipay.co.in",true);
            message.setRecipients(Message.RecipientType.TO,internetAddresses);
        }
        else if (leadSource.equalsIgnoreCase("LS_Wowpe")){
            InternetAddress[] internetAddresses=InternetAddress.parse("anitha@wowpe.in",true);
            message.setRecipients(Message.RecipientType.TO,internetAddresses);
        }
        message.setSubject("Document Rejected - "+documentType +" | " +leadNumber);
        String body ="<body><html><p>Hi Team,</p>\n" +
                "<p>Greetings from Bijlipay!</p>\n<p>There is an update on this lead <b>"+leadNumber+"</b>. Please check the onboarding portal for more information</p>";
//        helper.setText(body,true);
//        Resource resource = fileStorageService.loadFileAsResource("Logo.png");
//        helper.addInline("attachment.png", resource);
        String signature = body+ "<p><b>Best Regards,<br/>Bijlipay Team</b></p>" +
//                "<img src = D:/MASTER_ONBOARD/digital/DocumentMoule/uploadDocumentDir/Logo.png>" +
                "</body></html>";
        message.setContent(signature, "text/html");
//        Transport.send(message);
//        helper.setText(body + signature, true);
        javaMailSender.send(message);
    }
}
