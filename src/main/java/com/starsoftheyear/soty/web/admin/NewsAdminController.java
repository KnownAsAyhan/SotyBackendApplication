package com.starsoftheyear.soty.web.admin;

import com.starsoftheyear.soty.dto.NewsCreateRequest;
import com.starsoftheyear.soty.dto.NewsDto;
import com.starsoftheyear.soty.dto.NewsUpdateRequest;
import com.starsoftheyear.soty.service.NewsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/admin/news")
@RequiredArgsConstructor
public class NewsAdminController {

    private final NewsService service;

    @PostMapping
    public ResponseEntity<NewsDto> create(@Valid @RequestBody NewsCreateRequest req) {
        NewsDto created = service.create(req);
        // public read URL
        URI location = URI.create("/api/news/" + created.slug());
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{slug}")
    public ResponseEntity<NewsDto> update(
            @PathVariable String slug,
            @Valid @RequestBody NewsUpdateRequest req
    ) {
        NewsDto updated = service.update(slug, req);
        // If slug changed, expose the new public location
        URI contentLocation = URI.create("/api/news/" + updated.slug());
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
