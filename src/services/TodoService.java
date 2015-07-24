/**
 * 
 */
package services;

import iservice.Service;

import java.util.Date;

import data.AccountData;
import data.Todo;
import database.TodoDAO;
import exceptions.InternalServerException;

/**
 * Logic behind todos
 */
public class TodoService extends Service<TodoDAO> {

	/** Creates a TodoService */
	TodoService(TodoDAO dao) {
		super(dao);
	}

	/** Creates a todo and assigns it to a specific user */
	int create(String description, int idAccount) throws InternalServerException {
		return run(dao -> {
			int idTodo = dao.create(description);
			dao.assign(idAccount, idTodo);
			return idTodo;
		})
		.unwrap();
	}
	
	/** Creates a todo and assigns it to a specific user */
	int create(String description, Date dueDate, int idAccount) throws InternalServerException {
		return run(dao -> {
			int idTodo = dao.create(description, dueDate);
			if (idTodo == TodoDAO.CREATE_FAILED) throw new InternalServerException();
			dao.assign(idAccount, idTodo);
			return idTodo;
		})
		.unwrap();
	}

	/** Creates a todo and assigns it to a specific user */
	int create(String description, AccountData[] accounts) throws InternalServerException {
		return run(dao -> {
			int idTodo = dao.create(description);
			for (AccountData account : accounts) {
				dao.assign(account.getId(), idTodo);
			}
			return idTodo;
		})
		.unwrap();
	}
	
	/** Creates a todo and assigns it to a specific user */
	int create(String description, Date dueDate, AccountData[] accounts) throws InternalServerException {
		return run(dao -> {
			int idTodo = dao.create(description, dueDate);
			for (AccountData account : accounts) {
				dao.assign(account.getId(), idTodo);
			}
			return idTodo;
		})
		.unwrap();
	}
	
	/** Gets the todos for the given user */
	public Todo[] get(int idAccount) throws InternalServerException {
		return run(dao -> {
			return dao.get(idAccount);
		})
		.unwrap();
	}
	
	/** Makes the gien todo as done */
	void done(int idTodo) throws InternalServerException {
		run(dao -> {
			dao.delete(idTodo);
		})
		.unwrap();
	}
}
