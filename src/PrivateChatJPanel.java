package buzmo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PrivateChatJPanel extends JPanel
{
    //Variables
    String recipientEmail = "";

    GridBagConstraints gbc;
    JPanel topPanel;
    JPanel botPanel;

    //Naviagtion Components
    JButton backButton;

    //Chat dispaly and control Components
    JTextArea historyTextArea;
    JTextField draftTextField;
    JScrollPane historyScroll;
    JScrollPane draftScroll;
    JButton sendButton;

    //List of freinds and select Components
    JTextArea contactsTextArea;
    JTextField recipientTextField;
    JScrollPane contactsScroll;
    JScrollPane recipientScroll;
    JButton selectButton;

    public PrivateChatJPanel()
    {
	this.repaint();

	//Chat dispaly and control Components
	historyTextArea = new JTextArea("Select a friend\n");
	historyTextArea.setEditable(false);
	historyTextArea.setLineWrap(true);
	historyTextArea.setWrapStyleWord(false);

	draftTextField= new JTextField("Enter message");

	historyScroll = new JScrollPane(historyTextArea);
	draftScroll = new JScrollPane(draftTextField);

	sendButton = new JButton("Send");

	//Naviagtion Components
	backButton = new JButton("Back");

	//contacts dispaly and select Components
	contactsTextArea = new JTextArea(DBInteractor.getContactLists(BuzmoJFrame.con));
	contactsTextArea.setEditable(false);
	contactsTextArea.setLineWrap(true);
	contactsTextArea.setWrapStyleWord(false);

	recipientTextField = new JTextField("Enter friend's email");

	contactsScroll = new JScrollPane(contactsTextArea);
	recipientScroll = new JScrollPane(recipientTextField);

	selectButton = new JButton("Select");

	//Pannels
	gbc = new GridBagConstraints();
	topPanel = new JPanel(new BorderLayout());
	botPanel = new JPanel(new GridBagLayout());

	//set layout manager for this panel
	setLayout(new GridLayout());

	//add components to top panel
	//topPanel.add(titleLabel, BorderLayout.PAGE_START);
	topPanel.add(historyScroll, BorderLayout.CENTER);

	//add components to bot panel
	//edit message components
	gbc.gridx = 0;
	gbc.gridy = 0;
	gbc.ipady = 50;
	gbc.ipadx = 70;
	gbc.gridwidth = 3;
	gbc.gridheight = 3;
	gbc.fill = GridBagConstraints.HORIZONTAL;
	botPanel.add(draftScroll, gbc);
	gbc.gridx = 0;
	gbc.gridy = 3;
	gbc.ipady = 0;
	gbc.gridheight = 3;
	botPanel.add(sendButton, gbc);
	gbc.gridx = 0;
	gbc.gridy = 6;
	botPanel.add(backButton, gbc);

	//edit contact list components
	gbc.gridx = 3;
	gbc.gridy = 0;
	gbc.ipady = 50;
	gbc.gridwidth = 1;
	gbc.gridheight = 3;
	gbc.fill = GridBagConstraints.HORIZONTAL;
	botPanel.add(contactsScroll, gbc);
	gbc.gridx = 3;
	gbc.gridy = 3;
	gbc.gridheight = 3;
	gbc.ipady = 20;
	botPanel.add(recipientScroll, gbc);
	gbc.gridx = 3;
	gbc.gridy = 6;
	gbc.ipady = 0;
	botPanel.add(selectButton, gbc);

	topPanel.setOpaque(false);
	botPanel.setOpaque(false);

	//add bot and top panel to this.panel
	add(topPanel);
	add(botPanel);

	//event managers
	selectButton.addMouseListener(new MouseAdapter() {
		@Override
		public void mouseReleased(MouseEvent e) {
			String temp = recipientTextField.getText();
			Boolean complete = DBInteractor.isContact(BuzmoJFrame.con, temp);
			if(complete){
				recipientEmail = temp;
				historyTextArea.setText("<Chat with "+recipientEmail+">\n"+DBInteractor.loadChatHistory(BuzmoJFrame.con, recipientEmail));
			}
			else{
				historyTextArea.append("Can not find "+temp+" in your contact list\n");
			}
		}
	});
	sendButton.addMouseListener(new MouseAdapter() {
		@Override
		public void mouseReleased(MouseEvent e) {
			if(recipientEmail.equals("")){
				historyTextArea.append("Please select a friend\n");
				return;
			}
			Boolean complete = DBInteractor.addMessageToPrivateChat(BuzmoJFrame.con, draftTextField.getText(), recipientEmail);
			if(complete){
				historyTextArea.setText("<Chat with "+recipientEmail+">\n"+DBInteractor.loadChatHistory(BuzmoJFrame.con, recipientEmail));
			}
			else{
				historyTextArea.append("Sending message failed\n");
			}
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
