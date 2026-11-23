package com.bijlipay.model.dto.signzy.fetch;

import java.util.List;

public class FilesDto {

    private String name;
    private String type;
    private String size;
    private String date;
    private String parent;
    private List<String> mime;
    private String doctype;
    private String description;
    private String issuerid;
    private String issuer;
    private String id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public List<String> getMime() {
        return mime;
    }

    public void setMime(List<String> mime) {
        this.mime = mime;
    }

    public String getDoctype() {
        return doctype;
    }

    public void setDoctype(String doctype) {
        this.doctype = doctype;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIssuerid() {
        return issuerid;
    }

    public void setIssuerid(String issuerid) {
        this.issuerid = issuerid;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
