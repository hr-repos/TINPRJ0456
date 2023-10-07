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
                creation_date DATETIME      NOT NULL DEFAULT NOW(),
                description   VARCHAR(1000) NOT NULL DEFAULT '',
                creator_name  VARCHAR(60)   NOT NULL
            )
            """).update().complete();

        main.sql().statement("""
            CREATE TABLE IF NOT EXISTS sensors (
                id   TINYINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
                name VARCHAR(20) UNIQUE NOT NULL
            )
            """).update().complete();

        main.sql().statement("""
            CREATE TABLE IF NOT EXISTS data (
                measure_date DATETIME         NOT NULL DEFAULT NOW(),
                project_id   INT UNSIGNED     NOT NULL,
                sensor_id    TINYINT UNSIGNED NOT NULL,
                value        DECIMAL(9, 3)    NOT NULL DEFAULT 0,
            
                CONSTRAINT foreign_key_data_project_id FOREIGN KEY (project_id) REFERENCES projects (id),
                CONSTRAINT foreign_key_data_sensor_id FOREIGN KEY (sensor_id) REFERENCES sensors (id)
            )
            """).update().complete();
    }
}
