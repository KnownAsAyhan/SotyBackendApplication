package com.starsoftheyear.soty.dto;

import com.starsoftheyear.soty.domain.Category;

/**
 * Public/Admin view of a category.
 * Kept separate from the JPA entity to control exactly what we expose.
 */
public record CategoryDto(
        Long id,
        String slug,
        String name,
        String description,
        Integer sortOrder,
        String status
) {
    public static CategoryDto from(Category c) {
        return new CategoryDto(
                c.getId(),
                c.getSlug(),
                c.getName(),
                c.getDescription(),
                c.getSortOrder(),
                c.getStatus()
        );
    }
}
