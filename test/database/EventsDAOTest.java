package database;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.Date;

import org.junit.runner.RunWith;

import com.treetest.junit.DataFile;
import com.treetest.junit.ParameterVariables;
import com.treetest.junit.ReturnVariable;
import com.treetest.junit.TreeTestRunner;

import data.Event;

@RunWith(TreeTestRunner.class)
public class EventsDAOTest {
	private final EventsDAO dao;
	
	@ParameterVariables("database")
	public EventsDAOTest(IDatabase database) {
		dao = database.getEventsDAO();
	}
	
	private Event[] getAllEvents() throws SQLException {
		Date start = new Date(0);
		Date end = new Date(Long.MIN_VALUE);
		return dao.get(start, end);
	}
	
	private Event getEvent(Event[] events, String title, Date start, Date end, String description) {
		for (Event event : events) {
			if (event.getTitle().equals(title) && 
					event.getStartTime().equals(start) && 
					event.getEndTime().equals(end) && 
					event.getDescription().equals(description)) return event;
		}
		return null;
	}
	
	private Event getEvent(Event[] events, int id) {
		for (Event event : events) {
			if (event.getId() == id) return event;
		}
		return null;
	}
	
	@DataFile("testdata/events.txt")
	@ReturnVariable("idEvent")
	public int start(String title, Date start, Date end, String description) throws SQLException {
		int numEvents = getAllEvents().length;
		
		dao.create(title, start, end, description);
		Event[] events = dao.get(start, end);
		
		assertTrue(events.length > 0);
		assertEquals(numEvents + 1, getAllEvents().length);
		
		Event event = getEvent(events, title, start, end, description);
		assertNotNull(event);
		int idEvent = event.getId();
		assertTrue(dao.exists(idEvent));
		
		assertTrue(idEvent > 0);
		assertEquals(title, event.getTitle());
		assertEquals(start, event.getStartTime());
		assertEquals(end, event.getEndTime());
		assertEquals(description, event.getDescription());
		
		return idEvent;
	}
	
	@DataFile("testdata/getevents.txt")
	public void get(Date start, Date end, int[] ids) throws SQLException {
		Event[] events = dao.get(start, end);
		
		assertEquals(ids.length, events.length);
		
		for (int i = 0; i < events.length; i++) {
			assertEquals(ids[i], events[i].getId());
		}
	}
	
	@ParameterVariables("idEvent")
	public void end(int idEvent) throws SQLException {
		Event[] events = getAllEvents();
		int numEvents = events.length;
		assertNotNull(getEvent(events, idEvent));
		
		dao.cancel(idEvent);
		
		events = getAllEvents();
		assertEquals(numEvents - 1, events.length);
		assertNull(getEvent(events, idEvent));
		assertFalse(dao.exists(idEvent));
	}
}
