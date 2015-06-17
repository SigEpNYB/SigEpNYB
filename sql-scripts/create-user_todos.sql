CREATE TABLE user_todos (
idAccount INT NOT NULL,
idTodo INT NOT NULL,
FOREIGN KEY (idAccount) REFERENCES accounts(idAccount),
FOREIGN KEY (idTodo) REFERENCES todos(idTodo)
);