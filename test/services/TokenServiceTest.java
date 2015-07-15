package services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.treetest.junit.TreeTestRunner;

import data.Token;
import exceptions.InternalServerException;
import exceptions.InvalidCredentialsException;
import exceptions.TokenNotFoundException;

@RunWith(TreeTestRunner.class)
public class TokenServiceTest {
	TokenService service;
	
	@Before
	public void setUp() throws Exception {
		service = Services.getTokenService();
	}

	@Test
	public void testLogin() throws InternalServerException, InvalidCredentialsException, TokenNotFoundException {
		Date now = Calendar.getInstance().getTime();
		
		String tokenStr = service.login("mtr73", "pass1");
		
		assertNotNull(tokenStr);
		assertTrue(Math.abs(tokenStr.length() - 30) < 2);
		
		Token token = service.getTokenInfo(tokenStr);
		
		assertEquals(token.getToken(), tokenStr);
		assertEquals(token.getIdAccount(), 1);
		assertNotNull(token.getLoggedIn());
		assertEquals(token.getLoggedIn(), token.getLastActive());
		assertTrue(Math.abs(now.getTime() - token.getLoggedIn().getTime()) < 1000);
		
		service.logout(tokenStr);
	}
	
	@Test
	public void testLoginTwice() throws InternalServerException, InvalidCredentialsException {
		String token1 = service.login("mtr73", "pass1");

		assertNotNull(token1);
		assertTrue(Math.abs(token1.length() - 30) < 2);
		
		String token2 = service.login("mtr73", "pass1");
		
		assertEquals(token1, token2);
		
		service.logout(token2);
		
		try {
			service.getTokenInfo(token1);
			fail();
		} catch (TokenNotFoundException e) { }
	}
	
	@Test(expected = InvalidCredentialsException.class)
	public void testLoginBadPassword() throws InternalServerException, InvalidCredentialsException {
		service.login("mtr73", "badpass");
	}

	@Test(expected = InvalidCredentialsException.class)
	public void testLoginBadCredentials() throws InternalServerException, InvalidCredentialsException {
		service.login("badnet", "badpass");
	}

	@Test
	public void testLogout() throws InternalServerException, InvalidCredentialsException {
		String token1 = service.login("mtr73", "pass1");
		service.logout(token1);
		
		try {
			service.getTokenInfo(token1);
			fail();
		} catch (TokenNotFoundException e) { }
		
		String token2 = service.login("mtr73", "pass1");
		
		assertTrue(!token1.equals(token2));
		
		service.logout(token2);
	}

}
