package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import model.Conference;
import model.Manuscript;
import model.Review;
import model.Role;
import model.User;
import model.XMLHelper;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * The Program Chair Panel that shows contents only for a Program Chair Role.
 * 
 * @author Steve Hipolito, Kim Howard
 *  December 2, 2012
 */
@SuppressWarnings("serial")
public class SubProgramChairPanel extends UserPanel {
	public static final String DIR = "data" + File.separatorChar
			+ "Conferences" + File.separatorChar;

	private JList manuscriptJlist;
	private JList reviewerJlist;
	private JPanel assignedPanel = new JPanel(new BorderLayout());
	
	private int revIndex;
	private int manIndex;
	private Document usersXML;
	private Map<String, User> usersMap;

	private List<Manuscript> manList;
	private String[] reviewerMaster = { "null" };
	private String[] manuscriptsList = null;
	private String[] assignedList = null;

	/** Constructs a new ProgramChairPanel. */
	public SubProgramChairPanel(final JButton the_subitMan_button,
			final JButton the_confMaker_button,
			final Conference the_conference, final User the_user,
			Document userXML, Map<String, User> userMap) {
		super(the_subitMan_button, the_confMaker_button, the_conference,
				the_user);
		usersXML = userXML;
		usersMap = userMap;

		addToNavigation(createButton("My Assigned Manuscripts"));
		addToNavigation(createButton("Assign Reviewers"));

		initialize();
	}

	public void initialize() {
		reviewerMaster = getReviewersMaster();
		createManList();
		if (getCurrentConference() != null)
			updatePanel(createManuscriptsPanel()); //
	}

	/*
	 * Panel to Display the Manuscripts
	 */
	private JPanel createManuscriptsPanel() {
		JPanel manuscriptsPanel = new JPanel(new BorderLayout(5, 5));
		
		Border border = BorderFactory.createMatteBorder(0, 0, 3, 0, Color.BLACK);
		JPanel head = new JPanel(new BorderLayout());
		head.setBorder(border);
		
		JLabel title = new JLabel("    My Assigned Manuscripts - " + getCurrentConference().getConferenceName());
		title.setFont(new Font("Arial", Font.BOLD, 20));
		
		JLabel deadline = new JLabel("   Recommendation Deadline: " + 
				getCurrentConference().getConferenceDeadline().get(2) + 
				"        ");
		deadline.setFont(new Font("Arial", Font.BOLD, 15));
		
		head.add(new JLabel(" "), BorderLayout.NORTH);
		head.add(title, BorderLayout.CENTER);
		head.add(deadline, BorderLayout.EAST);
		head.add(new JLabel(" "), BorderLayout.SOUTH);
		
		JPanel body = new JPanel(new BorderLayout());
		JPanel labels = new JPanel(new GridLayout(1, 4));
		labels.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK));
		labels.add(new JLabel("               Manuscript"));
		labels.add(new JLabel("      Author"));
		labels.add(new JLabel("           Recommendation"));
		labels.add(new JLabel("               Reviewers"));
		body.setBorder(border);
		body.add(labels, BorderLayout.NORTH);
		body.add(manTable());
		
		JPanel foot = new JPanel(new FlowLayout());
		
		manuscriptsPanel.add(head, BorderLayout.NORTH);
		manuscriptsPanel.add(body, BorderLayout.CENTER);
		manuscriptsPanel.add(foot, BorderLayout.SOUTH);
		return manuscriptsPanel;
	}

	private JPanel manTable() {
		JPanel grid = new JPanel(new GridLayout(manList.size(), 4, 0, 0));
		for (int i = 0; i < manList.size(); i++) {
			JPanel dmButtonPanel = new JPanel(new GridBagLayout());
			JPanel ulButtonPanel = new JPanel(new GridBagLayout());
			JPanel vrButtonPanel = new JPanel(new GridBagLayout());
			dmButtonPanel.add(createDLorRVorDMButton(manList.get(i).getTitle(), manList.get(i), true)); 
			ulButtonPanel.add(createDLorRVorDMButton("Write Recommendation", manList.get(i), false));
			vrButtonPanel.add(createDLorRVorDMButton("Reviewers", manList.get(i), false));
			
			JPanel manPanel = new JPanel(new GridLayout());
			manPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
			manPanel.add(dmButtonPanel);
			grid.add(manPanel);
			
			manPanel = new JPanel(new GridLayout());
			manPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
			manPanel.add(new JLabel(manList.get(i).get_author().getUniqueID()));
			grid.add(manPanel);
			
			manPanel = new JPanel(new GridLayout());
			manPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
			manPanel.add(ulButtonPanel);
			grid.add(manPanel);
			
			manPanel = new JPanel(new GridLayout());
			manPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
			manPanel.add(vrButtonPanel);
			grid.add(manPanel);
		}
		return grid;
	}

	private JButton createDLorRVorDMButton(final String the_string, final Manuscript the_man, final boolean isDM) {
		final JButton button = new JButton(the_string);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent the_event) {
				if (the_string.equalsIgnoreCase("Write Recommendation")) {
					RecommendationSubmission rs = new RecommendationSubmission(getCurrentConference(), the_man);
					rs.show();
				} else if (isDM) { //download manuscript
					FileDownloader.downloadManuscript(the_man);
				} else if (the_string.equalsIgnoreCase("Reviewers")) {
					ReviewerViewer.viewReviewers(the_man);
				}
			}
		});
		return button;
	}

	/**
	 * Panel that user can Select Manuscripts and Assign a Reviewer.
	 * 
	 * @return A created Assigning Panel
	 */
	public JPanel createAssigningPanel() {
		JPanel assignmentPanel = new JPanel(new BorderLayout(5, 5));

		JLabel title = new JLabel("    Assign Manuscripts to Reviewers");
		title.setFont(new Font("Arial", Font.BOLD, 20));
		Border border = BorderFactory
				.createMatteBorder(0, 0, 3, 0, Color.BLACK);

		JPanel head = new JPanel();
		head.setLayout(new BoxLayout(head,BoxLayout.PAGE_AXIS));
		head.setBorder(border);
		head.add(new JLabel(" "));
		head.add(title);
		head.add(new JLabel("        Assign Reviewers Manuscripts to review."));
		head.add(new JLabel(" "));
		
		JPanel gridMain = new JPanel(new GridLayout(1, 3, 10, 10));
		gridMain.setBorder(border);
		gridMain.add(assignedPanel);
		gridMain.add(manuscriptList());
		gridMain.add(reviewerList());
		JPanel gridNorth = new JPanel(new GridLayout(1, 3, 10, 10));
		gridNorth.add(new JLabel("Reviewers Assignments"));
		gridNorth.add(new JLabel("Manuscripts"));
		gridNorth.add(new JLabel("Reviewers"));
		JPanel body = new JPanel(new BorderLayout());
		body.add(gridNorth, BorderLayout.NORTH);
		body.add(gridMain, BorderLayout.CENTER);
		
		JPanel foot = new JPanel(new FlowLayout());
		foot.add(createButton("Assign"));
		
		assignmentPanel.add(head, BorderLayout.NORTH);
		assignmentPanel.add(body, BorderLayout.CENTER);
		assignmentPanel.add(foot, BorderLayout.SOUTH);
		return assignmentPanel;
	}

	/**
	 * Creates a list of Conference Manuscripts in a JList
	 */
	public JPanel manuscriptList() {
		JList assignedJList = new JList(assignedList);
		assignedJList.setEnabled(false);
		assignedPanel.add(new JScrollPane(assignedJList));
		
		JPanel cur_panel = new JPanel(new BorderLayout());
		manuscriptJlist = new JList(manuscriptsList);
		manuscriptJlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		manuscriptJlist.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				manIndex = manuscriptJlist.getSelectedIndex();

			}
		});
		cur_panel.add(new JScrollPane(manuscriptJlist));
		return cur_panel;
	}

	/**
	 * Creates a list of reviewers in a JList.
	 */
	public JPanel reviewerList() {
		JPanel cur_panel = new JPanel(new BorderLayout());
		reviewerJlist = new JList(reviewerMaster);
		reviewerJlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		reviewerJlist.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				revIndex = reviewerJlist.getSelectedIndex();
			}
		});
		cur_panel.add(new JScrollPane(reviewerJlist));
		return cur_panel;
	}

	/**
	 * Gets a master list of reviews from the XMLfile. This list contains all
	 * the Reviewers for the Conferences.
	 * 
	 * @author Kim Howard December 1, 2012
	 */
	public String[] getReviewersMaster() {
		if (getCurrentConference() != null) {
			Role role = getCurrentUser().getCurrentRole();
			if (role != null && role == Role.SUBPROGRAM_CHAIR) {
				List<String> reviewerIDS = new ArrayList<String>();
				Element docEle = usersXML.getDocumentElement();
				NodeList userList = docEle.getElementsByTagName("user");
				reviewerIDS = XMLHelper.getNodeList(userList);
				reviewerMaster = new String[reviewerIDS.size()];
				int index = 0;
				reviewerMaster = new String[reviewerIDS.size()];
				for (String s : reviewerIDS) {
					if (!s.equals(getCurrentUser().getUniqueID())) {
						reviewerMaster[index] = s;
						index++;
					}

				}
			}
		}
		return reviewerMaster;
	}

	/**
	 * Creates a list of String representations of the Conference Manuscripts.
	 * 
	 * @author Kim Howard
	 * 
	 */
	public void createManList() {
		if (getCurrentConference() != null) {
			manList = getCurrentConference()
					.getManuscriptList(getCurrentUser());

			manuscriptsList = new String[manList.size()];
			int index = 0;
			for (Manuscript m : manList) {
				// does not get manuscript if the current user is the author
				if (!m.get_author().getUniqueID().equals(getCurrentUser().getUniqueID())) {
					assignedList = new String[manList.size()];
					manuscriptsList[index] = m.getTitle();
					if (!m.getReviews().isEmpty()){
						String reviewers = "";
						for (int i = 0; i < m.getReviews().size(); i++) {
							reviewers = reviewers + "   " + m.getReviews().get(i).get_reviewer().getUniqueID();
							assignedList[index] = m.getTitle() + "   -" + reviewers;
						}
					}
					index++;
				}
			}
		}
	}

	/** Creates a new JButton with the_string as the name. */
	private JButton createButton(final String the_string) {
		final JButton button = new JButton(the_string);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent the_event) {
				activate(the_string);
			}
		});
		return button;
	}

	/**
	 * Helper method that activates the given the_action.
	 * 
	 * @author Kim Howard Steve Hipolito
	 * */
	private void activate(String the_action) {
		if (the_action.equals("Assign")) {
			assign();
		} else if (the_action.equals("Assign Reviewers")) {
			updatePanel(createAssigningPanel());
		} else if (the_action.equals("My Assigned Manuscripts")) {
			updatePanel(createManuscriptsPanel());
		}
	}

	/**
	 * Helper method that Creates and assigns the selected Reviewer to a Review
	 * Object. The XML file associated with that review is then updated to
	 * reflect the changes.
	 * 
	 * @author Kim Howard
	 */
	public void assign() {
		boolean cantAssign = false;
		String userID = reviewerMaster[revIndex];
		String manTitle = manuscriptsList[manIndex];
		Manuscript man = getCurrentConference().chooseManuscript(manTitle);
		// to check how many manuscripts they already have they need to be
		// reviewer temporarily
		usersMap.get(userID).setCurrentRole(Role.REVIEWER);
		// check that Reviewer assigned is not Author of that manuscript
		if (userID.equals(man.get_author().getUniqueID())) {
			JOptionPane.showMessageDialog(getMainPanel(), usersMap.get(userID)
					.getUserName()
					+ "cannot be assign to their own Manuscript to review");
			cantAssign = true;
		} else if (getCurrentConference().getManuscriptList(
				usersMap.get(userID)).size() >= MAX) {
			JOptionPane.showMessageDialog(getMainPanel(), usersMap.get(userID)
					.getUserName()
					+ " alredy has the maximum ("
					+ MAX
					+ ") Manuscripts Assigned to Review");
			cantAssign = true;
		} else {
			List<Review> l = man.getReviews();
			for (Review r : l) {
				if (r.get_reviewer().getUniqueID().equals(userID)) {
					JOptionPane.showMessageDialog(getMainPanel(),usersMap.get(userID).getUserName()
											+ " is already a reviewer for this manuscript");
					cantAssign = true;
				}
			}
		}
		//if everythign else is good go ahead and assign
		if (!cantAssign) {
			Review review = new Review(usersMap.get(userID));
			man.addReview(review);
			getCurrentConference().addUser(usersMap.get(userID), Role.REVIEWER);
			String revXML_dir = DIR
					+ getCurrentConference().getConferenceName()
					+ File.separatorChar + "Manuscripts" + File.separatorChar
					+ man.getTitle() + File.separatorChar + "reviews.xml";
			Document revXML = XMLHelper.getDoc(new File(revXML_dir));
			XMLHelper.addReviewer(revXML, userID, revXML_dir);
			JOptionPane.showMessageDialog(getMainPanel(), "Confirmed: "
					+ usersMap.get(userID).getUniqueID() + " is assigned to "
					+ "Manuscript: " + man.getTitle() + " to review");
			updatePanel(createAssigningPanel());
		}
		usersMap.get(userID).setCurrentRole(Role.USER);
	}

	/**
	 * Helper method to Update the panels
	 * 
	 * @param the_comp
	 */
	private void updatePanel(Component the_comp) {
		getMainPanel().removeAll();
		getMainPanel().add(the_comp);
		getMainPanel().repaint();
		getMainPanel().revalidate();
	}
}
