package model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * A class to create a manuscript
 * 
 * @author Daniel C. Beraun
 *  December 2, 2012
 */
public class Manuscript {
	// Instance fields
	private String my_title;
	private User my_author;
	private File manuscript_doc;
	private Decision decision;
	private List<Review> my_reviews;
	private Recommendation my_recommendation;

	/**
	 * Constructor that constructs complete Manuscript Object...data from XML.
	 * 
	 * @param the_author
	 * @param the_file
	 * @param title
	 * @param the_decision
	 * @param the_reviews
	 * @param the_rec
	 */
	public Manuscript(User the_author, File the_file, String title,
			Decision the_decision, List<Review> the_reviews,
			Recommendation the_rec) {
		my_title = title;
		my_author = the_author;
		manuscript_doc = the_file;
		decision = the_decision;
		my_reviews = the_reviews;
		my_recommendation = the_rec;

	}

	/**
	 * Used in the construction of a new Manuscript Object and also used from
	 * XML data when the manuscript is newly created.
	 * 
	 * @param the_author
	 * @param the_manuscript
	 * @param title
	 */
	public Manuscript(User the_author, File the_manuscript, String title) {
		my_title = title;
		my_author = the_author;
		manuscript_doc = the_manuscript;
		decision = Decision.UNDECIDED;
		my_reviews = new ArrayList<Review>();
	}

	/**
	 * Constructor used when one or more reviews have been added, used by the
	 * XML to construct the object.
	 * 
	 * @param the_author
	 * @param the_manuscript
	 * @param title
	 * @param the_reviews
	 */
	public Manuscript(User the_author, File the_manuscript, String title,
			List<Review> the_reviews) {
		my_title = title;
		my_author = the_author;
		manuscript_doc = the_manuscript;
		decision = Decision.UNDECIDED;
		my_reviews = the_reviews;
	}

	public String getTitle() {
		return my_title;
	}
	
	public void setTitle(String the_title) {
		my_title = the_title;
	}

	public void addReview(Review the_review) {
		my_reviews.add(the_review);
	}

	public List<Review> getReviews() {
		return my_reviews;
	}

	// Accessor and mutators (getters and setters)
	public User get_author() {
		return my_author;
	}

	public void set_author(User the_author) {
		my_author = the_author;
	}

	public File getManuscriptFile() {
		return manuscript_doc;
	}

	public void set_Manuscript_doc(File the_manuscript) {
		manuscript_doc = the_manuscript;
	}

	public Decision get_Decision() {
		return decision;
	}

	public void set_Decision(Decision the_decision) {
		decision = the_decision;
	}

	public Recommendation getRecommendation() {
		return my_recommendation;
		
	}
	public void setRecommendation(Recommendation rec)
	{
		my_recommendation = rec;
	}
}
