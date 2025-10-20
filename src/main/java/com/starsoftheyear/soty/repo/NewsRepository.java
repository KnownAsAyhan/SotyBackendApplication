package com.starsoftheyear.soty.repo;

import com.starsoftheyear.soty.domain.News;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NewsRepository extends JpaRepository<News, Long> {

    Optional<News> findBySlug(String slug);

    boolean existsBySlug(String slug);

    // Public reads
    List<News> findAllByStatusOrderByCreatedAtDesc(String status);

    Optional<News> findBySlugAndStatus(String slug, String status);
}
