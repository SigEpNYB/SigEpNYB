package services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import data.AccountData;
import data.Role;
import exceptions.AccountNotFoundException;
import exceptions.InternalServerException;
import exceptions.InvalidCredentialsException;
import exceptions.InvalidTokenException;
import exceptions.PermissionDeniedException;

public class AccountServiceTest {
	AccountsService service;
	
	@Before
	public void setUp() throws Exception {
		service = Services.getAccountService();
	}

	@Test
	public void testCreate() throws InternalServerException, InvalidCredentialsException, InvalidTokenException, PermissionDeniedException, AccountNotFoundException {
		TokenService tokenService = Services.getTokenService();
		String token = tokenService.login("mtr73", "pass1");
		
		service.create(token, "bla", "Mr", "Bla");
		
		AccountData accountData = null;
		for (AccountData data : service.getAccounts(token)) {
			if (data.getNetid().equals("bla")) {
				accountData = data;
				break;
			}
		}
		
		assertNotNull(accountData);
		assertTrue(accountData.getId() > 1);
		assertEquals(accountData.getNetid(), "bla");
		assertEquals(accountData.getFirstName(), "Mr");
		assertEquals(accountData.getLastName(), "Bla");
		
		RoleService roleService = Services.getRoleService();
		
		assertTrue(roleService.has(accountData.getId(), Role.BROTHER));
		
		service.delete(token, "bla");
		
		tokenService.logout(token);
	}
	
	@Test(expected = InvalidTokenException.class)
	public void testCreateBadToken() throws InternalServerException, InvalidTokenException, PermissionDeniedException {
		service.create("badtoken", "dfqe", "sdg", "sadg");
	}
	
	@Test()
	public void testCreateBadPermission() throws InternalServerException, InvalidCredentialsException {
		TokenService tokenService = Services.getTokenService();
		String token = tokenService.login("net123", "letmein");
		
		try {
			service.create(token, "flhwq", "wrg	", "wqrgq");
		} catch (InvalidTokenException e) {
			fail("should be a valid token");
		} catch (PermissionDeniedException e) {
			tokenService.logout(token);
			return;
		}
		
		fail("Expected PermissionDeniedException");
	}

	@Test
	public void testGetId() throws InternalServerException, AccountNotFoundException {
		assertEquals(service.getId("mtr73", "pass1"), 1);
	}
	
	@Test(expected = AccountNotFoundException.class)
	public void testNotFoundBadPass() throws InternalServerException, AccountNotFoundException {
		service.getId("mtr73", "badpass");
	}
	
	@Test(expected = AccountNotFoundException.class)
	public void testNotFoundBadCreds() throws InternalServerException, AccountNotFoundException {
		service.getId("badnet", "badpass");
	}

	@Test
	public void testGetAccount() throws InternalServerException, InvalidCredentialsException, InvalidTokenException {
		TokenService tokenService = Services.getTokenService();
		String token = tokenService.login("mtr73", "pass1");
		
		AccountData account = service.getAccount(token);
		
		assertEquals(account.getId(), 1);
		assertEquals(account.getNetid(), "mtr73");
		assertEquals(account.getFirstName(), "Max");
		assertEquals(account.getLastName(), "Rademacher");
		
		tokenService.logout(token);
	}
	
	@Test(expected = InvalidTokenException.class)
	public void testGetAccountBadToken() throws InternalServerException, InvalidTokenException {
		service.getAccount("badtoken");
	}

	@Test
	public void testGetAccounts() throws InternalServerException, InvalidCredentialsException, InvalidTokenException, PermissionDeniedException {
		TokenService tokenService = Services.getTokenService();
		String token = tokenService.login("net123", "letmein");
		
		AccountData[] accounts = service.getAccounts(token);
		
		assertEquals(accounts.length, 3);
		
		assertEquals(accounts[0].getId(), 1);
		assertEquals(accounts[0].getNetid(), "mtr73");
		assertEquals(accounts[0].getFirstName(), "Max");
		assertEquals(accounts[0].getLastName(), "Rademacher");
		
		assertEquals(accounts[1].getId(), 2);
		assertEquals(accounts[1].getNetid(), "net123");
		assertEquals(accounts[1].getFirstName(), "Joe");
		assertEquals(accounts[1].getLastName(), "Shmoe");
		
		assertEquals(accounts[2].getId(), 3);
		assertEquals(accounts[2].getNetid(), "nofun");
		assertEquals(accounts[2].getFirstName(), "No");
		assertEquals(accounts[2].getLastName(), "Permissions");
	}
	
	@Test(expected = InvalidTokenException.class)
	public void testGetAccountsBadToken() throws InternalServerException, InvalidTokenException, PermissionDeniedException {
		service.getAccounts("badtoken");
	}
	
	@Test
	public void testGetAccountsBadPermission() throws InternalServerException, InvalidCredentialsException {
		TokenService tokenService = Services.getTokenService();
		String token = tokenService.login("nofun", "plz");
		
		try {
			service.create(token, "flhwq", "wrg	", "wqrgq");
		} catch (InvalidTokenException e) {
			fail("should be a valid token");
		} catch (PermissionDeniedException e) {
			tokenService.logout(token);
			return;
		}
		
		fail("Excepted PermissionDeniedException");
	}

	@Test
	public void testDelete() throws InternalServerException, InvalidCredentialsException, InvalidTokenException, PermissionDeniedException, AccountNotFoundException {
		TokenService tokenService = Services.getTokenService();
		String token = tokenService.login("mtr73", "pass1");
		
		service.create(token, "bla", "Mr", "Bla");

		service.delete(token, "bla");
		
		for (AccountData account : service.getAccounts(token)) {
			if (account.getNetid().equals("bla")) {
				fail("'bla' should've been deleted");
			}
		}

		tokenService.logout(token);
	}
	
	@Test(expected = InvalidTokenException.class)
	public void testDeleteBadToken() throws InternalServerException, InvalidTokenException, PermissionDeniedException, AccountNotFoundException {
		service.delete("badtoken", "badnet");
	}
	
	@Test
	public void testDeleteBadNet() throws InternalServerException, InvalidCredentialsException {
		TokenService tokenService = Services.getTokenService();
		String token = tokenService.login("mtr73", "pass1");
		
		try {
			service.delete(token, "batnet");
		} catch (InvalidTokenException e) {
			fail("should be a valid token");
		} catch (PermissionDeniedException e) {
			fail("should have permission");
		} catch (AccountNotFoundException e) {
			tokenService.logout(token);
			return;
		}
		
		fail("Expected PermissionDeniedException");
	}
	
	public void testDeleteBadPermission() throws InternalServerException, InvalidCredentialsException {
		TokenService tokenService = Services.getTokenService();
		String token = tokenService.login("net123", "letmein");
		
		try {
			service.delete(token, "nofun");
		} catch (InvalidTokenException e) {
			fail("should be a valid token");
		} catch (PermissionDeniedException e) {
			tokenService.logout(token);
			return;
		} catch (AccountNotFoundException e) {
			fail("should have found account");
		}
		
		fail("Expected PermissionDeniedException");
	}

}
