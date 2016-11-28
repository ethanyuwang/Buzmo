package buzmo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class NavigationJPanel extends JPanel
{
    //Java GUI Components
    JLabel welcomeLabel;
    JButton circleFeedButton;
    JButton privateChatButton;
    JButton groupChatButton;
    JButton contactsButton;
    JButton signOutButton;

    GridBagConstraints gbc;
    JPanel topPanel;
    JPanel botPanel;

    //Topic words
    JButton addButton;
    JTextArea historyTextArea;
    JTextArea draftTextArea;
    JScrollPane historyScroll;
    JScrollPane draftScroll;

    public NavigationJPanel()
    {
	this.repaint();
	welcomeLabel = new JLabel("Welcome " + BuzmoJFrame.userEmail);
	circleFeedButton = new JButton("Circle Feed");
	privateChatButton = new JButton("Private Chat");
	groupChatButton = new JButton("Group Chat");
	contactsButton = new JButton("Contacts");
	signOutButton = new JButton("Sign out");

	//Topic words
	addButton = new JButton("Add Topic Words");
        historyTextArea = new JTextArea("<Topic Words>\n" + DBInteractor.getUserTopicWords(BuzmoJFrame.con));
        historyTextArea.setEditable(false);
        historyTextArea.setLineWrap(true);
        historyTextArea.setWrapStyleWord(false);
        historyScroll = new JScrollPane(historyTextArea);
        draftTextArea= new JTextArea("Enter topic words divided by space");
        draftTextArea.setLineWrap(true);
        draftTextArea.setWrapStyleWord(false);
        draftScroll = new JScrollPane(draftTextArea);

	//WelcomeLabel font and size
	welcomeLabel.setFont(new Font("Serif", Font.BOLD, 28));
	welcomeLabel.setVerticalAlignment(SwingConstants.CENTER);
	welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);

	// Panels
	gbc = new GridBagConstraints();
	topPanel = new JPanel(new GridBagLayout());
	botPanel = new JPanel(new GridBagLayout());

	//add components to top panel
	gbc.gridx = 0;
	gbc.gridy = 0;
        gbc.ipady = 50;
        gbc.ipadx = 300;
	gbc.fill = GridBagConstraints.HORIZONTAL;
	topPanel.add(welcomeLabel, gbc);
	gbc.gridx = 0;
	gbc.gridy = 1;
        gbc.ipady = 100;
	topPanel.add(historyScroll, gbc);
	gbc.gridx = 0;
	gbc.gridy = 2;
        gbc.ipady = 50;
	topPanel.add(draftScroll, gbc);
	gbc.gridx = 0;
	gbc.gridy = 3;
	gbc.ipady = 0;
	topPanel.add(addButton, gbc);	

	//set layout manager for this panel
	setLayout(new GridLayout(2,1));

	//add components to bot panel
	gbc.gridx = 3;
	gbc.gridy = 1;
	gbc.gridwidth = 1;
	gbc.fill = GridBagConstraints.HORIZONTAL;
	botPanel.add(circleFeedButton, gbc);
	gbc.gridx = 3;
	gbc.gridy = 2;
	botPanel.add(privateChatButton, gbc);
	gbc.gridx = 3;
	gbc.gridy = 3;
	botPanel.add(groupChatButton, gbc);
	gbc.gridx = 3;
	gbc.gridy = 4;
	botPanel.add(contactsButton, gbc);
	gbc.gridx = 3;
	gbc.gridy = 5;
	botPanel.add(signOutButton, gbc);

	topPanel.setOpaque(false);
	botPanel.setOpaque(false);

	//add bot and top panel to this.panel
	add(topPanel);
	add(botPanel);

        //event managers
        addButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
			String[] topicArray = draftTextArea.getText().split("\\s+");
			if(DBInteractor.addUserTopicWords(BuzmoJFrame.con, topicArray)){
				historyTextArea.setText("<Topic Words>\n" + DBInteractor.getUserTopicWords(BuzmoJFrame.con));
			}
			else{
				historyTextArea.setText("FAILED to add topic words");
			}
                }
        });
        circleFeedButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                        BuzmoJFrame.setCurrentPanelTo(new CirclePostJPanel());
                }
        });
        privateChatButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                        BuzmoJFrame.setCurrentPanelTo(new PrivateChatJPanel());
                }
        });
        groupChatButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                        BuzmoJFrame.setCurrentPanelTo(new GroupChatJPanel());
                }
        });
        contactsButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                        BuzmoJFrame.setCurrentPanelTo(new ContactsJPanel());
                }
        });
	signOutButton.addMouseListener(new MouseAdapter() {
		@Override
		public void mouseReleased(MouseEvent e) {
			BuzmoJFrame.setCurrentPanelTo(new LoginJPanel());
		}
	});
    }
}
