package junit_tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import model.Loader;
import model.Manuscript;
import model.Review;
import model.Role;
import model.User;
import model.XMLHelper;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * The Tests for the Loader Class.
 * 
 * @author Navjot Kamal, Kim Howard
 *  December 2, 2012
 */
public class LoaderTests {
	public static final String DATA_FOLDER = "data" + File.separatorChar;
	public static final String USER_XML = DATA_FOLDER + "Users.xml";
	public static final String CONFERENCES_XML = DATA_FOLDER
			+ "Conferences.xml";
	public static final String MANUSCRIPTS_XML = "TestData"
			+ File.separatorChar + "Manuscripts";

	public static final String UNIQUEIDS[] = { "SylviaCrumb",
			"StephenFaircloth", "JeffDrew", "AmandaPalmer", "GloriaMurphy",
			"EugeneLadd", "JimmySouthwick", "LouisCage", "CarlosByrum",
			"BrianAviles", "SandraGallant", "JanieLayton", "MollyPeebles",
			"RyanKesler", "JeffLindstrom", "JosephHawkins", "BrandonPerrine",
			"JeanetteMingo", "LeonardMarkley", "SusanIsbell", "JuanParke",
			"GladysPyle", "RobertMagallanes", "DorothyFleetwood",
			"JesseMetzger", "BlancaDorsey", "CurtisStidham",
			"FannieMagallanes", "JoshuaBrownlee", "AllenKennard" };
	Loader testLoader;
	Map<String, User> testUserMap;

	@Before
	/**
	 * Sets Up the Loader before running the tests
	 * 
	 * @throws Exception
	 */
	public void setUp() throws FileNotFoundException {
		testLoader = new Loader(DATA_FOLDER);
		testUserMap = testLoader.getUserMap();
	}

	/**
	 * Tests that the XMLHelper.addReviewer is doing what it is supposed to do.
	 * Adds a review node to the XML file.
	 * @author Kim Howard
	 * December 2, 2012
	 */
	@Test
	public void addReviewerXMLTest()

	{
		String dir = "TestData" + File.separatorChar + "reviewsTemplate.xml";
		File xmlFile = new File(dir);
		Document xmlDoc = XMLHelper.getDoc(xmlFile);	
		
				
		Review testReview1 = new Review(testUserMap.get("StephenFaircloth"));
		Review testReview2 = new Review(testUserMap.get("AllenKennard"));
		Review testReview3 = new Review(testUserMap.get("RyanKesler"));
		Review testReview4 = new Review(testUserMap.get("JeffDrew"));
		String[] revs = { "StephenFaircloth", "AllenKennard", "RyanKesler",
				"JeffDrew" };

		XMLHelper.addReviewer(xmlDoc, testReview1.get_reviewer().getUniqueID(),
				dir);
		XMLHelper.addReviewer(xmlDoc, testReview2.get_reviewer().getUniqueID(),
				dir);
		XMLHelper.addReviewer(xmlDoc, testReview3.get_reviewer().getUniqueID(),
				dir);
		XMLHelper.addReviewer(xmlDoc, testReview4.get_reviewer().getUniqueID(),
				dir);

		List<Review> listOfReviews = testLoader.getReviews(xmlFile);
		int i = 0;
		
		for (Review r : listOfReviews) {
			
			assertTrue("Tests for equality",
					revs[i].equals(r.get_reviewer().getUniqueID()));
			i++;
			i = i % 4;
			
		}
		/**
		 * Tests that the correct file was added to each directory
		 */
		for(int j = 0; j < listOfReviews.size(); j++)
		{
			XMLHelper.addReviewDir(xmlDoc, dir, "StephenFaircloth", dir);
			XMLHelper.addReviewDir(xmlDoc, dir, "AllenKennard", dir);
			XMLHelper.addReviewDir(xmlDoc, dir, "RyanKesler", dir);
			XMLHelper.addReviewDir(xmlDoc, dir, "JeffDrew", dir);
		}
	
		
		String expected = xmlFile.toString();		 
		for(int j = 0; j < 4; j++)
		{			
			assertTrue("Tests for equality", expected.equals(listOfReviews.get(j).getReviewFile().toString()));
		}
		
	}

	/**
	 * Tests the containsUser method because when the System is loaded all the
	 * users in the UNIQUEIDS had better be in there. This also ensures that the
	 * UserXML file located in the DATA_FOLDER is read correctly.
	 */
	@Test
	public void testContainsUser() {
		for (int i = 0; i < UNIQUEIDS.length; i++) {
			String userID = UNIQUEIDS[i];
			assertTrue(
					"Tests to see if that userID is in the User List of system.",
					testLoader.containsUser(userID));
			assertFalse("Tests to see if a randome userID produces a result",
					testLoader.containsUser("anyUser"));
		}
	}

	@Test
	/**
	 * Tests the getConference() in the Loader class for it's cases.
	 */
	public void testGetConference() {
		assertEquals("Tests if the Conference is the same name", testLoader
				.getConferenceList().keySet().toArray()[5], testLoader
				.getConference("JeffsConference").getConferenceName());
		assertEquals("Tests if the Conference is the same name", testLoader
				.getConferenceList().keySet().toArray()[3], testLoader
				.getConference("SylviaConference").getConferenceName());
		assertEquals("Tests if null is passed in", null,
				testLoader.getConference(null));
	}

	/**
	 * This tests the getUser() method because this accessor needs to get the
	 * correct User from the uniqueID. It goes through a map and therefore there
	 * is a little bit of computation. This also ensures that the UserXML is
	 * read correctly.
	 */
	@Test
	public void testGetUser() {
		Map<String, User> user_map = testLoader.getUserMap();
		String expected;
		String actual;
		String actualrandom = "No Name";
		for (int i = 0; i < UNIQUEIDS.length; i++) {
			expected = user_map.get(UNIQUEIDS[i]).getUniqueID();
			actual = testLoader.getUser(UNIQUEIDS[i]).getUniqueID();
			assertTrue("Tests for equality", expected.equals(actual));
			assertFalse("Tests that any random string will not produce a User",
					expected.equals(actualrandom));
			assertEquals(
					"Tests to see when passed in a user not in te conference produces"
							+ "the correct result", null,
					testLoader.getUser("NotInConference"));
		}
	}

	@Test
	/**
	 * Tests the getProgramChair with the different conferences as inputs.
	 */
	public void testGetProgramChair() {
		assertEquals("Testing the Program Chair for Conference_1",
				testLoader.getUser("JeffDrew"),
				testLoader.getProgramChair("JeffsConference"));
		assertEquals("Testing the Program Chair for Conference_2",
				testLoader.getUser("StephenFaircloth"),
				testLoader.getProgramChair("Stephen's Super Conference"));
		assertEquals("Testing the Program Chair for JeffsConference",
				testLoader.getUser("JeffDrew"),
				testLoader.getProgramChair("Super Geeks"));
		assertEquals("Testing the Program Chair for Jeffs New Conference",
				testLoader.getUser("SylviaCrumb"),
				testLoader.getProgramChair("SylviaConference"));
		assertEquals(
				"Testing the Program Chair for a conference that doesn't exist",
				null, testLoader.getProgramChair("NotAConference"));
		assertEquals("Testing the Program Chair for a null conference", null,
				testLoader.getProgramChair(null));
	}

	@Test
	/**
	 * Tests the createConferenceManuscripts() for the Loader Class for the 
	 * different cases that could arise.
	 */
	public void testCreateConferenceManuscripts() {
		String location = DATA_FOLDER + "Conferences" + File.separatorChar
				+ "JeffsConference" + File.separatorChar + "Manuscripts"
				+ File.separatorChar;
		List<String> manuscriptList = new ArrayList<String>();
		Map<String, Manuscript> map = testLoader
				.createConferenceManuscripts(location);
		for (String key : map.keySet()) {
			manuscriptList.add(key);
		}

		List<String> expectedList = new ArrayList<String>();
		expectedList.add("SylviaToJeff2");
		expectedList.add("SylviaToJeff1");
		expectedList.add("JeffToJeff");

		assertEquals("Checks for the names of the manuscripts match",
				expectedList, manuscriptList);
		assertEquals("Checks for null input", null,
				testLoader.createConferenceManuscripts(null));
	}

	@Test
	/**
	 * Tests the getReviews() for the Loader Class for the 
	 * different cases that could arise.
	 */
	public void testgetReviews() {
		String location = DATA_FOLDER + "Conferences" + File.separatorChar
				+ "Super Geeks" + File.separatorChar + "Manuscripts"
				+ File.separatorChar;
		String title = null;
		List<Review> reviews = new ArrayList<Review>();

		org.w3c.dom.Document conferenceManuscriptsXML = XMLHelper
				.getDoc(new File(location + "/Manuscripts.xml"));
		Element xmlDoc = conferenceManuscriptsXML.getDocumentElement();// gets
																		// root
																		// element
		NodeList listOfManuscripts = xmlDoc.getElementsByTagName("manuscript");

		for (int i = 0; i < listOfManuscripts.getLength(); i++) {
			Node manuscriptNode = listOfManuscripts.item(i);
			Element manuscriptElement = (Element) manuscriptNode;

			title = XMLHelper.nodeToString("title", manuscriptElement);
			reviews = testLoader.getReviews(new File(location
					+ File.separatorChar + title + File.separatorChar
					+ "reviews.xml"));
			if (!title.equals("StephenGeeks"))
				assertEquals("No reviews for the manuscript",
						new ArrayList<Review>(), reviews);
			else {
				List<File> iteratedReviews = new ArrayList<File>();
				for (Review review : reviews) {
					iteratedReviews.add(review.getReviewFile());
				}

				List<File> reviewFiles = new ArrayList<File>();
				reviewFiles.add(new File(location + File.separatorChar + title
						+ File.separatorChar + "Reviews" + File.separatorChar
						+ "review1.txt"));
				reviewFiles.add(new File(location + File.separatorChar + title
						+ File.separatorChar + "Reviews" + File.separatorChar
						+ "review2.txt"));
				assertEquals("2 reviews for manuscript", reviewFiles,
						iteratedReviews);
				assertEquals("Checking for null input", null,
						testLoader.getReviews(null));
			}
		}
	}
}