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
    JButton backButton;
    GridBagConstraints gbc;
    JPanel topPanel;
    JPanel botPanel;

    public NavigationJPanel()
    {
	this.repaint();
	welcomeLabel = new JLabel("Welcome " + BuzmoJFrame.userEmail);
	circleFeedButton = new JButton("Circle Feed");
	privateChatButton = new JButton("Private Chat");
	groupChatButton = new JButton("Group Chat");
	contactsButton = new JButton("Contacts");
	backButton = new JButton("Back");

	gbc = new GridBagConstraints();
	topPanel = new JPanel(new BorderLayout());
	botPanel = new JPanel(new GridBagLayout());

	//set layout manager for this panel
	setLayout(new GridLayout(2,1));

	//set title font and size
	welcomeLabel.setFont(new Font("Serif", Font.BOLD, 38));
	welcomeLabel.setVerticalAlignment(SwingConstants.CENTER);
	welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);

	//add components to top panel
	topPanel.add(welcomeLabel, BorderLayout.CENTER);

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
	botPanel.add(backButton, gbc);

	topPanel.setOpaque(false);
	botPanel.setOpaque(false);

	//add bot and top panel to this.panel
	add(topPanel);
	add(botPanel);

        //event managers
        circleFeedButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                        BuzmoJFrame.setCurrentPanelTo(new CircleFeedJPanel());
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
	backButton.addMouseListener(new MouseAdapter() {
		@Override
		public void mouseReleased(MouseEvent e) {
			BuzmoJFrame.setCurrentPanelTo(new LoginJPanel());
		}
	});
    }
}
