package com.bijlipay.property;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class FileConfig {

    @Value("${files.path}")
    private String filesPath;

    public Path resolve(String fileName) {
        return Paths.get(filesPath, fileName);
    }

    public String getFilesPath() {
        return filesPath;
    }

    public void setFilesPath(String filesPath) {
        this.filesPath = filesPath;
    }
}

