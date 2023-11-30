CREATE TABLE data (
    measure_date DATETIME         NOT NULL DEFAULT NOW(),
    project_id   INT UNSIGNED     NOT NULL,
    sensor_id    TINYINT UNSIGNED NOT NULL,
    value        DECIMAL(9, 3)    NOT NULL DEFAULT 0,

    CONSTRAINT foreign_key_data_project_id FOREIGN KEY (project_id) REFERENCES projects (id),
    CONSTRAINT foreign_key_data_sensor_id FOREIGN KEY (sensor_id) REFERENCES sensors (id)
);

CREATE INDEX index_data_measure_date ON data (measure_date);
CREATE INDEX index_data_project_id ON data (project_id);
CREATE INDEX index_data_sensor_id ON data (sensor_id);
