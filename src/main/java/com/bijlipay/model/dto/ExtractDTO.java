package com.bijlipay.model.dto;

import java.util.Arrays;

public class ExtractDTO {

    private String[] files;
    private String type;
    private String frontUrl;


    public String[] getFiles() {
        return files;
    }

    public void setFiles(String[] files) {
        this.files = files;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFrontUrl() {
        return frontUrl;
    }

    public void setFrontUrl(String frontUrl) {
        this.frontUrl = frontUrl;
    }

    @Override
    public String toString() {
        return "ExtractDTO{" +
                "files=" + Arrays.toString(files) +
                ", type='" + type + '\'' +
                ", frontUrl='" + frontUrl + '\'' +
                '}';
    }
}
