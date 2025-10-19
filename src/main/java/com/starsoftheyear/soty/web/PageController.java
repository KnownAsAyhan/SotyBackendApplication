package com.starsoftheyear.soty.web;

import com.starsoftheyear.soty.dto.PageDto;
import com.starsoftheyear.soty.service.PageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pages")
@RequiredArgsConstructor
public class PageController {
    private final PageService service;

    @GetMapping
    public List<PageDto> list() {
        return service.findPublished();
    }

    @GetMapping("/{slug}")
    public PageDto get(@PathVariable String slug) {
        return service.getBySlug(slug);
    }
}
