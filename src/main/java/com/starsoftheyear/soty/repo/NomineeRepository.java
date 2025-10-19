package com.starsoftheyear.soty.repo;

import com.starsoftheyear.soty.domain.Nominee;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NomineeRepository extends JpaRepository<Nominee, Long> {
    Optional<Nominee> findBySlug(String slug);
    boolean existsBySlug(String slug);

    // Traverse nested property (category.slug) and fetch category eagerly
    @EntityGraph(attributePaths = "category")
    List<Nominee> findByCategory_SlugIgnoreCase(String categorySlug);
}
