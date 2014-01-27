package gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import model.Conference;
import model.User;

/**
 * The User Panel that is inherited from by the Roles panels. This will have a
 * my_navigation_panel panel, and a my_main_panel, which all Roles panels will
 * have as well.
 * 
 * @author Steve Hipolito
 *  December 2, 2012
 */
@SuppressWarnings("serial")
public class UserPanel extends JPanel {
	public static final int MAX = 4;
	
	private JPanel my_navigation_panel;
	private JPanel my_main_panel;

	private Conference my_current_conference;
	private User my_current_user;
	
	private JPanel my_2_buttons_panel;
	private JPanel west_panel = new JPanel(new BorderLayout());
	
	protected final Border my_border = BorderFactory.createCompoundBorder(
			BorderFactory.createRaisedBevelBorder(),
			BorderFactory.createLoweredBevelBorder());

	/** Constructs a new UserPanel. */
	public UserPanel(final JButton the_subMan_button, 
			final JButton the_confMaker_button,
			final Conference the_conference,
			final User the_user) {
		
		my_current_conference = the_conference;
		my_current_user = the_user;
		
		setLayout(new BorderLayout());
		my_navigation_panel = new JPanel();
		my_main_panel = new JPanel(new BorderLayout());
		my_navigation_panel.setLayout(new BoxLayout(my_navigation_panel, BoxLayout.PAGE_AXIS));
		my_navigation_panel.setBorder(my_border);
		my_main_panel.setBorder(my_border);
		
		my_2_buttons_panel = new JPanel();
		my_2_buttons_panel.setLayout(new BoxLayout(my_2_buttons_panel,
				BoxLayout.PAGE_AXIS));
		my_2_buttons_panel.add(the_subMan_button);
		my_2_buttons_panel.add(the_confMaker_button);

		my_navigation_panel.add(new JLabel("Navigation"));
		my_navigation_panel.add(new JLabel(" "));
		my_navigation_panel.add(my_2_buttons_panel);
		
		west_panel.add(my_navigation_panel, BorderLayout.CENTER);
		
		add(west_panel, BorderLayout.WEST);
		add(my_main_panel);
	}
	
	public void makeConfDeadlineList(final Map<String, List<String>> conference_map) {
		final JPanel conf_deadlines = new JPanel();
		conf_deadlines.setLayout(new BoxLayout(conf_deadlines, BoxLayout.PAGE_AXIS));
		conf_deadlines.setBorder(my_border);
		final List<String> values = conference_map.get(my_current_conference.getConferenceName());
		conf_deadlines.add(new JLabel(" "));
		conf_deadlines.add(new JLabel("  Conferences Deadlines:"));
		conf_deadlines.add(new JLabel("  ===================="));
		conf_deadlines.add(new JLabel("  "+values.get(0)+" - Submissions"));
		conf_deadlines.add(new JLabel("  "+values.get(1)+" - Reviews"));
		conf_deadlines.add(new JLabel("  "+values.get(2)+" - Recommendations "));
		conf_deadlines.add(new JLabel(" "));
		west_panel.add(conf_deadlines, BorderLayout.SOUTH);
	}
	
	/** adds a component (usually a button) to the navigation panel */
	public void addToNavigation(final Component the_component) {
		my_navigation_panel.add(the_component);
	}

	/** returns the main JPanel (the area next to the navigation) */
	public JPanel getMainPanel() {
		return my_main_panel;
	}
	
	public void setCurrentConference(final Conference the_coference) {
		my_current_conference = the_coference;
	}
	
	public void setCurrentUser(final User the_user) {
		my_current_user = the_user;
	}
	
	public Conference getCurrentConference() {
		return my_current_conference;
	}
	
	public User getCurrentUser() {
		return my_current_user;
	}
	
	public void setButtons(final JButton the_submitMan_button, 
			final JButton the_confMaker_button) {	
		my_2_buttons_panel.removeAll();
		my_2_buttons_panel.add(the_submitMan_button);
		my_2_buttons_panel.add(the_confMaker_button);
		my_2_buttons_panel.validate();
		my_2_buttons_panel.repaint();
	}
}
