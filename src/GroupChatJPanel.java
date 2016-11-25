package buzmo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GroupChatJPanel extends JPanel
{
    //Variables
    String currentGroupName = "";

    GridBagConstraints gbc;
    JPanel topPanel;
    JPanel topWestPanel;
    JPanel medPanel;
    JPanel botPanel;

    //Naviagtion Components
    JButton backButton;

    //Chat dispaly and control Components
    JTextArea historyTextArea;
    JTextField draftTextField;
    JScrollPane historyScroll;
    JScrollPane draftScroll;
    JButton sendButton;

    //Create new group Components
    JTextField createField;
    JScrollPane createScroll;
    JButton createButton;

    //List of GroupMembers Components
    JTextArea groupMembersTextArea;
    JScrollPane groupMembersScroll;

    //List of GroupChats and select Components
    JTextArea groupChatsListTextArea;
    JTextField groupChatSelectTextField;
    JScrollPane groupChatsListScroll;
    JScrollPane groupChatSelectScroll;
    JButton selectButton;

    //List of pending request and add Components
    JTextArea pendingRequestsTextArea;
    JTextField addTextField;
    JScrollPane pendingRequestsScroll;
    JScrollPane addScroll;
    JButton addButton;

    //List of contacts and invite Components
    JTextArea contactsTextArea;
    JTextField inviteTextField;
    JScrollPane contactsScroll;
    JScrollPane inviteScroll;
    JButton inviteButton;

    //change current group name and duration Components
    JTextField changeGroupNameTextField;
    JTextField changeGroupDurationTextField;
    JScrollPane changeGroupNameScroll;
    JScrollPane changeGroupDurationScroll;
    JButton changeGroupNameBUtton;
    JButton changeGroupDurationBUtton;



    public GroupChatJPanel()
    {
	this.repaint();

	//Chat dispaly and control Components
	historyTextArea = new JTextArea("Select a chat group to start\n");
	historyTextArea.setEditable(false);
	historyTextArea.setLineWrap(true);
	historyTextArea.setWrapStyleWord(false);

	draftTextField= new JTextField("Enter message");

	historyScroll = new JScrollPane(historyTextArea);
	draftScroll = new JScrollPane(draftTextField);

	sendButton = new JButton("Send");
	
	//Create new group Components
    	createField = new JTextField("New chat group");
    	createScroll = new JScrollPane(createField);
    	createButton = new JButton("Create");

    	//List of GroupMembers Components
    	groupMembersTextArea = new JTextArea("Members:\n"+DBInteractor.getGroupMembers(BuzmoJFrame.con, currentGroupName));
    	groupMembersScroll = new JScrollPane(groupMembersTextArea);

   	//List of GroupChats and select Components
    	groupChatsListTextArea = new JTextArea("Groups:\n"+DBInteractor.getGroups(BuzmoJFrame.con));
	historyTextArea.setEditable(false);
	historyTextArea.setLineWrap(true);
	historyTextArea.setWrapStyleWord(false);
 
   	groupChatSelectTextField = new JTextField("Select group to chat with");
 
   	groupChatsListScroll = new JScrollPane(groupChatsListTextArea);
    	groupChatSelectScroll = new JScrollPane(groupChatSelectTextField);
    	selectButton = new JButton("Select");

	//contacts dispaly and invite Components
	contactsTextArea = new JTextArea("Contacts:\n"+DBInteractor.getContactLists(BuzmoJFrame.con));
	contactsTextArea.setEditable(false);
	contactsTextArea.setLineWrap(true);
	contactsTextArea.setWrapStyleWord(false);

	inviteTextField = new JTextField("Enter friend's email");

	contactsScroll = new JScrollPane(contactsTextArea);
	inviteScroll = new JScrollPane(inviteTextField);

	inviteButton = new JButton("Invite");

    	//List of request and add Components
   	pendingRequestsTextArea = new JTextArea("You're invited to the following groups:\n"+DBInteractor.getPendingGroupChatInvites(BuzmoJFrame.con));
	pendingRequestsTextArea.setEditable(false);
	pendingRequestsTextArea.setLineWrap(true);
	pendingRequestsTextArea.setWrapStyleWord(false);

    	addTextField = new JTextField("Select a group to add");
    	
	pendingRequestsScroll= new JScrollPane(pendingRequestsTextArea);
    	addScroll= new JScrollPane(addTextField);

    	addButton = new JButton("add");

    //change current group name and duration Components
    changeGroupNameTextField = new JTextField("Change group name to");
    changeGroupDurationTextField = new JTextField("Current duration is " + DBInteractor.getGroupChatDuration(BuzmoJFrame.con, currentGroupName) +" days");
    changeGroupNameScroll = new JScrollPane(changeGroupNameTextField);
    changeGroupDurationScroll = new JScrollPane(changeGroupDurationTextField);
    changeGroupNameBUtton = new JButton("Change");
    changeGroupDurationBUtton = new JButton("Set to");

	//Naviagtion Components
	backButton = new JButton("Back");

	//Pannels
	gbc = new GridBagConstraints();
	topWestPanel = new JPanel(new BorderLayout());
	topPanel = new JPanel(new BorderLayout());
	medPanel = new JPanel(new GridBagLayout());
	botPanel = new JPanel(new GridBagLayout());

	//set layout manager for this panel
	setLayout(new GridLayout());

	//add components to top panel
	topWestPanel.add(groupChatsListScroll, BorderLayout.NORTH);
	topWestPanel.add(groupMembersScroll, BorderLayout.CENTER);
	topPanel.add(historyScroll, BorderLayout.CENTER);
	topPanel.add(topWestPanel, BorderLayout.WEST);

	//add components to med panel
	//Select chat group components
	gbc.gridx = 0;
	gbc.gridy = 0;
	gbc.ipady = 50;
	gbc.ipadx = 70;
	gbc.gridwidth = 3;
	gbc.gridheight = 3;
	gbc.fill = GridBagConstraints.HORIZONTAL;
	medPanel.add(groupChatSelectScroll, gbc);
	gbc.gridx = 0;
	gbc.gridy = 3;
	gbc.ipady = 0;
	gbc.gridheight = 3;
	medPanel.add(selectButton, gbc);

	//edit message components
	gbc.gridx = 0;
	gbc.gridy = 0;
	gbc.ipady = 50;
	gbc.ipadx = 70;
	gbc.gridwidth = 3;
	gbc.gridheight = 3;
	gbc.fill = GridBagConstraints.HORIZONTAL;
	medPanel.add(draftScroll, gbc);
	gbc.gridx = 0;
	gbc.gridy = 3;
	gbc.ipady = 0;
	gbc.gridheight = 3;
	medPanel.add(sendButton, gbc);

	//Create new group Components
	gbc.gridx = 0;
	gbc.gridy = 6;
	gbc.ipady = 50;
	gbc.ipadx = 70;
	gbc.gridwidth = 3;
	gbc.gridheight = 3;
	gbc.fill = GridBagConstraints.HORIZONTAL;
	medPanel.add(createScroll, gbc);
	gbc.gridx = 0;
	gbc.gridy = 9;
	gbc.ipady = 0;
	gbc.gridheight = 3;
	medPanel.add(createButton, gbc);

	//Navigation Components
	gbc.gridx = 0;
	gbc.gridy = 12;
	medPanel.add(backButton, gbc);

	//add components to bot panel
	//answer pending request components
	gbc.gridx = 3;
	gbc.gridy = 0;
	gbc.ipady = 50;
	gbc.gridwidth = 1;
	gbc.gridheight = 3;
	gbc.fill = GridBagConstraints.HORIZONTAL;
	botPanel.add(pendingRequestsScroll, gbc);
	gbc.gridx = 3;
	gbc.gridy = 3;
	gbc.gridheight = 3;
	gbc.ipady = 20;
	botPanel.add(addScroll, gbc);
	gbc.gridx = 3;
	gbc.gridy = 6;
	gbc.ipady = 0;
	botPanel.add(addButton, gbc);

	//edit contact list components
	gbc.gridx = 3;
	gbc.gridy = 10;
	gbc.ipady = 50;
	gbc.gridwidth = 1;
	gbc.gridheight = 3;
	gbc.fill = GridBagConstraints.HORIZONTAL;
	botPanel.add(contactsScroll, gbc);
	gbc.gridx = 3;
	gbc.gridy = 13;
	gbc.gridheight = 3;
	gbc.ipady = 20;
	botPanel.add(inviteScroll, gbc);
	gbc.gridx = 3;
	gbc.gridy = 16;
	gbc.ipady = 0;
	botPanel.add(inviteButton, gbc);

	//change current group name and duration Components
	gbc.gridx = 3;
	gbc.gridy = 19;
	gbc.gridheight = 3;
	gbc.ipady = 20;
	botPanel.add(changeGroupNameScroll, gbc);
	gbc.gridx = 3;
	gbc.gridy = 22;
	gbc.ipady = 0;
	botPanel.add(changeGroupNameBUtton, gbc);
	gbc.gridx = 3;
	gbc.gridy = 23;
	gbc.gridheight = 3;
	gbc.ipady = 20;
	botPanel.add(changeGroupDurationScroll, gbc);
	gbc.gridx = 3;
	gbc.gridy = 27;
	gbc.ipady = 0;
	botPanel.add(changeGroupDurationBUtton, gbc);

	topPanel.setOpaque(false);
	medPanel.setOpaque(false);
	botPanel.setOpaque(false);

	//add bot and top panel to this.panel
	add(topPanel);
	add(medPanel);
	add(botPanel);

	//event managers
	createButton.addMouseListener(new MouseAdapter() {
		@Override
		public void mouseReleased(MouseEvent e) {
			String temp = createField.getText();
			Boolean complete = DBInteractor.createGroup(BuzmoJFrame.con, temp);
			if(complete){
				historyTextArea.append("You created chat group: "+temp+"\n");
				groupChatsListTextArea.setText("Groups:\n"+DBInteractor.getGroups(BuzmoJFrame.con));
			}
			else{
				historyTextArea.append("Can not create chat group: "+temp+"\n");
			}
		}
	});
	selectButton.addMouseListener(new MouseAdapter() {
		@Override
		public void mouseReleased(MouseEvent e) {
			String temp = groupChatSelectTextField.getText();
			Boolean complete = DBInteractor.isGroup(BuzmoJFrame.con, temp);
			if(complete){
				currentGroupName=temp;
				historyTextArea.setText("You entered chat group: "+temp+"\n");
				historyTextArea.append(DBInteractor.loadGroupChatHistory(BuzmoJFrame.con, currentGroupName));
			}
			else{
				historyTextArea.append("Can not find chat group: "+temp+"\n");
			}
		}
	});
	inviteButton.addMouseListener(new MouseAdapter() {
		@Override
		public void mouseReleased(MouseEvent e) {
			String temp = inviteTextField.getText();
			Boolean complete = DBInteractor.inviteToGroupChat(BuzmoJFrame.con, currentGroupName, temp);
			if(complete){
				historyTextArea.append("You invited "+temp+" to this chat group\n");
			}
			else{
				historyTextArea.append("Can not invite "+temp+" to this chat group:\n");
			}
		}
	});
	addButton.addMouseListener(new MouseAdapter() {
		@Override
		public void mouseReleased(MouseEvent e) {
			String temp = addTextField.getText();
			Boolean complete = DBInteractor.addGroupChat(BuzmoJFrame.con, temp);
			if(complete){
				historyTextArea.append("You added chat group: "+temp+"\n");
			}
			else{
				historyTextArea.append("Can not add chat group: "+temp+"\n");
			}
		}
	});

	sendButton.addMouseListener(new MouseAdapter() {
		@Override
		public void mouseReleased(MouseEvent e) {
			if(currentGroupName.equals("")){
				historyTextArea.append("Please select a group\n");
				return;
			}
			Boolean complete = DBInteractor.addMessageToGroupChat(BuzmoJFrame.con, draftTextField.getText(), currentGroupName);
			if(complete){
				historyTextArea.setText(DBInteractor.loadGroupChatHistory(BuzmoJFrame.con, currentGroupName));
			}
			else{
				historyTextArea.append("Sending message failed\n");
			}
		}
	});

	changeGroupNameBUtton.addMouseListener(new MouseAdapter() {
		@Override
		public void mouseReleased(MouseEvent e) {
			String temp = changeGroupNameTextField.getText();
			Boolean complete = DBInteractor.changeGroupChatName(BuzmoJFrame.con, currentGroupName, temp);
			if(complete){
				historyTextArea.append("You changed current group name to: "+temp+"\n");
			}
			else{
				historyTextArea.append("Change current group name to: "+temp+"failed\n");
			}
		}
	});
	changeGroupDurationBUtton.addMouseListener(new MouseAdapter() {
		@Override
		public void mouseReleased(MouseEvent e) {
			String temp = changeGroupDurationTextField.getText();
			Boolean complete = DBInteractor.changeGroupChatDuration(BuzmoJFrame.con, currentGroupName, temp);
			if(complete){
				historyTextArea.append("You changed current group duration to: "+temp+"\n");
			}
			else{
				historyTextArea.append("Change current group duration to: "+temp+"failed\n");
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
