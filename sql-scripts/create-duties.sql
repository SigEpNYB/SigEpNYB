CREATE TABLE duties (
idDuty INT NOT NULL AUTO_INCREMENT,
idEvent INT NOT NULL,
idType INT NOT NULL,
idOriginal INT,
idAccount INT,
PRIMARY KEY (idDuty),
FOREIGN KEY (idEvent) REFERENCES events(idEvent),
FOREIGN KEY (idType) REFERENCES duty_types(idType),
FOREIGN KEY (idOriginal) REFERENCES accounts(idAccount),
FOREIGN KEY (idAccount) REFERENCES accounts(idAccount)
);
