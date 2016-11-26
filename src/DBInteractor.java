package buzmo;

import java.sql.*;
import java.io.*;
import java.net.*;

public class DBInteractor {

	public static Connection connectToDB(){
		try {	
                        Class.forName("oracle.jdbc.driver.OracleDriver");
                        String url = "jdbc:oracle:thin:@uml.cs.ucsb.edu:1521:xe";
			String username = "glee";
			String password = "304";
                        Connection con = DriverManager.getConnection(url,username, password);
			return con;
		}
		catch(Exception e){System.out.println(e); return null;}
	}
	
	public static Boolean addUser(Connection con, String email, String pass, 
				String name, String phone, String screenname){
		try {
			Statement st = con.createStatement();
			// Note: insert order needs to match table column order
			String sql = "INSERT INTO USERS " +
			"VALUES ('" + email + "', " +
			"'" + pass + "', " +
			"'" + name + "', " +
			phone + ", " +
			"'" + screenname + "')";
			st.executeUpdate(sql);
			return true;
		}
		catch(Exception e){System.out.println(e); return false;}
	}

	public static Boolean loginUser(Connection con, String email, String pass){
		try {
			Statement st = con.createStatement();
			String sql = "SELECT U.PASSWORD FROM USERS U WHERE U.EMAIL_ADDRESS='" + email + "'";
			ResultSet rs = st.executeQuery(sql);
			if(rs.next()){
				System.out.println(rs.getString(1));
				System.out.println(pass);
				if (pass.equals(rs.getString(1))){
					BuzmoJFrame.userEmail = email;
					return true;
				}
			}
			return false;
		}
		catch(Exception e){System.out.println(e); return false;}
	}
	
	public static Boolean requestContact(Connection con, String requestEmail){
		try {
			String senderEmail = BuzmoJFrame.userEmail;
			Statement st = con.createStatement();
			int count;
			// check contact list
			String sql = "SELECT count(*) FROM CONTACT_LISTS C WHERE " +
			"C.owner='" + senderEmail + "' AND " + 
			"C.friend='" + requestEmail + "'";
			ResultSet rs = st.executeQuery(sql);
			if(rs.next()){
				count = rs.getInt(1);
				if(count > 0){
					return false;
				}
			}
			else{return false;}
			// check pending list
			sql = "SELECT count(*) FROM CONTACT_PENDING_LISTS C WHERE " +
			"(C.receiver='" + requestEmail + "' AND " +
			" C.sender='" + senderEmail + "') " + 
			"OR " +
			"(C.receiver='" + senderEmail + "' AND " +
			" C.sender='" + requestEmail + "')"; 
			rs = st.executeQuery(sql);			
			if(rs.next()){
				count = rs.getInt(1);
				if(count > 0){
					return false;
				}
			}
			else{return false;}
			// add to table
			sql = "INSERT INTO CONTACT_PENDING_LISTS " +
			"VALUES ('" + requestEmail + "', " +
			" '" + senderEmail + "')";
			st.executeUpdate(sql);
			return true;
		}
		catch(Exception e){System.out.println(e); return false;}
	}
	public static Boolean acceptContact(Connection con, String acceptEmail){
		try {
			String receiverEmail = BuzmoJFrame.userEmail;
			Statement st = con.createStatement();
			// check pending list
			String sql = "SELECT count(*) FROM CONTACT_PENDING_LISTS C WHERE " +
			"(C.receiver='" + receiverEmail + "' AND " +
			" C.sender='" + acceptEmail + "')"; 
			ResultSet rs = st.executeQuery(sql);			
			if(rs.next()){
				int count = rs.getInt(1);
				if(count != 1){
					return false;
				}
			}
			// remove from pending list
			sql = "DELETE FROM CONTACT_PENDING_LISTS C " +
			"WHERE C.receiver='" + receiverEmail + "' " + 
			"AND C.sender='" + acceptEmail + "'";
			st.executeUpdate(sql);
			// add to contact list both ways
			sql = "INSERT INTO CONTACT_LISTS " + 
			"VALUES ('" + acceptEmail + "', " + 
			" '" + receiverEmail + "')";
			st.executeUpdate(sql);
			sql = "INSERT INTO CONTACT_LISTS " + 
			"VALUES ('" + receiverEmail + "', " + 
			" '" + acceptEmail + "')";
			st.executeUpdate(sql);
			return true;
		}
		catch(Exception e){System.out.println(e); return false;}
	}

	//Used for ContactsJPannel, PrivateChatJPanel, GroupChatJPanel
	public static String getContactLists(Connection con){
		try {
			String ret = "";
			String myEmail = BuzmoJFrame.userEmail;
			Statement st = con.createStatement();
			String sql = "SELECT C.friend FROM CONTACT_LISTS C " +
			"WHERE C.owner='" + myEmail + "'";
			ResultSet rs = st.executeQuery(sql);
			while(rs.next()){
				ret += rs.getString(1);
				ret += "\n";
			}
			return ret;
		}
		catch(Exception e){System.out.println(e); return "";}
	}

	public static String getContactPendingLists(Connection con){
		try {
			String ret = "";
			String myEmail = BuzmoJFrame.userEmail;
			Statement st = con.createStatement();
			String sql = "SELECT C.sender FROM CONTACT_PENDING_LISTS C " +
			"WHERE C.receiver='" + myEmail + "'";
			ResultSet rs = st.executeQuery(sql);
			while(rs.next()){
				ret += rs.getString(1);
				ret += "\n";
			}
			return ret;
		}
		catch(Exception e){System.out.println(e); return "";}
	}
	//Used for PrivateChatJPanel 
	public static Boolean isContact(Connection con, String recipientEmail){
		try {
			String myEmail = BuzmoJFrame.userEmail;	
			Statement st = con.createStatement();
			String sql = "SELECT count(*) FROM CONTACT_LISTS C WHERE " +
			"(C.owner='" + myEmail + "' AND " +
			" C.friend='" + recipientEmail + "')"; 
			ResultSet rs = st.executeQuery(sql);			
			if(rs.next()){
				int count = rs.getInt(1);
				if(count != 1){
					return false;
				}
			}
			return true;
		}
		catch(Exception e){System.out.println(e); return false;}
	}

	//Used for PrivateChatJPanel 
	public static Boolean addMessageToPrivateChat(Connection con, String message, String recipientEmail){
		try {
			String myEmail = BuzmoJFrame.userEmail;
			Timestamp ts = getCurrentTimeStamp();
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
			ps.setString(5, "N/A");
			ps.setString(6, myEmail);
			ps.setString(7, myEmail);
			ps.setString(8, recipientEmail);
			ps.addBatch();
			ps.executeBatch();
			con.commit();

			//Add a copy to recipient
			ps = con.prepareStatement(sql);
			messageWithTimeAndOwner = messageWithTime + recipientEmail;
			ps.setInt(1, messageWithTimeAndOwner.hashCode());
			ps.setInt(1, messageWithTime.hashCode());
			ps.setString(2, message);
			ps.setTimestamp(3, ts);
			ps.setString(4, "private");
			ps.setString(5, "N/A");
			ps.setString(6, recipientEmail);
			ps.setString(7, myEmail);
			ps.setString(8, recipientEmail);
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
	

	//Used for GroupChatJPanel 
	public static Boolean createGroup(Connection con, String groupName){
		try { 
			Statement st = con.createStatement();
			// Note: insert order needs to match table column order
			String sql = "INSERT INTO Group_chats " +
			"VALUES (" + groupName.hashCode() + ", '" +
			groupName + "', " +
			7 + ", " +
			"'" + BuzmoJFrame.userEmail + "')";
			st.executeUpdate(sql);
			return true;
		}
		catch(Exception e){System.out.println(e); return false;}
}
	//Used for GroupChatJPanel 
	public static String getGroups(Connection con){
		try {
			String ret = "";
			String myEmail = BuzmoJFrame.userEmail;
			Statement st = con.createStatement();
			String sql = "SELECT C.group_name FROM Group_chats C " +
			"WHERE C.owner='" + myEmail + "'";
			ResultSet rs = st.executeQuery(sql);
			while(rs.next()){
				ret += rs.getString(1);
				ret += "\n";
			}
			return ret;
		}
		catch(Exception e){System.out.println(e); return "";}
}
	//Used for GroupChatJPanel 
	public static String getGroupID(Connection conm, String groupName){
		/*try {
			int ret = -100;
			String myEmail = BuzmoJFrame.userEmail;
			Statement st = con.createStatement();
			String sql = "SELECT C.group_name FROM Group_chats C " +
			"WHERE C.group_name='" + groupName + "'";
			ResultSet rs = st.executeQuery(sql);
			ret = rs.getString(1);
			return ret;
		}
		catch(Exception e){System.out.println(e); return ret;}*/
		return "Error";
}
	//Used for GroupChatJPanel 
	public static Boolean changeGroupChatName(Connection con, String groupName, String newGroupName){
		/*try {
			String myEmail = BuzmoJFrame.userEmail;	
			Statement st = con.createStatement();
			String sql = "UPDATE Group_chats SET group_name = '" +
			newGroupName + "' WHERE owner='" + myEmail + "' AND group_name = '"+
			groupName + "')"; 
			st.executeQuery(sql);			
			return true;
		}
		catch(Exception e){System.out.println(e); return false;}*/
		return false;
}
	//Used for GroupChatJPanel 
	public static Boolean changeGroupChatDuration(Connection con, String groupName, String newDuration){
		/*try {
			String myEmail = BuzmoJFrame.userEmail;	
			Statement st = con.createStatement();
			String sql = "UPDATE Group_chats SET duration = " +
			newDuration + " WHERE owner='" + myEmail + "' AND group_name = '"+
			groupName + "')"; 
			st.executeQuery(sql);			
			return true;
		}
		catch(Exception e){System.out.println(e); return false;}*/
		return false;
}
	//Used for GroupChatJPanel 
	public static Boolean isGroup(Connection con, String groupName){
		try {
			String myEmail = BuzmoJFrame.userEmail;	
			Statement st = con.createStatement();
			String sql = "SELECT C.group_name FROM Group_chats C WHERE " +
			"(C.owner='" + myEmail + "')"; 
			ResultSet rs = st.executeQuery(sql);			
			while(rs.next()){
				if (groupName==rs.getString(1)){
					return true;
				}
				else{
					return false;
				}
			}
			return true;
		}
		catch(Exception e){System.out.println(e); return false;}
}
	public static int getGroupChatDuration(Connection con, String groupName){
		/*try {
			String myEmail = BuzmoJFrame.userEmail;	
			Statement st = con.createStatement();
			String sql = "SELECT C.duration FROM Group_chats C WHERE " +
			"(C.owner='" + myEmail + "' AND C.group_name='" + groupName + "')"; 
			ResultSet rs = st.executeQuery(sql);			
			return rs.getInt(1));
		}
		catch(Exception e){System.out.println(e); return -10;}*/
		return -10;
}

	public static String getGroupChatDurationWrapper(Connection con, String groupName){
		/*try {
			int	duration = getGroupChatDuration(con, groupName)
			if (duration>0) {
				return String(duration);
			}
			else {
				return "Error";
			}
			return rs.getInt(1));
		}
		catch(Exception e){System.out.println(e); return "Error";;}*/
		return "Error";
}
	//Used for GroupChatJPanel 
	public static Boolean addMessageToGroupChat(Connection con, String message, String groupName){
		/*try {
			String myEmail = BuzmoJFrame.userEmail;
			Timestamp ts = getCurrentTimeStamp();
			String messageWithTime = message+ts.toString();

			//Add a copy to sender
			String sql = "INSERT INTO MESSAGES VALUES (?,?,?,?,?,?,?,?)";	
			PreparedStatement ps = con.prepareStatement(sql);
			con.setAutoCommit(false);
			String messageWithTimeAndOwner = messageWithTime + myEmail;
			ps.setInt(1, messageWithTimeAndOwner.hashCode());
			ps.setString(2, message);
			ps.setTimestamp(3, ts);
			ps.setString(4, "group");
			ps.setString(5, "N/A");
			ps.setString(6, myEmail);
			ps.setString(7, myEmail);
			ps.setString(8, groupName);
			ps.addBatch();
			ps.executeBatch();
			con.commit();
			return true;
		}
		catch(Exception e){System.out.println(e); return false;}*/
		return false;
}

	//Used for GroupChatJPanel 
	public static String loadGroupChatHistory(Connection con, String groupName){
		/*try {
			//cleanOldGroupChatHistory(con, groupName);
			String ret = "";
			String myEmail = BuzmoJFrame.userEmail;	
			Statement st = con.createStatement();
			String sql = "SELECT M.text_string, M.sender, M.timestamp FROM MESSAGES M WHERE " +
			"M.type='group' AND M.owner='" + groupName + "' AND " + 
			"((M.sender='" + myEmail +
			"')) ORDER BY M.timestamp";
			ResultSet rs = st.executeQuery(sql);			
			while(rs.next()){
				ret += rs.getString(2) + " (";
				ret += rs.getString(3) + "): ";
				ret += rs.getString(1) + "\n";
			}
			return ret;
		}
		catch(Exception e){System.out.println(e); return "";}*/
		return "Error";
}

	public static Boolean cleanOldGroupChatHistory(Connection con, String groupName){
		/*try {
			int currentDuration = getGroupChatDuration(con, groupName);

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
		catch(Exception e){System.out.println(e); return "";}*/
		return true;
}
	//Used for GroupChatJPanel 
	public static Boolean inviteToGroupChat(Connection con, String groupName, String requestEmail){
		/*try {
			String senderEmail = BuzmoJFrame.userEmail;
			Statement st = con.createStatement();
			int count;
			// check contact list
			String sql = "SELECT count(*) FROM CONTACT_LISTS C WHERE " +
			"C.owner='" + senderEmail + "' AND " + 
			"C.friend='" + requestEmail + "'";
			ResultSet rs = st.executeQuery(sql);
			if(rs.next()){
				count = rs.getInt(1);
				if(count > 0){
					return false;
				}
			}
			else{return false;}
			// check pending list
			sql = "SELECT count(*) FROM Group_pending_lists C WHERE " +
			"(C.receiver='" + requestEmail + "' AND " +
			" C.sender='" + senderEmail + "') " + 
			"OR " +
			"(C.receiver='" + senderEmail + "' AND " +
			" C.sender='" + requestEmail + "')"; 
			rs = st.executeQuery(sql);			
			if(rs.next()){
				count = rs.getInt(1);
				if(count > 0){
					return false;
				}
			}
			else{return false;}
			// add to table
			sql = "INSERT INTO CONTACT_PENDING_LISTS " +
			"VALUES ('" + requestEmail + "', " +
			" '" + senderEmail + "')";
			st.executeUpdate(sql);
			return true;
		}
		catch(Exception e){System.out.println(e); return false;}*/
		return true;
	}

	//Used for GroupChatJPanel 
	public static Boolean addGroupChat(Connection con, String groupName){
		/*try {
			String receiverEmail = BuzmoJFrame.userEmail;
			Statement st = con.createStatement();
			// check pending list
			String sql = "SELECT count(*) FROM CONTACT_PENDING_LISTS C WHERE " +
			"(C.receiver='" + receiverEmail + "' AND " +
			" C.sender='" + acceptEmail + "')"; 
			ResultSet rs = st.executeQuery(sql);			
			if(rs.next()){
				int count = rs.getInt(1);
				if(count != 1){
					return false;
				}
			}
			// remove from pending list
			sql = "DELETE FROM CONTACT_PENDING_LISTS C " +
			"WHERE C.receiver='" + receiverEmail + "' " + 
			"AND C.sender='" + acceptEmail + "'";
			st.executeUpdate(sql);
			// add to contact list both ways
			sql = "INSERT INTO CONTACT_LISTS " + 
			"VALUES ('" + acceptEmail + "', " + 
			" '" + receiverEmail + "')";
			st.executeUpdate(sql);
			sql = "INSERT INTO CONTACT_LISTS " + 
			"VALUES ('" + receiverEmail + "', " + 
			" '" + acceptEmail + "')";
			st.executeUpdate(sql);
			return true;
		}
		catch(Exception e){System.out.println(e); return false;}*/
		return true;
	}
	//Used for GroupChatJPanel 
	public static String getGroupMembers(Connection con, String currentGroupName){
		/*try {
			String ret = "";
			String myEmail = BuzmoJFrame.userEmail;
			Statement st = con.createStatement();
			String sql = "SELECT C.friend FROM CONTACT_LISTS C " +
			"WHERE C.owner='" + myEmail + "'";
			ResultSet rs = st.executeQuery(sql);
			while(rs.next()){
				ret += rs.getString(1);
				ret += "\n";
			}
			return ret;
		}
		catch(Exception e){System.out.println(e); return "";}*/
		return "empty";
}


	//Used for GroupChatJPanel 
	public static String getPendingGroupChatInvites(Connection con){
		/*try {
			String ret = "";
			String myEmail = BuzmoJFrame.userEmail;
			Statement st = con.createStatement();
			String sql = "SELECT C.sender FROM CONTACT_PENDING_LISTS C " +
			"WHERE C.receiver='" + myEmail + "'";
			ResultSet rs = st.executeQuery(sql);
			while(rs.next()){
				ret += rs.getString(1);
				ret += "\n";
			}
			return ret;
		}
		catch(Exception e){System.out.println(e); return "";}
	}*/
		return ":";
	}



	//Helper functions
	private static Timestamp getCurrentTimeStamp() {
		java.util.Date today = new java.util.Date();
		return new Timestamp(today.getTime());
	}

	public enum loadDBControl {
		none, addUser, addContacts, addPrivateMessages, addGroups, addCircle, addTopicWords, addManager, finish
	} 

	public static Boolean loadDB(Connection con){
		
		loadDBControl ldbc=loadDBControl.none;
		String database="resource/data_base_sampler.txt";
		//URL url=getClass().getResource(database);
		//File db = new File(url.getPath());
		File db = new File(database);

		try (BufferedReader br = new BufferedReader(new FileReader(db))){
			String line;
			line = br.readLine();
			if (line==null) 
				System.out.println("null file");

			while ((line = br.readLine())!=null){
				System.out.println(line);
				//decide what to do
				if (line=="Users")
				{
					ldbc=loadDBControl.addUser;
					System.out.println("Start adding" + line);
					continue;
				}
				else if(line=="Friends")
				{
					ldbc=loadDBControl.addContacts;
					System.out.println("Start adding" + line);
					continue;
				}
				else if(line=="Private_Messages")
				{
					ldbc=loadDBControl.addPrivateMessages;
					continue;
				}
				else if(line=="Chat_groups")
				{
					ldbc=loadDBControl.addGroups;
					continue;
				}
				else if(line=="Circle_feeds")
				{
					ldbc=loadDBControl.addCircle;
					continue;
				}
				else if(line=="User_Topic_words")
				{
					ldbc=loadDBControl.addTopicWords;
					continue;
				}
				else if(line=="Managers")
				{
					ldbc=loadDBControl.addManager;
					continue;
				}
				else if(line=="Finish")
				{
					ldbc=loadDBControl.finish;
					continue;
				}

				//do specific task
				switch(ldbc) {
					case addUser: {
						if (addUser(con, line)==false)
							return false;
						else
							System.out.println("Adding user " + line);
						break;
					}
					case addContacts: {
						if (addContactsCircle(con, line)==false)
							return false;
						else
							System.out.println("Adding contacts " + line);
						break;
					}
					case addPrivateMessages: {
						//if (addUser(con, line)==false)
						//	return false;
						break;
					}
					case addGroups: {
						//if (addContactsCircle(con, line)==false)
						//	return false;
						break;
					}

					default: {
						break;
					}				
				}
			}
			if (ldbc==loadDBControl.finish)
				return true;
			else
				return false;
		}
		catch(Exception e){System.out.println(e); return false;}
	}

	public static Boolean addUser(Connection con, String line){
		String[] informations = line.split(",");
		return addUser(con, informations[1], informations[2], informations[0], informations[3], informations[4]);
	}

	public static Boolean addContactsCircle(Connection con, String line){
		String[] contacts = line.split(",");

		for (int owner = 0; owner<(contacts.length-1); owner++) {
			for (int contact = owner+1; contact<(contacts.length-1); contact++) {
				if (addContactsDirectly(con, contacts[owner], contacts[contact])==false)
					return false;
			}
		}
		return true;
	}

	//debug and managing purpose, not normal program flow
	public static Boolean addContactsDirectly(Connection con, String owner, String contact){
		try {
			Statement st = con.createStatement();
			// check pending list
			String acceptEmail = getEmialWithName(con, owner);
			String receiverEmail = getEmialWithName(con, contact);
			// add to contact list both ways
			String sql = "INSERT INTO CONTACT_LISTS " + 
			"VALUES ('" + acceptEmail + "', " + 
			" '" + receiverEmail + "')";
			st.executeUpdate(sql);
			sql = "INSERT INTO CONTACT_LISTS " + 
			"VALUES ('" + receiverEmail + "', " + 
			" '" + acceptEmail + "')";
			st.executeUpdate(sql);
			return true;
		}
		catch(Exception e){System.out.println(e); return false;}
	}
	//Used for GroupChatJPanel 
	public static String getEmialWithName(Connection con, String name){
		try {
			String ret = "";
			Statement st = con.createStatement();
			String sql = "SELECT U.email_address FROM Users U " +
			"WHERE U.name='" + name + "'";
			ResultSet rs = st.executeQuery(sql);
			ret += rs.getString(1);
			return ret;
		}
		catch(Exception e){System.out.println(e); return "";}
}
}
