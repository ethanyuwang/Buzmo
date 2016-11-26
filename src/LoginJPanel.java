package buzmo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginJPanel extends JPanel
{
    //Java GUI Components
    JButton loginButton;
    JButton signUpButton;
    JButton loadDBButton;
    JLabel titleLabel;
    JTextField emailField;
    JTextField passField;


    GridBagConstraints gbc;
    JPanel topPanel;
    JPanel botPanel;

    public LoginJPanel()
    {
	this.repaint();
	loginButton = new JButton("Login");
	signUpButton = new JButton("Sign up");
	loadDBButton = JButton("Load Existing Database");
	titleLabel = new JLabel("Buzmo");
	emailField = new JTextField("email");
	passField = new JTextField("password");

	gbc = new GridBagConstraints();
	topPanel = new JPanel(new BorderLayout());
	botPanel = new JPanel(new GridBagLayout());

	//set layout manager for this panel
	setLayout(new GridLayout(2,1));

	//set title font and size
	titleLabel.setFont(new Font("Serif", Font.BOLD, 38));
	titleLabel.setVerticalAlignment(SwingConstants.CENTER);
	titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

	//add components to top panel
	topPanel.add(titleLabel, BorderLayout.CENTER);

	//add components to bot panel
	gbc.gridx = 3;
	gbc.gridy = 1;
	gbc.gridwidth = 1;
	gbc.fill = GridBagConstraints.HORIZONTAL;
	botPanel.add(emailField, gbc);
	gbc.gridx = 3;
	gbc.gridy = 2;
	botPanel.add(passField, gbc);
	gbc.gridx = 3;
	gbc.gridy = 3;
	botPanel.add(loginButton, gbc);
	gbc.gridx = 3;
	gbc.gridy = 4;
	botPanel.add(signUpButton, gbc);
	gbc.gridx = 3;
	gbc.gridy = 5;
	botPanel.add(loadDBButton, gbc);

	topPanel.setOpaque(false);
	botPanel.setOpaque(false);

	//add bot and top panel to this.panel
	add(topPanel);
	add(botPanel);

	//event managers
	signUpButton.addMouseListener(new MouseAdapter() {
		@Override
		public void mouseReleased(MouseEvent e) {
			BuzmoJFrame.setCurrentPanelTo(new SignUpJPanel());
		}
	});
	loginButton.addMouseListener(new MouseAdapter() {
		@Override
		public void mouseReleased(MouseEvent e) {
			Boolean complete = DBInteractor.loginUser(BuzmoJFrame.con, emailField.getText(), passField.getText());
			if(complete){
				System.out.println("Login SUCCESS");
				BuzmoJFrame.setCurrentPanelTo(new NavigationJPanel());
			}
			else{
				System.out.println("Login FAIL");
			}
		}
	});
	loadDBButton.addMouseListener(new MouseAdapter() {
		@Override
		public void mouseReleased(MouseEvent e) {
			Boolean complete = DBInteractor.loadDB(BuzmoJFrame.con);
			if(complete){
				System.out.println("Load existing data base SUCCESS");
			}
			else{
				System.out.println("Load existing data base FAIL");
			}
		}
	});
    }
}
