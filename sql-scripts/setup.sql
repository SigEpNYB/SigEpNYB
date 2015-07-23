CREATE USER fratsite IDENTIFIED BY 'jeff';
GRANT ALL PRIVILEGES ON *.* TO fratsite;

CREATE USER 'builder'@'localhost';
GRANT ALL PRIVILEGES ON *.* TO 'builder'@'localhost';

source reset-all.sql;
