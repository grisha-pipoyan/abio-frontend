package com.brutus.abio.data.service;

import com.brutus.abio.data.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface VideoRepository extends JpaRepository<Video, Long>, JpaSpecificationExecutor<Video> {

}
