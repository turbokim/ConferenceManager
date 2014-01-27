package model;
/**
 * An enumerator class to create
 * decision options.
 * @author Daniel C. Beraun 
 *  December 2, 2012
 */
public enum Decision {

	/**
	 * Accepted decision.
	 */
	ACCEPTED("accepted"),

	/**
	 * Unaccepted decision.
	 */
	UNACCEPTED("unaccepted"),

	/**
	 * Undecided decision.
	 */
	UNDECIDED("undecided");
	
	public String my_decision;
	/**
	 * @author Kim Howard, Navjot Kamal
	 * Returns the Decision associated with the string.
	 * @param the_string
	 * @return The decision.
	 */
	public static Decision getDecisionType(String the_string) {
		Decision the_decision = null;
		
		if (the_string == null)
			return the_decision;
		if (the_string.equalsIgnoreCase(ACCEPTED.toString())) {
			the_decision = ACCEPTED;
		} else if (the_string.equalsIgnoreCase(UNACCEPTED.toString())) {
			the_decision = UNACCEPTED;
		} else if (the_string.equalsIgnoreCase(UNDECIDED.toString())) {
			the_decision = UNDECIDED;
		} 
		return the_decision;
	}

	private Decision(final String the_decision) {
		my_decision = the_decision;
	}

	public String getDecision() {
		return my_decision;
	}

}
