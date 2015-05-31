CREATE TABLE user_roles (
idAccount INT NOT NULL,
idRole INT NOT NULL,
FOREIGN KEY (idAccount) REFERENCES accounts(idAccount),
FOREIGN KEY (idRole) REFERENCES roles(idRole)
);
