package model;
import java.io.File;

/**
 * A class to create a review object
 * @author Daniel C. Beraun
 *  December 2, 2012
 * 
 */
public class Review {
	
	// Instance fields
	private User my_reviewer;		
	private File my_review_file;
	/**
	 * So the loader can instantiate objects from a file
	 * 
	 * @param theReviewer
	 * @param file_directory
	 */
	public Review(User theReviewer, String file_directory)
	{
		my_reviewer = theReviewer;
		my_review_file = new File(file_directory);
	}
	/**
	 * When a reviewer is assigned by a subprogram chair then 
	 * this review object is created and accessible by the reviewer
	 * to complete review. The reviewer will need to use setReviewFile()
	 * when he/she uploads a file to this Review.
	 * @param theReviewer
	 */
	public Review(User theReviewer) {
		my_reviewer = theReviewer;
		my_review_file = null;
	}	
	
	// Accessor and mutators (getters and setters)
	public User get_reviewer() {
		return my_reviewer;
	}

	public void set_reviewer(User the_reviewer) {
		my_reviewer = the_reviewer;
	}

	public File getReviewFile() {
		return my_review_file;
	}	
	
	public void setReviewFile(File the_file) {
		my_review_file = the_file;
	}
}
