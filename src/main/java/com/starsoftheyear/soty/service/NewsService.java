package com.starsoftheyear.soty.service;

import com.starsoftheyear.soty.domain.News;
import com.starsoftheyear.soty.dto.NewsCreateRequest;
import com.starsoftheyear.soty.dto.NewsDto;
import com.starsoftheyear.soty.dto.NewsUpdateRequest;
import com.starsoftheyear.soty.repo.NewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsService {

    private final NewsRepository repo;

    // ---------- Admin (write) ----------

    public NewsDto create(NewsCreateRequest req) {
        String slug = norm(req.slug());
        if (repo.existsBySlug(slug)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Slug already exists");
        }

        News n = new News();
        n.setSlug(slug);
        n.setTitle(norm(req.title()));
        n.setExcerpt(norm(req.excerpt()));
        n.setBody(req.body());                 // body can be large, keep as-is
        n.setImageUrl(norm(req.imageUrl()));
        n.setStatus(hasText(req.status()) ? req.status() : "draft");

        if ("published".equals(n.getStatus()) && n.getPublishedAt() == null) {
            n.setPublishedAt(LocalDateTime.now());
        }

        return NewsDto.from(repo.save(n));
    }

    public NewsDto update(String slug, NewsUpdateRequest req) {
        News n = repo.findBySlug(norm(slug))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "News not found"));

        // optional slug change
        if (hasText(req.slug())) {
            String newSlug = norm(req.slug());
            if (!newSlug.equals(n.getSlug()) && repo.existsBySlug(newSlug)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Slug already exists");
            }
            n.setSlug(newSlug);
        }

        if (hasText(req.title()))     n.setTitle(norm(req.title()));
        if (req.excerpt() != null)    n.setExcerpt(norm(req.excerpt()));
        if (req.body() != null)       n.setBody(req.body());
        if (req.imageUrl() != null)   n.setImageUrl(norm(req.imageUrl()));
        if (hasText(req.status())) {
            String newStatus = req.status();
            if ("published".equals(newStatus) && n.getPublishedAt() == null) {
                n.setPublishedAt(LocalDateTime.now());
            }
            n.setStatus(newStatus);
        }

        return NewsDto.from(repo.save(n));
    }

    public void delete(String slug) {
        News n = repo.findBySlug(norm(slug))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "News not found"));
        repo.delete(n);
    }

    // ---------- Public (read) ----------

    @Transactional(readOnly = true)
    public List<NewsDto> findPublished() {
        return repo.findAllByStatusOrderByCreatedAtDesc("published")
                .stream().map(NewsDto::from).toList();
    }

    @Transactional(readOnly = true)
    public NewsDto getPublishedBySlug(String slug) {
        return repo.findBySlugAndStatus(norm(slug), "published")
                .map(NewsDto::from)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "News not found"));
    }

    // ---------- utils ----------

    private static String norm(String s) { return s == null ? null : s.trim(); }
    private static boolean hasText(String s) { return s != null && !s.isBlank(); }
}
