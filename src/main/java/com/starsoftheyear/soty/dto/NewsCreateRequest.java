package com.starsoftheyear.soty.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Admin: create a news post.
 */
public record NewsCreateRequest(
        @NotBlank
        @Pattern(regexp = "^[a-z0-9-]+$", message = "slug must contain lowercase letters, numbers or dashes")
        @Size(max = 128)
        String slug,

        @NotBlank
        @Size(max = 255)
        String title,

        @Size(max = 500)
        String excerpt,

        String body,

        @Size(max = 255)
        String imageUrl,

        // optional; defaults to draft if null/blank; allowed: draft|published
        @Pattern(regexp = "^(draft|published)$", message = "status must be 'draft' or 'published'")
        String status
) {}
