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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Observable;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import model.Conference;
import model.User;
import model.XMLHelper;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/** 
 * @author Steve Hipolito
 *  December 2, 2012
 */
public class ConferenceMaker extends Observable {
	private static final String[] MONTHS_NAMES = {
				"Jan", "Feb", "Mar", "Apr",
				"May", "Jun", "Jul", "Aug",
				"Sep", "Oct", "Nov", "Dec" }; 
	
	private static final int MONTHS = 12;
	private static final int DAYS30 = 30;
	private static final int DAYS31 = 31;
	private static final int YEAR_START = 2010;
	private static final int YEAR_END = 2020;
	private static final int DEADLINES = 3;

	private JFrame my_frame;
	private final JTextField my_conf_name_input = new JTextField(20);
	private JPanel my_day_cb_panel;
	
	private Calendar cal = Calendar.getInstance();
	private int my_conf_date_month;
	private int my_conf_date_day;
	private int my_conf_date_year;
	
	private JLabel[] deadlines_labels = new JLabel[DEADLINES];
	private ArrayList<String> deadlines_strings = new ArrayList<String>();

	private User my_user;
	private Conference my_created_conf;

	public ConferenceMaker(final User the_user) {
		my_user = the_user;
		
		for (int i = 0; i < DEADLINES; i++) {
			deadlines_labels[i] = new JLabel();
			deadlines_strings.add("");
		}
	}
	
	/** Returns the created conf */
	public Conference getCreatedConference() {
		return my_created_conf;
	}

	/** Creates/shows the main pop up Frame */
	public void show() {
		my_frame = new JFrame();
		my_frame.setLayout(new BorderLayout());
		my_frame.setTitle("Create a new Conference");
		
		final JPanel conf_form_panel = new JPanel(new GridLayout(8, 1));
		
		final JPanel conf_form_name = new JPanel(new FlowLayout(FlowLayout.LEFT));
		final JPanel conf_form_date = new JPanel(new FlowLayout(FlowLayout.LEFT));
		final JPanel conf_form_deadline1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		final JPanel conf_form_deadline2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		final JPanel conf_form_deadline3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		my_day_cb_panel = new JPanel(new BorderLayout());
		final JPanel my_month_cb_panel = new JPanel(new BorderLayout());
		final JPanel my_year_cb_panel = new JPanel(new BorderLayout());
		
		conf_form_name.add(new JLabel("     Conference Name: "));
		conf_form_name.add(my_conf_name_input);
		
		my_day_cb_panel.add(mdyComboBox(0, DAYS31));
		my_month_cb_panel.add(mdyComboBox(0, MONTHS));
		my_year_cb_panel.add(mdyComboBox(YEAR_START, YEAR_END));
		
		conf_form_date.add(new JLabel("          Month"));
		conf_form_date.add(my_month_cb_panel);
		conf_form_date.add(new JLabel("  Day"));
		conf_form_date.add(my_day_cb_panel);
		conf_form_date.add(new JLabel("  Year"));
		conf_form_date.add(my_year_cb_panel);
		
		conf_form_deadline3.add(new JLabel("          Submissions  -  "));
		conf_form_deadline3.add(deadlines_labels[2]);
		conf_form_deadline2.add(new JLabel("          Reviews  -  "));
		conf_form_deadline2.add(deadlines_labels[1]);
		conf_form_deadline1.add(new JLabel("          Recommendations  - "));
		conf_form_deadline1.add(deadlines_labels[0]);

		conf_form_panel.add(new JLabel("   Create a new Conference:"));
		conf_form_panel.add(conf_form_name);
		conf_form_panel.add(new JLabel("       Conference Date:"));
		conf_form_panel.add(conf_form_date);
		conf_form_panel.add(new JLabel("       Deadlines:"));
		conf_form_panel.add(conf_form_deadline3);
		conf_form_panel.add(conf_form_deadline2);
		conf_form_panel.add(conf_form_deadline1);

		final JPanel paper_form_upload = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		paper_form_upload.add(createButton("Create Conference"));
		paper_form_upload.add(createButton("Cancel"));

		my_frame.getContentPane().add(conf_form_panel, BorderLayout.CENTER);
		my_frame.getContentPane().add(paper_form_upload, BorderLayout.SOUTH);

		my_frame.pack();
		my_frame.setResizable(false);
		my_frame.setVisible(true);
	}

	/** Creates a new JButton with the_string as the name. */
	private JButton createButton(final String the_string){
		final JButton button = new JButton(the_string);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent the_event){
				if (the_string.equalsIgnoreCase("Create Conference")) {
					createConference();   
				} else if (the_string.equalsIgnoreCase("Cancel")) {
					my_frame.dispose();
				}
			}
		});
		return button;
	}
	
	/** Called when user clicks on the "Create Conference" button. */
	private void createConference() {
		if (my_conf_name_input.getText().trim().isEmpty()) {
			JOptionPane.showConfirmDialog(null,
					"Fill in all fields!",
					"Create a new Conference", JOptionPane.DEFAULT_OPTION);
		} else {
			createConferenceObject();
			my_frame.dispose();
			final int ok = JOptionPane.showConfirmDialog(null,
					my_conf_name_input.getText() + " Created!\n" +
							"You are the Program Chair for this conference.",
					"Create a new Conference", JOptionPane.DEFAULT_OPTION);
			if (ok == JOptionPane.OK_OPTION) {
				setChanged();
				notifyObservers("createdConf");
			}
		} 
	}
	
	private JComboBox mdyComboBox(int start, final int numAmount) {
		String[] numbers = new String[numAmount-start];
		for (int i = (0+start); i < numAmount; i++) {
			numbers[i-start] = Integer.toString(i+1);
		}
		JComboBox combobox = new JComboBox(numbers);
		combobox.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent the_event){
				JComboBox cb = (JComboBox)the_event.getSource();
				final int selected_num = Integer.parseInt((String) cb.getSelectedItem());
				calculateDate(numAmount, selected_num);
			}
		});
		combobox.setSelectedIndex(0);
		return combobox;
	}
	
	/** Sets up the date to be used for creating the Conference object, 
	 * while making sure the days combo box shows the right amount of 
	 * days depending on the month selected. */
	private void calculateDate(int numAmount, int selected_num) {
		if (numAmount == MONTHS) {
			my_conf_date_month = selected_num;
			my_day_cb_panel.removeAll();
			JComboBox day_combobox;
			if ((selected_num < 8)) { //January - July
				if ((selected_num % 2 == 0)) { //Even Months < August
					day_combobox = mdyComboBox(0, DAYS30);
				} else { //Odd Months < August
					day_combobox = mdyComboBox(0, DAYS31);
				}
			} else { //August - December
				if ((selected_num % 2 == 0)) { //Even Months > July
					day_combobox = mdyComboBox(0, DAYS31);
				} else { //Odd Months > July
					day_combobox = mdyComboBox(0, DAYS30);
				}
			}
			my_day_cb_panel.add(day_combobox);
			my_day_cb_panel.validate();
			my_day_cb_panel.repaint();
		} else if (numAmount == DAYS30 || numAmount == DAYS31) {
			my_conf_date_day = selected_num;
		} else {
			my_conf_date_year = selected_num;
		}
		
		cal.set(my_conf_date_year, my_conf_date_month-1, my_conf_date_day);
		String date;
		for (int i = 0; i < DEADLINES; i++) {
			cal.add(Calendar.DAY_OF_MONTH, -5);
			date = MONTHS_NAMES[cal.get(Calendar.MONTH)] + " " + 
					cal.get(Calendar.DATE) + ", " + cal.get(Calendar.YEAR);
			deadlines_labels[i].setText(date);
			deadlines_strings.set(i, date);
		}
	}

	private void createConferenceObject() {
		my_created_conf = new Conference(my_conf_name_input.getText(), 
				my_user, deadlines_strings);
		//user is now program chair for the created conference
		//my_created_conf.addUser(my_user, Role.PROGRAM_CHAIR);

		String directory = "data" + File.separatorChar +
				"Conferences" + File.separatorChar +
				my_conf_name_input.getText();
		File folder = new File(directory);
		folder.mkdir(); //make the Conference folder
		
		String mansFolderDir = directory + File.separatorChar + "Manuscripts";
		File mansFolder = new File(mansFolderDir);
		mansFolder.mkdir(); //make the Manuscripts folder
		
		//get the template file for Manuscripts.xml
		directory = "data" + File.separatorChar + "templates" + 
				File.separatorChar + "ManuscriptsTemplate.xml";
		File manTemplateXML = new File(directory);
		
		//Copy ManuscriptsTemplate.xml and put in created 
		//	Manuscripts folder as Manuscripts.xml
		InputStream inStream = null;
		OutputStream outStream = null;
		try {
			File stored_file = new File(mansFolderDir + File.separatorChar + 
					"Manuscripts.xml");
			inStream = new FileInputStream(manTemplateXML);
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
		
		//append the new Conference to Conferences.xml
		directory = "data" + File.separatorChar + "Conferences.xml";
		updateConferencesXML(directory);
	}

	/** Method updates the my_ConferencesXML file with the
     *  newly created Conference. 
     *  @author Kim Howard 
     */
    private void updateConferencesXML(final String xml_dir) {
        final Document my_ConferencesXML = XMLHelper.getDoc(new File("data" +
                File.separatorChar + "Conferences.xml"));
       
        Node conferences = my_ConferencesXML.getFirstChild();
       
        Element conference = my_ConferencesXML.createElement("conference");   
        Element deadlines = my_ConferencesXML.createElement("deadlines");
       
        XMLHelper.appendElement(my_ConferencesXML, conference, "name", my_created_conf.getConferenceName());
        XMLHelper.appendElement(my_ConferencesXML, conference, "program_chairID", my_created_conf.getProgramChair().getUniqueID());
        XMLHelper.appendElement(my_ConferencesXML, deadlines, "submissions", deadlines_strings.get(2));
        XMLHelper.appendElement(my_ConferencesXML, deadlines, "reviews", deadlines_strings.get(1));
        XMLHelper.appendElement(my_ConferencesXML, deadlines, "recommendations", deadlines_strings.get(0));
       
        conference.appendChild(deadlines);
        conferences.appendChild(conference);
       
        XMLHelper.writeDoc(my_ConferencesXML, xml_dir);                  
    }
}

