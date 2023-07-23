package com.brutus.abio.data.service;

import com.brutus.abio.data.entity.PromoCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PromoCodeRepository extends JpaRepository<PromoCode, Long>, JpaSpecificationExecutor<PromoCode> {

}
