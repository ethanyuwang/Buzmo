package buzmo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

public class CirclePostJPanel extends JPanel
{
   /* //JPanel
    GridBagConstraints gbc;
    JPanel topPanel;
    JPanel botPanel;

    //Naviagtion Components
    JButton backButton;
    JButton createButton;
    JButton searchButton;
    JButton deleteButton;

    //Chat dispaly and control Components
    JLabel shareLabel;
    JLabel draftLabel;
    JLabel topicLabel;
    JLabel searchLabel;
    JTextArea historyTextArea;
    JTextArea draftTextArea;
    JTextArea topicTextArea;
    JTextArea searchTextArea;
    JScrollPane historyScroll;
    JScrollPane draftScroll;
    JScrollPane topicScroll;
    JScrollPane searchScroll;
    JCheckBox publicCheckBox;
    CheckBoxList checkBoxList;
    JCheckBox[] cbList;
    String[] strList;

    public CirclePostJPanel()
    {
	this.repaint();

	//CheckBoxList
	checkBoxList = new CheckBoxList();
	strList = DBInteractor.getContactListsAsArray(BuzmoJFrame.con);
	if(strList != null){ // if user has friends
		checkBoxList = new CheckBoxList();
		cbList = new JCheckBox[strList.length + 1];
		cbList[0] = new JCheckBox("All");
		for(int i=0; i<strList.length; i++){
			cbList[i+1] = new JCheckBox(strList[i]);
		}
	}
	else{
		cbList = new JCheckBox[1];
		cbList[0] = new JCheckBox("All");
	}
        checkBoxList.setListData(cbList);

	//Scroll displays
	historyTextArea = new JTextArea("<Circle Feed>\n" + DBInteractor.getCircleFeedHistory(BuzmoJFrame.con));
	historyTextArea.setEditable(false);
	historyTextArea.setLineWrap(true);
	historyTextArea.setWrapStyleWord(false);
	historyScroll = new JScrollPane(historyTextArea);

	draftTextArea= new JTextArea("Enter message");
	draftTextArea.setLineWrap(true);
	draftTextArea.setWrapStyleWord(false);
	draftScroll = new JScrollPane(draftTextArea);
	
	topicTextArea= new JTextArea("Enter topic words divided by space");
	topicTextArea.setLineWrap(true);
	topicTextArea.setWrapStyleWord(false);
	topicScroll = new JScrollPane(topicTextArea);

	searchTextArea= new JTextArea(DBInteractor.defaultUsersTopicWords(BuzmoJFrame.con));
	searchTextArea.setLineWrap(true);
	searchTextArea.setWrapStyleWord(false);
	searchScroll = new JScrollPane(searchTextArea);

	//Naviagtion Components
	createButton = new JButton("Create");
	searchButton = new JButton("Search");
	backButton = new JButton("Back");
	deleteButton = new JButton("Delete");

	//ETC
	shareLabel = new JLabel("Share with: ");
	draftLabel = new JLabel("Message: ");
	topicLabel = new JLabel("Topic words: ");
	searchLabel = new JLabel("Search with topic words: ");
	publicCheckBox = new JCheckBox("Public");

	//Pannels
	gbc = new GridBagConstraints();
	topPanel = new JPanel(new BorderLayout());
	botPanel = new JPanel(new GridBagLayout());

	//set layout manager for this panel
	setLayout(new GridLayout());

	//add components to top panel
	topPanel.add(historyScroll, BorderLayout.CENTER);

	//add components to bot panel
	gbc.fill = GridBagConstraints.HORIZONTAL;
	gbc.gridx = 0;
	gbc.gridy = 0;
	botPanel.add(shareLabel, gbc);
	gbc.gridx = 0;
	gbc.gridy = 1;
	gbc.ipady = 50;
	gbc.ipadx = 300;
	botPanel.add(checkBoxList, gbc);
	gbc.gridx = 0;
	gbc.gridy = 2;
	gbc.ipady = 0;
	gbc.ipadx = 0;
	botPanel.add(draftLabel, gbc);
	gbc.gridx = 0;
	gbc.gridy = 3;
	gbc.ipady = 50;
	gbc.ipadx = 300;
	botPanel.add(draftScroll, gbc);
	gbc.gridx = 0;
	gbc.gridy = 4;
	gbc.ipady = 0;
	gbc.ipadx = 0;
	botPanel.add(topicLabel, gbc);
	gbc.gridx = 0;
	gbc.gridy = 5;
	gbc.ipady = 50;
	gbc.ipadx = 300;
	botPanel.add(topicScroll, gbc);
	gbc.gridx = 0;
	gbc.gridy = 6;
	gbc.ipady = 0;
	gbc.ipadx = 0;
	botPanel.add(publicCheckBox, gbc);
	gbc.gridx = 0;
	gbc.gridy = 7;
	botPanel.add(createButton, gbc);
	gbc.gridx = 0;
	gbc.gridy = 9;
	botPanel.add(searchLabel, gbc);
	gbc.gridx = 0;
	gbc.gridy = 10;
	gbc.ipady = 50;
	gbc.ipadx = 300;
	botPanel.add(searchScroll, gbc);
	gbc.gridx = 0;
	gbc.gridy = 11;
	gbc.ipady = 0;
	gbc.ipadx = 0;
	botPanel.add(searchButton, gbc);
	gbc.gridx = 0;
	gbc.gridy = 12;
	botPanel.add(deleteButton, gbc);
	gbc.gridx = 0;
	gbc.gridy = 13;
	botPanel.add(backButton, gbc);

	topPanel.setOpaque(false);
	botPanel.setOpaque(false);

	//add bot and top panel to this.panel
	add(topPanel);
	add(botPanel);

	//event managers	
	createButton.addMouseListener(new MouseAdapter() {
		@Override
		public void mouseReleased(MouseEvent e) {
			String msgStr = draftTextArea.getText();
			String[] topicArray = topicTextArea.getText().split("\\s+");		
			Boolean publicChecked = publicCheckBox.isSelected();
			// Create circle post & handle topic word links to user/message
			int post_id = DBInteractor.createCirclePost(BuzmoJFrame.con, msgStr, topicArray, publicChecked);
			if(post_id == -1){System.out.println("FAILED to create circle post"); return;}
			System.out.println("SUCCESS created circle post");
			// Link circle feed to viewers
			for(int i=0; i<cbList.length; i++){
				//cbList length has due to "All"
				if(i==0){
					if(DBInteractor.linkReceiversToCirclePost(BuzmoJFrame.con, BuzmoJFrame.userEmail, post_id)){
						System.out.println("SUCCESS linked user to circle feed");}
					else{System.out.println("FAILED to link user to circle feed"); return;}	
				}
				else if(cbList[i].isSelected()){
					if(DBInteractor.linkReceiversToCirclePost(BuzmoJFrame.con, strList[i-1], post_id)){
						System.out.println("SUCCESS linked user to circle feed");}
					else{System.out.println("FAILED to link user to circle feed"); return;}	
				}
			}
			// reload
			System.out.println("Reloading circle posts");
			historyTextArea.setText("<Circle Feed>\n" + DBInteractor.getCircleFeedHistory(BuzmoJFrame.con));
		}
	});
        searchButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
			String[] topicArray = searchTextArea.getText().split("\\s+");
			String found = DBInteractor.searchCirclePosts(BuzmoJFrame.con, topicArray);
			System.out.println("Reloading circle posts");
			historyTextArea.setText("<Circle Feed>\n" + found);	
                }
        });
        backButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                        BuzmoJFrame.setCurrentPanelTo(new NavigationJPanel());
                }
        });
	cbList[0].addItemListener(new ItemListener() {
		@Override
		public void itemStateChanged(ItemEvent e) {
			if(e.getStateChange() == ItemEvent.SELECTED) {
				for(int i=1; i<cbList.length; i++){
					cbList[i].setSelected(true);
				}
			}
			else {
				for(int i=1; i<cbList.length; i++){
					cbList[i].setSelected(false);
				}
			}
		}
	});
    }*/
}
