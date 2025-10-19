package com.starsoftheyear.soty.web.admin;

import com.starsoftheyear.soty.dto.PageCreateRequest;
import com.starsoftheyear.soty.dto.PageDto;
import com.starsoftheyear.soty.dto.PageUpdateRequest;
import com.starsoftheyear.soty.service.PageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/admin/pages")
@RequiredArgsConstructor
public class PageAdminController {

    private final PageService service;

    @GetMapping
    public ResponseEntity<List<PageDto>> listAll() {
        return ResponseEntity.ok(service.findAllAdmin()); // includes draft + published
    }

    @PostMapping
    public ResponseEntity<PageDto> create(@Valid @RequestBody PageCreateRequest req) {
        PageDto created = service.create(req);
        URI location = URI.create("/api/pages/" + created.slug()); // public read URL
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{slug}")
    public ResponseEntity<PageDto> update(
            @PathVariable String slug,
            @Valid @RequestBody PageUpdateRequest req
    ) {
        PageDto updated = service.update(slug, req);
        // If slug changed, tell clients the new public location
        URI contentLocation = URI.create("/api/pages/" + updated.slug());
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
