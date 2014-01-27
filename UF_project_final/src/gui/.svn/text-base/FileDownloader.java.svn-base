package gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import model.Manuscript;
import model.Recommendation;
import model.Review;

/** A file downloader. Copies the given file and saves it 
 * to a location determined by a JFileChooser.
 * 
 * @author Steve Hipolito
 *  December 2, 2012
 */
public class FileDownloader {
	
	private FileDownloader() {
		throw new AssertionError();
	}
	
	/** Downloads the given Manuscript */
	public static void downloadManuscript(final Manuscript the_man) {
		JFileChooser fc = new JFileChooser();
		fc.setSelectedFile(the_man.getManuscriptFile());
		int result = fc.showSaveDialog(null);
		if (result == JFileChooser.APPROVE_OPTION) {
			InputStream inStream = null;
			OutputStream outStream = null;
			File stored_man_file = null;
			try {
				stored_man_file = new File(fc.getSelectedFile().toString());
				inStream = new FileInputStream(the_man.getManuscriptFile());
				outStream = new FileOutputStream(stored_man_file);
				byte[] buffer = new byte[1024];
				int length;
				while ((length = inStream.read(buffer)) > 0){
					outStream.write(buffer, 0, length);
				}
				inStream.close();
				outStream.close();
				JOptionPane.showConfirmDialog(null,
						the_man.getTitle() + "  downloaded and saved to \n" + 
						stored_man_file.getAbsolutePath(),
						"File Download", JOptionPane.DEFAULT_OPTION);
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	/** Downloads the given Review */
	public static void downloadReview(final Review the_review) {
		//null check
		if (the_review.getReviewFile() == null) {
			JOptionPane.showConfirmDialog(null,
					"This review has no file yet. A reviewer has " +
					"yet to upload one",
					"File Download", JOptionPane.DEFAULT_OPTION);
		} else {
			JFileChooser fc = new JFileChooser();
			fc.setSelectedFile(the_review.getReviewFile());
			int result = fc.showSaveDialog(null);
			if (result == JFileChooser.APPROVE_OPTION) {
				InputStream inStream = null;
				OutputStream outStream = null;
				File stored_man_file = null;
				try {
					stored_man_file = new File(fc.getSelectedFile().toString());
					inStream = new FileInputStream(the_review.getReviewFile());
					outStream = new FileOutputStream(stored_man_file);
					byte[] buffer = new byte[1024];
					int length;
					while ((length = inStream.read(buffer)) > 0){
						outStream.write(buffer, 0, length);
					}
					inStream.close();
					outStream.close();
					JOptionPane.showConfirmDialog(null,
							"The review has been downloaded and saved to \n" + 
							stored_man_file.getAbsolutePath(),
							"File Download", JOptionPane.DEFAULT_OPTION);
				} catch(IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/** Downloads the given Recommendation */
	public static void downloadRecommendation(final Recommendation the_rec) {
		//null check
		if (the_rec == null || the_rec.get_recommendation_doc().getPath().equals("null")) {
			JOptionPane.showConfirmDialog(null,
					"Either there is not a SubprogramChair assigned to this Manuscript yet, \n" +
					"or the Subprogram Chair has yet to write a recommendation.",
					"File Download", JOptionPane.DEFAULT_OPTION);
		} else {
			JFileChooser fc = new JFileChooser();
			fc.setSelectedFile(the_rec.get_recommendation_doc());
			int result = fc.showSaveDialog(null);
			if (result == JFileChooser.APPROVE_OPTION) {
				InputStream inStream = null;
				OutputStream outStream = null;
				File stored_man_file = null;
				try {
					stored_man_file = new File(fc.getSelectedFile().toString());
					inStream = new FileInputStream(the_rec.get_recommendation_doc());
					outStream = new FileOutputStream(stored_man_file);
					byte[] buffer = new byte[1024];
					int length;
					while ((length = inStream.read(buffer)) > 0){
						outStream.write(buffer, 0, length);
					}
					inStream.close();
					outStream.close();
					JOptionPane.showConfirmDialog(null,
							"The recommendation has been downloaded and saved to \n" + 
							stored_man_file.getAbsolutePath(),
							"File Download", JOptionPane.DEFAULT_OPTION);
				} catch(IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
