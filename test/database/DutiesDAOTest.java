package database;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.SQLException;

import org.junit.runner.RunWith;

import com.treetest.junit.DataFile;
import com.treetest.junit.ParameterVariables;
import com.treetest.junit.ReturnVariable;
import com.treetest.junit.TreeTestRunner;

import data.Duty;
import data.DutyType;

@RunWith(TreeTestRunner.class)
public class DutiesDAOTest {
	DutiesDAO dao;
	
	@ParameterVariables("database")
	public DutiesDAOTest(IDatabase database) {
		dao = database.getDutiesDAO();
	}
	
	@DataFile("testdata/duties.txt")
	@ReturnVariable("duty")
	public Duty start(int idEvent, DutyType type) throws SQLException {
		int numDuties = dao.getForEvent(idEvent).length;
		dao.create(idEvent, type);
		Duty[] duties = dao.getForEvent(idEvent);
		assertEquals(numDuties + 1, duties.length);
		
		Duty duty = duties[duties.length - 1];
		assertTrue(duty.getId() > 0);
		assertEquals(idEvent, duty.getIdEvent());
		assertEquals(type, duty.getType());
		assertEquals(0, duty.getIdAccount());
		
		return duty;
	}
	
	@DataFile("testdata/dutyassignments.txt")
	public void assign(Duty duty, int idAccount) throws SQLException {
		Duty[] unassigned = dao.getUnassigned();
		boolean found = false;
		for (Duty d : unassigned) {
			if (d.getId() == duty.getId()) {
				found = true;
				break;
			}
		}
		if (!found) fail();
		
		int numUnassigned = unassigned.length;
		dao.assign(duty.getId(), idAccount);
		unassigned = dao.getUnassigned();
		assertEquals(numUnassigned - 1, unassigned.length);
		
		for (Duty d : unassigned) {
			if (d.getId() == duty.getId()) fail();
		}
		
		for (Duty d : dao.getForEvent(duty.getIdEvent())) {
			if (d.getId() == duty.getId()) {
				assertEquals(idAccount, d.getIdAccount());
				return;
			}
		}
		
		fail();
	}
	
	@DataFile("testdata/reassignments.txt")
	public void reAssign(Duty duty, int idAccount) throws SQLException {
		dao.assign(duty.getId(), idAccount);
		
		for (Duty d : dao.getForEvent(duty.getIdEvent())) {
			if (d.getId() == duty.getId()) {
				assertEquals(idAccount, d.getIdAccount());
				return;
			}
		}
		
		fail();
	}
	
	@DataFile("testdata/dutycounts.txt")
	public void getCount(int idAccount, DutyType type, int count) throws SQLException {
		int actualCount = dao.getCount(idAccount, type);
		assertEquals(count, actualCount);
	}
	
	@ParameterVariables("duty")
	public void end(Duty duty) throws SQLException {
		int numDuties = dao.getForEvent(duty.getIdEvent()).length;
		dao.delete(duty.getId());
		Duty[] duties = dao.getForEvent(duty.getIdEvent());
		assertEquals(numDuties - 1, duties.length);
		
		for (Duty d : duties) {
			if (d.getId() == duty.getId()) fail();
		}
	}

}
