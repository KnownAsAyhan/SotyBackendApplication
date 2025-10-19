package com.starsoftheyear.soty.dto;

import com.starsoftheyear.soty.domain.Page;

public record PageDto(Long id, String slug, String title, String body, String status) {
    public static PageDto from(Page p) {
        return new PageDto(p.getId(), p.getSlug(), p.getTitle(), p.getBody(), p.getStatus());
    }
}
