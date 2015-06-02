CREATE TABLE role_permissions (
idRole INT NOT NULL,
idPermission INT NOT NULL,
FOREIGN KEY (idRole) REFERENCES roles(idRole),
FOREIGN KEY (idPermission) REFERENCES permissions(idPermission)
);
