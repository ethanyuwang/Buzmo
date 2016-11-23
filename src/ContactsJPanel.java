package buzmo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ContactsJPanel extends JPanel
{
    //Java GUI Components
    JLabel titleLabel;
    JTextArea contactTextArea;
    JTextArea pendingTextArea;
    JScrollPane contactScroll;
    JScrollPane pendingScroll;
    JTextField requestField;
    JTextField acceptField;
    JButton requestButton;
    JButton acceptButton;
    JButton backButton;

    GridBagConstraints gbc;
    JSplitPane topPanel;
    JPanel botPanel;

    public ContactsJPanel()
    {
	this.repaint();
	titleLabel = new JLabel("CONTACTS");
	contactTextArea = new JTextArea(DBInteractor.getContactLists(BuzmoJFrame.con));
	contactTextArea.setEditable(false);
	contactTextArea.setLineWrap(true);
	contactTextArea.setWrapStyleWord(false);
	pendingTextArea = new JTextArea(DBInteractor.getContactPendingLists(BuzmoJFrame.con));
	pendingTextArea.setEditable(false);
	pendingTextArea.setLineWrap(true);
	pendingTextArea.setWrapStyleWord(false);
	contactScroll = new JScrollPane(contactTextArea);
	pendingScroll = new JScrollPane(pendingTextArea);
	requestField = new JTextField("enter_email");
	acceptField = new JTextField("enter_email");
	requestButton = new JButton("Send Friend Request");
	acceptButton = new JButton("Accept Friend Request");
	backButton = new JButton("Back");

	gbc = new GridBagConstraints();
	topPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, contactScroll, pendingScroll);
	botPanel = new JPanel(new GridBagLayout());

	//set layout manager for this panel
	setLayout(new GridLayout(2,1));

	//add components to top panel
	topPanel.setDividerLocation(300);

	//add components to bot panel
	gbc.gridx = 3;
	gbc.gridy = 1;
	gbc.gridwidth = 1;
	gbc.fill = GridBagConstraints.HORIZONTAL;
	botPanel.add(requestField, gbc);
	gbc.gridx = 3;
	gbc.gridy = 2;
	botPanel.add(requestButton, gbc);
	gbc.gridx = 3;
	gbc.gridy = 3;
	botPanel.add(acceptField, gbc);
	gbc.gridx = 3;
	gbc.gridy = 4;
	botPanel.add(acceptButton, gbc);
	gbc.gridx = 3;
	gbc.gridy = 5;
	botPanel.add(backButton, gbc);

	//add bot and top panel to this.panel
	add(topPanel);
	add(botPanel);

	//event managers
	requestButton.addMouseListener(new MouseAdapter() {
		@Override
		public void mouseReleased(MouseEvent e) {
			Boolean complete = DBInteractor.requestContact(BuzmoJFrame.con, requestField.getText());
			if(complete){
				System.out.println("Request Contact SUCCESS");
				BuzmoJFrame.setCurrentPanelTo(new ContactsJPanel());
			}
			else{
				System.out.println("Request Contact FAIL");
			}
		}
	});
	acceptButton.addMouseListener(new MouseAdapter() {
		@Override
		public void mouseReleased(MouseEvent e) {
			Boolean complete = DBInteractor.acceptContact(BuzmoJFrame.con, acceptField.getText());
			if(complete){
				System.out.println("Accept Contact SUCCESS");
				BuzmoJFrame.setCurrentPanelTo(new ContactsJPanel());
			}
			else{
				System.out.println("Accept Contact FAIL");
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
