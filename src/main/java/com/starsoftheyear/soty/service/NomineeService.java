package com.starsoftheyear.soty.service;

import com.starsoftheyear.soty.domain.Category;
import com.starsoftheyear.soty.domain.Nominee;
import com.starsoftheyear.soty.dto.NomineeCreateRequest;
import com.starsoftheyear.soty.dto.NomineeDto;
import com.starsoftheyear.soty.dto.NomineeUpdateRequest;
import com.starsoftheyear.soty.repo.CategoryRepository;
import com.starsoftheyear.soty.repo.NomineeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NomineeService {

    private final NomineeRepository nomineeRepo;
    private final CategoryRepository categoryRepo;

    // ------------- Admin (write) -------------

    public NomineeDto create(NomineeCreateRequest req) {
        String slug = norm(req.getSlug());
        if (nomineeRepo.existsBySlug(slug)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Slug already exists");
        }

        Category category = categoryRepo.findBySlug(norm(req.getCategorySlug()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        Nominee n = new Nominee();
        n.setSlug(slug);
        n.setName(req.getName().trim());
        n.setDescription(req.getDescription());
        n.setImageUrl(req.getImageUrl());
        n.setStatus(norm(req.getStatus(), "draft"));
        n.setSortOrder(req.getSortOrder() == null ? 0 : req.getSortOrder());
        n.setCategory(category);

        return NomineeDto.from(nomineeRepo.save(n));
    }

    public NomineeDto update(String slug, NomineeUpdateRequest req) {
        Nominee n = nomineeRepo.findBySlug(norm(slug))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nominee not found"));

        // Optional slug change
        if (hasText(req.getSlug())) {
            String newSlug = norm(req.getSlug());
            if (!newSlug.equalsIgnoreCase(n.getSlug()) && nomineeRepo.existsBySlug(newSlug)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Slug already exists");
            }
            n.setSlug(newSlug);
        }

        if (hasText(req.getName()))        n.setName(req.getName().trim());
        if (req.getDescription() != null)  n.setDescription(req.getDescription());
        if (req.getImageUrl() != null)     n.setImageUrl(req.getImageUrl());
        if (hasText(req.getStatus()))      n.setStatus(norm(req.getStatus()));
        if (req.getSortOrder() != null)    n.setSortOrder(req.getSortOrder());

        if (hasText(req.getCategorySlug())) {
            Category category = categoryRepo.findBySlug(norm(req.getCategorySlug()))
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
            n.setCategory(category);
        }

        return NomineeDto.from(nomineeRepo.save(n));
    }

    public void delete(String slug) {
        Nominee n = nomineeRepo.findBySlug(norm(slug))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nominee not found"));
        nomineeRepo.delete(n);
    }

    // ------------- Public (read) -------------

    @Transactional(readOnly = true)
    public List<NomineeDto> findPublishedByCategory(String categorySlug) {
        String cat = norm(categorySlug);
        return nomineeRepo.findByCategory_SlugIgnoreCase(cat).stream()
                .filter(n -> "published".equalsIgnoreCase(n.getStatus()))
                .sorted((a, b) -> {
                    Integer as = a.getSortOrder() == null ? Integer.MAX_VALUE : a.getSortOrder();
                    Integer bs = b.getSortOrder() == null ? Integer.MAX_VALUE : b.getSortOrder();
                    int cmp = Integer.compare(as, bs);
                    return (cmp != 0) ? cmp : Long.compare(a.getId(), b.getId());
                })
                .map(NomineeDto::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public NomineeDto getPublishedBySlug(String slug) {
        return nomineeRepo.findBySlug(norm(slug))
                .filter(n -> "published".equalsIgnoreCase(n.getStatus()))
                .map(NomineeDto::from)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nominee not found"));
    }

    // ------------- utils -------------

    private static boolean hasText(String s) {
        return s != null && !s.isBlank();
    }

    private static String norm(String s) {
        return s == null ? null : s.trim().toLowerCase();
    }

    private static String norm(String s, String dflt) {
        String v = norm(s);
        return v == null || v.isBlank() ? dflt : v;
    }
}
