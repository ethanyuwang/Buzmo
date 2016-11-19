package buzmo;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BuzmoLoginScreen extends JPanel
{
    //Java GUI Components
    JButton loginButton;
    JButton signUpButton;
    JLabel titleLabel;

    GridBagConstraints gbc;
    JPanel topPanel;
    JPanel botPanel;

    public BuzmoLoginScreen()
    {
	this.repaint();
	loginButton = new JButton("Login");
	signUpButton = new JButton("Sign up");
	titleLabel = new JLabel("Buzmo");
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
	botPanel.add(loginButton, gbc);
	gbc.gridx = 3;
	gbc.gridy = 2;
	botPanel.add(signUpButton, gbc);

	topPanel.setOpaque(false);
	botPanel.setOpaque(false);

	//add bot and top panel to this.panel
	add(topPanel);
	add(botPanel);

	//event managers
	
    }
}
