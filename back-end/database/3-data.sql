CREATE TABLE IF NOT EXISTS data (
    measure_date BIGINT             NOT NULL DEFAULT UNIX_TIMESTAMP(),
    sensor_id    MEDIUMINT UNSIGNED NOT NULL,
    value        DECIMAL(9, 3)      NOT NULL DEFAULT 0,

    CONSTRAINT foreign_key_data_sensor_id FOREIGN KEY (sensor_id) REFERENCES sensors (id)
);

CREATE INDEX IF NOT EXISTS index_data_measure_date ON data (measure_date);
CREATE INDEX IF NOT EXISTS index_data_sensor_id ON data (sensor_id);
