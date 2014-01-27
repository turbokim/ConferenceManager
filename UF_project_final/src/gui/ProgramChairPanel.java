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
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import model.Conference;
import model.Decision;
import model.Manuscript;
import model.Recommendation;
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
public class ProgramChairPanel extends UserPanel {

	private JList manuscriptJlist;
	private JList reviewerJlist;
	private JPanel assignedPanel = new JPanel(new BorderLayout());

	private int revIndex;
	private int manIndex;
	private Document usersXML;
	private Document mansXML;
	private String mansXML_dir;
	private Map<String, User> usersMap;

	private List<Manuscript> manList;
	private String[] reviewerMaster = { "null" };
	private String[] manuscriptsList = null;
	private String[] assignedList = null;

	/** Constructs a new ProgramChairPanel. */
	public ProgramChairPanel(final JButton the_subitMan_button,
			final JButton the_confMaker_button,
			final Conference the_conference, final User the_user,
			Document userXML, Map<String, User> userMap, Document the_mansXML) {
		super(the_subitMan_button, the_confMaker_button, the_conference,
				the_user);
		usersXML = userXML;
		usersMap = userMap;
		mansXML = the_mansXML;

		addToNavigation(createButton("Conference Manuscripts"));
		addToNavigation(createButton("Assign SubProgram Chairs"));

		initialize();
	}
	public void setUsersXML(Document userXML)
	{
		usersXML = userXML;
	}

	public void setUsersMap(Map<String, User> userMap)
	{
		usersMap = userMap;
	}
	public void initialize() {
		reviewerMaster = getReviewersMaster();
		createManList();	
		if (getCurrentConference() != null) {
			updatePanel(createManuscriptsPanel());
			mansXML_dir = "data" + File.separatorChar + 
					"Conferences" + File.separatorChar + 
					getCurrentConference().getConferenceName() + 
					File.separatorChar + "Manuscripts" + 
					File.separatorChar + "Manuscripts.xml";
		}
	}
	
	private JPanel createManuscriptsPanel() {
		JPanel manuscriptsPanel = new JPanel(new BorderLayout(5, 5));
		
		JLabel title = new JLabel("    All Submitted Manuscripts - " + getCurrentConference().getConferenceName());
		title.setFont(new Font("Arial", Font.BOLD, 20));
		Border border = BorderFactory.createMatteBorder(0, 0, 3, 0, Color.BLACK);
		
		JPanel head = new JPanel();
		head.setLayout(new BoxLayout(head,BoxLayout.PAGE_AXIS));
		head.setBorder(border);
		head.add(new JLabel(" "));
		head.add(title);
		head.add(new JLabel(" "));
		
		JPanel body = new JPanel(new BorderLayout());
		JPanel labels = new JPanel(new GridLayout(1, 5));
		labels.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK));
		labels.add(new JLabel("          Manuscript"));
		labels.add(new JLabel("   Author"));
		labels.add(new JLabel("      Recommendation"));
		labels.add(new JLabel("          Reviewers"));
		labels.add(new JLabel("     Decision"));	
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
		JPanel grid = new JPanel(new GridLayout(manList.size(), 5, 0, 0));
		for (int i = 0; i < manList.size(); i++) {
			JPanel dmButtonPanel = new JPanel(new GridBagLayout());
			JPanel dlButtonPanel = new JPanel(new GridBagLayout());
			JPanel vrButtonPanel = new JPanel(new GridBagLayout());
			dmButtonPanel.add(createDLorRVorDMButton(manList.get(i).getTitle(), manList.get(i), true)); 
			dlButtonPanel.add(createDLorRVorDMButton("Recommendation", manList.get(i), false));
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
			manPanel.add(dlButtonPanel);
			grid.add(manPanel);
			
			manPanel = new JPanel(new GridLayout());
			manPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
			manPanel.add(vrButtonPanel);
			grid.add(manPanel);
			
			manPanel = new JPanel(new GridLayout());
			manPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
			manPanel.add(decisionPanel(manList.get(i)));
			grid.add(manPanel);
		}
		return grid;
	}
	
	private JPanel decisionPanel(final Manuscript the_man) {
		JPanel decGrid = new JPanel(new GridLayout(3, 1));
		ButtonGroup bp = new ButtonGroup();
		decGrid.add(createRadioButton(bp, "Accepted", Decision.ACCEPTED, the_man));
		decGrid.add(createRadioButton(bp, "Unaccepted", Decision.UNACCEPTED, the_man));
		decGrid.add(createRadioButton(bp, "Undecided", Decision.UNDECIDED, the_man));
		return decGrid;
	}
	
	private JRadioButton createRadioButton(final ButtonGroup bp, final String name, final Decision dec, final Manuscript the_man) {
		final JRadioButton rb = new JRadioButton(name);
		rb.setSelected(the_man.get_Decision() == dec);
		rb.setEnabled(the_man.getRecommendation() != null);
		rb.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent the_event) {
				getCurrentConference().removeManuscript(the_man);
				the_man.set_Decision(dec);
				getCurrentConference().addManuscript(the_man);
				XMLHelper.changeManDecision(mansXML, the_man.getTitle(), dec.getDecision(), mansXML_dir);
			}
		});
		bp.add(rb);
		return rb;
	}
	
	private JButton createDLorRVorDMButton(final String the_string, final Manuscript the_man, final boolean isDM) {
		final JButton button = new JButton(the_string);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent the_event) {
				if (the_string.equalsIgnoreCase("Recommendation")) { //download recommendation
					FileDownloader.downloadRecommendation(the_man.getRecommendation());
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
	 * Panel that user can Select Manuscripts and Assign a Program Chair to.
	 * 
	 * @return
	 */
	public JPanel createAssigningPanel() {
		JPanel assignmentPanel = new JPanel(new BorderLayout(5, 5));
		
		JLabel title = new JLabel("    Assign SubProgram Chairs");
		title.setFont(new Font("Arial", Font.BOLD, 20));
		Border border = BorderFactory.createMatteBorder(0, 0, 3, 0, Color.BLACK);
		
		JPanel head = new JPanel();
		head.setLayout(new BoxLayout(head,BoxLayout.PAGE_AXIS));
		head.setBorder(border);
		head.add(new JLabel(" "));
		head.add(title);
		head.add(new JLabel("        Promote Reviewers to be SubProgram Chairs, and assign them Manscripts."));
		head.add(new JLabel(" "));
		
		JPanel gridMain = new JPanel(new GridLayout(1, 3, 10, 10));
		gridMain.setBorder(border);
		gridMain.add(assignedPanel);
		gridMain.add(manuscriptList());
		gridMain.add(reviewerList());
		JPanel gridNorth = new JPanel(new GridLayout(1, 3, 10, 10));
		gridNorth.add(new JLabel("Subprogram Chair Assignments"));
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
	 * Gets a master list of reviews from the XMLfile
	 * 
	 * @author Kim Howard December 1, 2012
	 */
	public String[] getReviewersMaster() {
		if (getCurrentConference() != null) {
			Role role = getCurrentUser().getCurrentRole();
			if (role != null && role == Role.PROGRAM_CHAIR) {
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
	 * Creates a string list of manuscripts from the Conference.
	 * 
	 * @return
	 */
	public void createManList() {
		if (getCurrentConference() != null) {
			manList = getCurrentConference()
					.getManuscriptList(getCurrentUser());
			assignedList = new String[manList.size()];
			manuscriptsList = new String[manList.size()];
			int index = 0;
			for (Manuscript m : manList) {
				manuscriptsList[index] = m.getTitle();
				if (m.getRecommendation() != null )
					assignedList[index] = m.getTitle() + "  -  " + m.getRecommendation().get_subprogram_chair().getUniqueID();
				index++;
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

	/** Helper method that activates the given the_action. */
	private void activate(String the_action) {
		if (the_action.equals("Assign")) {
			assign();
		} else if (the_action.equals("Assign SubProgram Chairs")) {
			updatePanel(createAssigningPanel());
		} else if (the_action.equals("Conference Manuscripts")) {
			updatePanel(createManuscriptsPanel());
		} 
	}
	
	private void assign() {
		String userID = reviewerMaster[revIndex];
		String manTitle = manuscriptsList[manIndex];
		Manuscript man = getCurrentConference().chooseManuscript(manTitle);
		
		//only assign manuscripts with no recommendation/subprogramchair
		if (man.getRecommendation() == null) {
			if (man.get_author().getUniqueID().equalsIgnoreCase(userID)) {
				JOptionPane.showConfirmDialog(null,
						"A SubProgram Chair cannot be designated to a paper that " +
						"he or she authored!",
						"Cannot Assign", JOptionPane.DEFAULT_OPTION);
			} else {
				//Set the Manuscript's recommendation
				man.setRecommendation(new Recommendation(usersMap.get(userID)));
				//Add the SubProgram Chair role to the reviewer
				getCurrentConference().addUser(usersMap.get(userID),
						Role.SUBPROGRAM_CHAIR);
				//Adds a SubprogramChair (UserID) and "null" file path in the XML
				String manXML_dir = ("data" + File.separatorChar +
						"Conferences" + File.separatorChar +
						getCurrentConference().getConferenceName() + File.separatorChar +
						"Manuscripts" + File.separatorChar + "Manuscripts.xml");
				Document manXML = XMLHelper.getDoc(new File(manXML_dir));
				XMLHelper.addSubprogramChair(manXML, manTitle, userID, manXML_dir);
				//confirmation
				JOptionPane.showConfirmDialog(null,
						man.getTitle() + "  has been assigned to \n" +
						man.getRecommendation().get_subprogram_chair().getUniqueID(),
						"Assign SubProgramChairs", JOptionPane.DEFAULT_OPTION);
				updatePanel(createAssigningPanel());
			}
		} else {
			JOptionPane.showConfirmDialog(null,
					man.getTitle() + "  already has been assigned to \n" +
					"a SubProgram Chair!",
					"Cannot Assign", JOptionPane.DEFAULT_OPTION);
		}
	}

	private void updatePanel(Component the_comp) {
		getMainPanel().removeAll();
		getMainPanel().add(the_comp);
		getMainPanel().repaint();
		getMainPanel().revalidate();
	}

}
