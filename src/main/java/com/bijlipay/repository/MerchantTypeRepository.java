package com.bijlipay.repository;

import com.bijlipay.model.MerchantType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MerchantTypeRepository extends JpaRepository<MerchantType,Integer> {
    @Query("SELECT m FROM MerchantType m")
    List<MerchantType> getAll();

    @Query("SELECT m FROM MerchantType m where m.isActive=1")
    List<MerchantType> getAllActiveList();

    @Query("SELECT m FROM MerchantType m where m.DOActive=1")
    List<MerchantType> getAllActive();

    @Query("SELECT m FROM MerchantType m WHERE m.merchantTypeName=?1")
    MerchantType findByName(String merchantType);
}
