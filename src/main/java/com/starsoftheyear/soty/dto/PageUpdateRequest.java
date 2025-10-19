package com.starsoftheyear.soty.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Payload for updating an existing Page.
 * - Target page is addressed by current slug in the URL: /api/admin/pages/{slug}
 * - You may optionally rename the slug via newSlug (must be unique & url-safe).
 * - If status is omitted/null, the service keeps the current status.
 */
public class PageUpdateRequest {

    /**
     * Optional slug rename.
     * Must be lowercase letters, digits, or dashes; max length 128.
     * If null/blank, slug stays as-is.
     */
    @Size(max = 128, message = "newSlug must be at most 128 characters")
    @Pattern(regexp = "^[a-z0-9-]+$", message = "newSlug must contain only lowercase letters, numbers, or dashes")
    private String newSlug;

    /** Required page title (max 255). */
    @NotBlank(message = "title is required")
    @Size(max = 255, message = "title must be at most 255 characters")
    private String title;

    /** Required page body (can be long HTML/markdown). */
    @NotBlank(message = "body is required")
    private String body;

    /**
     * Optional status:
     * - "draft" or "published"
     * If null/blank, the service preserves the current status.
     */
    @Pattern(regexp = "^(draft|published)$", message = "status must be 'draft' or 'published'")
    private String status;

    // Getters / Setters
    public String getNewSlug() { return newSlug; }
    public void setNewSlug(String newSlug) { this.newSlug = newSlug; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
