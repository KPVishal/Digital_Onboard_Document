package com.bijlipay.repository;

import com.bijlipay.model.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RegionRepository extends JpaRepository<Region, Integer> {

    @Query(value = "Select * from region where region_code=?1",nativeQuery = true)
    Region findRegionById(String id);
}
