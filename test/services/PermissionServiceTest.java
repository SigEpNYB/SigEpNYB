package services;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.treetest.junit.TreeTestRunner;

import data.Permission;
import exceptions.InternalServerException;

@RunWith(TreeTestRunner.class)
public class PermissionServiceTest {
	PermissionService service;
	
	@Before
	public void setUp() throws Exception {
		service = Services.getPermissionService();
	}

	@Test
	public void testHasPermission() throws InternalServerException {
		assertTrue(service.hasPermission(1, Permission.DELETEACCOUNT));
		assertFalse(service.hasPermission(2, Permission.POSTEVENTS));
		assertTrue(service.hasPermission(2, Permission.GETEVENTS));
		assertFalse(service.hasPermission(300, Permission.GETEVENTS));
	}

}
