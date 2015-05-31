CREATE TABLE tokens (
token VARCHAR(30) NOT NULL,
idAccount INT NOT NULL,
loggedIn DATETIME NOT NULL,
lastActive DATETIME NOT NULL,
UNIQUE (token),
UNIQUE (idAccount),
FOREIGN KEY (idAccount) REFERENCES accounts(idAccount) 
);
