package com.bijlipay.model.dto;

import com.bijlipay.model.QrVpa;
import java.util.List;

public class VpaShareDto {

    private String leadNumber;
    private List<String> vpas;

    private String vpaStatus;

    public String getVpaStatus() {
        return vpaStatus;
    }

    public void setVpaStatus(String vpaStatus) {
        this.vpaStatus = vpaStatus;
    }
//    private List<QrVpa> vpaList;

    public String getLeadNumber() {
        return leadNumber;
    }

    public void setLeadNumber(String leadNumber) {
        this.leadNumber = leadNumber;
    }

    public List<String> getVpas() {
        return vpas;
    }

    public void setVpas(List<String> vpas) {
        this.vpas = vpas;
    }

//    public List<QrVpa> getVpaList() {
//        return vpaList;
//    }
//
//    public void setVpaList(List<QrVpa> vpaList) {
//        this.vpaList = vpaList;
//    }
}
