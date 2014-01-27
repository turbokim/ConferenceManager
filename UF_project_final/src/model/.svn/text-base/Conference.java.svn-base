package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Conference class that should keep track of all conferences and holds the
 * users and the manuscripts for a particular Conference.
 * 
 * @author Navjot Kamal, Kim Howard
 *  December 2, 2012
 * 
 */
public final class Conference {

	private String my_conference_name;
	private User my_program_chair;
	private List<String> deadlines;
	private Map<String, Manuscript> my_manuscripts;

	private Map<String, User> my_users;
	private Map<String, List<User>> role_users; // all users of each role
	private Map<String, List<Role>> user_roles; // all roles of each user

	/**
	 * Conference Constructor so the Loader can Load an existing Confernece
	 * 
	 * @param the_conference_name
	 * @param the_deadlines
	 * @param the_manuscripts
	 */
	public Conference(String the_conference_name, User programChair,
			List<String> the_deadlines, Map<String, Manuscript> the_manuscripts) {
		my_conference_name = the_conference_name;
		my_program_chair = programChair;
		deadlines = the_deadlines;
		my_manuscripts = the_manuscripts;
		initializeMaps();
		populateMaps();
	}

	/**
	 * Creates a Conference with a list of users and manuscripts.
	 * 
	 * @param the_conference_name
	 *            The name of the Conference to be created.
	 * @param the_deadline
	 *            The deadline to submit a paper for this conference, format:
	 *            01/02/0003
	 */
	public Conference(String the_conference_name, User programChair,
			List<String> the_deadlines) {
		my_conference_name = the_conference_name;
		my_program_chair = programChair;
		deadlines = the_deadlines;
		my_manuscripts = new HashMap<String, Manuscript>();
		initializeMaps();
	}

	/**
	 * Initializes the role_users Maps. Adds the Program Chair of this
	 * Conference too the user lists.
	 */
	public void initializeMaps() {
		my_users = new HashMap<String, User>();
		role_users = new HashMap<String, List<User>>();
		role_users.put(Role.PROGRAM_CHAIR.toString(), new ArrayList<User>());
		role_users.put(Role.SUBPROGRAM_CHAIR.toString(), new ArrayList<User>());
		role_users.put(Role.REVIEWER.toString(), new ArrayList<User>());
		role_users.put(Role.AUTHOR.toString(), new ArrayList<User>());

		user_roles = new HashMap<String, List<Role>>();
		// adds Program Chair to the lists
		addUser(my_program_chair, Role.PROGRAM_CHAIR);
	}

	/**
	 * Method goes through each manuscript, extracts all users with their roles,
	 * and adds that user to the my_users, role_users, and user_roles maps.
	 * 
	 * Used when an already created Conference Loads into the system
	 */
	public void populateMaps() {
		Collection<String> titles = my_manuscripts.keySet();
		for (String title : titles) {
			extractAuthor(title);
			extractSubprogramChair(title);
			extractReviewers(title);
		}
	}

	/**
	 * Gets the Author of a Manuscript and adds that user to the user lists
	 * 
	 * @author Kim Howard
	 * @param title
	 *            The manuscript title.
	 */
	private void extractAuthor(String title) {
		User user = my_manuscripts.get(title).get_author();
		addUser(user, Role.AUTHOR);

	}

	/**
	 * Gets the SubprogramChairs from all the Manuscripts and adds to the user
	 * lists.
	 * 
	 * @author Kim Howard
	 * @param title
	 *            The title of the Manuscript.
	 */
	public void extractSubprogramChair(String title) {
		if (!(my_manuscripts.get(title).getRecommendation() == null)) {
			User user = my_manuscripts.get(title).getRecommendation()
					.get_subprogram_chair();
			addUser(user, Role.SUBPROGRAM_CHAIR);
		}

	}

	/**
	 * Gets the Reviewers from all the Manuscripts and adds to the user lists.
	 * 
	 * @author Kim Howard
	 * @param title
	 *            The manuscript title.
	 */
	public void extractReviewers(String title) {
		List<Review> reviews = my_manuscripts.get(title).getReviews();

		for (Review review : reviews) {
			User user = review.get_reviewer();
			addUser(user, Role.REVIEWER);
		}
	}

	public User getProgramChair() {
		return my_program_chair;
	}

	/**
	 * Adds a user to the Conference A user is automatically added when a
	 * manuscripts is submitted(author), a Reviewer is assigned to a manuscript,
	 * and a Subprogram Chair is assigned to a manuscript;
	 * 
	 * @param user
	 *            The user to the added.
	 * @param role
	 *            The role of the user to be added.
	 */

	public void addUser(User user, Role role) {

		String uniqueID = user.getUniqueID();
		if (!containsUser(user)) {
			my_users.put(uniqueID, user);
			user_roles.put(uniqueID, new ArrayList<Role>());
		}
		if (user_roles.containsKey(uniqueID)
				&& !user_roles.get(uniqueID).contains(role)) {
			user_roles.get(uniqueID).add(role);
		}

		// need to update current role of author to AUTHOR
		// the author's persistent data needs to be changed here in the xml file
		List<User> users = role_users.get(role);
		boolean exists = false;
		if (users != null) {
			for (User u : users) {
				if (uniqueID.equals(u.getUniqueID())) {
					exists = true;
					break;
				}
			}
		}

		if (!exists) {
			role_users.get(role.toString()).add(user);
		}
	}

	/**
	 * Returns the name for the specific conference.
	 * 
	 * @return my_conference_name The Name of the conference.
	 */
	public String getConferenceName() {
		return my_conference_name;
	}

	/**
	 * Returns the deadline for the specific conference.
	 * 
	 * @return my_conference_deadline The deadline of the conference.
	 */
	public List<String> getConferenceDeadline() {
		return deadlines;
	}

	/**
	 * Returns a list of roles for a user of a conference used by the GUI to
	 * determine which user panel to view.
	 * 
	 * @param the_user The User.
	 * @return The list of Roles for that User
	 */
	public List<Role> getUserRoles(User the_user) {
		return user_roles.get(the_user.getUniqueID());
	}

	/**
	 * Returns the users for the specific conference.
	 * 
	 * @return List of Users for a conference.
	 */

	// changed because get users is used for the whole system..this is more
	// explicit
	public List<User> getConferenceUsers() {
		Collection<User> users = my_users.values();
		List<User> userList = new ArrayList<User>();
		for (User user : users) {
			userList.add(user);
		}
		return userList;
	}

	/**
	 * Verifies if the Conference already has existing User
	 * 
	 * @param the_user The user.
	 * @return boolean T/F If user is in my_users list or not.
	 */
	public boolean containsUser(User the_user) {
		return my_users.containsKey(the_user.getUniqueID());
	}

	/**
	 * Adds a manuscript to the list associated with a specific author then
	 * the Author is added to the user lists.
	 * 
	 * @param the_manuscript
	 *            The manuscript to be added to the list.
	 */
	public void addManuscript(Manuscript the_manuscript) {
		my_manuscripts.put(the_manuscript.getTitle(), the_manuscript);
		addUser(the_manuscript.get_author(), Role.AUTHOR);
	}

	/**
	 * Check for manuscript with same name
	 */
	public boolean containsManuscript(Manuscript the_manuscript) {
		return my_manuscripts.containsKey(the_manuscript.getTitle());
	}

	/**
	 * Returns the number of manuscripts this user has in this conference.
	 * 
	 * @author Kim Howard November 30, 2012
	 * @param user
	 *            The user requesting.
	 * @return The number of manuscripts authored in this conference.
	 */
	public int getManuscriptCount(User user) {
		int i = 0;
		for (String man : my_manuscripts.keySet()) {
			if (my_manuscripts.get(man).get_author().getUniqueID()
					.equals(user.getUniqueID()))
				i++;
		}
		return i;
	}

	/**
	 * Gets a Manuscript list according to the current User Role
	 * 
	 * @author Kim Howard
	 * @param the_user
	 *            The user requesting the manuscript.
	 * @return The list of Manuscripts requested.
	 */
	public List<Manuscript> getManuscriptList(User the_user) {
		List<Manuscript> manuscripts = new ArrayList<Manuscript>();
		Role user_role = the_user.getCurrentRole();

		switch (user_role) {
		case PROGRAM_CHAIR:// returns all manuscripts submitted
			for (String man : my_manuscripts.keySet()) {
				manuscripts.add(my_manuscripts.get(man));
			}
			break;
		case SUBPROGRAM_CHAIR: // returns only manuscripts assigned to certain
								// Subprogram chair
			for (String man : my_manuscripts.keySet()) {
				if(my_manuscripts.get(man).getRecommendation() != null)
				{
					if (my_manuscripts.get(man).getRecommendation()
							.get_subprogram_chair().getUniqueID()
							.equals(the_user.getUniqueID())) {
						manuscripts.add(my_manuscripts.get(man));
					}
				}
				
			}
			break;
		case REVIEWER:// returns only manuscripts assigned to certain Reviewer
			List<Review> revs;
			for (String man : my_manuscripts.keySet()) {
				revs = my_manuscripts.get(man).getReviews();
				
				for (Review r : revs) {
					if (r.get_reviewer().getUniqueID()
							.equals(the_user.getUniqueID())) {
						manuscripts.add(my_manuscripts.get(man));
					}
				}
			}
			break;
		case AUTHOR:// return only manuscripts assigned to certain authors
			for (String man : my_manuscripts.keySet()) {
				if (my_manuscripts.get(man).get_author().getUniqueID()
						.equals(the_user.getUniqueID())) {
					manuscripts.add(my_manuscripts.get(man));
				}
			}
			break;
		}

		return manuscripts;
	}

	/**
	 * Chooses a manuscript.
	 * 
	 * @param the_manuscript
	 *            The manuscript to be chosen from the list.
	 */
	public Manuscript chooseManuscript(Manuscript the_manuscript) {
		Manuscript temp = null;
		if (my_manuscripts.containsValue(the_manuscript)) {
			temp = my_manuscripts.get(the_manuscript.getTitle());
		}
		return temp;
	}
	public Manuscript chooseManuscript(String title)
	{
		Manuscript temp = null;
		if(my_manuscripts.containsKey(title))
		{
			temp = my_manuscripts.get(title);
		}
		return temp;
	}

	/**
	 * Removes a manuscript from the list associated with a specific author.
	 * 
	 * @param the_manuscript
	 *            The manuscript to be removed from the list.
	 */
	public void removeManuscript(Manuscript the_manuscript) {
		if (my_manuscripts.containsValue(the_manuscript)) {
			my_manuscripts.values().remove(the_manuscript);
			// need to remove author role if there aren't anymore manuscripts
			// for that author
		}
	}

	// used for testing
	public List<Manuscript> getManuscripts() {
		List<Manuscript> manuscripts = new ArrayList<Manuscript>();
		for (String man : my_manuscripts.keySet()) {
			manuscripts.add(my_manuscripts.get(man));
		}
		return manuscripts;
	}
	public List<User> getSubprogramChair(User requesting_user){
		List<User> subprogramChairs = new ArrayList<User>();
		if(requesting_user.getCurrentRole().equals(Role.PROGRAM_CHAIR))
		{
			subprogramChairs = role_users.get(Role.SUBPROGRAM_CHAIR);
		}
		return subprogramChairs;
	}

	/**
	 * Gets reviewers associated with Subprogram Chair
	 * 
	 * @author Kim Howard
	 * @param requesting_user
	 *            The user requesting the Reviewers
	 * @return The list of Reviewers.
	 */
	public List<User> getReviewers(User requesting_user) {
		List<User> reviewers = new ArrayList<User>();
		List<Review> reviews;
		Role the_role = requesting_user.getCurrentRole();
		switch (the_role) {
		case PROGRAM_CHAIR:
			// Map<reviewer, List<User>
			for (String man : my_manuscripts.keySet()) {
				reviews = my_manuscripts.get(man).getReviews();
				for (Review r : reviews) {
					reviewers.add(r.get_reviewer());
				}
			}
			;
			break;
		case SUBPROGRAM_CHAIR:
			for (String man : my_manuscripts.keySet()) {
				if (my_manuscripts.get(man).getRecommendation()
						.get_subprogram_chair().getUniqueID()
						.equals(requesting_user.getUniqueID()))

				{
					reviews = my_manuscripts.get(man).getReviews();
					for (Review r : reviews) {
						reviewers.add(r.get_reviewer());
					}
				}
			}
			break;
		}
		return reviewers;
	}

	/**
	 * Returns Authors associated with Program Chair, Subprogram Chair and
	 * Reviewer
	 * 
	 * @author Kim Howard
	 * @param requesting_user
	 *            The user requesting the Authors.
	 * @return The list of Authors requested.
	 */
	public List<User> getAuthors(User requesting_user) {
		List<User> authors = new ArrayList<User>();
		List<Review> reviews;
		switch (requesting_user.getCurrentRole()) {
		case PROGRAM_CHAIR:
			for (String manuscript : my_manuscripts.keySet()) {
				reviews = my_manuscripts.get(manuscript).getReviews();
				for (Review r : reviews) {
					authors.add(r.get_reviewer());
				}
			}
			break;
		case SUBPROGRAM_CHAIR:
			for (String manuscript : my_manuscripts.keySet()) {
				if (my_manuscripts.get(manuscript).getRecommendation()
						.get_subprogram_chair().getUniqueID()
						.equals(requesting_user.getUniqueID())) {
					reviews = my_manuscripts.get(manuscript).getReviews();
					for (Review r : reviews) {
						authors.add(r.get_reviewer());
					}
				}
			}
			break;
		case REVIEWER:
			for (String manuscript : my_manuscripts.keySet()) {
				reviews = my_manuscripts.get(manuscript).getReviews();
				for (Review r : reviews) {
					if (r.get_reviewer().getUniqueID()
							.equals(requesting_user.getUniqueID())) {
						authors.add(r.get_reviewer());
					}
				}
			}
			break;
		}
		return authors;
	}

}
