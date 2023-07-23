package com.brutus.abio.data.service;

import com.brutus.abio.data.entity.Header;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface HeaderRepository extends JpaRepository<Header, Long>, JpaSpecificationExecutor<Header> {

}
