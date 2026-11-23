package com.bijlipay.repository;

import com.bijlipay.model.DocumentCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentCategoryRepository extends JpaRepository<DocumentCategory, Long> {
    @Query("SELECT d FROM DocumentCategory d WHERE d.id IN (?1)")
    List<DocumentCategory> fetchDocumentCategoryListById(List<Long> valueList);

    @Query("SELECT d FROM DocumentCategory d WHERE d.id =?1")
    DocumentCategory getDocById(long catagory);

    @Query("select m from DocumentCategory m where m.name like '%Proof Of Business%'")
    List<DocumentCategory> findBusinessDoc();

}
