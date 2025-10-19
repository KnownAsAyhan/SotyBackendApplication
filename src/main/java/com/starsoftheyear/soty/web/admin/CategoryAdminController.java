package com.starsoftheyear.soty.web.admin;

import com.starsoftheyear.soty.dto.CategoryCreateRequest;
import com.starsoftheyear.soty.dto.CategoryDto;
import com.starsoftheyear.soty.dto.CategoryUpdateRequest;
import com.starsoftheyear.soty.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/admin/categories")
@RequiredArgsConstructor
public class CategoryAdminController {

    private final CategoryService service;

    @PostMapping
    public ResponseEntity<CategoryDto> create(@Valid @RequestBody CategoryCreateRequest req) {
        CategoryDto created = service.create(req);
        // Public read URL of the created category
        URI location = URI.create("/api/categories/" + created.slug());
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{slug}")
    public ResponseEntity<CategoryDto> update(
            @PathVariable String slug,
            @Valid @RequestBody CategoryUpdateRequest req
    ) {
        CategoryDto updated = service.update(slug, req);
        // If slug changed, tell clients the new public location
        URI contentLocation = URI.create("/api/categories/" + updated.slug());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_LOCATION, contentLocation.toString())
                .body(updated);
    }

    @DeleteMapping("/{slug}")
    public ResponseEntity<Void> delete(@PathVariable String slug) {
        service.delete(slug);
        return ResponseEntity.noContent().build();
    }
}
