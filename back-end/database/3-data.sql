CREATE TABLE IF NOT EXISTS data (
    measure_millis BIGINT             NOT NULL,
    sensor_id      MEDIUMINT UNSIGNED NOT NULL,
    value          SMALLINT UNSIGNED  NOT NULL,

    CONSTRAINT foreign_key_data_sensor_id FOREIGN KEY (sensor_id) REFERENCES sensors (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE INDEX IF NOT EXISTS index_data_measure_date ON data (measure_millis);
CREATE INDEX IF NOT EXISTS index_data_sensor_id ON data (sensor_id);
