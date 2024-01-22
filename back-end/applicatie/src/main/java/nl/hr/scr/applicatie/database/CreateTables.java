package nl.hr.scr.applicatie.database;

import nl.hr.scr.applicatie.Main;

/* Eli @ October 07, 2023 (nl.hr.scr.applicatie.database) */
public final class CreateTables
{
    public CreateTables(Main main) {
        main.sql().statement("""
            CREATE TABLE IF NOT EXISTS projects (
                id            INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
                name          VARCHAR(30)   NOT NULL,
                creation_date BIGINT        NOT NULL DEFAULT UNIX_TIMESTAMP(),
                description   VARCHAR(1000) NOT NULL DEFAULT '',
                creator_name  VARCHAR(60)   NOT NULL,
                active        BOOLEAN       NOT NULL DEFAULT FALSE
            )
            """).update().complete();

        main.sql().statement("CREATE INDEX IF NOT EXISTS index_projects_name ON projects (name)").update().complete();

        main.sql().statement("""
            CREATE TABLE IF NOT EXISTS sensors (
                id           MEDIUMINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
                name         VARCHAR(20) UNIQUE      NOT NULL,
                pin          TINYINT UNSIGNED UNIQUE NOT NULL,
                project_id   INT UNSIGNED            NOT NULL,
                calibrationA FLOAT DEFAULT NULL,
                calibrationB FLOAT DEFAULT NULL,
                calibrationC FLOAT DEFAULT NULL,
            
                CONSTRAINT foreign_key_sensors_project_id FOREIGN KEY (project_id) REFERENCES projects (id)
            )
            """).update().complete();

        main.sql().statement("CREATE INDEX IF NOT EXISTS index_sensors_project_id ON sensors (project_id)").update().complete();

        main.sql().statement("""
            CREATE TABLE IF NOT EXISTS data (
                measure_date BIGINT             NOT NULL DEFAULT UNIX_TIMESTAMP(),
                sensor_id    MEDIUMINT UNSIGNED NOT NULL,
                value        DECIMAL(9, 3)      NOT NULL DEFAULT 0,
            
                CONSTRAINT foreign_key_data_sensor_id FOREIGN KEY (sensor_id) REFERENCES sensors (id)
            )
            """).update().complete();

        main.sql().statement("CREATE INDEX IF NOT EXISTS index_data_measure_date ON data (measure_date)").update().complete();
        main.sql().statement("CREATE INDEX IF NOT EXISTS index_data_sensor_id ON data (sensor_id)").update().complete();
    }
}
