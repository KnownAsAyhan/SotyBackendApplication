package com.starsoftheyear.soty.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Admin: update a news post. All fields optional.
 */
public record NewsUpdateRequest(
        @Pattern(regexp = "^[a-z0-9-]+$", message = "slug must contain lowercase letters, numbers or dashes")
        @Size(max = 128)
        String slug,

        @Size(max = 255)
        String title,

        @Size(max = 500)
        String excerpt,

        String body,

        @Size(max = 255)
        String imageUrl,

        @Pattern(regexp = "^(draft|published)$", message = "status must be 'draft' or 'published'")
        String status
) {}
