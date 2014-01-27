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
 * that enables the user to edit a manuscript that has
 * been submitted already. The data, the conference, 
 * and the Manuscripts.xml for the conference will be
 * updated reflecting the changes.
 * 
 * @author Steve Hipolito
 *  December 2, 2012
 */
public class ManuscriptEditor extends Observable {

	private JFrame my_frame;
	private final JTextField my_paper_title_input = new JTextField(15);

	private Manuscript my_manuscript;
	private Conference my_conference;
	private User my_user;
	private PaperSubmission my_paper_sub;
	private Document my_manuscriptsXML;

	public ManuscriptEditor(final Conference the_conference, final User the_user, 
			final PaperSubmission the_paper_sub, final Manuscript the_man) {
		my_conference = the_conference;
		my_user = the_user;
		my_manuscript = the_man;
		my_paper_sub = the_paper_sub;
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
		my_frame.setTitle("Edit Manuscript - " + my_manuscript.getTitle());

		final JPanel paper_form_panel = new JPanel(new GridLayout(3, 1));
		final JPanel paper_form_file = new JPanel(new FlowLayout(FlowLayout.LEFT));
		final JPanel paper_form_title = new JPanel(new FlowLayout(FlowLayout.LEFT));

		paper_form_file.add(new JLabel("   "));
		paper_form_file.add(createButton("Unsubmit and Resubmit this Manuscript"));

		paper_form_title.add(new JLabel("     Manuscript Title: "));
		my_paper_title_input.setText(my_manuscript.getTitle());
		paper_form_title.add(my_paper_title_input);

		paper_form_panel.add(new JLabel("   Edit the Manuscript  '" + 
				my_manuscript.getTitle() + "'  submitted to  " + 
				my_conference.getConferenceName()));
		paper_form_panel.add(paper_form_file);
		paper_form_panel.add(paper_form_title);

		final JPanel paper_form_bottom = new JPanel(new BorderLayout());
		final JPanel paper_form_bottom_right = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		final JPanel paper_form_bottom_left = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		
		paper_form_bottom_right.add(createButton("OK"));
		paper_form_bottom_right.add(createButton("Cancel"));
		paper_form_bottom_left.add(createButton("Unsubmit"));
		
		paper_form_bottom.add(paper_form_bottom_right, BorderLayout.EAST);
		paper_form_bottom.add(paper_form_bottom_left, BorderLayout.WEST);

		my_frame.getContentPane().add(paper_form_panel, BorderLayout.CENTER);
		my_frame.getContentPane().add(paper_form_bottom, BorderLayout.SOUTH);

		my_frame.pack();
		my_frame.setResizable(false);
		my_frame.setVisible(true);
	}

	/** Creates a new JButton with the_string as the name. */
	private JButton createButton(final String the_string){
		final JButton button = new JButton(the_string);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent the_event){
				if (the_string.equalsIgnoreCase("Unsubmit and Resubmit this Manuscript")) {
					unsubmit(my_manuscript, false, true);
				} else if (the_string.equalsIgnoreCase("OK")) {
					renameAndOrReplace();
				} else if (the_string.equalsIgnoreCase("Cancel")) {
					my_frame.dispose();
				} else if (the_string.equalsIgnoreCase("Unsubmit")) {
					unsubmit(my_manuscript, false, false);
				}
			}
		});
		return button;
	}

	private void renameAndOrReplace() {
		if (my_paper_title_input.getText().trim().isEmpty()) {
			JOptionPane.showConfirmDialog(null,
					"Fill in all fields!",
					"Edit Manuscript", JOptionPane.DEFAULT_OPTION);
		} else {
			renameFolderAndReplaceFile();
			final int ok = JOptionPane.showConfirmDialog(null,
					"Edit Manuscript Successful",
					"Edit Manuscript", JOptionPane.DEFAULT_OPTION);
			if (ok == JOptionPane.OK_OPTION) {
				my_frame.dispose();
				setChanged();
				notifyObservers();
			}
		}
	}

	private void renameFolderAndReplaceFile() {
		final String input_title = my_paper_title_input.getText().trim();
		String newManFolder_dir = "data" + File.separatorChar +
				"Conferences" + File.separatorChar +
				my_conference.getConferenceName() + File.separatorChar +
				"Manuscripts" + File.separatorChar + input_title;
		
		final boolean wasRenamed = !input_title.equalsIgnoreCase(my_manuscript.getTitle());
		
		if (wasRenamed) {
			//add a new manuscript to the system with the new name
			addNewManuscriptToSystem(input_title, newManFolder_dir, my_manuscript.getManuscriptFile(), false);
			//delete the old manuscript with the OLD name from the system
			unsubmit(my_manuscript, true, false); //will delete this manuscript from system
		}
	}

	public void unsubmit(final Manuscript the_man, boolean forEditing, boolean resubmitting) {
		int clicked = JOptionPane.YES_OPTION;
		if (!forEditing) {
			clicked = JOptionPane.showConfirmDialog(null,
					"Are you sure you want to unsubmit  '" + 
							the_man.getTitle() + "' ?",
							"Unsubmit Manuscript", JOptionPane.YES_NO_OPTION);
		}
		if (clicked == JOptionPane.YES_OPTION) {
			//delete the manuscript folder and everything in it
			deleteFolder(the_man.getManuscriptFile().getParentFile());
			//remove the manuscript object from the conference
			my_conference.removeManuscript(the_man);
			//remove the manuscript element from the XML
			File XML_dir = new File("data" + File.separatorChar + 
					"Conferences" + File.separatorChar + 
					my_conference.getConferenceName() + 
					File.separatorChar + "Manuscripts" + 
					File.separatorChar + "Manuscripts.xml");
			Document manuscriptsXML = XMLHelper.getDoc(XML_dir);
			XMLHelper.removeManuscript(the_man.getTitle(), manuscriptsXML, XML_dir.getPath());
			if(!forEditing) {
				int ok = JOptionPane.showConfirmDialog(null,
						"The Manuscript  '"+the_man.getTitle()+
						"'  was removed from this Conference.",
						"Unsubmit Manuscript", JOptionPane.DEFAULT_OPTION);
				if (ok == JOptionPane.OK_OPTION) {
					my_frame.dispose();
					setChanged();
					notifyObservers();
				}
				//resubmitting
				if (resubmitting) {
					my_paper_sub.setTitleInputText(my_manuscript.getTitle());
					my_paper_sub.show();
				}
			}
		}
	}
	
	/** Recursive method that will delete everything inside 
	 * the given folder/file the_folder, including the_folder. */
	private boolean deleteFolder(final File the_folder) {
		if (the_folder.isDirectory()) {
			String[] children = the_folder.list();
			for (int i=0; i < children.length; i++) {
				boolean success = deleteFolder(new File(the_folder, children[i]));
				if (!success)
					return false;
			}
		}
		//The folder is now empty so delete it
		return the_folder.delete();
	}

	private void addNewManuscriptToSystem(String title, String manFolder_dir, File input_file, boolean sameNameNewFile) {
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
		final Manuscript newMan = new Manuscript(my_user, stored_man_file, title);
		my_conference.addManuscript(newMan);	

		//append the new manuscript to Manuscripts.xml
		final String xml_dir = "data" + File.separatorChar +
				"Conferences" + File.separatorChar +
				my_conference.getConferenceName() + File.separatorChar +
				"Manuscripts" + File.separatorChar + "Manuscripts.xml";
	
		//updates XML
		if (!sameNameNewFile) {
			XMLHelper.updateManuscriptsXML(xml_dir, manFolder_dir + 
					File.separatorChar + input_file.getName(), my_manuscriptsXML, newMan);
		}
	}
}

