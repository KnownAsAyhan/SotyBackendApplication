package com.starsoftheyear.soty.service;

import com.starsoftheyear.soty.domain.Page;
import com.starsoftheyear.soty.dto.PageCreateRequest;
import com.starsoftheyear.soty.dto.PageDto;
import com.starsoftheyear.soty.dto.PageUpdateRequest;
import com.starsoftheyear.soty.repo.PageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PageService {

    private final PageRepository repo;

    // Public pages (read)
    public List<PageDto> findPublished() {
        return repo.findAll().stream()
                .filter(p -> "published".equalsIgnoreCase(p.getStatus()))
                .map(PageDto::from)
                .toList();
    }

    public PageDto getBySlug(String slug) {
        return repo.findBySlug(slug)
                .map(PageDto::from)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Page not found"));
    }

    // Admin: list all pages (draft + published)
    public List<PageDto> findAllAdmin() {
        return repo.findAll().stream()
                .map(PageDto::from)
                .toList();
    }

    // Admin: create page
    public PageDto create(PageCreateRequest req) {
        String slug = req.getSlug().trim().toLowerCase();

        if (repo.existsBySlug(slug)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Slug already exists");
        }

        Page p = new Page();
        p.setSlug(slug);
        p.setTitle(req.getTitle().trim());
        p.setBody(req.getBody());
        p.setStatus(
                (req.getStatus() == null || req.getStatus().isBlank())
                        ? "draft"
                        : req.getStatus().trim().toLowerCase()
        );

        Page saved = repo.save(p);
        return PageDto.from(saved);
    }

    // Admin: update page (optionally rename slug)
    public PageDto update(String currentSlug, PageUpdateRequest req) {
        Page page = repo.findBySlug(currentSlug)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Page not found"));

        // Optional slug change
        if (req.getNewSlug() != null && !req.getNewSlug().isBlank()) {
            String newSlug = req.getNewSlug().trim().toLowerCase();
            if (!newSlug.equals(currentSlug) && repo.existsBySlug(newSlug)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "newSlug already exists");
            }
            page.setSlug(newSlug);
        }

        page.setTitle(req.getTitle().trim());
        page.setBody(req.getBody());
        page.setStatus(
                (req.getStatus() == null || req.getStatus().isBlank())
                        ? page.getStatus()
                        : req.getStatus().trim().toLowerCase()
        );

        repo.save(page);
        return PageDto.from(page);
    }

    // Admin: delete page (hard delete for now)
    public void delete(String slug) {
        Page page = repo.findBySlug(slug)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Page not found"));
        repo.delete(page);
    }
}
