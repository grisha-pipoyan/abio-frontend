package com.brutus.abio.data.service;

import com.brutus.abio.data.entity.Header;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class HeaderService {

    private final HeaderRepository repository;

    public HeaderService(HeaderRepository repository) {
        this.repository = repository;
    }

    public Optional<Header> get(Long id) {
        return repository.findById(id);
    }

    public Header update(Header entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<Header> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<Header> list(Pageable pageable, Specification<Header> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
