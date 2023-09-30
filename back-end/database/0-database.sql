CREATE DATABASE scr
    CHARACTER SET = 'utf8mb4'
    COLLATE = 'utf8mb4_general_ci';

CREATE USER 'scr'@'localhost' IDENTIFIED BY '9qzBztVPQFRkGVYPyml8k6BKLB8iS10x';
GRANT ALL ON scr.* TO 'scr'@'localhost';
FLUSH PRIVILEGES;
