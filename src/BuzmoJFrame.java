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
	public static Timestamp base_time;
	public static Timestamp start_time;
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

		// Exiting
		mainWindow.addWindowListener(new java.awt.event.WindowAdapter() {
    			@Override
    			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				DBInteractor.updateBaseTime(con);
			}
		});
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
		base_time = DBInteractor.getBaseTime(con);
		java.util.Date today = new java.util.Date();
		start_time = new Timestamp(today.getTime());
		new BuzmoJFrame();
	}
}
