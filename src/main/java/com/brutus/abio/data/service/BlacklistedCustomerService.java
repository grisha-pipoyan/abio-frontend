package com.brutus.abio.data.service;

import com.brutus.abio.data.entity.BlacklistedCustomer;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class BlacklistedCustomerService {

    private final BlacklistedCustomerRepository repository;

    public BlacklistedCustomerService(BlacklistedCustomerRepository repository) {
        this.repository = repository;
    }

    public Optional<BlacklistedCustomer> get(Long id) {
        return repository.findById(id);
    }

    public BlacklistedCustomer update(BlacklistedCustomer entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<BlacklistedCustomer> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<BlacklistedCustomer> list(Pageable pageable, Specification<BlacklistedCustomer> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
