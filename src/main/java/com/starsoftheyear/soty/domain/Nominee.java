package com.starsoftheyear.soty.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(
        name = "nominees",
        uniqueConstraints = @UniqueConstraint(name = "uq_nominees_slug", columnNames = "slug"),
        indexes = {
                @Index(name = "idx_nominees_status_order", columnList = "status, sort_order"),
                @Index(name = "idx_nominees_category", columnList = "category_id")
        }
)
@Getter @Setter
public class Nominee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 128)
    private String slug;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;         // <-- matches n.getDescription()

    @Column(name = "image_url", length = 1024)
    private String imageUrl;            // <-- matches n.getImageUrl()

    @Column(nullable = false, length = 32)
    private String status = "published";

    @Column(name = "sort_order")
    private Integer sortOrder = 0;      // <-- matches n.getSortOrder()

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_nominees_category"))
    private Category category;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;
}
