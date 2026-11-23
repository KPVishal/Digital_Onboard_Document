package com.bijlipay.repository;

import com.bijlipay.model.SimProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SimRepository extends JpaRepository<SimProvider,Long> {
    @Query(value="Select s from SimProvider s where s.networkName=?1")
    SimProvider findSimByName(String simName);
}
