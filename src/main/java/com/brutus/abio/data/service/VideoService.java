package com.brutus.abio.data.service;

import com.brutus.abio.data.entity.Video;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class VideoService {

    private final VideoRepository repository;

    public VideoService(VideoRepository repository) {
        this.repository = repository;
    }

    public Optional<Video> get(Long id) {
        return repository.findById(id);
    }

    public Video update(Video entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<Video> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<Video> list(Pageable pageable, Specification<Video> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
