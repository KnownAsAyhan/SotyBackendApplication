package com.starsoftheyear.soty.web.admin;

import com.starsoftheyear.soty.dto.NomineeCreateRequest;
import com.starsoftheyear.soty.dto.NomineeDto;
import com.starsoftheyear.soty.dto.NomineeUpdateRequest;
import com.starsoftheyear.soty.service.NomineeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/admin/nominees")
@RequiredArgsConstructor
public class NomineeAdminController {

    private final NomineeService service;

    @PostMapping
    public ResponseEntity<NomineeDto> create(@Valid @RequestBody NomineeCreateRequest req) {
        NomineeDto created = service.create(req);

        // Public read URL for this nominee
        URI location = URI.create("/api/nominees/" + created.slug());
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{slug}")
    public ResponseEntity<NomineeDto> update(
            @PathVariable String slug,
            @Valid @RequestBody NomineeUpdateRequest req
    ) {
        NomineeDto updated = service.update(slug, req);

        // If slug changed, tell clients where to read it publicly
        String publicUrl = "/api/nominees/" + updated.slug();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_LOCATION, publicUrl)
                .body(updated);
    }

    @DeleteMapping("/{slug}")
    public ResponseEntity<Void> delete(@PathVariable String slug) {
        service.delete(slug);
        return ResponseEntity.noContent().build();
    }
}
