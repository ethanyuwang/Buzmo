package buzmo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SignUpJPanel extends JPanel
{
    //Java GUI Components
    JButton signUpButton;
    JLabel signUpLabel;
    JTextField emailField;
    JTextField passField;
    JTextField nameField;
    JTextField screennameField;
    JTextField phoneField;

    GridBagConstraints gbc;
    JPanel topPanel;
    JPanel botPanel;

    public SignUpJPanel()
    {
	this.repaint();
	signUpButton = new JButton("Sign up");
	signUpLabel = new JLabel("Sign up");

	// User Info
	emailField = new JTextField("email");
	passField = new JTextField("password");
	nameField = new JTextField("name");
	phoneField = new JTextField("phone number");
	screennameField = new JTextField("screenname (optional)");

	gbc = new GridBagConstraints();
	topPanel = new JPanel(new BorderLayout());
	botPanel = new JPanel(new GridBagLayout());

	//set layout manager for this panel
	setLayout(new GridLayout(2,1));

	//set title font and size
	signUpLabel.setFont(new Font("Serif", Font.BOLD, 38));
	signUpLabel.setVerticalAlignment(SwingConstants.CENTER);
	signUpLabel.setHorizontalAlignment(SwingConstants.CENTER);

	//add components to top panel
	topPanel.add(signUpLabel, BorderLayout.CENTER);

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
	botPanel.add(nameField, gbc);
	gbc.gridx = 3;
	gbc.gridy = 4;
	botPanel.add(phoneField, gbc);
	gbc.gridx = 3;
	gbc.gridy = 5;
	botPanel.add(screennameField, gbc);
	gbc.gridx = 3;
	gbc.gridy = 6;
	botPanel.add(signUpButton, gbc);

	topPanel.setOpaque(false);
	botPanel.setOpaque(false);

	//add bot and top panel to this.panel
	add(topPanel);
	add(botPanel);

	//event managers
	signUpButton.addMouseListener(new MouseAdapter() {
		@Override
		public void mouseReleased(MouseEvent e) {
			Boolean complete = DBInteractor.addUser(BuzmoJFrame.con, emailField.getText(), passField.getText(),
				nameField.getText(), phoneField.getText(), screennameField.getText());
			if(complete){
				BuzmoJFrame.setCurrentPanelTo(new LoginJPanel());
			}
		}
	});
    }
}
