package com.brutus.abio.data.service;

import com.brutus.abio.data.entity.DeliveryRegion;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class DeliveryRegionService {

    private final DeliveryRegionRepository repository;

    public DeliveryRegionService(DeliveryRegionRepository repository) {
        this.repository = repository;
    }

    public Optional<DeliveryRegion> get(Long id) {
        return repository.findById(id);
    }

    public DeliveryRegion update(DeliveryRegion entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<DeliveryRegion> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<DeliveryRegion> list(Pageable pageable, Specification<DeliveryRegion> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
