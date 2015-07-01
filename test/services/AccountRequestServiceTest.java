package services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import data.AccountData;
import data.AccountRequest;
import data.Todo;
import exceptions.AccountExistsException;
import exceptions.AccountNotFoundException;
import exceptions.InternalServerException;
import exceptions.InvalidCredentialsException;
import exceptions.InvalidTokenException;
import exceptions.PermissionDeniedException;
import exceptions.RequestExistsException;

public class AccountRequestServiceTest {
	AccountRequestService service;
	
	@Before
	public void setUp() throws Exception {
		service = Services.getAccountRequestService();
	}

	@Test
	public void testCreate() throws InternalServerException, InvalidCredentialsException, PermissionDeniedException, InvalidTokenException, AccountNotFoundException, AccountExistsException, RequestExistsException {
		TodoService todoService = Services.getTodoService();
		assertEquals(todoService.get(1).length, 0);
		
		TokenService tokenService = Services.getTokenService();
		String token = tokenService.login("mtr73", "pass1");
		
		assertEquals(service.get(token).length, 0);
		service.create("a", "b", "c", "d");
		AccountRequest[] requests = service.get(token);
		assertEquals(requests.length, 1);
		
		Todo[] todos = todoService.get(1);
		assertEquals(todos.length, 1);
		
		service.reject(token, requests[0].getId());
	}

	@Test(expected = InvalidTokenException.class)
	public void testGetBadToken() throws InternalServerException, PermissionDeniedException, InvalidTokenException {
		service.get("badtoken");
	}
	
	@Test(expected = PermissionDeniedException.class)
	public void testGetBadPermission() throws InternalServerException, InvalidCredentialsException, PermissionDeniedException, InvalidTokenException {
		TokenService tokenService = Services.getTokenService();
		String token = tokenService.login("net123", "letmein");
		
		try {
			service.get(token);
		} finally {
			tokenService.logout(token);
		}
	}

	@Test
	public void testAccept() throws InternalServerException, InvalidCredentialsException, PermissionDeniedException, InvalidTokenException, AccountNotFoundException, AccountExistsException, RequestExistsException {
		TokenService tokenService = Services.getTokenService();
		String token = tokenService.login("mtr73", "pass1");
		
		service.create("foobar", "bla", "foo", "bar");
		AccountRequest request = service.get(token)[0];

		TodoService todoService = Services.getTodoService();
		assertEquals(todoService.get(1).length, 1);
		
		service.accept(token, request.getId());
		assertEquals(todoService.get(1).length, 0);
		
		assertEquals(service.get(token).length, 0);
		
		AccountData[] accounts = Services.getAccountService().getAccounts(token);
		AccountData account = null;
		for (AccountData data : accounts) {
			if (data.getNetid().equals(request.getNetid())) {
				account = data;
				break;
			}
		}
		
		assertNotNull(account);
		assertTrue(account.getId() > 1);
		assertEquals(account.getNetid(), "foobar");
		assertEquals(account.getFirstName(), "foo");
		assertEquals(account.getLastName(), "bar");
		
		Services.getAccountService().delete(token, account.getNetid());
	}
	
	@Test(expected = InvalidTokenException.class)
	public void testAcceptBadToken() throws InternalServerException, PermissionDeniedException, InvalidTokenException, AccountNotFoundException {
		service.accept("badtoken", -1);
	}
	
	@Test(expected = PermissionDeniedException.class)
	public void testAcceptBadPermission() throws InternalServerException, InvalidCredentialsException, PermissionDeniedException, InvalidTokenException, AccountNotFoundException {
		TokenService tokenService = Services.getTokenService();
		String token = tokenService.login("net123", "letmein");
		
		try {
			service.accept(token, -1);
		} finally {
			tokenService.logout(token);
		}
	}
	
	@Test(expected = AccountNotFoundException.class)
	public void testAcceptBadId() throws InternalServerException, InvalidCredentialsException, PermissionDeniedException, InvalidTokenException, AccountNotFoundException {
		TokenService tokenService = Services.getTokenService();
		String token = tokenService.login("mtr73", "pass1");
		
		try {
			service.accept(token, -1);
		} finally {
			tokenService.logout(token);
		}
	}

	@Test
	public void testReject() throws InternalServerException, PermissionDeniedException, InvalidTokenException, AccountNotFoundException, InvalidCredentialsException, AccountExistsException, RequestExistsException {
		TokenService tokenService = Services.getTokenService();
		String token = tokenService.login("mtr73", "pass1");
		
		service.create("blarp", "bla", "foo", "bar");
		AccountRequest request = service.get(token)[0];

		TodoService todoService = Services.getTodoService();
		assertEquals(todoService.get(1).length, 1);
		
		service.reject(token, request.getId());
		
		assertEquals(todoService.get(1).length, 0);
		
		assertEquals(service.get(token).length, 0);
		
		AccountData[] accounts = Services.getAccountService().getAccounts(token);
		AccountData account = null;
		for (AccountData data : accounts) {
			if (data.getNetid().equals(request.getNetid())) {
				account = data;
				break;
			}
		}
		
		assertNull(account);
	}
	
	@Test(expected = InvalidTokenException.class)
	public void testRejectBadToken() throws InternalServerException, PermissionDeniedException, InvalidTokenException, AccountNotFoundException {
		service.reject("badtoken", -1);
	}
	
	@Test(expected = PermissionDeniedException.class)
	public void testRejectBadPermission() throws InternalServerException, InvalidCredentialsException, PermissionDeniedException, InvalidTokenException, AccountNotFoundException {
		TokenService tokenService = Services.getTokenService();
		String token = tokenService.login("net123", "letmein");
		
		try {
			service.reject(token, -1);
		} finally {
			tokenService.logout(token);
		}
	}

}
