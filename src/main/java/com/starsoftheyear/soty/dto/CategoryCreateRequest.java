package com.starsoftheyear.soty.dto;

import jakarta.validation.constraints.*;

/**
 * Payload for creating a Category (admin).
 * - slug: required, lowercase letters/numbers/dashes only
 * - name: required
 * - description: optional
 * - sortOrder: optional (>= 0). If null, service will default to 0.
 * - status: optional, allowed values: draft | published (case-insensitive)
 */
public class CategoryCreateRequest {

    @NotBlank
    @Pattern(regexp = "^[a-z0-9-]+$", message = "Slug must contain lowercase letters, numbers or dashes")
    @Size(max = 128)
    private String slug;

    @NotBlank
    @Size(max = 255)
    private String name;

    @Size(max = 4000)
    private String description;

    @Min(0)
    private Integer sortOrder; // null -> service defaults to 0

    @Pattern(regexp = "(?i)draft|published", message = "Status must be 'draft' or 'published'")
    private String status;     // null/blank -> service defaults to 'draft'

    // ---- getters & setters ----
    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
