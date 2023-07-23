package com.brutus.abio.data.service;

import com.brutus.abio.data.entity.DeliveryRegion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DeliveryRegionRepository
        extends
            JpaRepository<DeliveryRegion, Long>,
            JpaSpecificationExecutor<DeliveryRegion> {

}
