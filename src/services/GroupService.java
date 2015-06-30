/**
 * 
 */
package services;

import iservice.Service;
import data.AccountData;
import data.Group;
import database.GroupsDAO;
import exceptions.InternalServerException;

/**
 * Logic behind groups
 */
public class GroupService extends Service<GroupsDAO> {

	/** Creates a GroupService */
	GroupService(GroupsDAO dao) {
		super(dao);
	}
	
	/** Adds a member to the group */
	void addMember(Group group, int idAccount) throws InternalServerException {
		run(dao -> {
			dao.add(group, idAccount);
		})
		.unwrap();
	}

	/** Gets the members of the group */
	AccountData[] getMembers(Group group) throws InternalServerException {
		return run(dao -> {
			return dao.getMembers(group);
		})
		.unwrap();
	}
	
	/** Removes the member from the group */
	void removeMember(Group group, int idAccount) throws InternalServerException {
		run(dao -> {
			dao.remove(group, idAccount);
		})
		.unwrap();
	}
}
