CREATE TABLE role_links (
idRole INT NOT NULL,
pageName VARCHAR(30),
href VARCHAR(30),
FOREIGN KEY (idRole) REFERENCES roles(idRole)
);
