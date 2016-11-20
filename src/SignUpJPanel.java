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

    GridBagConstraints gbc;
    JPanel topPanel;
    JPanel botPanel;

    public SignUpJPanel()
    {
	this.repaint();
	signUpButton = new JButton("Sign up");
	signUpLabel = new JLabel("Sign up");
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
	botPanel.add(signUpButton, gbc);

	topPanel.setOpaque(false);
	botPanel.setOpaque(false);

	//add bot and top panel to this.panel
	add(topPanel);
	add(botPanel);

	//event managers
    }
}
