package com.bijlipay.repository;

import com.bijlipay.model.Device;
import com.bijlipay.model.LeadSource;
import com.bijlipay.model.MDRCharge;
import com.bijlipay.model.MerchantCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MDRChargeRepository extends JpaRepository<MDRCharge, Long> {
}
