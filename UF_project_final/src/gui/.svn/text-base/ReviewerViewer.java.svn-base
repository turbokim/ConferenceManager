package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.Manuscript;
import model.Review;

/** A class to show a pop up frame to view a list of 
 * reviewers reviewing a give Manuscript
 * @author Steve Hipolito
 *  December 2, 2012
 */
public class ReviewerViewer extends Observable {

	private ReviewerViewer() {
		throw new AssertionError();
	}
	
	public static void viewReviewers(Manuscript the_man) {
		final JFrame frame = new JFrame();
		frame.setLayout(new BorderLayout());
		frame.setSize(350, 150);
		frame.setTitle("Reviewers for " + the_man.getTitle());
		
		JPanel head = new JPanel(new GridLayout(1, 2));
		head.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK));
		head.add(new JLabel("     Reviewer"));
		head.add(new JLabel("               Review File"));
		
		JPanel grid = new JPanel(new GridLayout(the_man.getReviews().size(), 2));
		for (int i = 0; i < the_man.getReviews().size(); i++) {
			JPanel panel = new JPanel(new GridLayout());
			panel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
			panel.add(new JLabel("     "+the_man.getReviews().get(i).get_reviewer().getUniqueID()));
			grid.add(panel);
			
			panel = new JPanel(new GridLayout());
			JPanel buttonPanel = new JPanel(new GridBagLayout());
			buttonPanel.add(createDLButton(the_man.getReviews().get(i)));
			panel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
			panel.add(buttonPanel);
			grid.add(panel);
		}
		
		JPanel cancelPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton cancelButton = new JButton("OK");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent the_event) {
				frame.dispose();
			}
		});
		cancelPanel.add(cancelButton);
		
		frame.getContentPane().add(head, BorderLayout.NORTH);
		frame.getContentPane().add(grid, BorderLayout.CENTER);
		frame.getContentPane().add(cancelPanel, BorderLayout.SOUTH);
		frame.setVisible(true);
	}
	
	private static JButton createDLButton(final Review review) {
		final JButton button = new JButton("Download");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent the_event) {
				FileDownloader.downloadReview(review);
			}
		});
		return button;
	}
}
