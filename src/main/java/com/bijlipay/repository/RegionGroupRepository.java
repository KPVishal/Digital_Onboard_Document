package com.bijlipay.repository;

import com.bijlipay.model.RegionGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RegionGroupRepository extends JpaRepository<RegionGroup, Integer> {


}
