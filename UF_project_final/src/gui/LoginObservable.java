package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import model.Loader;
import model.User;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Class that creates the login panel and reads
 * all users from the master list from data.
 *
 * @author Steve Hipolito
 *  December 2, 2012
 */
public class LoginObservable extends Observable {

	private Loader my_loader;
	private JPanel my_login_panel;
	private Border my_border;
	private JTextField my_login_field;
	private User my_logged_in_user;
	String userstring;
	
	final JFrame chooser_frame = new JFrame(); //for testing

	/** Constructs a new LoginObservable. */
	public LoginObservable(final Loader the_loader, 
			final Border the_border) {
		my_loader = the_loader;
		my_border = the_border;
	}

	/** Returns the Login panel. */
	public JPanel getLoginPanel() {       		
		my_login_panel = new JPanel(new GridBagLayout());
		my_login_panel.setBorder(my_border);

		final JPanel login_panel_inside = new JPanel(new BorderLayout(5, 5));
		final JPanel login_panel_inside_north = new JPanel(new BorderLayout());
		
		final JPanel conf_list = new JPanel();
		conf_list.setLayout(new BoxLayout(conf_list, BoxLayout.PAGE_AXIS));
		conf_list.setBorder(my_border);
		final Map<String, List<String>> conference_map = 
				my_loader.getConferenceList();
		final Set<String> keys = conference_map.keySet();
		conf_list.add(new JLabel(" "));
		conf_list.add(new JLabel("  Conferences:"));
		conf_list.add(new JLabel("  ============"));
		for (String key : keys)
			conf_list.add(new JLabel("  "+key));
		conf_list.add(new JLabel(" "));

		final JPanel usernamelogin = new JPanel(new FlowLayout());
		usernamelogin.setBorder(my_border);
		my_login_field = new JTextField(10);
		my_login_field.setMaximumSize(my_login_field.getPreferredSize());
		my_login_field.setText("JeffDrew");
		usernamelogin.add(new JLabel("UniqueID: "));
		usernamelogin.add(my_login_field);

		login_panel_inside_north.add(conf_list, BorderLayout.NORTH);
		login_panel_inside_north.add(usernamelogin, BorderLayout.SOUTH);
		
		login_panel_inside.add(login_panel_inside_north, BorderLayout.NORTH);
		login_panel_inside.add(createButton("Login"), BorderLayout.CENTER);
		login_panel_inside.add(createButton("Exit"), BorderLayout.EAST);
		
		userChooser();//for testing
		
		my_login_panel.add(login_panel_inside);
		return my_login_panel;
	}

	/** Returns the logged in user */
	public User getLoggedInUser() {
		return my_logged_in_user;
	}

	/** Creates a new JButton with the_string as the name. */
	private JButton createButton(final String the_string){
		final JButton button = new JButton(the_string);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent the_event){
				if (the_string.equalsIgnoreCase("Login")) {
					String input = my_login_field.getText();
					userstring = my_login_field.getText();
					if (my_loader.getUser(input) != null) {
						
						my_logged_in_user = my_loader.getUser(input);
						setChanged();
						notifyObservers(the_string);
						chooser_frame.dispose();
					} else {
						JOptionPane.showConfirmDialog(my_login_panel,
								"UniqueID  " + input + "  not found!",
								the_string, JOptionPane.DEFAULT_OPTION);
					}             
				} else if (the_string.equalsIgnoreCase("Exit")) {
					chooser_frame.dispose();
					setChanged();
					notifyObservers(the_string);
				}
			}
		});
		return button;
	}
	
	/** FOR TESTING */
	private void userChooser() {			
		Document usersXML = my_loader.getUsersXML();
		//Document usersXML = doc;
		Element docEle = usersXML.getDocumentElement();
		NodeList userList = docEle.getElementsByTagName("user");
		String[] users = new String[userList.getLength()+1];
		users[0] = "User Chooser";
		for (int i = 0; i < userList.getLength(); i++) {
			Node userNode = userList.item(i);
			Element userElement = (Element) userNode;
			users[i+1] = userElement.getAttribute("id");
		}
		
		final JComboBox combobox = new JComboBox(users);
		combobox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent the_event) {
				JComboBox cb = (JComboBox) the_event.getSource();
				if (!cb.getSelectedItem().equals("User Chooser")) {
					my_login_field.setText((String) cb.getSelectedItem());
					chooser_frame.dispose();
				}
			}
		});
		combobox.setSelectedIndex(0);
		
		chooser_frame.add(combobox);
		chooser_frame.pack();
		chooser_frame.setLocationRelativeTo(null);
		chooser_frame.setVisible(true);
	}
}
