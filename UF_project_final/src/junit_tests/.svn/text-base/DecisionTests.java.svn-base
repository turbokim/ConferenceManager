package junit_tests;

import static org.junit.Assert.*;

import java.io.File;

import model.Decision;
import model.Manuscript;
import model.User;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests the different cases for the Decision class.
 * 
 * @author Navjot Kamal
 *  December 2, 2012
 */
public class DecisionTests {

	Manuscript howToLive;
	Manuscript superMan;
	Manuscript toBeNotToBe;
	File file1;
	File file2;
	File file3;
	
	@Before
	/**
	 * Sets Up the data needed to test the methods.
	 */
	public void setUp() {
		User peterPan = new User("Peter Pan", "peter@gmail.com", "peterPan", "author");
		User maryAnn = new User("Mary Ann", "peter@gmail.com", "peterPan", "reviewer");
		User janePan = new User("Jane Pan", "peter@gmail.com", "peterPan",
				"program_chair");
		
		howToLive = new Manuscript(peterPan, file1, "My first manuscript");
		superMan = new Manuscript(maryAnn, file2, "My second manuscript");
		toBeNotToBe = new Manuscript(janePan, file3, "My third manuscript");
	}

	@Test
	/**
	 * Tests the getDecisionType() in the Decision class for it's various cases.
	 */
	public void testGetDecisionType() {
		// Sets the two paper's decisions, different from the default NOT_DECIDED
		superMan.set_Decision(Decision.UNACCEPTED);
		toBeNotToBe.set_Decision(Decision.ACCEPTED);
		
		assertEquals("Tests with the input as a manuscript's decision being undecided", 
				Decision.UNDECIDED, Decision.getDecisionType(howToLive.get_Decision().toString()));
		assertEquals("Tests with the input as a manuscript's decision being not accepted", 
				Decision.UNACCEPTED, Decision.getDecisionType(superMan.get_Decision().toString()));
		assertEquals("Tests with the input as a manuscript's decision being accepted", 
				Decision.ACCEPTED, Decision.getDecisionType(toBeNotToBe.get_Decision().toString()));
		assertEquals("Tests with the input as a manuscript's decision being null", 
				null, Decision.getDecisionType(null));
		assertEquals("Tests with the input as not a real decision type", 
				null, Decision.getDecisionType("NotADecision"));
	}
}