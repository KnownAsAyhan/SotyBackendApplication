package com.starsoftheyear.soty.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Admin create payload for a Nominee.
 */
public class NomineeCreateRequest {

    @NotBlank
    @Pattern(regexp = "[a-z0-9-]+", message = "Slug must contain lowercase letters, numbers or dashes")
    @Size(max = 128)
    private String slug;

    @NotBlank
    @Size(max = 255)
    private String name;

    @Size(max = 2000)
    private String description;

    @Size(max = 2048)
    private String imageUrl;

    // optional; if blank the service will default to "draft"
    @Pattern(regexp = "(?i)draft|published", message = "status must be 'draft' or 'published'")
    private String status;

    // optional; default 0 in service if null
    private Integer sortOrder;

    @NotBlank
    @Pattern(regexp = "[a-z0-9-]+", message = "categorySlug must contain lowercase letters, numbers or dashes")
    @Size(max = 128)
    private String categorySlug;

    // ---- getters / setters ----
    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }

    public String getCategorySlug() { return categorySlug; }
    public void setCategorySlug(String categorySlug) { this.categorySlug = categorySlug; }
}
