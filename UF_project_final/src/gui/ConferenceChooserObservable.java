package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import model.Conference;

/**
 * Class that creates the conference chooser panel and reads all conferences
 * from data. This also creates a combo box with all conferences listed.
 * 
 * @author Steve Hipolito
 *  December 2, 2012
 */
public class ConferenceChooserObservable extends Observable {

	private JPanel my_confchooser_panel;
	private Border my_border;
	private String[] my_conferences;
	private Conference my_selected_conference;
	private String my_highlighted_conference;
	
	private JButton my_subMan_button;
	private JButton my_createConf_button;
	
	private Map<String, List<String>> conference_map;

	/** Constructs a new ConferenceChooserObservable. */
	public ConferenceChooserObservable(Map<String, List<String>> conferenceList, 
			final Border the_border) {
		conference_map = conferenceList;
		my_border = the_border;		
		
		my_subMan_button = createButton("Submit a Manuscript");
		my_createConf_button = createButton("Create a new Conference");
	}

	/** Returns the Conference Chooser panel. */
	public JPanel getConferenceChooserPanel() {
		readAllConferences();

		my_confchooser_panel = new JPanel(new GridBagLayout());
		my_confchooser_panel.setBorder(my_border);
		
		final JPanel my_confchooser_panel_inside = new JPanel(new BorderLayout(
				10, 10));
		final JPanel login_panel_inside_north = new JPanel(new BorderLayout());
		
		final JPanel conf_list = new JPanel();
		conf_list.setLayout(new BoxLayout(conf_list, BoxLayout.PAGE_AXIS));
		conf_list.setBorder(my_border);

		final Set<String> keys = conference_map.keySet();
		conf_list.add(new JLabel(" "));
		conf_list.add(new JLabel("  Conferences:"));
		conf_list.add(new JLabel("  ============"));
		for (String key : keys)
			conf_list.add(new JLabel("  "+key));
		conf_list.add(new JLabel(" "));

		final JPanel button_panel = new JPanel(new FlowLayout());
		button_panel.add(my_subMan_button);
		button_panel.add(my_createConf_button);
		
		login_panel_inside_north.add(conf_list, BorderLayout.NORTH);
		
		my_confchooser_panel_inside.add(login_panel_inside_north,
				BorderLayout.NORTH);
		my_confchooser_panel_inside.add(getAllConferencesComboBox(),
				BorderLayout.CENTER);
		my_confchooser_panel_inside.add(createButton("Go to Conference"), BorderLayout.EAST);
		my_confchooser_panel_inside.add(button_panel, BorderLayout.SOUTH);
		
		my_confchooser_panel.add(my_confchooser_panel_inside);
		return my_confchooser_panel;
	}

	/** Returns the selected Conference */
	public Conference getSelectedConference() {
		return my_selected_conference;
	}

	/** Returns all conferences */
	public String[] getConferences() {
		readAllConferences();
		return my_conferences;
	}
	
	public JButton getSubManButton() {
		return my_subMan_button;
	}
	
	public JButton getConfMakerButton() {
		return my_createConf_button;
	}

	/** Creates a new JButton with the_string as the name. */
	private JButton createButton(final String the_string) {
		final JButton button = new JButton(the_string);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent the_event) {
				setChanged();
				notifyObservers(the_string);
			}
		});
		return button;
	}

	/** Reads all the conferences from the loader and puts them into a String[] */
	private void readAllConferences() {
		final Set<String> keys = conference_map.keySet();
		my_conferences = new String[conference_map.size() + 1];
		my_conferences[0] = "Choose Conference";
		int i = 1;
		for (String key : keys) {
			my_conferences[i++] = key;
		}
	}

	/** Returns a combo box with all conferences */
	public JComboBox getAllConferencesComboBox() {
		readAllConferences();
		final JComboBox combobox = new JComboBox(my_conferences);
		combobox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent the_event) {
				JComboBox cb = (JComboBox) the_event.getSource();
				my_highlighted_conference = (String) cb.getSelectedItem();
			}
		});
		combobox.setSelectedIndex(0);
		return combobox;
	}
	
	public String getHighlighted() {
		return my_highlighted_conference;
	}
	
	public void setHighlighted(final String the_item) {
		my_highlighted_conference = the_item;
	}
	
	public void setSelectedConference(final Conference the_conference) {
		my_selected_conference = the_conference;
	}
}
