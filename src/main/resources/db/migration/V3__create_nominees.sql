-- V3__create_nominees.sql
-- Creates nominees table and seeds a few examples.

CREATE TABLE IF NOT EXISTS nominees (
                                        id          BIGINT PRIMARY KEY AUTO_INCREMENT,
                                        slug        VARCHAR(128) NOT NULL,
    name        VARCHAR(255) NOT NULL,
    summary     TEXT NULL,
    photo_url   VARCHAR(500) NULL,
    sort_order  INT NOT NULL DEFAULT 0,
    status      VARCHAR(10) NOT NULL DEFAULT 'published',
    category_id BIGINT NOT NULL,
    created_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT uq_nominees_slug UNIQUE (slug),
    CONSTRAINT fk_nominees_category
    FOREIGN KEY (category_id) REFERENCES categories(id)
    );

-- Helpful indexes
CREATE INDEX idx_nominees_category_sort
    ON nominees (category_id, sort_order);

CREATE INDEX idx_nominees_status
    ON nominees (status);

-- --- Seed data (only if the slug doesn't exist yet) ---
-- These assume you still have 'film' and 'music' categories from V2.

-- Film nominees
INSERT INTO nominees (slug, name, summary, photo_url, sort_order, status, category_id)
SELECT 'inception', 'Inception', 'Sci-fi heist thriller by Christopher Nolan', NULL, 1, 'published',
       (SELECT id FROM categories WHERE slug = 'film')
    WHERE NOT EXISTS (SELECT 1 FROM nominees WHERE slug = 'inception');

INSERT INTO nominees (slug, name, summary, photo_url, sort_order, status, category_id)
SELECT 'the-dark-knight', 'The Dark Knight', 'Superhero crime drama', NULL, 2, 'published',
       (SELECT id FROM categories WHERE slug = 'film')
    WHERE NOT EXISTS (SELECT 1 FROM nominees WHERE slug = 'the-dark-knight');

-- Music nominees
INSERT INTO nominees (slug, name, summary, photo_url, sort_order, status, category_id)
SELECT 'daft-punk', 'Daft Punk', 'Electronic music duo', NULL, 1, 'published',
       (SELECT id FROM categories WHERE slug = 'music')
    WHERE NOT EXISTS (SELECT 1 FROM nominees WHERE slug = 'daft-punk');

INSERT INTO nominees (slug, name, summary, photo_url, sort_order, status, category_id)
SELECT 'adele', 'Adele', 'Singer-songwriter', NULL, 2, 'published',
       (SELECT id FROM categories WHERE slug = 'music')
    WHERE NOT EXISTS (SELECT 1 FROM nominees WHERE slug = 'adele');
