CREATE TABLE duty_switches (
idDuty INT NOT NULL,
idAccount INT NOT NULL,
idTodo INT NOT NULL,
FOREIGN KEY (idDuty) REFERENCES duties(idDuty),
FOREIGN KEY (idAccount) REFERENCES accounts(idAccount),
FOREIGN KEY (idTodo) REFERENCES todos(idTodo)
);
