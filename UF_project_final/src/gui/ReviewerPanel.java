package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import model.Conference;
import model.Manuscript;
import model.Role;
import model.User;

/**
 * The Reviewer Panel that shows contents only for a Reviewer Role.
 * 
 * @author Daniel Beraun
 * @author Steve Hipolito
 *  December 2, 2012
 * 
 */
@SuppressWarnings("serial")
public class ReviewerPanel extends UserPanel implements Observer {
	// instance fields
	private Conference my_conf;
	private JPanel panel = getMainPanel();
	ReviewSubmission my_review_submission;
	private List<Manuscript> my_manuscripts;
	private JList jlManuscripts;
	private JLabel deadline_label;
	private int index;
	private JScrollPane sp1;
	private String reviews_deadline;

	/** Constructs a new ReviewerPanel. */
	public ReviewerPanel(final JButton the_subitMan_button,
			final JButton the_confMaker_button,
			final Conference the_conference, final User the_user) {
		super(the_subitMan_button, the_confMaker_button, the_conference,
				the_user);

		initializeComponents();
	}

	public void initializeJList() {
		String[] titles = getThisReviewerManuscripts();
		jlManuscripts = new JList(titles);
		// register listeners for JList of manuscripts
		jlManuscripts.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jlManuscripts.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				index = jlManuscripts.getSelectedIndex();

			}
		});
		sp1 = new JScrollPane(jlManuscripts);
		panel.add(sp1, BorderLayout.CENTER);
	}

	public void initializeComponents() {
		if (getCurrentConference() != null) {
			panel.setLayout(new BorderLayout());
			JPanel south_panel = new JPanel(new FlowLayout());
			JPanel center_panel = new JPanel(new GridLayout(20, 1));
			JLabel lb_deadlines = new JLabel("REVIEW SUBMISSION DEADLINE");

			reviews_deadline = getCurrentConference().getConferenceDeadline()
					.get(1);
			deadline_label = new JLabel(reviews_deadline);
			center_panel.add(lb_deadlines);
			center_panel.add(deadline_label);
			

			south_panel.add(createButton("Submit Review"), BorderLayout.SOUTH);

			panel.add(south_panel, BorderLayout.SOUTH);
			panel.add(new JLabel("MY MANUSCRIPTS TO REVIEW"),
					BorderLayout.NORTH);
			panel.add(center_panel, BorderLayout.EAST);
			initializeJList();
		}
	}

	/** Called when the user clicks on the "Submit a Review" */
	private void submitReview() {
		if (!jlManuscripts.isSelectionEmpty()) {
			Manuscript selected = my_manuscripts.get(index);
			my_review_submission = new ReviewSubmission(getCurrentConference(),
					selected, getCurrentUser());
			my_review_submission.addObserver(this);
			my_review_submission.show();
		} else {
			JOptionPane.showConfirmDialog(null,
					"Select a manuscript to review!", "Upload to Manuscript",
					JOptionPane.DEFAULT_OPTION);
		}
	}

	private String[] getThisReviewerManuscripts() {
		my_conf = getCurrentConference();
		String[] titles = { "null", " " };
		if (my_conf != null) {
			Role role = getCurrentUser().getCurrentRole();
			if (role != null && role == Role.REVIEWER) {
				my_manuscripts = my_conf.getManuscriptList(getCurrentUser());
				if (my_manuscripts != null) {
					titles = new String[my_manuscripts.size()];
					for (int i = 0; i < my_manuscripts.size(); i++) {
						titles[i] = my_manuscripts.get(i).getTitle();
					}
				}
			}
		}
		return titles;
	}

	/** Creates a new JButton with the_string as the name. */
	private JButton createButton(final String the_string) {
		final JButton button = new JButton(the_string);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent the_event) {
				activate(the_string);
			}
		});
		return button;
	}

	/** Helper method that activates the given the_action. */
	private void activate(String the_action) {
		if (the_action.equals("Submit Review")) {
			submitReview();
		}
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub

	}

}
