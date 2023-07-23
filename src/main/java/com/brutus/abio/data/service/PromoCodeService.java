package com.brutus.abio.data.service;

import com.brutus.abio.data.entity.PromoCode;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class PromoCodeService {

    private final PromoCodeRepository repository;

    public PromoCodeService(PromoCodeRepository repository) {
        this.repository = repository;
    }

    public Optional<PromoCode> get(Long id) {
        return repository.findById(id);
    }

    public PromoCode update(PromoCode entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<PromoCode> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<PromoCode> list(Pageable pageable, Specification<PromoCode> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
