package buzmo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

public class UserSearchJPanel extends JPanel
{
    //JPanel
    GridBagConstraints gbc;
    JPanel topPanel;
    JPanel botPanel;
    //Button
    JButton searchButton;
    JButton backButton;
    //Label
    JLabel emailLabel;
    JLabel topicLabel;
    JLabel timestampLabel;
    JLabel postnumLabel;
    //JTextArea
    JTextArea resultTextArea;
    JTextArea emailTextArea;
    JTextArea topicTextArea;
    JTextArea timestampTextArea;
    JTextArea postnumTextArea;
    //JScrollPane
    JScrollPane resultScroll;    
    JScrollPane emailScroll;    
    JScrollPane topicScroll;    
    JScrollPane timestampScroll;    
    JScrollPane postnumScroll;    

    public UserSearchJPanel()
    {
	this.repaint();

	//Labels
	emailLabel = new JLabel("Enter emails divided by space (gets users with those emails)");
	topicLabel = new JLabel("Enter topic words divided by space (gets users with those topic words)");
	timestampLabel = new JLabel("Enter a timestamp (gets users that created a post after the timestamp)");
	postnumLabel = new JLabel("Enter an integer (gets users that created at least that many posts within 7 days)");

	//Scroll displays
	resultTextArea = new JTextArea("<User Search Results>");
	resultTextArea.setEditable(false);
	resultTextArea.setLineWrap(true);
	resultTextArea.setWrapStyleWord(false);
	resultScroll = new JScrollPane(resultTextArea);

	emailTextArea = new JTextArea("e.g. john@gmail.com ethan@gmail.com");
	emailTextArea.setLineWrap(true);
	emailTextArea.setWrapStyleWord(false);
	emailScroll = new JScrollPane(emailTextArea);

	topicTextArea = new JTextArea("e.g. soccer basketball kobe");
	topicTextArea.setLineWrap(true);
	topicTextArea.setWrapStyleWord(false);
	topicScroll = new JScrollPane(topicTextArea);

	timestampTextArea = new JTextArea("e.g. 2007-09-23 10:10:10.0");
	timestampTextArea.setLineWrap(true);
	timestampTextArea.setWrapStyleWord(false);
	timestampScroll = new JScrollPane(timestampTextArea);

	postnumTextArea = new JTextArea("e.g. 8");
	postnumTextArea.setLineWrap(true);
	postnumTextArea.setWrapStyleWord(false);
	postnumScroll = new JScrollPane(postnumTextArea);

	//Buttons
	searchButton = new JButton("Search Users");
	backButton = new JButton("Back");

	//Pannels
	gbc = new GridBagConstraints();
	topPanel = new JPanel(new BorderLayout());
	botPanel = new JPanel(new GridBagLayout());

	//set layout manager for this panel
	setLayout(new GridLayout());

	//add components to top panel
	topPanel.add(resultScroll, BorderLayout.CENTER);

	//add components to bot panel
	gbc.fill = GridBagConstraints.HORIZONTAL;
	gbc.gridx = 0;
	gbc.gridy = 0;
	gbc.ipady = 0;
	botPanel.add(emailLabel, gbc);
	gbc.gridx = 0;
	gbc.gridy = 1;
	gbc.ipady = 50;
	botPanel.add(emailScroll, gbc);
	gbc.gridx = 0;
	gbc.gridy = 2;
	gbc.ipady = 0;
	botPanel.add(topicLabel, gbc);
	gbc.gridx = 0;
	gbc.gridy = 3;
	gbc.ipady = 80;
	botPanel.add(topicScroll, gbc);
	gbc.gridx = 0;
	gbc.gridy = 4;
	gbc.ipady = 0;
	botPanel.add(timestampLabel, gbc);
	gbc.gridx = 0;
	gbc.gridy = 5;
	gbc.ipady = 20;
	botPanel.add(timestampScroll, gbc);
	gbc.gridx = 0;
	gbc.gridy = 6;
	gbc.ipady = 0;
	botPanel.add(postnumLabel, gbc);
	gbc.gridx = 0;
	gbc.gridy = 7;
	gbc.ipady = 20;
	botPanel.add(postnumScroll, gbc);
	gbc.gridx = 0;
	gbc.gridy = 8;
	gbc.ipady = 0;
	botPanel.add(searchButton, gbc);
	gbc.gridx = 0;
	gbc.gridy = 9;
	botPanel.add(backButton, gbc);

	topPanel.setOpaque(false);
	botPanel.setOpaque(false);

	//add bot and top panel to this.panel
	add(topPanel);
	add(botPanel);

	//event managers	
        searchButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
			String[] emailArray = emailTextArea.getText().split("\\s+");
			String[] topicArray = topicTextArea.getText().split("\\s+");
			String timestamp = timestampTextArea.getText();
			String postnum_str = postnumTextArea.getText();

                        String result = DBInteractorManagerMode.searchUsers(BuzmoJFrame.con, 
			emailArray, topicArray, timestamp, postnum_str);
                        resultTextArea.setText("<User Search Results>\n\n" + result);
			System.out.println("Search Updated");
                }
        });
        backButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                        BuzmoJFrame.setCurrentPanelTo(new NavigationJPanel());
                }
        });
    }
}

