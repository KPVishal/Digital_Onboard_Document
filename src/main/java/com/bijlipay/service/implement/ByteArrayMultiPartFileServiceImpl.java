package com.bijlipay.service.implement;

import com.bijlipay.model.LeadInformation;
import com.bijlipay.service.ByteArrayMultiPartFileService;

import com.bijlipay.service.FileStorageService;
import com.bijlipay.util.ByteArrayMultiPartFile;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import javax.sound.midi.Patch;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

@Service
public class ByteArrayMultiPartFileServiceImpl implements ByteArrayMultiPartFileService {
    private static final Logger logger = LoggerFactory.getLogger(ByteArrayMultiPartFileServiceImpl.class);
    @Autowired
    private FileStorageService fileStorageService;

    @Override
    public String convertUrlToPdf(String signedFileUrl, LeadInformation leadInformation) throws IOException {
        if (signedFileUrl != null && !signedFileUrl.isEmpty()) {
            logger.info("signedURL -- {}", signedFileUrl);
            try {
                URL url = new URL(signedFileUrl);
                InputStream inputStream = url.openStream();
                byte[] bytes = IOUtils.toByteArray(inputStream);
                String newFileName = leadInformation.getLeadId() + "_" + "AgreementSigned.pdf";
                MultipartFile multipartFile = new ByteArrayMultiPartFile(
                        "file",
                        newFileName,
                        MediaType.APPLICATION_PDF_VALUE,
                        bytes
                );
                //store input file
                logger.info("new File Name --- : {}", newFileName);
                Map<String, String> response = fileStorageService.storeFile(multipartFile, newFileName);
                return response.get("fileName");
            } catch (MalformedURLException e) {
                e.printStackTrace();
                logger.info("MalformedURLException ----:{}", e.getMessage());
                return null;
            }
        } else {
            logger.info("signedURL --:{}", signedFileUrl);
            return null;
        }

    }
    @Override
    public String convertUrlToPdfForContract(String signedFileUrl, LeadInformation leadInformation, String fileName) throws IOException {
        if (signedFileUrl != null && !signedFileUrl.isEmpty()) {
            logger.info("signedURL -- {}", signedFileUrl);
            try {
                URL url = new URL(signedFileUrl);
                InputStream inputStream = url.openStream();
                byte[] bytes = IOUtils.toByteArray(inputStream);
                String newFileName = leadInformation.getLeadId() + "_" + fileName+".pdf";
                MultipartFile multipartFile = new ByteArrayMultiPartFile(
                        "file",
                        newFileName,
                        MediaType.APPLICATION_PDF_VALUE,
                        bytes
                );
                //store input file
                logger.info("new File Name --- : {}", newFileName);
                Map<String, String> response = fileStorageService.storeFile(multipartFile, newFileName);
                logger.info("file name -------- : {}",response.get("fileName"));
                return response.get("fileName");
            } catch (MalformedURLException e) {
                e.printStackTrace();
                logger.info("MalformedURLException ----:{}", e.getMessage());
                return null;
            }
        } else {
            logger.info("signedURL --:{}", signedFileUrl);
            return null;
        }
    }
    @Override
    public MultipartFile convertByteArrayOutputStreamToMultiPartFile(ByteArrayOutputStream byteArrayOutputStream, String fileName, String contentType) {
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return new ByteArrayMultiPartFile("file", fileName, contentType, byteArray);
    }
}
