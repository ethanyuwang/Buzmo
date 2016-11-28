package buzmo;

import javax.swing.*;
import java.sql.*;

public class BuzmoJFrame
{
	//GUI Constants
	public static JFrame mainWindow;
	public static final int WIDTH = 1200;
	public static final int HEIGHT = 700;
	// Database
	public static Connection con;
	public static String userEmail;
	public static boolean is_manager;
	LoginJPanel cp;

	//Constructor
	public BuzmoJFrame()
	{
		mainWindow = new JFrame();
		cp = new LoginJPanel();
		
		mainWindow.setSize(WIDTH, HEIGHT);
		mainWindow.setResizable(false);
		mainWindow.setTitle("Buzmo");
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWindow.setLocationRelativeTo(null);
		mainWindow.add(cp);
		mainWindow.setVisible(true);
	}

	public static void setCurrentPanelTo(JPanel panel)
	{
		mainWindow.setContentPane(panel);
		mainWindow.revalidate();
		mainWindow.repaint();
		panel.requestFocusInWindow();
	}

	public static void main(String [] args)
	{
		con = DBInteractor.connectToDB();
		new BuzmoJFrame();
	}
}
