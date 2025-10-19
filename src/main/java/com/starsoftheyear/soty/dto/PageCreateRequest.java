package com.starsoftheyear.soty.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class PageCreateRequest {

    @NotBlank
    @Pattern(regexp = "^[a-z0-9-]+$", message = "slug must contain only lowercase letters, numbers, or dashes")
    @Size(max = 128)
    private String slug;

    @NotBlank
    @Size(max = 255)
    private String title;

    @NotBlank
    private String body;

    // optional (we'll default to 'draft' if null)
    @Pattern(regexp = "^(draft|published)$", message = "status must be 'draft' or 'published'")
    private String status;

    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
