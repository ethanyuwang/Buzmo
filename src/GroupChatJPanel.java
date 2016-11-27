package buzmo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GroupChatJPanel extends JPanel
{
    //set Variables
    int ChatDispalyPanelWidth = (int) (BuzmoJFrame.WIDTH*0.5);
    int ChatControlPanelWidth = (int) (BuzmoJFrame.WIDTH*0.25);
    int GroupControlPanelWidth = (int) (BuzmoJFrame.WIDTH*0.25);
    int pannelHeight = BuzmoJFrame.HEIGHT;

    //Dynamic Variables
    String currentGroupName = "";

    //GUI
    GridBagConstraints gbc;
    JSplitPane ChatDispalyPanel;
    JPanel ChatDispalSubyPanel;

    JPanel ChatControlPanel;
    JPanel GroupControlPanel;
    JPanel ControlPanel;

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
    	groupMembersTextArea = new JTextArea("Members:\n"+DBInteractorGroupChat.getGroupMembers(BuzmoJFrame.con, currentGroupName));
    	groupMembersScroll = new JScrollPane(groupMembersTextArea);

   	//List of GroupChats and select Components
    	groupChatsListTextArea = new JTextArea("Groups:\n"+DBInteractorGroupChat.getGroups(BuzmoJFrame.con));
	historyTextArea.setEditable(false);
	historyTextArea.setLineWrap(true);
	historyTextArea.setWrapStyleWord(false);
 
   	groupChatSelectTextField = new JTextField("Select group to chat with");
 
   	groupChatsListScroll = new JScrollPane(groupChatsListTextArea,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    JScrollBar bar = groupChatsListScroll.getVerticalScrollBar();
	bar.setPreferredSize(new Dimension(40, 60));
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
   	pendingRequestsTextArea = new JTextArea("You're invited to:\n"+DBInteractorGroupChat.getPendingGroupChatInvites(BuzmoJFrame.con));
	pendingRequestsTextArea.setEditable(false);
	pendingRequestsTextArea.setLineWrap(true);
	pendingRequestsTextArea.setWrapStyleWord(false);

    	addTextField = new JTextField("Select a group to add");
    	
	pendingRequestsScroll= new JScrollPane(pendingRequestsTextArea);
    	addScroll= new JScrollPane(addTextField);

    	addButton = new JButton("add");

    //change current group name and duration Components
    changeGroupNameTextField = new JTextField("Change group name to");
    changeGroupDurationTextField = new JTextField("Current duration is " + DBInteractorGroupChat.getGroupChatDurationWrapper(BuzmoJFrame.con, currentGroupName) +" days");
    changeGroupNameScroll = new JScrollPane(changeGroupNameTextField);
    changeGroupDurationScroll = new JScrollPane(changeGroupDurationTextField);
    changeGroupNameBUtton = new JButton("Change");
    changeGroupDurationBUtton = new JButton("Set to");

	//Naviagtion Components
	backButton = new JButton("Back");

	//Pannels
	gbc = new GridBagConstraints();
	ChatDispalSubyPanel = new JPanel(new GridLayout(2,1));
	//ChatDispalyPanel = new JPanel(new BorderLayout());
	//ChatDispalyPanel.setPreferredSize(new Dimension(ChatDispalyPanelWidth, pannelHeight));
	//ChatDispalyPanel.setSize(ChatDispalyPanelWidth, pannelHeight);

	ChatControlPanel = new JPanel(new GridBagLayout());
	//ChatControlPanel.setPreferredSize(new Dimension(ChatControlPanelWidth, pannelHeight));
	ChatControlPanel.setSize(ChatControlPanelWidth, pannelHeight);

	GroupControlPanel = new JPanel(new GridBagLayout());
	//GroupControlPanel.setPreferredSize(new Dimension(GroupControlPanelWidth, pannelHeight));
	GroupControlPanel.setSize(GroupControlPanelWidth, pannelHeight);

	ControlPanel = new JPanel(new GridLayout());

	//set layout manager for this panel
	setLayout(new GridLayout());

	//add components to top panel
	ChatDispalSubyPanel.add(groupChatsListScroll);
	ChatDispalSubyPanel.add(groupMembersScroll);

	//ChatDispalyPanel.setPreferredSize(new java.awt.Dimension((int)(ChatDispalyPanelWidth*0.4), pannelHeight));
	
	ChatDispalyPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, ChatDispalSubyPanel, historyScroll);
	ChatDispalyPanel.setDividerLocation(125);

	//ChatDispalyPanel.add(ChatDispalSubyPanel, BorderLayout.WEST);
	//ChatDispalyPanel.add(historyScroll, BorderLayout.CENTER);

	//add components to med panel
	//Select chat group components
	gbc.gridx = 0;
	gbc.gridy = 0;
	gbc.ipady = 50;
	gbc.ipadx = 70;
	gbc.gridwidth = 3;
	gbc.gridheight = 3;
	ChatControlPanel.add(groupChatSelectScroll, gbc);
	gbc.gridx = 0;
	gbc.gridy = 6;
	gbc.ipady = 0;
	gbc.gridheight = 3;
	ChatControlPanel.add(selectButton, gbc);

	//edit message components
	gbc.gridx = 0;
	gbc.gridy = 9;
	gbc.ipady = 50;
	gbc.ipadx = 70;
	gbc.gridwidth = 3;
	gbc.gridheight = 3;
	gbc.fill = GridBagConstraints.HORIZONTAL;
	ChatControlPanel.add(draftScroll, gbc);
	gbc.gridx = 0;
	gbc.gridy = 12;
	gbc.ipady = 0;
	gbc.gridheight = 3;
	ChatControlPanel.add(sendButton, gbc);

	//Create new group Components
	gbc.gridx = 0;
	gbc.gridy = 15;
	gbc.ipady = 50;
	gbc.ipadx = 80;
	gbc.gridwidth = 3;
	gbc.gridheight = 3;
	gbc.fill = GridBagConstraints.HORIZONTAL;
	ChatControlPanel.add(createScroll, gbc);
	gbc.gridx = 0;
	gbc.gridy = 18;
	gbc.ipady = 0;
	gbc.gridheight = 3;
	ChatControlPanel.add(createButton, gbc);

	//Navigation Components
	gbc.gridx = 0;
	gbc.gridy = 21;
	ChatControlPanel.add(backButton, gbc);

	//add components to bot panel
	GridBagConstraints c = new GridBagConstraints();
	c.fill = GridBagConstraints.HORIZONTAL;
	//answer pending request components
	c.gridx = 0;
	c.gridy = 0;
	c.ipady = 70;
	c.ipadx = 150;
	
	GroupControlPanel.add(pendingRequestsScroll, c);
	c.gridy = 1;
	c.ipady = 40;
	GroupControlPanel.add(addScroll, c);
	c.gridy = 2;
	c.ipady = 0;
	GroupControlPanel.add(addButton, c);

	//edit contact list components
	c.gridy = 3;
	c.ipady = 120;
	GroupControlPanel.add(contactsScroll, c);
	c.gridy = 4;
	c.ipady = 50;
	GroupControlPanel.add(inviteScroll, c);
	c.gridy = 5;
	c.ipady = 0;
	GroupControlPanel.add(inviteButton, c);

	//change current group name and duration Components
	c.gridy = 6;
	c.ipady = 40;
	GroupControlPanel.add(changeGroupNameScroll, c);
	c.gridy = 7;
	c.ipady = 0;
	GroupControlPanel.add(changeGroupNameBUtton, c);
	c.gridy = 8;
	c.ipady = 40;
	GroupControlPanel.add(changeGroupDurationScroll, c);
	c.gridy = 9;
	c.ipady = 0;
	GroupControlPanel.add(changeGroupDurationBUtton, c);

	gbc.gridx = 0;
	gbc.gridy = 0;
	gbc.ipady = 0;
	gbc.ipadx = 0;
	gbc.gridheight = 0;
	gbc.gridwidth = 0;
	ControlPanel.add(ChatControlPanel, gbc);
	gbc.gridx = 1;
	gbc.gridy = 0;
	ControlPanel.add(GroupControlPanel, gbc);

	ChatDispalyPanel.setOpaque(false);
	ChatControlPanel.setOpaque(false);
	GroupControlPanel.setOpaque(false);

	//add bot and top panel to this.panel

	add(ChatDispalyPanel);
	add(ControlPanel);


	//event managers
	createButton.addMouseListener(new MouseAdapter() {
		@Override
		public void mouseReleased(MouseEvent e) {
			String temp = createField.getText();
			Boolean complete = DBInteractorGroupChat.createGroup(BuzmoJFrame.con, temp);
			if(complete){
				historyTextArea.append("You created chat group: "+temp+"\n");
				groupChatsListTextArea.setText("Groups:\n"+DBInteractorGroupChat.getGroups(BuzmoJFrame.con));
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
			Boolean complete = DBInteractorGroupChat.isGroup(BuzmoJFrame.con, temp);
			if(complete){
				currentGroupName=temp;
				changeGroupDurationTextField.setText("Current duration is " + DBInteractorGroupChat.getGroupChatDurationWrapper(BuzmoJFrame.con, currentGroupName) +" days");
				groupMembersTextArea.setText("Members:\n"+DBInteractorGroupChat.getGroupMembers(BuzmoJFrame.con, currentGroupName));
				historyTextArea.setText("You entered chat group: "+temp+"\n");
				historyTextArea.append(DBInteractorGroupChat.loadGroupChatHistory(BuzmoJFrame.con, currentGroupName));
			}
			else{
				historyTextArea.append("Can not find chat group: "+temp+"\n");
			}
		}
	});
	inviteButton.addMouseListener(new MouseAdapter() {
		@Override
		public void mouseReleased(MouseEvent e) {
			if(currentGroupName.equals("")){
				historyTextArea.append("Please select a group\n");
				return;
			}
			String temp = inviteTextField.getText();
			Boolean complete = DBInteractorGroupChat.inviteToGroupChat(BuzmoJFrame.con, currentGroupName, temp);
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
			Boolean complete = DBInteractorGroupChat.addGroupChat(BuzmoJFrame.con, temp);
			if(complete){
				historyTextArea.append("You added chat group: "+temp+"\n");
			}
			else{
				historyTextArea.append("Can not add chat group: "+temp+"\n");
			}
			addTextField.setText("Select a group to add");
		}
	});

	sendButton.addMouseListener(new MouseAdapter() {
		@Override
		public void mouseReleased(MouseEvent e) {
			if(currentGroupName.equals("")){
				historyTextArea.append("Please select a group\n");
				return;
			}
			Boolean complete = DBInteractorGroupChat.addMessageToGroupChat(BuzmoJFrame.con, draftTextField.getText(), currentGroupName);
			if(complete){
				historyTextArea.setText(DBInteractorGroupChat.loadGroupChatHistory(BuzmoJFrame.con, currentGroupName));
			}
			else{
				historyTextArea.append("Sending message failed\n");
			}
		}
	});

	changeGroupNameBUtton.addMouseListener(new MouseAdapter() {
		@Override
		public void mouseReleased(MouseEvent e) {
			if(currentGroupName.equals("")){
				historyTextArea.append("Please select a group\n");
				return;
			}
			String temp = changeGroupNameTextField.getText();
			Boolean complete = DBInteractorGroupChat.changeGroupChatName(BuzmoJFrame.con, currentGroupName, temp);
			if(complete){
				groupChatsListTextArea.setText("Groups:\n"+DBInteractorGroupChat.getGroups(BuzmoJFrame.con));
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
			if(currentGroupName.equals("")){
				historyTextArea.append("Please select a group\n");
				return;
			}
			String temp = changeGroupDurationTextField.getText();
			Boolean complete = DBInteractorGroupChat.changeGroupChatDuration(BuzmoJFrame.con, currentGroupName, temp);
			if(complete){
				changeGroupDurationTextField.setText("Current duration is " + DBInteractorGroupChat.getGroupChatDurationWrapper(BuzmoJFrame.con, currentGroupName) +" days");
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
