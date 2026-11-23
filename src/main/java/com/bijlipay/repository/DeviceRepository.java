package com.bijlipay.repository;

import com.bijlipay.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceRepository extends JpaRepository<Device,Long> {
    @Query("SELECT d FROM Device d WHERE d.deviceName=?1")
    Device findDevice(String deviceName);
}
