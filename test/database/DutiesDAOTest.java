package database;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import util.Settings;

import com.treetest.junit.DataFile;
import com.treetest.junit.ParameterVariables;
import com.treetest.junit.ReturnVariable;
import com.treetest.junit.TreeTestRunner;

import data.Duty;
import data.DutyType;
import data.Event;

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
	
	
	
	
	
	
	
	Database db;
	EventsDAO eventsDAO;
	Event event;

	@Before
	public void setUp() throws Exception {
		Settings settings = Settings.getInstance();
		db = new Database(settings.getDatabaseUser(), settings.getDatabasePassword(), settings.getDatabase());
		dao = db.getDutiesDAO();

		eventsDAO = db.getEventsDAO();
		Date now = Calendar.getInstance().getTime();
		now.setTime((long) (now.getTime() + (Math.random() * 60000)));
		eventsDAO.create("test", now, now, "bla bla");
		event = eventsDAO.get(now, now)[0];
	}
	
	@After
	public void tearDown() throws Exception {
		eventsDAO.cancel(event.getId());
		
		db.close();
	}

	@Test
	public void testCreate() throws SQLException {
		dao.create(event.getId(), DutyType.DRIVER);
		Duty[] duties = dao.getForEvent(event.getId());
		
		assertEquals(duties.length, 1);
		assertTrue(duties[0].getId() >= 1);
		assertEquals(duties[0].getIdEvent(), event.getId());
		assertEquals(duties[0].getType(), DutyType.DRIVER);
		assertEquals(duties[0].getIdAccount(), Duty.UNASSIGNED);
		
		dao.delete(duties[0].getId());
	}

	@Test
	public void testAssign() throws SQLException {
		dao.create(event.getId(), DutyType.RISKMANAGER);
		Duty[] duties = dao.getForEvent(event.getId());

		assertEquals(duties.length, 1);
		assertEquals(duties[0].getIdAccount(), Duty.UNASSIGNED);
		
		dao.assign(duties[0].getId(), 1);
		duties = dao.getForEvent(event.getId());

		assertEquals(duties.length, 1);
		assertEquals(duties[0].getIdAccount(), 1);
		
		dao.delete(duties[0].getId());
	}

	@Test
	public void testGetUnassigned() throws SQLException {
		Date date = Calendar.getInstance().getTime();
		date.setTime(date.getTime() + 60000);
		eventsDAO.create("test2", date, date, "a test event");
		Event[] events = eventsDAO.get(date, date);
		assertEquals(events.length, 1);
		Event event2 = events[0];
		
		dao.create(event.getId(), DutyType.SETCLEAN);
		dao.create(event.getId(), DutyType.SOBER);
		dao.create(event2.getId(), DutyType.DRIVER);
		Duty[] duties = dao.getForEvent(event.getId());
		Duty[] duties2 = dao.getForEvent(event2.getId());
		
		assertEquals(duties.length, 2);
		assertEquals(duties2.length, 1);
		assertEquals(dao.getUnassigned().length, 3);
		
		dao.assign(duties[0].getId(), 2);
		assertEquals(dao.getUnassigned().length, 2);
		
		dao.assign(duties2[0].getId(), 1);
		assertEquals(dao.getUnassigned().length, 1);

		dao.assign(duties[1].getId(), 2);
		assertEquals(dao.getUnassigned().length, 0);
		
		dao.delete(duties[0].getId());
		dao.delete(duties[1].getId());
		dao.delete(duties2[0].getId());
	}

	@Test
	public void testDelete() throws SQLException {
		Duty[] duties = dao.getForEvent(event.getId());
		assertEquals(duties.length, 0);
		
		dao.create(event.getId(), DutyType.RISKMANAGER);
		duties = dao.getForEvent(event.getId());
		assertEquals(duties.length, 1);
		
		dao.delete(duties[0].getId());
		duties = dao.getForEvent(event.getId());
		assertEquals(duties.length, 0);
	}

}
