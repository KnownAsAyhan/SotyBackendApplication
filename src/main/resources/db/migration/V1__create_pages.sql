CREATE TABLE IF NOT EXISTS pages (
                                     id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                     slug VARCHAR(128) NOT NULL UNIQUE,
    title VARCHAR(255) NOT NULL,
    body MEDIUMTEXT NOT NULL,
    status VARCHAR(16) NOT NULL DEFAULT 'draft', -- draft | published
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
    );

-- seed one example page
INSERT INTO pages (slug, title, body, status)
VALUES ('about', 'About', 'Welcome to Stars of the Year.', 'published')
    ON DUPLICATE KEY UPDATE title = VALUES(title);
