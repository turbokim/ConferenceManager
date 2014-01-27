package junit_tests;

import static org.junit.Assert.*;

import model.Role;
import model.User;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests for Role Class that goes through all of the different role cases.
 * 
 * @author Navjot Kamal
 *  December 2, 2012
 */
public class RoleTests {
	User SylviaCrumb;
	User StephenFaircloth;
	User JeffDrew;
	User AmandaPalmer;
	User GloriaMurphy;
	
	@Before
	/**
	 * Sets Up the data needed to test the methods.
	 */
	public void setUp() {
		SylviaCrumb = new User("Sylvia Crumb", "SylviaCrumb", "SylviaCrumb@email.com", "USER");
		StephenFaircloth = new User("Stephen Faircloth", "StephenFaircloth", "StephenFaircloth@email.com", "PROGRAM_CHAIR");
		JeffDrew = new User("Jeff Drew", "JeffDrew", "JeffDrew@email.com", "SUBPROGRAM_CHAIR");
		AmandaPalmer = new User("Amanda Palmer", "AmandaPalmer", "AmandaPalmer@email.com", "REVIEWER");
		GloriaMurphy = new User("GloriaMurphy", "GloriaMurphy", "GloriaMurphy@email.com", "AUTHOR");
	}

	@Test
	/**
	 * Tests the getRoleType() in the Role class for it's various cases.
	 */
	public void testGetRoleType() {
		assertEquals("Tests with the input as a User matches the output", 
				Role.USER, Role.getRoleType(SylviaCrumb.getCurrentRole().toString()));
		assertEquals("Tests with the input as a Program Chair matches the output", 
				Role.PROGRAM_CHAIR, Role.getRoleType(StephenFaircloth.getCurrentRole().toString()));
		assertEquals("Tests with the input as a Subprogram Chair matches the output", 
				Role.SUBPROGRAM_CHAIR, Role.getRoleType(JeffDrew.getCurrentRole().toString()));
		assertEquals("Tests with the input as a Reviewer matches the output", 
				Role.REVIEWER, Role.getRoleType(AmandaPalmer.getCurrentRole().toString()));
		assertEquals("Tests with the input as a Author matches the output", 
				Role.AUTHOR, Role.getRoleType(GloriaMurphy.getCurrentRole().toString()));
		assertEquals("Tests with the input not a real role type", 
				null, Role.getRoleType("NotARole"));
		assertEquals("Tests with null input", 
				null, Role.getRoleType(null));
	}
}