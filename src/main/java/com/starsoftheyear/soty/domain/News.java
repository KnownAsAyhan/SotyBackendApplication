package com.starsoftheyear.soty.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "news")
@Getter
@Setter
@NoArgsConstructor
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 128, nullable = false, unique = true)
    private String slug;

    @Column(length = 255, nullable = false)
    private String title;

    @Column(length = 500)
    private String excerpt;

    @Lob
    @Column(columnDefinition = "MEDIUMTEXT")
    private String body;

    @Column(name = "image_url", length = 255)
    private String imageUrl;

    @Column(length = 16, nullable = false)
    private String status = "draft"; // draft | published

    @Column(name = "created_at", updatable = false, insertable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    @Column(name = "published_at")
    private LocalDateTime publishedAt;
}
