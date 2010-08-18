package kiwi.test.service.user;

import kiwi.api.user.UserService;
import kiwi.test.base.KiWiTest;

import org.jboss.seam.Component;
import org.jboss.seam.security.Identity;
import org.testng.annotations.Test;

@Test
public class PermissionServiceTest extends KiWiTest {
	
	/**
	 * Test creation of users using userService.createUser(login, firstName, Password)
	 * @throws Exception
	 */
	@Test
	public void testCreateUserFull() throws Exception {
		/*
		 * Create a User using the UserService
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() throws Exception {

            	Identity.setSecurityEnabled(false);
            	
            	UserService        us = (UserService) Component.getInstance("userService");
            	
            	us.createUser("testusr1", "Hans", "Mustermann", "hansPW");

             }
    	}.run();
	}
}
