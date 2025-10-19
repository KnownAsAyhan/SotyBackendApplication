package com.starsoftheyear.soty.web;

import com.starsoftheyear.soty.dto.CategoryDto;
import com.starsoftheyear.soty.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService service;

    /**
     * Public: list published categories (ordered by sort_order).
     */
    @GetMapping
    public List<CategoryDto> listPublished() {
        return service.findPublished();
    }

    /**
     * Public: get a single published category by slug.
     * If the slug exists but the category is not published, respond 404.
     */
    @GetMapping("/{slug}")
    public CategoryDto getBySlug(@PathVariable String slug) {
        CategoryDto dto = service.getBySlug(slug);
        if (!"published".equalsIgnoreCase(dto.status())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found");
        }
        return dto;
    }
}
