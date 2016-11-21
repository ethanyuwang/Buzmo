package buzmo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CircleJPanel extends JPanel
{
    //Java GUI Components
    JLabel circleLabel;

    GridBagConstraints gbc;
    JPanel topPanel;
    JPanel botPanel;

    public CircleJPanel()
    {
	this.repaint();
	circleLabel = new JLabel("Circle");

	gbc = new GridBagConstraints();
	topPanel = new JPanel(new BorderLayout());
	botPanel = new JPanel(new GridBagLayout());

	//set layout manager for this panel
	setLayout(new GridLayout(2,1));

	//set title font and size
	circleLabel.setFont(new Font("Serif", Font.BOLD, 38));
	circleLabel.setVerticalAlignment(SwingConstants.CENTER);
	circleLabel.setHorizontalAlignment(SwingConstants.CENTER);

	//add components to top panel
	topPanel.add(circleLabel, BorderLayout.CENTER);

	topPanel.setOpaque(false);
	botPanel.setOpaque(false);

	//add bot and top panel to this.panel
	add(topPanel);
	add(botPanel);
    }
}
