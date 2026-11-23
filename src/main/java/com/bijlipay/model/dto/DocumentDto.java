package com.bijlipay.model.dto;

import com.bijlipay.model.SubDocumentType;

import java.util.List;

public class DocumentDto {
    private LeadDto leadDto;
    private List<LeadDocumentDto> documentDtoList;

    public LeadDto getLeadDto() {
        return leadDto;
    }

    public void setLeadDto(LeadDto leadDto) {
        this.leadDto = leadDto;
    }

    public List<LeadDocumentDto> getDocumentDtoList() {
        return documentDtoList;
    }

    public void setDocumentDtoList(List<LeadDocumentDto> documentDtoList) {
        this.documentDtoList = documentDtoList;
    }
}
