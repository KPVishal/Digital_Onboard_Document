package com.bijlipay.repository;

import com.bijlipay.model.DocumentCategory;
import com.bijlipay.model.DocumentType;
import com.bijlipay.model.MerchantCategoryCode;
import com.bijlipay.model.SubDocumentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubDocumentTypeRepository extends JpaRepository<SubDocumentType, Long> {

    @Query("select s from SubDocumentType s where s.subDocumentTypeId =?1")
    SubDocumentType findBySubDocumentId(long subDocumentType);

    @Query("SELECT s.documentType FROM SubDocumentType s WHERE s.documentCategory= ?1 GROUP BY s.documentType")
    List<DocumentType> fetchDocumentTypeList(DocumentCategory documentCategory);

    @Query("SELECT s FROM SubDocumentType s WHERE s.documentCategory =?1 AND s.isSignzyVerificationRequired=?2")
    List<SubDocumentType> fetchSignzyVerificationRequiredSubDocTypeList(DocumentCategory documentCategory,boolean value);

    @Query("select s from SubDocumentType s where s.mccCategoryIndicator IN (?1)")
    List<SubDocumentType> findDocumentListByMccIndicator(List<Integer> mccIndicator);
    @Query("SELECT s FROM SubDocumentType s WHERE s.documentType =?1")
    List<SubDocumentType> fetchSubDocumentTypeList(DocumentType documentType);

    @Query("SELECT s FROM SubDocumentType s WHERE s.name=?1 AND s.documentCategory= ?2 AND s.merchantCategoryCode=?3")
    SubDocumentType findSubDocNameAndCatagory(String name, DocumentCategory catagory, MerchantCategoryCode mcc);
    @Query("SELECT s FROM SubDocumentType s WHERE s.name=?1 AND s.documentCategory= ?2")
    SubDocumentType findSubDocNameAndCatagoryOther(String name, DocumentCategory catagory);

    @Query("SELECT s FROM SubDocumentType s WHERE s.name=?1 AND s.documentCategory= ?2")
    List<SubDocumentType> findSubDocNameAndCatagoryOtherList(String name, DocumentCategory catagory);

    @Query("SELECT s FROM SubDocumentType s WHERE s.documentCategory= ?1")
    List<SubDocumentType> fetchDocumentType(DocumentCategory documentCategory);

    @Query("SELECT s FROM SubDocumentType s WHERE s.name=?1 AND s.mccCategoryIndicator= ?2")
    SubDocumentType findSubDocNameAndMccIndicator(String name, int catagory);
}
