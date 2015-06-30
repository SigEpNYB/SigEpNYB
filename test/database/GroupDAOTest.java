package database;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

import util.Settings;
import data.Group;

public class GroupDAOTest {
	Database db;
	GroupsDAO dao;

	@Before
	public void setUp() throws Exception {
		Settings settings = Settings.getInstance();
		db = new Database(settings.getDatabaseUser(), settings.getDatabasePassword(), settings.getDatabase());
		dao = db.getGroupsDAO();
	}

	@Test
	public void test() throws SQLException {
		int length = dao.getMembers(Group.ACCOUNT_REQUEST_REVIEWERS).length;
		dao.add(Group.ACCOUNT_REQUEST_REVIEWERS, 2);
		dao.add(Group.ACCOUNT_REQUEST_REVIEWERS, 3);
		assertEquals(dao.getMembers(Group.ACCOUNT_REQUEST_REVIEWERS).length, length + 2);
		dao.remove(Group.ACCOUNT_REQUEST_REVIEWERS, 3);
		assertEquals(dao.getMembers(Group.ACCOUNT_REQUEST_REVIEWERS).length, length + 1);
		dao.remove(Group.ACCOUNT_REQUEST_REVIEWERS, 2);
		assertEquals(dao.getMembers(Group.ACCOUNT_REQUEST_REVIEWERS).length, length);
	}

}
