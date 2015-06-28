DROP TABLE IF EXISTS duties;
DROP TABLE IF EXISTS duty_types;
DROP TABLE IF EXISTS account_requests;
DROP TABLE IF EXISTS user_todos;
DROP TABLE IF EXISTS todos;
DROP TABLE IF EXISTS role_pages;
DROP TABLE IF EXISTS pages;
DROP TABLE IF EXISTS events;
DROP TABLE IF EXISTS user_roles;
DROP TABLE IF EXISTS role_permissions;
DROP TABLE IF EXISTS roles;
DROP TABLE IF EXISTS permissions;
DROP TABLE IF EXISTS tokens;
DROP TABLE IF EXISTS accounts;

source create-accounts.sql;
source create-tokens.sql;
source create-permissions.sql;
source create-roles.sql;
source create-role_permissions.sql;
source create-user_roles.sql;
source create-events.sql;
source create-pages.sql;
source create-role_pages.sql
source create-todos.sql;
source create-user_todos.sql;
source create-account_requests.sql;
source create-duty_types.sql;
source create-duties.sql;