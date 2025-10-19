package com.starsoftheyear.soty.service;

import com.starsoftheyear.soty.dto.PageDto;
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
}
