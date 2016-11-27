package buzmo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GroupChatDeleteJPanel extends JPanel
{
    //Dynamic Variables
    String currentGroupName = "";

    GridBagConstraints gbc;
    JPanel topPanel;
    JPanel botPanel;

    //Button Components
    JButton backButton;
    JButton deleteButton;
    JButton selectButton;

    //Chat dispaly and control Components
    JTextArea historyTextArea;
    JScrollPane historyScroll;
    JTextArea draftTextArea;
    JScrollPane draftScroll;

    //List of freinds and select Components
    JTextArea contactsTextArea;
    JScrollPane contactsScroll;
    JTextArea recipientTextArea;
    JScrollPane recipientScroll;

    public GroupChatDeleteJPanel()
    {
	this.repaint();

	//Chat dispaly and control Components
	historyTextArea = new JTextArea("Please select a group chat\n");
	historyTextArea.setEditable(false);
	historyTextArea.setLineWrap(true);
	historyTextArea.setWrapStyleWord(false);
	historyScroll = new JScrollPane(historyTextArea);

	draftTextArea = new JTextArea("Enter message_id");
	draftTextArea.setLineWrap(true);
	draftTextArea.setWrapStyleWord(false);
	draftScroll = new JScrollPane(draftTextArea);

	//Contacts dispaly and select Components
	contactsTextArea = new JTextArea("<Group Chat List>\n"+DBInteractorGroupChat.getGroups(BuzmoJFrame.con));
	contactsTextArea.setEditable(false);
	contactsTextArea.setLineWrap(true);
	contactsTextArea.setWrapStyleWord(false);
	contactsScroll = new JScrollPane(contactsTextArea);

	recipientTextArea = new JTextArea("Enter group chat name");
	recipientTextArea.setLineWrap(true);
	recipientTextArea.setWrapStyleWord(false);
	recipientScroll = new JScrollPane(recipientTextArea);

	//Button Components
	backButton = new JButton("Back");
	deleteButton = new JButton("Delete");
	selectButton = new JButton("Select Group Chat");

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
	gbc.ipadx = 100;
	gbc.gridwidth = 1;
	gbc.gridheight = 3;
	gbc.fill = GridBagConstraints.HORIZONTAL;
	botPanel.add(draftScroll, gbc);
	gbc.gridx = 0;
	gbc.gridy = 3;
	gbc.ipady = 0;
	botPanel.add(deleteButton, gbc);
	gbc.gridx = 0;
	gbc.gridy = 6;
	botPanel.add(backButton, gbc);

	//contact list components
	gbc.gridx = 3;
	gbc.gridy = 0;
	gbc.ipady = 200;
	gbc.ipadx = 100;
	gbc.gridwidth = 1;
	gbc.gridheight = 3;
	gbc.fill = GridBagConstraints.HORIZONTAL;
	botPanel.add(contactsScroll, gbc);
	gbc.gridx = 3;
	gbc.gridy = 3;
	gbc.ipady = 50;
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
                        String temp = recipientTextArea.getText();
                        Boolean complete = DBInteractorGroupChat.isGroup(BuzmoJFrame.con, temp);
                        if(complete){
                                currentGroupName=temp;
                                historyTextArea.setText("<Chat group: "+temp+">\nChoose a message to delete\n\n");
                                historyTextArea.append(DBInteractorGroupChat.loadUsersMessages(BuzmoJFrame.con, currentGroupName));
                        }
                        else{
                                historyTextArea.append("Can not find chat group: "+temp+"\n");
                        }
                }
        });
	deleteButton.addMouseListener(new MouseAdapter() {
		@Override
		public void mouseReleased(MouseEvent e) {
			if(DBInteractorGroupChat.deleteGroupMessage(BuzmoJFrame.con, draftTextArea.getText())) {
				draftTextArea.setText("Deletion complete");
				//reload
                                historyTextArea.setText("<Chat group: "+currentGroupName+">\nChoose a message to delete\n\n");
                                historyTextArea.append(DBInteractorGroupChat.loadUsersMessages(BuzmoJFrame.con, currentGroupName));
                        }
                        else {
                                draftTextArea.setText("Failed to delete: double check your message_id\n");
                        }
		}
	});
	backButton.addMouseListener(new MouseAdapter() {
		@Override
		public void mouseReleased(MouseEvent e) {
			BuzmoJFrame.setCurrentPanelTo(new GroupChatJPanel());
		}
	});
    }
}
