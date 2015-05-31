CREATE TABLE tokens (
token VARCHAR(30) NOT NULL,
idAccount INT NOT NULL,
loggedIn DATETIME NOT NULL,
lastActive DATETIME NOT NULL,
PRIMARY KEY (token),
FOREIGN KEY (idAccount) REFERENCES accounts(idAccount) 
);
