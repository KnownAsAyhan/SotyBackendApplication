package com.starsoftheyear.soty.dto;

import com.starsoftheyear.soty.domain.Nominee;

/**
 * Read-only representation of a Nominee.
 * Includes light category context (slug & name) for easy rendering on the client.
 */
public record NomineeDto(
        Long id,
        String slug,
        String name,
        String description,
        String status,
        Integer sortOrder,
        String imageUrl,
        String categorySlug,
        String categoryName
) {
    public static NomineeDto from(Nominee n) {
        if (n == null) return null;
        var c = n.getCategory();
        return new NomineeDto(
                n.getId(),
                n.getSlug(),
                n.getName(),
                n.getDescription(),
                n.getStatus(),
                n.getSortOrder(),
                n.getImageUrl(),
                c != null ? c.getSlug() : null,
                c != null ? c.getName() : null
        );
    }
}
