package gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import model.Conference;
import model.Loader;
import model.Role;
import model.User;

/**
 * Main GUI Frame for the MSEE application.
 * 
 * @author Steve Hipolito
 *  December 2, 2012
 */
@SuppressWarnings("serial")
public class MainFrame extends JFrame implements Observer {

	private static final int PAPER_MAX = 4;

	private static final int WIDTH = 1000;
	private static final int HEIGHT = 700;

	private final Border my_border = BorderFactory.createCompoundBorder(
			BorderFactory.createRaisedBevelBorder(),
			BorderFactory.createLoweredBevelBorder());

	private User my_current_user;
	private Conference my_current_conference;
	private Conference my_highlighted_conference;

	private JPanel my_title_panel;
	private JPanel my_main_panel;
	private JComboBox my_conf_chooser;

	private Loader my_loader;
	private LoginObservable my_login_obs;
	private ConferenceChooserObservable my_confchooser_obs;
	private PaperSubmission my_paper_sub;
	private ConferenceMaker my_conf_maker;
	private JButton my_submitMan_button;
	private JButton my_confMaker_button;
	private boolean conference_chosen = false;

	private UserPanel my_user_panel;
	private ProgramChairPanel my_progchair_panel;
	private SubProgramChairPanel my_subprogchair_panel;
	private ReviewerPanel my_reviewer_panel;
	private AuthorPanel my_author_panel;

	/** Constructs a new Main JFrame. */
	public MainFrame() {
		super("MSEE Conference Master - User Friendlies");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		try {
			my_loader = new Loader("data" + File.separatorChar);
		} catch (FileNotFoundException e) {
		}
		my_login_obs = new LoginObservable(my_loader, my_border);
		my_confchooser_obs = new ConferenceChooserObservable(
				my_loader.getConferenceList(), my_border);

		my_login_obs.addObserver(this);
		my_confchooser_obs.addObserver(this);

		my_submitMan_button = my_confchooser_obs.getSubManButton();
		my_confMaker_button = my_confchooser_obs.getConfMakerButton();
	}

	/** Starts the frame. */
	public void start() {
		setVisible(true);
		setSize(WIDTH, HEIGHT);
		setLayout(new BorderLayout());
		components();
		setLocationRelativeTo(null);
	}

	/** Creates the main components of the frame. */
	private void components() {
		my_title_panel = new JPanel(new BorderLayout());
		my_title_panel.setVisible(false);
		my_main_panel = new JPanel(new BorderLayout());

		add(my_title_panel, BorderLayout.NORTH);
		add(my_main_panel, BorderLayout.CENTER);
		mainScreen(my_login_obs.getLoginPanel()); // Show login screen.
	}

	/**
	 * Updates the current user and current conference, by getting the user from
	 * my_login_obs, and getting the conference from my_conference_obs.
	 */
	private void updateConfUser(final Role the_role) {
		my_current_user = my_login_obs.getLoggedInUser();
		my_current_conference = my_confchooser_obs.getSelectedConference();
		my_current_user.setCurrentRole(the_role);

		my_user_panel = new UserPanel(my_submitMan_button, my_confMaker_button,
				my_current_conference, my_current_user);
		my_progchair_panel = new ProgramChairPanel(my_submitMan_button,
				my_confMaker_button, my_current_conference, my_current_user,
				my_loader.getUsersXML(), my_loader.getUserMap(), my_loader.getconferenceManuscriptsXML());
		my_subprogchair_panel = new SubProgramChairPanel(my_submitMan_button,
				my_confMaker_button, my_current_conference, my_current_user,
				my_loader.getUsersXML(), my_loader.getUserMap());
		my_reviewer_panel = new ReviewerPanel(my_submitMan_button,
				my_confMaker_button, my_current_conference, my_current_user);
		my_author_panel = new AuthorPanel(my_submitMan_button,
				my_confMaker_button, my_current_conference, my_current_user);
	}

	public void userPanelHelper(UserPanel user_panel) {
		user_panel.setCurrentConference(my_current_conference);
		user_panel.setCurrentUser(my_current_user);
	}

	/** Updates the title area. */
	private void updateTitleArea() {
		my_title_panel.removeAll();
		titleEast();
		titleCenter();
		titleWest();
		my_title_panel.setVisible(true);
		my_title_panel.validate();
		my_title_panel.repaint();
	}

	/** Creates the main screen area */
	private void mainScreen(final Component the_component) {
		my_main_panel.removeAll();
		my_main_panel.add(the_component);
		my_main_panel.revalidate();
		my_main_panel.repaint();
	}

	/** The title east area. */
	private void titleEast() {
		final JPanel title_east_panel = new JPanel(new GridLayout(2, 2, 5, 5));

		// Get the list of roles for the current user for the current
		// conference.
		String[] my_current_user_roles = new String[1];
		if (conference_chosen) {
			List<Role> role = my_current_conference
					.getUserRoles(my_current_user);
			my_current_user_roles = new String[role.size() + 1];
			int i = 1;
			for (Role r : role) {
				my_current_user_roles[i] = r.toString().toLowerCase();
				i++;
			}
		}
		my_current_user_roles[0] = "Change Role";

		final JComboBox combobox = new JComboBox(my_current_user_roles);
		combobox.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent the_event) {
				JComboBox cb = (JComboBox) the_event.getSource();
				String the_role = (String) cb.getSelectedItem();
				if (!the_role.equalsIgnoreCase("Change Role")) {
					roleChanger(Role.getRoleType(the_role));
				}
			}
		});
		combobox.setSelectedIndex(0);
		combobox.setEnabled(conference_chosen);

		final JButton goToConfButton = createButton("Go to Conference");
		goToConfButton.setEnabled(conference_chosen);
		my_conf_chooser = my_confchooser_obs.getAllConferencesComboBox();
		my_conf_chooser.setEnabled(conference_chosen);

		title_east_panel.add(my_conf_chooser);
		title_east_panel.add(goToConfButton);
		title_east_panel.add(combobox);
		title_east_panel.add(createButton("Logout"));
		title_east_panel.setBorder(my_border);

		my_title_panel.add(title_east_panel, BorderLayout.EAST);
	}

	/** The title center area. */
	private void titleCenter() {
		final JPanel title_center_panel = new JPanel(new GridLayout(2, 2));

		final JLabel current_user_label = new JLabel("Hello,  "
				+ my_current_user.getUserName() + "!");
		final JLabel current_conference_label = new JLabel("Conference:  none");
		final JLabel progChair_label = new JLabel("Program Chair:  none");
		if (conference_chosen) {
			current_conference_label.setText("Conference:  " + 
					my_current_conference.getConferenceName());
			progChair_label.setText("Program Chair:  " + 
					my_current_conference.getProgramChair().getUniqueID());
		}
		final JLabel current_user_role_label = new JLabel("Your Role:  "
				+ my_current_user.getCurrentRole());
		
		current_user_label.setHorizontalAlignment(SwingConstants.CENTER);
		current_conference_label.setHorizontalAlignment(SwingConstants.CENTER);
		current_user_role_label.setHorizontalAlignment(SwingConstants.CENTER);
		progChair_label.setHorizontalAlignment(SwingConstants.CENTER);

		title_center_panel.add(current_user_label);
		title_center_panel.add(current_conference_label);
		title_center_panel.add(current_user_role_label);
		title_center_panel.add(progChair_label);
		title_center_panel.setBorder(my_border);

		my_title_panel.add(title_center_panel);
	}

	/** The title west area. */
	private void titleWest() {
		final JPanel title_west_panel = new JPanel(new BorderLayout());
		ImageIcon my_icon = new ImageIcon("logo5.jpg");
		final JLabel logo = new JLabel(my_icon);
		title_west_panel.add(logo);
		title_west_panel.setBorder(my_border);

		my_title_panel.add(title_west_panel, BorderLayout.WEST);
	}

	/** Creates a new JButton with the_string as the name. */
	private JButton createButton(final String the_string) {
		final JButton button = new JButton(the_string);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent the_event) {
				// What this button does when clicked.
				activate(the_string);
			}
		});
		return button;
	}

	/**
	 * Activates the given the_string. This is called mostly by the action
	 * listener of a button or an option pane.
	 */
	private void activate(final String the_string) {
		if (the_string.equalsIgnoreCase("Login")) {
			login();
		} else if (the_string.equalsIgnoreCase("Logout")) {
			logout();
		} else if (the_string.equalsIgnoreCase("Go to Conference")) {
			goToConference();
		} else if (the_string.equalsIgnoreCase("Exit")) {
			dispose(); // Closes this frame.
		} else if (the_string.equalsIgnoreCase("Submit a Manuscript")) {
			submitManuscript();
		} else if (the_string.equalsIgnoreCase("uploadedMan")) {
			uploadedManuscript();
		} else if (the_string.equalsIgnoreCase("Create a new Conference")) {
			createNewConference();
		} else if (the_string.equalsIgnoreCase("createdConf")) {
			newConferenceCreated();
		}
	}

	/** This is called when the user clicks the Login button */
	private void login() {
		roleChanger(Role.USER); // The person that logs in becomes a USER.
		// Show the conference chooser panel.
		mainScreen(my_confchooser_obs.getConferenceChooserPanel());
	}

	/** This is called when the user clicks the Logout button */
	private void logout() {
		my_title_panel.setVisible(false); // Hide the title area.
		mainScreen(my_login_obs.getLoginPanel()); // Show login screen.
		conference_chosen = false; // A conference is not chosen anymore.
	}

	/**
	 * This is called when the user clicks on the "Go to Conference" button,
	 * where the system will try to go to the selected
	 */
	private void goToConference() {
		String highlighted = my_confchooser_obs.getHighlighted();
		// Only try to go to a conference when the user don't pick the
		// first item from the combo box labeled "Choose Conference".
		if (!highlighted.equalsIgnoreCase("Choose Conference")) {
			final Conference conf = my_loader.getConference(highlighted);
			if (conf.containsUser(my_current_user)) {
				// If the user is part of the selected conference.
				my_confchooser_obs.setSelectedConference(conf);
				conference_chosen = true;
				// RoleChanger will update my_current_conference
				// and my_current_user first. User goes in the
				// conference as a USER (Role).
				roleChanger(Role.USER);
			} else {
				// The user is NOT part of the selected conference.
				JOptionPane.showConfirmDialog(
						null,
						"You are not in the conference "
								+ conf.getConferenceName() + ".\nTo be "
								+ "in this conference, submit a manuscript "
								+ "as an author.", "Go to Conference",
						JOptionPane.DEFAULT_OPTION);
			}
		}
	}

	/**
	 * Called when the user clicks on the "Submit a Manuscript" button.
	 */
	private void submitManuscript() {
		final String selected = my_confchooser_obs.getHighlighted();
		// The current_conference could be null (user have yet to
		// pick a conference to go into, after logging in),
		// so a temporary Conference conf is used.
		Conference conf;
		if (!conference_chosen
				&& !my_loader.getConferenceList().containsKey(selected)) {
			// A conference was not chosen yet, and the button was
			// clicked while the "Choose Conference" item was selected.
			conf = null;
		} else if (!conference_chosen) {
			// A conference was not chosen yet, so load the
			// conference with the same as the selected.
			conf = my_loader.getConference(selected);
		} else {
			// A conference already chosen, simply use
			// my_current_conference.
			conf = my_current_conference;
		}

		if (conf != null) {
			if (conf.getManuscriptCount(my_current_user) < PAPER_MAX) {
				// Only do a new paper submission if the user has less than
				// PAPER_MAX submissions.
				my_paper_sub = new PaperSubmission(conf, my_current_user);
				my_paper_sub.addObserver(this);
				my_paper_sub.show();
				// my_highlighted_conference will be the the new conference
				// that the my_current_conference will be set to if the user
				// uploads a manuscript and hits OK.
				my_highlighted_conference = conf;
			} else {
				JOptionPane.showConfirmDialog(null, "Max submissions of "
						+ PAPER_MAX + " reached!", "Submit a Manuscript",
						JOptionPane.DEFAULT_OPTION);
			}
		}
	}

	/**
	 * Only happens when a manuscript is uploaded and goes to author screen when
	 * OK is pressed.
	 */
	private void uploadedManuscript() {
		my_current_user.setCurrentRole(Role.AUTHOR);
		my_confchooser_obs.setSelectedConference(my_highlighted_conference);
		conference_chosen = true;
		roleChanger(Role.AUTHOR);
	}

	/**
	 * This is called when the user clicks on the "Create a new Conference"
	 * button.
	 */
	private void createNewConference() {
		my_conf_maker = new ConferenceMaker(my_current_user);
		my_conf_maker.addObserver(this);
		my_conf_maker.show();
	}

	private void newConferenceCreated() {
		my_loader.createConferenceList();
		String newConf = my_conf_maker.getCreatedConference()
				.getConferenceName();
		my_confchooser_obs.setHighlighted(newConf);
		goToConference();
	}

	/**
	 * Changes the screen depending on selected role. This is called everytime
	 * the conference or the role changes.
	 */
	private void roleChanger(final Role the_role) {
		// updateConfUser() is called to update my_current_user first
		// to change its role, and also needs to update
		// my_current_conference first before calling updateTitleArea();
		updateConfUser(the_role);
		switch (the_role) {
		case USER:
			setConfUserForAndDisplay(my_user_panel, the_role);
			break;
		case PROGRAM_CHAIR:
			setConfUserForAndDisplay(my_progchair_panel, the_role);
			break;
		case SUBPROGRAM_CHAIR:
			setConfUserForAndDisplay(my_subprogchair_panel, the_role);
			break;
		case REVIEWER:
			setConfUserForAndDisplay(my_reviewer_panel, the_role);
			break;
		case AUTHOR:
			setConfUserForAndDisplay(my_author_panel, the_role);
			break;
		}
		updateTitleArea(); // Update title area, because the role changed.
	}

	/**
	 * Sets the given UserPanel's conference and user, and displays that
	 * UserPanel to the main screen.
	 */
	private void setConfUserForAndDisplay(final UserPanel the_panel,
			final Role the_role) {
		the_panel.setButtons(my_submitMan_button, my_confMaker_button);
		if (conference_chosen)
			the_panel.makeConfDeadlineList(my_loader.getConferenceList());
		mainScreen(the_panel);
	}

	/**
	 * The update method for the Observer interface. Takes the given the_arg as
	 * a string, and calls activate(the_arg). The_arg given, most of the time,
	 * is the name of a button.
	 */
	public void update(final Observable the_obj, final Object the_arg) {
		if (the_arg instanceof String) {
			activate((String) the_arg);
		}
	}

	public static void main(String... args) {
		MainFrame mf = new MainFrame();
		mf.start();
	}
}
