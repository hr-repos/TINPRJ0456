package nl.hr.scr.applicatie.database;

import nl.hr.scr.applicatie.Main;

/* Eli @ October 07, 2023 (nl.hr.scr.applicatie.database) */
public final class CreateTables
{
    public CreateTables(Main main) {
        main.sql().statement("""
            CREATE TABLE IF NOT EXISTS projects (
                id            INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
                name          VARCHAR(30)       NOT NULL,
                creation_unix BIGINT            NOT NULL DEFAULT UNIX_TIMESTAMP(),
                description   VARCHAR(1000)     NOT NULL DEFAULT '',
                creator_name  VARCHAR(60)       NOT NULL,
                frequency     SMALLINT UNSIGNED NOT NULL DEFAULT 1000,
                active        BOOLEAN           NOT NULL DEFAULT FALSE
            )
            """).update().complete();

        main.sql().statement("CREATE INDEX IF NOT EXISTS index_projects_name ON projects (name)").update().complete();

        main.sql().statement("""
            CREATE TABLE IF NOT EXISTS sensors (
                id           MEDIUMINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
                name         VARCHAR(20)      NOT NULL,
                pin          TINYINT UNSIGNED NOT NULL,
                project_id   INT UNSIGNED     NOT NULL,
                unit         VARCHAR(7)       NOT NULL DEFAULT 'μg/m³',
                calibrationA FLOAT            NOT NULL DEFAULT 0,
                calibrationB FLOAT            NOT NULL DEFAULT 1,
                calibrationC FLOAT            NOT NULL DEFAULT 0,
            
                CONSTRAINT foreign_key_sensors_project_id FOREIGN KEY (project_id) REFERENCES projects (id) ON DELETE CASCADE ON UPDATE CASCADE,
            
                CHECK (pin BETWEEN 0 AND 7),
                UNIQUE (pin, project_id)
            )
            """).update().complete();

        main.sql().statement("CREATE INDEX IF NOT EXISTS index_sensors_project_id ON sensors (project_id)").update().complete();

        main.sql().statement("""
            CREATE TABLE IF NOT EXISTS data (
                measure_millis BIGINT             NOT NULL,
                sensor_id      MEDIUMINT UNSIGNED NOT NULL,
                value          SMALLINT UNSIGNED  NOT NULL,
            
                CONSTRAINT foreign_key_data_sensor_id FOREIGN KEY (sensor_id) REFERENCES sensors (id) ON DELETE CASCADE ON UPDATE CASCADE
            )
            """).update().complete();

        main.sql().statement("CREATE INDEX IF NOT EXISTS index_data_measure_date ON data (measure_millis)").update().complete();
        main.sql().statement("CREATE INDEX IF NOT EXISTS index_data_sensor_id ON data (sensor_id)").update().complete();
    }
}
