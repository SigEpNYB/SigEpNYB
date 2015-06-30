CREATE TABLE group_members (
idGroup INT NOT NULL,
idAccount INT NOT NULL,
FOREIGN KEY (idGroup) REFERENCES groups(idGroup),
FOREIGN KEY (idAccount) REFERENCES accounts(idAccount)
);