CREATE TABLE version (
version INT NOT NULL
);

INSERT INTO version (version) VALUES (8);

source create-accounts.sql;
source create-tokens.sql;
source create-permissions.sql;
source create-roles.sql;
source create-role_permissions.sql;
source create-user_roles.sql;
source create-events.sql;
source create-pages.sql;
source create-role_pages.sql;
source create-todos.sql;
source create-user_todos.sql;
source create-account_requests.sql;
source create-duty_types.sql;
source create-duties.sql;
source create-groups.sql;
source create-group_members.sql;
source create-duty_switches.sql;
source create-fines.sql;

source populate-accounts.sql;
source populate-permissions.sql;
source populate-roles.sql;
source populate-role_permissions.sql;
source populate-user_roles.sql;
source populate-pages.sql;
source populate-role_pages.sql;
source populate-duty_types.sql;
source populate-groups.sql;
source populate-group_members.sql;