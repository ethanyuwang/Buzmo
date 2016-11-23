package buzmo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PrivateChatJPanel extends JPanel
{
    //Java GUI Components
    JButton loginButton;
    JButton signUpButton;
    JLabel titleLabel;
    JTextField emailField;
    JTextField passField;

    GridBagConstraints gbc;
    JPanel topPanel;
    JPanel botPanel;

    //Naviagtion Components
    JButton backButton;

    //Chat dispaly and control Components
    JTextField draftTextArea;
    JTextArea historyTextArea;
    JScrollPane draftScroll;
    JScrollPane historyScroll;
    JButton sentButton;

    //List of freinds and select Components
    JTextField draftTextArea;
    JTextArea historyTextArea;

    public PrivateChatJPanel()
    {
	this.repaint();

	//Chat dispaly and control Components
	draftTextArea = new JTextField("Enter message");

	historyTextArea = new JTextArea("Hello\nHello\nHello\n...\n");
	historyTextArea.setEditable(false);
	historyTextArea.setLineWrap(true);
	historyTextArea.setWrapStyleWord(false);

	draftScroll = new JScrollPane(draftTextArea);
	historyScroll = new JScrollPane(historyTextArea);

	sentButton = new JButton("Sent");

	//Naviagtion Components
	backButton = new JButton("Back");

	gbc = new GridBagConstraints();
	topPanel = new JPanel(new BorderLayout());
	botPanel = new JPanel(new GridBagLayout());

	//set layout manager for this panel
	setLayout(new GridLayout());

	/*//set title font and size
	titleLabel.setFont(new Font("Serif", Font.BOLD, 38));
	titleLabel.setVerticalAlignment(SwingConstants.CENTER);
	titleLabel.setHorizontalAlignment(SwingConstants.CENTER);*/

	//add components to top panel
	//topPanel.add(titleLabel, BorderLayout.PAGE_START);
	topPanel.add(historyScroll, BorderLayout.CENTER);

	//add components to bot panel


	gbc.gridx = 3;
	gbc.gridy = 1;
	gbc.gridwidth = 1;
	gbc.fill = GridBagConstraints.HORIZONTAL;
	botPanel.add(draftScroll, gbc);
	gbc.gridx = 3;
	gbc.gridy = 2;
	botPanel.add(sentButton, gbc);
	gbc.gridx = 3;
	gbc.gridy = 3;
	botPanel.add(backButton, gbc);

	topPanel.setOpaque(false);
	botPanel.setOpaque(false);

	//add bot and top panel to this.panel
	add(topPanel);
	add(botPanel);

	//event managers
	backButton.addMouseListener(new MouseAdapter() {
		@Override
		public void mouseReleased(MouseEvent e) {
			BuzmoJFrame.setCurrentPanelTo(new NavigationJPanel());
		}
	});
	sentButton.addMouseListener(new MouseAdapter() {
		@Override
		public void mouseReleased(MouseEvent e) {
			/*Boolean complete = DBInteractor.loginUser(BuzmoJFrame.con, emailField.getText(), passField.getText());
			if(complete){
				System.out.println("Login SUCCESS");
				BuzmoJFrame.setCurrentPanelTo(new NavigationJPanel());
			}
			else{
				System.out.println("Login FAIL");
			}*/
		}
	});
    }
}
