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
import model.User;
import model.XMLHelper;

import org.w3c.dom.Document;

/** A PaperSubmission object that shows a pop up frame
 * that takes a new manuscript input and puts it in the
 * appropriate directory and adds the new data to the
 * xml file.
 * 
 * @author Steve Hipolito
 *  December 2, 2012
 */
public class PaperSubmission extends Observable {

	private JFrame my_frame;
	private final JTextField my_file_input = new JTextField(30);
	private final JTextField my_paper_title_input = new JTextField(15);

	private Manuscript my_manuscript;
	private Conference my_conference;
	private User my_user;
	private Document my_manuscriptsXML;

	private final JFileChooser fileChooser = new JFileChooser();

	public PaperSubmission(final Conference the_conference, 
			final User the_user) {
		my_manuscript = null;
		my_conference = the_conference;
		my_user = the_user;
		my_manuscriptsXML = 
				XMLHelper.getDoc(new File("data" + File.separatorChar + 
						"Conferences" + File.separatorChar + 
						my_conference.getConferenceName() + 
						File.separatorChar + "Manuscripts" + 
						File.separatorChar + "Manuscripts.xml"));
	}

	/** Creates/shows the main pop up Frame */
	public void show() {
		my_frame = new JFrame();
		my_frame.setLayout(new BorderLayout());
		my_frame.setTitle("Submit Manuscript - " + my_conference.getConferenceName());

		final JPanel paper_form_panel = new JPanel(new GridLayout(3, 1));
		final JPanel paper_form_file = new JPanel(new FlowLayout(FlowLayout.LEFT));
		final JPanel paper_form_title = new JPanel(new FlowLayout(FlowLayout.LEFT));

		my_file_input.setEditable(false);

		paper_form_file.add(new JLabel("     File: "));
		paper_form_file.add(my_file_input);
		paper_form_file.add(createButton("Browse"));

		paper_form_title.add(new JLabel("     Manuscript Title: "));
		paper_form_title.add(my_paper_title_input);

		paper_form_panel.add(new JLabel("   Submit a new manuscript to " + my_conference.getConferenceName()));
		paper_form_panel.add(paper_form_file);
		paper_form_panel.add(paper_form_title);

		final JPanel paper_form_upload = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		paper_form_upload.add(createButton("Upload to Conference"));
		paper_form_upload.add(createButton("Cancel"));

		my_frame.getContentPane().add(paper_form_panel, BorderLayout.CENTER);
		my_frame.getContentPane().add(paper_form_upload, BorderLayout.SOUTH);

		my_frame.pack();
		my_frame.setResizable(false);
		my_frame.setVisible(true);
	}

	public void setConference(final Conference the_conference) {
		my_conference = the_conference;
		my_manuscriptsXML = 
				XMLHelper.getDoc(new File("data" + File.separatorChar + 
						"Conferences" + File.separatorChar + 
						my_conference.getConferenceName() + 
						File.separatorChar + "Manuscripts" + 
						File.separatorChar + "Manuscripts.xml"));
	}

	public void setUser(final User the_user) {
		my_user = the_user;
	}
	
	public void setTitleInputText(final String the_text) {
		my_paper_title_input.setText(the_text);
	}

	/** Creates a new JButton with the_string as the name. */
	private JButton createButton(final String the_string){
		final JButton button = new JButton(the_string);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent the_event){
				if (the_string.equalsIgnoreCase("Browse")) {
					int retVal = fileChooser.showOpenDialog(null);
					if (retVal == JFileChooser.APPROVE_OPTION && fileChooser.getSelectedFile() != null) {
						my_file_input.setText(fileChooser.getSelectedFile().getPath());
					}
				} else if (the_string.equalsIgnoreCase("Upload to Conference")) {
					uploadToConference();   
				} else if (the_string.equalsIgnoreCase("Cancel")) {
					my_frame.dispose();
				}
			}
		});
		return button;
	}

	/** Helper method to upload to the conference */
	private void uploadToConference() {
		if (my_file_input.getText().isEmpty() ||
				my_paper_title_input.getText().trim().isEmpty()) {
			JOptionPane.showConfirmDialog(null,
					"Fill in all fields!",
					"Upload to Conference", JOptionPane.DEFAULT_OPTION);
		} else {
			addManuscriptToConference();
			my_frame.dispose();
			final int ok = JOptionPane.showConfirmDialog(null,
					"Upload Successful!",
					"Upload to Conference", JOptionPane.DEFAULT_OPTION);
			if (ok == JOptionPane.OK_OPTION) {
				setChanged();
				notifyObservers("uploadedMan");
			}
		}
	}

	/** Adds a manuscript folder in the conference, along with the 
	 * uploaded manuscript file, a blank reviews.xml, and an empty 
	 * reviews folder inside. Creates a new Manuscript object and 
	 * adds it to the conference. Appends a new manuscript element
	 * to the Manuscsripts.xml of the Conference. */
	private void addManuscriptToConference() {              
		final String title = my_paper_title_input.getText();
		final File input_file = new File(my_file_input.getText());

		final String manFolder_dir = "data" + File.separatorChar +
				"Conferences" + File.separatorChar +
				my_conference.getConferenceName() + File.separatorChar +
				"Manuscripts" + File.separatorChar + title.trim();
		final File manfolder = new File(manFolder_dir);
		manfolder.mkdir(); //make the manuscript folder

		//copy file and put in created folder
		InputStream inStream = null;
		OutputStream outStream = null;
		File stored_man_file = null;
		try {
			stored_man_file = new File(manFolder_dir +
					File.separatorChar + input_file.getName());
			inStream = new FileInputStream(input_file);
			outStream = new FileOutputStream(stored_man_file);
			byte[] buffer = new byte[1024];
			int length;
			while ((length = inStream.read(buffer)) > 0){
				outStream.write(buffer, 0, length);
			}
			inStream.close();
			outStream.close();
		} catch(IOException e) {
			e.printStackTrace();
		}

		final String reviews_folder_directory = manFolder_dir + 
				File.separatorChar + "Reviews";
		final File reviews_folder = new File(reviews_folder_directory);
		reviews_folder.mkdir(); //make the Reviews folder

		//get the template file for reviews.xml
		final String revTempl_dir = "data" + File.separatorChar + "templates" + 
				File.separatorChar + "reviewsTemplate.xml";
		File reviewsTemplateXML = new File(revTempl_dir);

		//Copy reviewsTemplate.xml and put in created 
		//	Manuscripts folder as reviews.xml
		inStream = null;
		outStream = null;
		try {
			File stored_file = new File(manFolder_dir + File.separatorChar + 
					"reviews.xml");
			inStream = new FileInputStream(reviewsTemplateXML);
			outStream = new FileOutputStream(stored_file);
			byte[] buffer = new byte[1024];
			int length;
			while ((length = inStream.read(buffer)) > 0){
				outStream.write(buffer, 0, length);
			}
			inStream.close();
			outStream.close();
		} catch(IOException e) {
			e.printStackTrace();
		}

		//create a new Manuscript object to add to the conference
		my_manuscript = new Manuscript(my_user, stored_man_file, title);
		my_conference.addManuscript(my_manuscript);	

		//append the new manuscript to Manuscripts.xml
		final String xml_dir = "data" + File.separatorChar +
				"Conferences" + File.separatorChar +
				my_conference.getConferenceName() + File.separatorChar +
				"Manuscripts" + File.separatorChar + "Manuscripts.xml";
	
		//updates XML
		XMLHelper.updateManuscriptsXML(xml_dir, manFolder_dir + 
				File.separatorChar + input_file.getName(), my_manuscriptsXML, my_manuscript);
	}
}

