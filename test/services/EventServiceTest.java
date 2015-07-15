package services;

import static org.junit.Assert.assertNotNull;

import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.treetest.junit.TreeTestRunner;

import data.Event;
import exceptions.InternalServerException;
import exceptions.InvalidCredentialsException;
import exceptions.InvalidTokenException;
import exceptions.PermissionDeniedException;

@RunWith(TreeTestRunner.class)
public class EventServiceTest {
	EventService service;
	
	@Before
	public void setUp() throws Exception {
		service = Services.getEventService();
	}

	@Test
	public void testCreate() throws InternalServerException, PermissionDeniedException, InvalidTokenException, InvalidCredentialsException {
		TokenService tokenService = Services.getTokenService();
		String token = tokenService.login("mtr73", "pass1");
		
		Date now = Calendar.getInstance().getTime();
		service.create(token, "bla", now, now, "blablabla");
		Event[] events = service.get(token, now, now);
		
		Event event = null;
		for (Event e : events) {
			if (e.getTitle().equals("bla")) {
				event = e;
				break;
			}
		}
		
		assertNotNull(event);
		
		service.cancel(token, event.getId());
		
		tokenService.logout(token);
	}

	//TODO write more tests

}
