package database;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.sql.SQLException;

import org.junit.runner.RunWith;

import com.treetest.junit.DataFile;
import com.treetest.junit.ParameterVariables;
import com.treetest.junit.TreeTestRunner;

import data.AccountData;
import data.Group;

@RunWith(TreeTestRunner.class)
public class GroupDAOTest {
	private final GroupsDAO dao;

	@ParameterVariables("database")
	public GroupDAOTest(IDatabase database) {
		dao = database.getGroupsDAO();
	}
	
	@DataFile("testdata/groupmembers.txt")
	public void start(Group group, int idAccount) throws SQLException {
		AccountData[] members = dao.getMembers(group);
		for (AccountData account : members) {
			if (account.getId() == idAccount) fail();
		}
		
		int numMembers = members.length;
		dao.add(group, idAccount);
		members = dao.getMembers(group);
		assertEquals(numMembers + 1, members.length);
		
		for (AccountData account : members) {
			if (account.getId() == idAccount) return;
		}
		
		fail();
	}
	
	@DataFile("testdata/groupmembers.txt")
	public void end(Group group, int idAccount) throws SQLException {
		AccountData[] members = dao.getMembers(group);
		boolean found = false;
		for (AccountData account : members) {
			if (account.getId() == idAccount) {
				found = true;
				break;
			}
		}
		if (!found) fail();
		
		int numMembers = members.length;
		dao.remove(group, idAccount);
		members = dao.getMembers(group);
		assertEquals(numMembers - 1, members.length);
		
		for (AccountData account : members) {
			if (account.getId() == idAccount) fail();
		}
	}

}
