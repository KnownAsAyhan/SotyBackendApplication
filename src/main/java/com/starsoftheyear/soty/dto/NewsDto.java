package com.starsoftheyear.soty.dto;

import com.starsoftheyear.soty.domain.News;

import java.time.LocalDateTime;

/**
 * Read-only representation of a News post.
 */
public record NewsDto(
        Long id,
        String slug,
        String title,
        String excerpt,
        String body,
        String imageUrl,
        String status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime publishedAt
) {
    public static NewsDto from(News n) {
        if (n == null) return null;
        return new NewsDto(
                n.getId(),
                n.getSlug(),
                n.getTitle(),
                n.getExcerpt(),
                n.getBody(),
                n.getImageUrl(),
                n.getStatus(),
                n.getCreatedAt(),
                n.getUpdatedAt(),
                n.getPublishedAt()
        );
    }
}
