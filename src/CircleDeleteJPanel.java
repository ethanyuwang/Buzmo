package buzmo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CircleDeleteJPanel extends JPanel
{
    GridBagConstraints gbc;
    JPanel topPanel;
    JPanel botPanel;

    //Chat display and control Components
    JTextArea historyTextArea;
    JTextArea draftTextArea;
    JScrollPane historyScroll;
    JScrollPane draftScroll;
    JButton deleteButton;
    JButton backButton;

    public CircleDeleteJPanel()
    {
	this.repaint();

	//Chat dispaly and control Components
	historyTextArea = new JTextArea("<Select a post to delete>\n\n" + 
		DBInteractorCirclePost.getUsersPosts(BuzmoJFrame.con));
	historyTextArea.setEditable(false);
	historyTextArea.setLineWrap(true);
	historyTextArea.setWrapStyleWord(false);
	historyScroll = new JScrollPane(historyTextArea);

	draftTextArea= new JTextArea("Enter post_id");
	draftTextArea.setLineWrap(true);
	draftTextArea.setWrapStyleWord(false);
	draftScroll = new JScrollPane(draftTextArea);

	//Button Components
	deleteButton = new JButton("Delete");
	backButton = new JButton("Back");

	//Pannels
	gbc = new GridBagConstraints();
	topPanel = new JPanel(new BorderLayout());
	botPanel = new JPanel(new GridBagLayout());

	//set layout manager for this panel
	setLayout(new GridLayout());

	//add components to top panel
	topPanel.add(historyScroll, BorderLayout.CENTER);

	//add components to bot panel
	gbc.gridx = 0;
	gbc.gridy = 0;
	gbc.ipady = 50;
	gbc.ipadx = 200;
	gbc.gridwidth = 3;
	gbc.gridheight = 3;
	gbc.fill = GridBagConstraints.HORIZONTAL;
	botPanel.add(draftScroll, gbc);
	gbc.gridx = 0;
	gbc.gridy = 3;
	gbc.ipady = 0;
	gbc.gridheight = 3;
	botPanel.add(deleteButton, gbc);
	gbc.gridx = 0;
	gbc.gridy = 6;
	botPanel.add(backButton, gbc);

	topPanel.setOpaque(false);
	botPanel.setOpaque(false);

	//add bot and top panel to this.panel
	add(topPanel);
	add(botPanel);

	//event managers
	deleteButton.addMouseListener(new MouseAdapter() {
		@Override
		public void mouseReleased(MouseEvent e) {
			if(DBInteractorCirclePost.deletePost(BuzmoJFrame.con, draftTextArea.getText())){
				draftTextArea.setText("Deletion complete\n");	
				//reload
				historyTextArea.setText(DBInteractorCirclePost.getUsersPosts(BuzmoJFrame.con));
			}
			else {
				draftTextArea.setText("Failed to delete: double check your post_id\n");
			}
		}
	});
	backButton.addMouseListener(new MouseAdapter() {
		@Override
		public void mouseReleased(MouseEvent e) {
			BuzmoJFrame.setCurrentPanelTo(new CirclePostJPanel());
		}
	});
    }
}
