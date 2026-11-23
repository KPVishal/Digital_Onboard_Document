package com.bijlipay.repository;

import com.bijlipay.model.Auditing_Table;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditingTableRepository extends JpaRepository<Auditing_Table,Long> {


}
