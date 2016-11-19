package buzmo;
import javax.swing.*;

public class BuzmoLogin
{
	//GUI Constants
	public static JFrame mainWindow;
	public static final int WIDTH = 600;
	public static final int HEIGHT = 400;

	BuzmoLoginScreen cp;

	//Constructor
	public BuzmoLogin()
	{
		mainWindow = new JFrame();
		cp = new BuzmoLoginScreen();
		
		mainWindow.setSize(WIDTH, HEIGHT);
		mainWindow.setResizable(false);
		mainWindow.setTitle("Buzmo");
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWindow.setLocationRelativeTo(null);
		mainWindow.add(cp);
		mainWindow.setVisible(true);
	}

	public static void main(String [] args)
	{
		new BuzmoLogin();
	}

	public static void setCurrentPanelTo(JPanel panel)
	{
		mainWindow.setContentPane(panel);
		mainWindow.revalidate();
		mainWindow.repaint();
		panel.requestFocusInWindow();
	}
}
