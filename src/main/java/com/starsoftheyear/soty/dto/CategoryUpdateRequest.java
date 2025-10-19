package com.starsoftheyear.soty.dto;

import jakarta.validation.constraints.*;

/**
 * Payload for updating a Category (admin).
 * All fields are OPTIONAL. If a field is null it won't be changed.
 *
 * Notes:
 * - slug: if provided, we will rename the category's slug (must be lowercase letters/numbers/dashes).
 * - name: if provided, non-empty.
 * - description: optional free text.
 * - sortOrder: if provided, must be >= 0.
 * - status: if provided, must be 'draft' or 'published' (case-insensitive).
 */
public class CategoryUpdateRequest {

    // Optional new slug (rename). If present, must be valid and non-empty.
    @Size(min = 1, max = 128)
    @Pattern(regexp = "^[a-z0-9-]+$", message = "Slug must contain lowercase letters, numbers or dashes")
    private String slug;

    // Optional new name. If present, must be non-empty.
    @Size(min = 1, max = 255)
    private String name;

    // Optional new description
    @Size(max = 4000)
    private String description;

    // Optional new sort order
    @Min(0)
    private Integer sortOrder;

    // Optional new status: draft or published
    @Pattern(regexp = "(?i)draft|published", message = "Status must be 'draft' or 'published'")
    private String status;

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
