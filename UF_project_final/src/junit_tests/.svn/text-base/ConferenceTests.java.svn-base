package junit_tests;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Conference;
import model.Decision;
import model.Manuscript;
import model.Recommendation;
import model.Review;
import model.Role;
import model.User;

import org.junit.Before;
import org.junit.Test;

/**
 * JUnit suite to test Conference Methods.
 * 
 * @author Daniel C. Beraun
 * @author Navjot Kamal
 *  December 2, 2012
 */
public class ConferenceTests {
	private Manuscript manuscript_1, manuscript_2, manuscript_3;
	private User user_1, user_2, user_3, user_4;
	private File file_1, file_2, file_3;
	private Conference conference_1, conference_2;
	private List<Manuscript> manuscripts;
	private List<User> users;
	private List<String> deadlines;
	private String conf_name;
	private Map<String, Manuscript> my_manuscript_map;
	private Review review, review_1, review_2, review_3;

	@Before
	public void setUp() throws Exception {
		conf_name = "My conference";

		// initialize users
		user_1 = new User("Peter Pan", "peter@gmail.com", "peterPan", "author");
		user_2 = new User("Mary Ann", "peter@gmail.com", "peterPan", "reviewer");
		user_3 = new User("Jane Pan", "peter@gmail.com", "peterPan",
				"program_chair");
		user_4 = new User("Jane Pan", "peter@gmail.com", "peterPan",
				"subprogram_chair");
		review = new Review(user_2);
		review_1 = new Review(user_1);
		review_2 = new Review(user_3);
		review_3 = new Review(user_4);
		// initialize manuscripts
		manuscript_1 = new Manuscript(user_1, file_1, "My first manuscript");
		manuscript_2 = new Manuscript(user_2, file_2, "My second manuscript");
		manuscript_3 = new Manuscript(user_3, file_3, "My third manuscript");
		manuscript_1.addReview(review);
		manuscript_2.addReview(review_1);
		manuscript_3.addReview(review_2);
		manuscript_3.addReview(review_3);
		// initialize lists
		manuscripts = new ArrayList<Manuscript>();
		manuscripts.add(manuscript_1);
		manuscripts.add(manuscript_2);
		manuscripts.add(manuscript_3);
		users = new ArrayList<User>();
		users.add(user_1);
		users.add(user_2);
		users.add(user_3);
		users.add(user_4);
		deadlines = new ArrayList<String>();
		deadlines.add("12/12/12");
		deadlines.add("13/12/12");
		deadlines.add("14/12/12");

		// initialize conferences
		conference_1 = new Conference(conf_name, user_1, deadlines);
		conference_2 = new Conference(conf_name, user_3, deadlines);

	}

	@Test
	/**
	 * Tests the GetReviewers Method in the Conference Class.
	 */
	public void testGetReviewers() {
		// List of Deadlines for the conference
		List<String> deadlinesList = new ArrayList<String>();
		deadlinesList.add("12/1/2012");
		deadlinesList.add("3/1/2013");
		deadlinesList.add("6/1/2013");

		// Creates the users with their respective name, email, Unique ID,
		// and the role.
		User user = new User("Sylvia", "SylviaCrumb@email.com", "SylviaCrumb",
				"user");
		User programChair = new User("Sylvia", "SylviaCrumb@email.com",
				"SylviaCrumb", "PROGRAM_CHAIR");
		User subProgramChair = new User("Sylvia", "SylviaCrumb@email.com",
				"SylviaCrumb", "SUBPROGRAM_CHAIR");
		User subProgramChair2 = new User("Sylvia", "SylviaCrumb@email.com",
				"SylviaCrumb2", "SUBPROGRAM_CHAIR");
		User reviewer = new User("Sylvia", "SylviaCrumb@email.com",
				"SylviaCrumb2", "REVIEWER");

		// Location of the manuscript
		String manuscriptLocation = "src/data/Conferences/Conference_1/Manuscripts/Manuscript1.txt";

		// Creates a recommendation for a manuscript
		Recommendation recommendation = new Recommendation(subProgramChair);

		// Creates a list of reviews for a manuscript with the respective
		// reviewer
		List<Review> reviewList = new ArrayList<Review>();
		reviewList.add(new Review(subProgramChair));
		reviewList.add(new Review(programChair));
		reviewList.add(new Review(reviewer));

		// Creates a manuscript with the respective author, the file,
		// the title, the decision, the list of reviews and a recommendation
		Manuscript manuscriptToBeAdded = new Manuscript(user, new File(
				manuscriptLocation), "Manuscript1", Decision.UNDECIDED,
				reviewList, recommendation);

		// Creates a new conference with the name and the list of deadlines
		Conference conference = new Conference("Conference_1", programChair,
				deadlinesList);

		// Submits/Adds the manuscript to the conference
		conference.addManuscript(manuscriptToBeAdded);

		// List of Users in the conference who submitted a manuscript
		List<User> userList = new ArrayList<User>();
		userList.add(programChair);
		// userList.add(manuscriptToBeAdded.get_author());

		assertEquals(userList, conference.getConferenceUsers());

		// Creates a empty Reviewer List to test whether the user can
		// access the list of reviewers
		List<User> emptyReviewerList = new ArrayList<User>();

		assertEquals(emptyReviewerList, conference.getReviewers(user));
		;
		assertEquals(emptyReviewerList,
				conference.getReviewers(subProgramChair2));

		// Creates a list of reviewers for the manuscript
		List<User> manuscriptReviewerList = new ArrayList<User>();
		for (Review review : manuscriptToBeAdded.getReviews()) {
			manuscriptReviewerList.add(review.get_reviewer());
		}
		assertEquals(manuscriptReviewerList,
				conference.getReviewers(subProgramChair));

		assertEquals(manuscriptReviewerList,
				conference.getReviewers(programChair));
		// Checks to see if the manuscript has been added to the conference
		List<Manuscript> manuscriptList = new ArrayList<Manuscript>();
		manuscriptList.add(manuscriptToBeAdded);
		// Checks if the manuscript assigned to the subprogram chair works
		assertEquals(manuscriptList,
				conference.getManuscriptList(subProgramChair));
	}

	/**
	 * Tests adding a user to a conference.
	 */
	@Test
	public void testaddUser() {
		// Creates a new conference with the name and deadline of papers.
		conference_1.addUser(user_1, user_1.getCurrentRole());
		conference_1.addUser(user_2, user_2.getCurrentRole());
		conference_1.addUser(user_3, user_3.getCurrentRole());
		// return true if the user is in the conference
		assertTrue(conference_1.containsUser(user_2));

	}

	/**
	 * Tests adding a manuscript to a conference.
	 */
	@Test
	public void testAddManuscript() {
		conference_2.addManuscript(manuscript_1);
		conference_2.addManuscript(manuscript_2);
		// returns true if manuscript is in the conference
		assertTrue(conference_2.containsManuscript(manuscript_1));
		assertTrue(conference_2.containsManuscript(manuscript_2));
	}

	/**
	 * Tests if a manuscript is properly removed from a conference.
	 */
	@Test
	public void testRemoveManuscript() {
		conference_2.addManuscript(manuscript_1);
		conference_2.addManuscript(manuscript_2);
		conference_2.addManuscript(manuscript_3);

		conference_2.removeManuscript(manuscript_2);
		// returns true if conference does not contain said manuscript.
		assertTrue(!conference_2.containsManuscript(manuscript_2));

	}

	/**
	 * Tests whether the conference returns the correct list of manuscripts.
	 */
	@Test
	public void testGetManuscriptList() {
		conference_2.addManuscript(manuscript_3);
		conference_2.addManuscript(manuscript_2);
		conference_2.addManuscript(manuscript_1);
		List<Manuscript> actual = conference_2.getManuscriptList(user_3);

		int expected_size = 3;
		int actual_size = actual.size();
		// test for the same number of elements
		assertTrue(expected_size == actual_size);
		// test that list contains the added elements
		assertTrue(actual.contains(manuscript_1));
		assertTrue(actual.contains(manuscript_2));
		assertTrue(actual.contains(manuscript_3));
	}

	/**
	 * Tests if the conference class returns the manuscript requested.
	 */
	@Test
	public void testchooseManuscript() {
		conference_2.addManuscript(manuscript_2);
		conference_2.addManuscript(manuscript_1);
		Manuscript expected = manuscript_1;
		String title = manuscript_1.getTitle();
		Manuscript actual = conference_2.chooseManuscript(title);
		assertEquals(expected, actual);

	}

	/**
	 * Tests whether the class returns a list of authors based on the
	 * requester's current role.
	 */
	@Test
	public void testgetAuthors() {
		conference_1.addManuscript(manuscript_1);
		conference_1.addManuscript(manuscript_2);
		conference_1.addManuscript(manuscript_3);
		my_manuscript_map = new HashMap<String, Manuscript>();
		my_manuscript_map.put(manuscript_1.getTitle(), manuscript_1);
		my_manuscript_map.put(manuscript_2.getTitle(), manuscript_2);
		my_manuscript_map.put(manuscript_3.getTitle(), manuscript_3);
		List<User> list_authors = conference_1.getAuthors(user_3);
		// User is a program chair who gets all the authors from the conference
		assertTrue(list_authors.contains(user_2));
		assertTrue(list_authors.contains(user_1));
		assertTrue(list_authors.contains(user_3));
		
	

	}
}