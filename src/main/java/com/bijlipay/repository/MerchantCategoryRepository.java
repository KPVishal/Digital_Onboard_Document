package com.bijlipay.repository;

import com.bijlipay.model.MerchantCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MerchantCategoryRepository extends JpaRepository<MerchantCategory,Long> {
}
