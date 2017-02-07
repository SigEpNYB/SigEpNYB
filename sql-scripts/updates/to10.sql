source create-announcements.sql;

ALTER TABLE pages MODIFY href VARCHAR(60) NOT NULL;

INSERT INTO pages (name, href) VALUES ('Create Announcement', '/Fratsite/createannouncement.html');
INSERT INTO permissions (name) VALUES ('announcements.create');
INSERT INTO role_pages (idRole, idPage) VALUES (1, 7);
INSERT INTO role_pages (idRole, idPage) VALUES (3, 7);

INSERT INTO role_permissions (idRole, idPermission) VALUES (1, 18);
INSERT INTO role_permissions (idRole, idPermission) VALUES (3, 18);

INSERT INTO version (version) VALUES (10);