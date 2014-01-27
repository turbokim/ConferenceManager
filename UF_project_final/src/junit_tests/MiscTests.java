package junit_tests;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;

import model.Conference;
import model.Loader;
import model.Manuscript;
import model.User;
import model.XMLHelper;

import org.junit.Test;
import org.w3c.dom.Document;

public class MiscTests {

	/**
	 * Tests XMLHelper.updateManuscriptsXML()
	 * 
	 * @author Steve Hipolito
	 *  December 2, 2012
	 */
	@Test
	public void testUpdateManuscriptsXML() {
		Loader loader = null;
		try {
			loader = new Loader("Data" + File.separatorChar);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
		String mansDir = "Data" + File.separatorChar + 
				"Conferences" + File.separatorChar + 
				"Super Geeks" + File.separatorChar + 
				"Manuscripts";
		String manXML_dir = mansDir + File.separatorChar + "Manuscripts.xml";
		Document manuscriptDoc = XMLHelper.getDoc(new File(manXML_dir));
		User user = new User("", "", "JeffDrew", "user");;
		Manuscript testMan = new Manuscript(user, new File(mansDir + File.separatorChar + "ZZZZZ.txt"), mansDir + File.separatorChar + "StephenGeeks");
		
		XMLHelper.updateManuscriptsXML(manXML_dir, "StephenGeeks", manuscriptDoc, testMan);
		
		loader = null;
		try {
			loader = new Loader("Data" + File.separatorChar);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
		assertTrue(loader.getConference("Super Geeks").containsManuscript(testMan));
	}
}
