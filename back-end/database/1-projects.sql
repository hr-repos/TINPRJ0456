CREATE TABLE IF NOT EXISTS projects (
    id            INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    name          VARCHAR(30)       NOT NULL,
    creation_date BIGINT            NOT NULL DEFAULT UNIX_TIMESTAMP(),
    description   VARCHAR(1000)     NOT NULL DEFAULT '',
    creator_name  VARCHAR(60)       NOT NULL,
    frequency     SMALLINT UNSIGNED NOT NULL DEFAULT 1000,
    active        BOOLEAN           NOT NULL DEFAULT FALSE
);

CREATE INDEX IF NOT EXISTS index_projects_name ON projects (name);
