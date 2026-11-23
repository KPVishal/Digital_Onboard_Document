package com.bijlipay.service;

import com.bijlipay.model.LeadInformation;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public interface ByteArrayMultiPartFileService {
    String convertUrlToPdf(String signedFileUrl, LeadInformation leadInformation) throws IOException;

    String convertUrlToPdfForContract(String signedFileUrl, LeadInformation leadInformation, String fileName) throws IOException;

    MultipartFile convertByteArrayOutputStreamToMultiPartFile(ByteArrayOutputStream byteArrayOutputStream, String fileName, String contentType);
}
