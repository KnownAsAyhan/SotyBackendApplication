CREATE TABLE IF NOT EXISTS categories (
                                          id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                          slug VARCHAR(128) NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT NULL,
    sort_order INT NOT NULL DEFAULT 0,
    status VARCHAR(16) NOT NULL DEFAULT 'published',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT uq_categories_slug UNIQUE (slug)
    );

CREATE INDEX idx_categories_status_order ON categories (status, sort_order);

INSERT INTO categories (slug, name, description, sort_order, status)
SELECT 'film', 'Film', 'Film-related entries', 1, 'published'
    WHERE NOT EXISTS (SELECT 1 FROM categories WHERE slug = 'film');

INSERT INTO categories (slug, name, description, sort_order, status)
SELECT 'music', 'Music', 'Music-related entries', 2, 'published'
    WHERE NOT EXISTS (SELECT 1 FROM categories WHERE slug = 'music');
