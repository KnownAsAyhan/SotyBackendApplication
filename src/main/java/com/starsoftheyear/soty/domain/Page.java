package com.starsoftheyear.soty.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "pages", indexes = {
        @Index(name = "idx_pages_slug", columnList = "slug", unique = true)
})
public class Page {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 128, nullable = false, unique = true)
    private String slug;

    @Column(length = 255, nullable = false)
    private String title;

    @Lob
    @Column(nullable = false)
    private String body;

    @Column(length = 16, nullable = false)
    private String status; // "draft" | "published"

    // getters/setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
