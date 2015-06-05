CREATE TABLE role_pages (
idRole INT NOT NULL,
idPage INT NOT NULL,
FOREIGN KEY (idRole) REFERENCES roles(idRole),
FOREIGN KEY (idPage) REFERENCES pages(idPage)
);