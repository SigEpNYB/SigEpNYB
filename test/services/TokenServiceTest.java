package services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import data.Token;
import exceptions.InternalServerException;
import exceptions.InvalidCredentialsException;
import exceptions.TokenNotFoundException;

public class TokenServiceTest {
	TokenService service;
	
	@Before
	public void setUp() throws Exception {
		service = Services.getTokenService();
	}

	@Test
	public void testLogin() throws InternalServerException, InvalidCredentialsException, TokenNotFoundException {
		String tokenStr = service.login("mtr73", "pass1");
		
		assertNotNull(tokenStr);
		assertEquals(tokenStr.length(), 30);
		
		Token token = service.getTokenInfo(tokenStr);
		
		assertEquals(token.getToken(), tokenStr);
		assertEquals(token.getIdAccount(), 1);
		assertNotNull(token.getLoggedIn());
		assertEquals(token.getLoggedIn(), token.getLastActive());
		
		service.logout(tokenStr);
	}
	
	@Test
	public void testLoginTwice() throws InternalServerException, InvalidCredentialsException {
		String token1 = service.login("mtr73", "pass1");

		assertNotNull(token1);
		assertEquals(token1.length(), 30);
		
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
