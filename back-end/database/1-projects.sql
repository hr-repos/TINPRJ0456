CREATE TABLE projects (
    id            INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    name          VARCHAR(30)   NOT NULL,
    creation_date DATETIME      NOT NULL DEFAULT NOW(),
    description   VARCHAR(1000) NOT NULL DEFAULT '',
    creator_name  VARCHAR(60)   NOT NULL,
    active        BOOLEAN       NOT NULL DEFAULT FALSE
);

CREATE INDEX index_projects_name ON projects (name);
