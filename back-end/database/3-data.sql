CREATE TABLE IF NOT EXISTS data (
    measure_millis BIGINT             NOT NULL DEFAULT ROUND(UNIX_TIMESTAMP(CURTIME(4)) * 1000),
    sensor_id      MEDIUMINT UNSIGNED NOT NULL,
    value          DECIMAL(9, 3)      NOT NULL DEFAULT 0,

    CONSTRAINT foreign_key_data_sensor_id FOREIGN KEY (sensor_id) REFERENCES sensors (id)
);

CREATE INDEX IF NOT EXISTS index_data_measure_date ON data (measure_millis);
CREATE INDEX IF NOT EXISTS index_data_sensor_id ON data (sensor_id);
