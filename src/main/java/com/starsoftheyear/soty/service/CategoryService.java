package com.starsoftheyear.soty.service;

import com.starsoftheyear.soty.domain.Category;
import com.starsoftheyear.soty.dto.CategoryCreateRequest;
import com.starsoftheyear.soty.dto.CategoryDto;
import com.starsoftheyear.soty.dto.CategoryUpdateRequest;
import com.starsoftheyear.soty.repo.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository repo;

    // ---------- Public reads ----------
    public List<CategoryDto> findPublished() {
        return repo.findAllByStatusOrderBySortOrderAsc("published")
                .stream()
                .map(CategoryDto::from)
                .toList();
    }

    public CategoryDto getBySlug(String slug) {
        return repo.findBySlug(slug)
                .filter(c -> "published".equalsIgnoreCase(c.getStatus()))
                .map(CategoryDto::from)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
    }

    // ---------- Admin create ----------
    public CategoryDto create(CategoryCreateRequest req) {
        String slug = sanitizeSlug(req.getSlug());

        if (repo.existsBySlug(slug)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Slug already exists");
        }

        Category c = new Category();
        c.setSlug(slug);
        c.setName(req.getName().trim());
        c.setDescription(req.getDescription()); // may be null
        c.setSortOrder(req.getSortOrder() == null ? 0 : req.getSortOrder());
        c.setStatus(normalizeStatus(defaultIfBlank(req.getStatus(), "draft")));

        Category saved = repo.save(c);
        return CategoryDto.from(saved);
    }

    // ---------- Admin update (partial) ----------
    public CategoryDto update(String currentSlug, CategoryUpdateRequest req) {
        Category c = repo.findBySlug(currentSlug)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        // rename slug if provided
        if (req.getSlug() != null) {
            String newSlug = sanitizeSlug(req.getSlug());
            if (!newSlug.equals(c.getSlug()) && repo.existsBySlug(newSlug)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Slug already exists");
            }
            c.setSlug(newSlug);
        }

        if (req.getName() != null) {
            c.setName(req.getName().trim());
        }
        if (req.getDescription() != null) {
            c.setDescription(req.getDescription());
        }
        if (req.getSortOrder() != null) {
            c.setSortOrder(req.getSortOrder());
        }
        if (req.getStatus() != null) {
            c.setStatus(normalizeStatus(req.getStatus()));
        }

        Category saved = repo.save(c);
        return CategoryDto.from(saved);
    }

    // ---------- Admin delete ----------
    public void delete(String slug) {
        Category c = repo.findBySlug(slug)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
        repo.delete(c);
    }

    // ---------- helpers ----------
    private static String sanitizeSlug(String s) {
        return s.trim().toLowerCase();
    }

    private static String defaultIfBlank(String s, String def) {
        return (s == null || s.isBlank()) ? def : s;
    }

    private static String normalizeStatus(String s) {
        String v = s.trim().toLowerCase();
        if (!v.equals("draft") && !v.equals("published")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status must be 'draft' or 'published'");
        }
        return v;
    }
}
