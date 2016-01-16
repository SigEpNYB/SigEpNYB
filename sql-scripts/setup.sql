CREATE USER 'fratsite'@'localhost' IDENTIFIED BY 'jeff';
GRANT ALL PRIVILEGES ON *.* TO 'fratsite'@'localhost';

CREATE USER 'builder'@'localhost';
GRANT ALL PRIVILEGES ON *.* TO 'builder'@'localhost';

source reset-all.sql;
