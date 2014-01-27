package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;

import model.Conference;
import model.Decision;
import model.Manuscript;
import model.Role;
import model.User;

/**
 * The Author Panel that shows contents only for an Author Role.
 *
 * @author Steve Hipolito
 * December 2, 2012
 */
@SuppressWarnings("serial")
public class AuthorPanel extends UserPanel implements Observer {

	private List<Manuscript> man_list;
	private boolean ready;
	private JPanel my_table_panel;

	/** Constructs a new AuthorPanel. */
	public AuthorPanel(final JButton the_subitMan_button, 
			final JButton the_confMaker_button, 
			final Conference the_conference, 
			final User the_user) {		
		super(the_subitMan_button, the_confMaker_button, the_conference, the_user);

		initializeComponents();
	}

	private void initializeComponents() {
		ready = false;
		my_table_panel = new JPanel(new GridLayout(5, 2));

		getListAndNullCheck();

		final JPanel author_main_panel = new JPanel();
		author_main_panel.setLayout(new BoxLayout(author_main_panel,
				BoxLayout.PAGE_AXIS));

		JPanel head = new JPanel(new BorderLayout());
		JLabel msp = new JLabel("   My Submitted Manuscripts");
		msp.setFont(new Font("Arial", Font.BOLD, 20));

		JLabel deadline = new JLabel();
		if (ready) { //man_list null check
			deadline.setText("   Submission Deadline: " + 
					getCurrentConference().getConferenceDeadline().get(0) + 
					"        ");
			deadline.setFont(new Font("Arial", Font.BOLD, 15));
			makeTable();
		}

		author_main_panel.add(new JLabel(" "));
		author_main_panel.add(msp);
		author_main_panel.add(new JLabel(" "));
		author_main_panel.add(my_table_panel);

		head.add(new JLabel(" "), BorderLayout.NORTH);
		head.add(msp, BorderLayout.CENTER);
		head.add(deadline, BorderLayout.EAST);

		getMainPanel().add(head, BorderLayout.NORTH);
		getMainPanel().add(author_main_panel, BorderLayout.CENTER);
	}

	private void getListAndNullCheck() {
		if (getCurrentConference() != null) {
			Role role = getCurrentUser().getCurrentRole();
			if (role != null && role == Role.AUTHOR) {
				man_list = getCurrentConference().getManuscriptList(getCurrentUser());
				ready = true;			
			}	
		}
	}

	private void makeTable() {
		for (int i = 0; i < man_list.size(); i ++) {
			String man_name = man_list.get(i).getTitle();
			JLabel label = new JLabel("        " + man_name);
			label.setFont(new Font("Arial", Font.BOLD, 15));
			JPanel manPanel = new JPanel(new GridLayout(1, 5));
			Border border = BorderFactory.createMatteBorder(3, 0, 3, 0, Color.BLACK);
			if (man_list.size() > 1) {
				if (i == 0) { //top
					border = BorderFactory.createMatteBorder(3, 0, 0, 0, Color.BLACK);
				} else if (i == man_list.size() - 1) //bottom
					border = BorderFactory.createMatteBorder(1, 0, 3, 0, Color.BLACK);
				else { //middle
					border = BorderFactory.createMatteBorder(1, 0, 0, 0, Color.BLACK);
				}
			}
			manPanel.setBorder(border);
			manPanel.add(label);
			manPanel.add(createButtonWithPanel("Download", man_list.get(i)));
			manPanel.add(createButtonWithPanel("Edit", man_list.get(i)));
			manPanel.add(createButtonWithPanel("View Reviews", man_list.get(i)));
			manPanel.add(new JLabel("Status: " + man_list.get(i).get_Decision()));
			my_table_panel.add(manPanel);
		}
	}

	private JPanel createButtonWithPanel(final String the_string, final Manuscript the_man) {		
		final JPanel panel = new JPanel(new GridBagLayout());
		final JButton button = new JButton(the_string);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent the_event) {
				if (the_string.equalsIgnoreCase("Download")) {
					FileDownloader.downloadManuscript(the_man);
				} else if (the_string.equalsIgnoreCase("Edit")) {
					manuscriptEditor(the_man);
				} else if (the_string.equalsIgnoreCase("View Reviews")) {
					if (the_man.get_Decision() == Decision.UNDECIDED){
						JOptionPane.showConfirmDialog(null,
								"This manuscript has yet to be decided upon " +
								"by the Program Chair\n whether or not it will be " +
								"accepted into the conference",
								"View Reviews", JOptionPane.DEFAULT_OPTION);
					} else {
						ReviewerViewer.viewReviewers(the_man);
					}
				}
			}
		});
		panel.add(button);
		return panel;
	}

	private void manuscriptEditor(final Manuscript the_man) {
		PaperSubmission ps = new PaperSubmission(getCurrentConference(), getCurrentUser());
		ps.addObserver(this);
		ManuscriptEditor editor = new ManuscriptEditor( getCurrentConference(), getCurrentUser(), ps, the_man);
		editor.addObserver(this);
		editor.show();
	}

	public void update(Observable arg0, Object arg1) {
		refresh();
	}

	private void refresh() {
		getMainPanel().removeAll();
		initializeComponents();
		getMainPanel().repaint();
		getMainPanel().revalidate();
	}
}

