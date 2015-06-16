package services;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import data.Role;
import exceptions.InternalServerException;

public class RoleServiceTest {
	RoleService service;
	
	@Before
	public void setUp() throws Exception {
		service = Services.getRoleService();
	}

	@Test
	public void testAssign() throws InternalServerException {
		assertFalse(service.has(3, Role.BROTHER));
		service.assign(3, Role.BROTHER);
		assertTrue(service.has(3, Role.BROTHER));
		service.unassignAll(3);
	}
	
	@Test
	public void testHas() throws InternalServerException {
		assertTrue(service.has(1, Role.PRESIDENT));
		assertFalse(service.has(2, Role.PRESIDENT));
		assertFalse(service.has(3, Role.BROTHER));
	}
	
	@Test
	public void testUnassignAll() throws InternalServerException {
		assertFalse(service.has(3, Role.BROTHER));
		assertFalse(service.has(3, Role.VPPROGRAMMING));
		assertFalse(service.has(3, Role.PRESIDENT));
		
		service.assign(3, Role.BROTHER);
		service.assign(3, Role.VPPROGRAMMING);
		service.assign(3, Role.PRESIDENT);
		
		assertTrue(service.has(3, Role.BROTHER));
		assertTrue(service.has(3, Role.VPPROGRAMMING));
		assertTrue(service.has(3, Role.PRESIDENT));
		
		service.unassignAll(3);
		
		assertFalse(service.has(3, Role.BROTHER));
		assertFalse(service.has(3, Role.VPPROGRAMMING));
		assertFalse(service.has(3, Role.PRESIDENT));
	}

}
