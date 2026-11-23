package com.bijlipay.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;

public class ByteArrayMultiPartFile implements MultipartFile {

    private  String name;
    private  String originalFilename;
    private  String contentType;
    private  byte[] bytes;

    public ByteArrayMultiPartFile(String name, String originalFilename, String contentType, byte[] bytes) {
        this.name = name;
        this.originalFilename = originalFilename;
        this.contentType = contentType;
        this.bytes = bytes;
    }

    public ByteArrayMultiPartFile() {
    }



    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getOriginalFilename() {
        return this.originalFilename;
    }

    @Override
    public String getContentType() {
        return this.contentType;
    }

    @Override
    public boolean isEmpty() {
        return this.bytes == null || this.bytes.length == 0;
    }

    @Override
    public long getSize() {
        return this.bytes.length;
    }

    @Override
    public byte[] getBytes() throws IOException {
        return this.bytes;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(this.bytes);
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        try (FileOutputStream fos = new FileOutputStream(dest)) {
            fos.write(this.bytes);
        }
    }
}
