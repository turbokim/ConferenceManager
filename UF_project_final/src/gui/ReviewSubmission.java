package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Observable;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import model.Conference;
import model.Manuscript;
import model.Review;
import model.User;
import model.XMLHelper;

import org.w3c.dom.Document;

/**
 * * A ReviewSubmission object that shows a pop up frame that takes a new review
 * input and puts it in the appropriate directory and adds the new data to the
 * xml file.
 * 
 * @author Steve Hipolito
 * @author Daniel Beraun
 *  December 2, 2012
 */
public class ReviewSubmission extends Observable {

	private JFrame frame = new JFrame();
	private final JTextField my_file_input = new JTextField(30);
	private Manuscript my_manuscript;
	private Review my_review;
	private Conference my_conference;
	private User my_user;

	private final JFileChooser fileChooser = new JFileChooser();

	public ReviewSubmission(Conference the_conference,
			Manuscript the_manuscript, User the_user) {
		my_conference = the_conference;
		my_manuscript = the_manuscript;
		my_user = the_user;
	}

	/** Creates/shows the main pop up Frame */
	public void show() {
		frame = new JFrame();
		frame.setLayout(new BorderLayout());
		frame.setTitle("SUBMIT REVIEW - " + my_manuscript.getTitle());

		final JPanel paper_form_panel = new JPanel(new GridLayout(3, 1));
		final JPanel paper_form_file = new JPanel(new FlowLayout(
				FlowLayout.LEFT));
		final JPanel paper_form_title = new JPanel(new FlowLayout(
				FlowLayout.LEFT));

		my_file_input.setEditable(false);

		paper_form_file.add(new JLabel("     File: "));
		paper_form_file.add(my_file_input);
		paper_form_file.add(createButton("Browse"));

		paper_form_panel.add(new JLabel("   Submit a review to "
				+ my_manuscript.getTitle()));
		paper_form_panel.add(paper_form_file);
		paper_form_panel.add(paper_form_title);

		final JPanel paper_form_upload = new JPanel(new FlowLayout(
				FlowLayout.RIGHT));
		paper_form_upload.add(createButton("Upload to Manuscript"));
		paper_form_upload.add(createButton("Cancel"));

		frame.getContentPane().add(paper_form_panel, BorderLayout.CENTER);
		frame.getContentPane().add(paper_form_upload, BorderLayout.SOUTH);

		frame.pack();
		frame.setResizable(false);
		frame.setVisible(true);
	}

	/** Creates a new JButton with the_string as the name. */
	private JButton createButton(final String the_string) {
		final JButton button = new JButton(the_string);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent the_event) {
				if (the_string.equalsIgnoreCase("Browse")) {
					int retVal = fileChooser.showOpenDialog(null);
					if (retVal == JFileChooser.APPROVE_OPTION
							&& fileChooser.getSelectedFile() != null) {
						my_file_input.setText(fileChooser.getSelectedFile()
								.getPath());
					}
				} else if (the_string.equalsIgnoreCase("Upload to manuscript")) {
					uploadToManuscript();
				} else if (the_string.equalsIgnoreCase("Cancel")) {
					frame.dispose();
				}
			}
		});
		return button;
	}

	/** Helper method to attach to manuscript */
	private void uploadToManuscript() {
		if (my_file_input.getText().isEmpty()) {
			JOptionPane.showConfirmDialog(null,
					"Choose a file to upload, please!", "Upload to Manuscript",
					JOptionPane.DEFAULT_OPTION);
		} else {
			addReviewToManuscript();
			frame.dispose();
			final int ok = JOptionPane.showConfirmDialog(null,
					"Upload Successful!", "Upload to Manuscript",
					JOptionPane.DEFAULT_OPTION);
			if (ok == JOptionPane.OK_OPTION) {
				setChanged();
				notifyObservers("uploadedMan");
			}
		}
	}

	/**
	 * It puts the uploaded review in a folder in the system and adds it to the
	 * manuscript object.
	 */
	private void addReviewToManuscript() {
		final String title = my_manuscript.getTitle();
		final File input_file = new File(my_file_input.getText());

		final String revFolder_dir = "data" + File.separatorChar
				+ "Conferences" + File.separatorChar
				+ my_conference.getConferenceName() + File.separatorChar
				+ "Manuscripts" + File.separatorChar + title
				+ File.separatorChar + "Reviews" + File.separatorChar;

		// copy file and put in review folder
		InputStream inStream = null;
		OutputStream outStream = null;
		try {
			File stored_file = new File(revFolder_dir + File.separatorChar
					+ input_file.getName());
			inStream = new FileInputStream(input_file);
			outStream = new FileOutputStream(stored_file);
			byte[] buffer = new byte[1024];
			int length;
			while ((length = inStream.read(buffer)) > 0) {
				outStream.write(buffer, 0, length);
			}
			inStream.close();
			outStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		List<Review> list = my_manuscript.getReviews();
		// adds the path to the object.
		for (Review r : list) {
			if (my_user.getUniqueID().equals(r.get_reviewer().getUniqueID())) {
				r.setReviewFile(new File(revFolder_dir + File.separatorChar
						+ input_file.getName()));
			}
		}

		// updates the XML file- Kim
		String reviewsXMLFileLoc = "data" + File.separatorChar + "Conferences"
				+ File.separatorChar + my_conference.getConferenceName()
				+ File.separatorChar + "Manuscripts" + File.separatorChar
				+ title + File.separatorChar;
		Document reviewsXML = XMLHelper.getDoc(new File(reviewsXMLFileLoc
				+ "reviews.xml"));

		XMLHelper.addReviewDir(reviewsXML, revFolder_dir + File.separatorChar
				+ input_file.getName(), my_user.getUniqueID(),
				reviewsXMLFileLoc + "reviews.xml");
	}
}
