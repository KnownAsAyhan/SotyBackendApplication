package com.starsoftheyear.soty.repo;

import com.starsoftheyear.soty.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findBySlug(String slug);

    boolean existsBySlug(String slug);

    // For public listing: published categories ordered by sort_order ascending
    List<Category> findAllByStatusOrderBySortOrderAsc(String status);
}
