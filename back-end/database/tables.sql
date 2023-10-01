USE scr;

CREATE TABLE projects (
	project_id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
	project_name VARCHAR(255),
    project_creation_datetime DATETIME,
    project_description LONGTEXT,
    project_creater_name VARCHAR(255)

);

CREATE TABLE data (
	id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    project_ID INT,
    CONSTRAINT fk_Gameserie_id
    FOREIGN KEY (project_ID) REFERENCES projects(project_id),

    sensor1 FLOAT,
    sensor2 FLOAT,
    sensor3 FLOAT,
    sensor4 FLOAT,
    sensor5 FLOAT,
    sensor6 FLOAT,
    sensor7 FLOAT,
    sensor8 FLOAT,
    sensor9 FLOAT,
    sensor10 FLOAT

);