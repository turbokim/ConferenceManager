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
import model.XMLHelper;

import org.w3c.dom.Document;

/** A Recommendation Submission object that shows a pop up frame
 * that takes a new recommendation input and puts it in the
 * appropriate directory and adds the new data to the
 * xml file.
 * 
 * @author Steve Hipolito
 *  December 2, 2012
 */
public class RecommendationSubmission extends Observable {

	private JFrame my_frame;
	private final JTextField my_file_input = new JTextField(30);

	private Manuscript my_manuscript;
	private Conference my_conference;
	private Document my_manuscriptsXML;

	private final JFileChooser fileChooser = new JFileChooser();

	public RecommendationSubmission(final Conference the_conference, final Manuscript the_man) {
		my_conference = the_conference;
		my_manuscript = the_man;
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
		my_frame.setTitle("Submit Recommendation - " + my_conference.getConferenceName());

		final JPanel paper_form_panel = new JPanel(new GridLayout(3, 1));
		final JPanel paper_form_file = new JPanel(new FlowLayout(FlowLayout.LEFT));

		my_file_input.setEditable(false);

		paper_form_file.add(new JLabel("     File: "));
		paper_form_file.add(my_file_input);
		paper_form_file.add(createButton("Browse"));

		paper_form_panel.add(new JLabel("   Submit a new Recommendation to " + my_manuscript.getTitle()));
		paper_form_panel.add(paper_form_file);

		final JPanel paper_form_upload = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		paper_form_upload.add(createButton("Upload to Manuscript"));
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
				} else if (the_string.equalsIgnoreCase("Upload to Manuscript")) {
					uploadToManuscript();   
				} else if (the_string.equalsIgnoreCase("Cancel")) {
					my_frame.dispose();
				}
			}
		});
		return button;
	}

	/** Helper method to upload to the manuscript */
	private void uploadToManuscript() {
		if (my_file_input.getText().isEmpty()) {
			JOptionPane.showConfirmDialog(null,
					"Fill in all fields!",
					"Upload to Manuscript", JOptionPane.DEFAULT_OPTION);
		} else {
			addManuscriptToConference();
			my_frame.dispose();
			final int ok = JOptionPane.showConfirmDialog(null,
					"Upload Successful!",
					"Upload to Manuscript", JOptionPane.DEFAULT_OPTION);
			if (ok == JOptionPane.OK_OPTION) {
				setChanged();
				notifyObservers();
			}
		}
	}

	/** Sets the file of the already created recommendation */
	private void addManuscriptToConference() {              
		final File input_file = new File(my_file_input.getText());

		final String manFolder_dir = "data" + File.separatorChar +
				"Conferences" + File.separatorChar +
				my_conference.getConferenceName() + File.separatorChar +
				"Manuscripts" + File.separatorChar + my_manuscript.getTitle();

		//copy file and put in manuscript folder
		InputStream inStream = null;
		OutputStream outStream = null;
		File stored_rec_file = null;
		try {
			stored_rec_file = new File(manFolder_dir +
					File.separatorChar + input_file.getName());
			inStream = new FileInputStream(input_file);
			outStream = new FileOutputStream(stored_rec_file);
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

		//set the manuscript's recommendation's file
		my_manuscript.set_Manuscript_doc(stored_rec_file);
		
		final String xml_dir = "data" + File.separatorChar +
				"Conferences" + File.separatorChar +
				my_conference.getConferenceName() + File.separatorChar +
				"Manuscripts" + File.separatorChar + "Manuscripts.xml";
	
		//updates XML
		XMLHelper.changeRecDirectory(my_manuscriptsXML, my_manuscript.getTitle(), stored_rec_file.getPath(), xml_dir);
	}
}

