package com.starsoftheyear.soty.web;

import com.starsoftheyear.soty.dto.NomineeDto;
import com.starsoftheyear.soty.service.NomineeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Public (read-only) endpoints for nominees.
 * - List nominees of a category (published only)
 * - Get a single nominee by slug (published only)
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class NomineeController {

    private final NomineeService service;

    @GetMapping("/categories/{categorySlug}/nominees")
    public List<NomineeDto> listByCategory(@PathVariable String categorySlug) {
        return service.findPublishedByCategory(categorySlug.trim().toLowerCase());
    }

    @GetMapping("/nominees/{slug}")
    public NomineeDto getBySlug(@PathVariable String slug) {
        return service.getPublishedBySlug(slug.trim().toLowerCase());
    }
}
