CREATE TABLE sensors (
    id           TINYINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    name         VARCHAR(20) UNIQUE      NOT NULL,
    pin          TINYINT UNSIGNED UNIQUE NOT NULL,
    calibrationA FLOAT DEFAULT NULL,
    calibrationB FLOAT DEFAULT NULL,
    calibrationC FLOAT DEFAULT NULL
);

-- A * pow(x, 2)  +  B * x  +  C
-- x = raw value from sensor
