package buzmo;

import java.sql.*;
import java.io.*;
import java.net.*;
import java.lang.*;

public class DBInteractorPrivateChat {
	//Used for PrivateChatJPanel 
	public static Boolean addMessageToPrivateChat(Connection con, String message, String recipientEmail){
		try {
			String myEmail = BuzmoJFrame.userEmail;
			Timestamp ts = DBInteractor.getCurrentTimeStamp();
			String messageWithTime = message+ts.toString();

			//Add a copy to sender
			String sql = "INSERT INTO MESSAGES VALUES (?,?,?,?,?,?,?,?)";	
			PreparedStatement ps = con.prepareStatement(sql);
			con.setAutoCommit(false);
			String messageWithTimeAndOwner = messageWithTime + myEmail;
			ps.setInt(1, messageWithTimeAndOwner.hashCode());
			ps.setString(2, message);
			ps.setTimestamp(3, ts);
			ps.setString(4, "private");
			ps.setString(5, myEmail);
			ps.setString(6, myEmail);
			ps.setString(7, recipientEmail);
			ps.setNull(8, java.sql.Types.INTEGER);
			ps.addBatch();
			ps.executeBatch();
			con.commit();

			//Add a copy to recipient
			ps = con.prepareStatement(sql);
			messageWithTimeAndOwner = messageWithTime + recipientEmail;
			ps.setInt(1, messageWithTimeAndOwner.hashCode());
			ps.setString(2, message);
			ps.setTimestamp(3, ts);
			ps.setString(4, "private");
			ps.setString(5, recipientEmail);
			ps.setString(6, myEmail);
			ps.setString(7, recipientEmail);
			ps.setNull(8, java.sql.Types.INTEGER);
			ps.addBatch();
			ps.executeBatch();
			con.commit();
			con.setAutoCommit(true);
			return true;
		}
		catch(Exception e){System.out.println(e); return false;}
	}

	//Used for PrivateChatJPanel 
	public static String loadChatHistory(Connection con, String recipientEmail){
		try {
			String ret = "";
			String myEmail = BuzmoJFrame.userEmail;	
			Statement st = con.createStatement();
			String sql = "SELECT M.text_string, M.sender, M.timestamp FROM MESSAGES M WHERE " +
			"M.type='private' AND M.owner='" + myEmail + "' AND " + 
			"((M.sender='" + myEmail + "' AND " + 
			" M.receiver='" + recipientEmail + "') " +
			"OR " +
			"(M.sender='" + recipientEmail + "' AND " + 
			" M.receiver='" + myEmail + "')) ORDER BY M.timestamp";
			ResultSet rs = st.executeQuery(sql);			
			while(rs.next()){
				ret += rs.getString(2) + " (";
				ret += rs.getString(3) + "): ";
				ret += rs.getString(1) + "\n";
			}
			return ret;
		}
		catch(Exception e){System.out.println(e); return "Failed to load messages\n";}
	}
}
