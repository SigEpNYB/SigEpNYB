CREATE TABLE fines (
idFine INT NOT NULL AUTO_INCREMENT,
idAccount INT,
amount DECIMAL(6,2),
reason VARCHAR(50),
PRIMARY KEY (idFine),
FOREIGN KEY (idAccount) REFERENCES accounts(idAccount)
);
