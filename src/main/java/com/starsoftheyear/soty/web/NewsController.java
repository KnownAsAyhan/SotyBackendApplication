package com.starsoftheyear.soty.web;

import com.starsoftheyear.soty.dto.NewsDto;
import com.starsoftheyear.soty.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class NewsController {

    private final NewsService service;

    // Public: list all published news (newest first)
    @GetMapping("/news")
    public List<NewsDto> list() {
        return service.findPublished();
    }

    // Public: get a single published news item by slug
    @GetMapping("/news/{slug}")
    public NewsDto getBySlug(@PathVariable String slug) {
        return service.getPublishedBySlug(slug);
    }
}
