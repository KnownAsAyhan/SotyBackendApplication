-- V6: create news
-- Basic news posts with publish workflow

CREATE TABLE IF NOT EXISTS news (
                                    id            BIGINT PRIMARY KEY AUTO_INCREMENT,
                                    slug          VARCHAR(128)  NOT NULL,
    title         VARCHAR(255)  NOT NULL,
    excerpt       VARCHAR(500)  NULL,
    body          MEDIUMTEXT    NULL,
    image_url     VARCHAR(255)  NULL,
    status        VARCHAR(16)   NOT NULL DEFAULT 'draft',  -- draft|published
    created_at    TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    published_at  TIMESTAMP     NULL,
    CONSTRAINT uq_news_slug UNIQUE (slug)
    );

-- Helpful index to fetch published posts by recency
CREATE INDEX idx_news_status_created
    ON news (status, created_at);
