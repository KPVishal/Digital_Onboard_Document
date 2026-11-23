package com.bijlipay.model.dto.signzy.esign;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SignaturesDto {
    private List<Integer> pageNo;
    private List<String> signaturePosition;
    private int height;
    private int width;
    @JsonProperty("x-coordinate")
    private List<Integer> xCoordinate;
    @JsonProperty("y-coordinate")
    private List<Integer> yCoordinate;

    public List<Integer> getPageNo() {
        return pageNo;
    }

    public void setPageNo(List<Integer> pageNo) {
        this.pageNo = pageNo;
    }

    public List<String> getSignaturePosition() {
        return signaturePosition;
    }

    public void setSignaturePosition(List<String> signaturePosition) {
        this.signaturePosition = signaturePosition;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public List<Integer> getxCoordinate() {
        return xCoordinate;
    }

    public void setxCoordinate(List<Integer> xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public List<Integer> getyCoordinate() {
        return yCoordinate;
    }

    public void setyCoordinate(List<Integer> yCoordinate) {
        this.yCoordinate = yCoordinate;
    }
}
