package com.bijlipay.repository;

import com.bijlipay.model.DocumentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentTypeRepository extends JpaRepository<DocumentType, Long> {

//    @Query("select d from DocumentType d where d.id =?1")
//    DocumentType findByDocumentName(DocumentType documentType);

    @Query("select d from DocumentType d where d.id =?1")
    DocumentType findByDocumentId(long documentType);
}
