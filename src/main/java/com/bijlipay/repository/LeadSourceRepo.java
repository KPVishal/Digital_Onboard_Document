package com.bijlipay.repository;

import com.bijlipay.model.LeadSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LeadSourceRepo extends JpaRepository<LeadSource, Integer> {
    @Query("SELECT l FROM LeadSource l WHERE l.leadSourceName=?1")
    LeadSource findByLeadSourceName(String leadSourceName);
}
