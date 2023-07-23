package com.brutus.abio.data.service;

import com.brutus.abio.data.entity.BlacklistedCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BlacklistedCustomerRepository
        extends
            JpaRepository<BlacklistedCustomer, Long>,
            JpaSpecificationExecutor<BlacklistedCustomer> {

}
