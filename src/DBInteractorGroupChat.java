package buzmo;

import java.sql.*;
import java.io.*;
import java.net.*;
import java.lang.*;

public class DBInteractorGroupChat {
	//Used for GroupChatJPanel 
	public static Boolean createGroup(Connection con, String groupName){
		try { 
			Statement st = con.createStatement();
			int groupId = groupName.hashCode();
			// Create table
			String sql = "INSERT INTO Group_chats " +
			"VALUES (" + groupId + ", '" +
			groupName + "', " +
			7 + ", " +
			"'" + BuzmoJFrame.userEmail + "')";
			st.executeUpdate(sql);

			// link to group members
			sql = "INSERT INTO Group_chat_members " +
			"VALUES (" + groupId + ", '" +
			BuzmoJFrame.userEmail + "')";

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
	public static int getGroupID(Connection con, String groupName){
		try {
			int ret = -100;
			String myEmail = BuzmoJFrame.userEmail;
			Statement st = con.createStatement();
			String sql = "SELECT C.group_id FROM Group_chats C " +
			"WHERE C.group_name='" + groupName + "'";
			ResultSet rs = st.executeQuery(sql);
			while(rs.next()){
				ret = rs.getInt(1);
			}
			return ret;

		}
		catch(Exception e){System.out.println(e); return -100;}
	}
	//Used for GroupChatJPanel 
	public static String getGroupName(Connection con, int groupId){
		try {
			String ret = "";
			String myEmail = BuzmoJFrame.userEmail;
			Statement st = con.createStatement();
			String sql = "SELECT C.group_name FROM Group_chats C " +
			"WHERE C.group_id='" + groupId + "'";
			ResultSet rs = st.executeQuery(sql);
			while(rs.next()){
				ret = rs.getString(1);
			}
			return ret;

		}
		catch(Exception e){System.out.println(e); return "";}
	}

	//Used for GroupChatJPanel 
	public static Boolean changeGroupChatName(Connection con, String groupName, String newGroupName){
		try {
			String myEmail = BuzmoJFrame.userEmail;	
			Statement st = con.createStatement();
			String sql = "UPDATE Group_chats SET group_name = '" +
			newGroupName + "' WHERE owner='" + myEmail + "' AND group_name = '"+
			groupName + "'"; 
			st.executeQuery(sql);			
			return true;
		}
		catch(Exception e){System.out.println(e); return false;}
}
	//Used for GroupChatJPanel 
	public static Boolean changeGroupChatDuration(Connection con, String groupName, String newDuration){
		try {
			String myEmail = BuzmoJFrame.userEmail;	
			Statement st = con.createStatement();
			String sql = "UPDATE Group_chats SET duration = " +
			newDuration + " WHERE owner='" + myEmail + "' AND group_name = '"+
			groupName + "'"; 
			st.executeQuery(sql);			
			return true;
		}
		catch(Exception e){System.out.println(e); return false;}
}
	//Used for GroupChatJPanel 
	public static Boolean isGroup(Connection con, String groupName){
		try {
			String myEmail = BuzmoJFrame.userEmail;	
			Statement st = con.createStatement();
			int groupId = getGroupID(con, groupName);
			System.out.println("Group id is : "+groupId);
			String sql = "SELECT G.member FROM Group_chat_members G WHERE " +
			"(G.group_id=" + groupId + ")"; 
			ResultSet rs = st.executeQuery(sql);			
			while(rs.next()){
				if (myEmail.equals(rs.getString(1))){
					return true;
				}
			}
			return false;
		}
		catch(Exception e){System.out.println(e); return false;}
}
	public static int getGroupChatDuration(Connection con, String groupName){
		try {
			int ret=-10;
			String myEmail = BuzmoJFrame.userEmail;	
			Statement st = con.createStatement();
			String sql = "SELECT C.duration FROM Group_chats C WHERE " +
			"(C.owner='" + myEmail + "' AND C.group_name='" + groupName + "')"; 
			ResultSet rs = st.executeQuery(sql);

			if(rs.next()){
				ret = rs.getInt(1);
			}
			return ret;
		}

		catch(Exception e){System.out.println(e); return -10;}
	}

	public static String getGroupChatDurationWrapper(Connection con, String groupName){
		int	duration = getGroupChatDuration(con, groupName);
		if (duration>0) {
			return Integer.toString(duration);
		}
		else {
			return "Error";
		}
	}

	//Used for GroupChatJPanel 
	public static Boolean addMessageToGroupChat(Connection con, String message, String groupName){
		try {
			String myEmail = BuzmoJFrame.userEmail;
			Timestamp ts = DBInteractor.getCurrentTimeStamp();
			String messageWithTime = message+ts.toString();
			int groupId = getGroupID(con, groupName);

			//Add a copy to sender
			String sql = "INSERT INTO MESSAGES VALUES (?,?,?,?,?,?,?,?)";	
			PreparedStatement ps = con.prepareStatement(sql);
			con.setAutoCommit(false);
			String messageWithTimeAndOwner = messageWithTime + myEmail;

			ps.setInt(1, messageWithTimeAndOwner.hashCode());
			ps.setString(2, message);
			ps.setTimestamp(3, ts);
			ps.setString(4, "group");
			ps.setString(5, myEmail);
			ps.setString(6, myEmail);
			ps.setString(7, myEmail);
			ps.setInt(8, groupId);
			ps.addBatch();
			ps.executeBatch();
			con.commit();
			return true;
		}
		catch(Exception e){System.out.println(e); return false;}
}

	//Used for GroupChatJPanel 
	public static String loadGroupChatHistory(Connection con, String groupName){
		try {
			//cleanOldGroupChatHistory(con, groupName);
			String ret = "";
			String myEmail = BuzmoJFrame.userEmail;	
			int groupId = getGroupID(con, groupName);
			Statement st = con.createStatement();
			String sql = "SELECT M.text_string, M.sender, M.timestamp FROM MESSAGES M WHERE " +
			"M.type='group' AND M.group_id='" + groupId + 
			"' ORDER BY M.timestamp";
			ResultSet rs = st.executeQuery(sql);			
			while(rs.next()){
				ret += rs.getString(2) + " (";
				ret += rs.getString(3) + "): ";
				ret += rs.getString(1) + "\n";
			}
			return ret;
		}
		catch(Exception e){System.out.println(e); return "";}
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
		try {
			String senderEmail = BuzmoJFrame.userEmail;
			Statement st = con.createStatement();
			int groupId = getGroupID(con, groupName);
			int count;
			// check contact list
			String sql = "SELECT count(*) FROM CONTACT_LISTS C WHERE " +
			"C.owner='" + senderEmail + "' AND " + 
			"C.friend='" + requestEmail + "'";
			ResultSet rs = st.executeQuery(sql);
			if(rs.next()){
				count = rs.getInt(1);
				if(count < 0){
					System.out.println("inviteToGroupChat failed at no such contact\n");
					return false;
				}
			}
			else{
				System.out.println("inviteToGroupChat failed at contact not found\n");
				return false;
			}

			// check pending list
			sql = "SELECT count(*) FROM Group_pending_lists C WHERE " +
			"(C.pending_people='" + requestEmail + "' AND " +
			" C.group_id='" + groupId + "')"; 
			rs = st.executeQuery(sql);			
			if(rs.next()){
				count = rs.getInt(1);
				if(count > 0){
					System.out.println("inviteToGroupChat failed at multiple requests exist\n");
					return false;
				}
			}
			else{
				System.out.println("inviteToGroupChat failed at pending list not found\n");
				return false;
			}

			// add to table
			sql = "INSERT INTO Group_pending_lists " +
			"VALUES ('" + requestEmail + "', " +
			" '" + groupId + "')";
			st.executeUpdate(sql);
			return true;
		}
		catch(Exception e){System.out.println(e); return false;}
	}

	//Used for GroupChatJPanel 
	public static Boolean addGroupChat(Connection con, String groupName){
		try {
			int groupId = getGroupID(con, groupName);
			String receiverEmail = BuzmoJFrame.userEmail;
			Statement st = con.createStatement();
			// check pending list
			String sql = "SELECT count(*) FROM Group_pending_lists G WHERE " +
			"(G.pending_people='" + receiverEmail + "' AND " +
			" G.group_id='" + groupId + "')"; 
			ResultSet rs = st.executeQuery(sql);			
			if(rs.next()){
				int count = rs.getInt(1);
				if(count != 1){
					return false;
				}
			}
			// remove from pending list
			sql = "DELETE FROM Group_pending_lists G " +
			"WHERE G.pending_people='" + receiverEmail + "' " + 
			"AND G.group_id='" + groupId + "'";
			st.executeUpdate(sql);
			// add to GROUP MEMEBERS list
			sql = "INSERT INTO Group_chat_members " + 
			"VALUES ('" + groupId + "', " + 
			" '" + receiverEmail + "')";
			st.executeUpdate(sql);
			
			return true;
		}
		catch(Exception e){System.out.println(e); return false;}
	}
	//Used for GroupChatJPanel 
	public static String getGroupMembers(Connection con, String groupName){
		try {
			String ret = "";
			int groupId = getGroupID(con, groupName);
			String myEmail = BuzmoJFrame.userEmail;
			Statement st = con.createStatement();
			String sql = "SELECT G.member FROM Group_chat_members G WHERE " +
			"(G.group_id=" + groupId + ")"; 
			ResultSet rs = st.executeQuery(sql);			
			while(rs.next()){
				ret+=rs.getString(1);
				ret += "\n";
			}
			return ret;

		}
		catch(Exception e){System.out.println(e); return "";}
	}


	//Used for GroupChatJPanel 
	public static String getPendingGroupChatInvites(Connection con){
		try {
			String ret = "";
			String myEmail = BuzmoJFrame.userEmail;
			Statement st = con.createStatement();
			String sql = "SELECT G.group_id FROM Group_pending_lists G " +
			"WHERE G.pending_people='" + myEmail + "'";
			ResultSet rs = st.executeQuery(sql);
			while(rs.next()){
				String groupName = getGroupName(con, rs.getInt(1));
				ret += groupName;
				ret += "\n";
			}
			return ret;
		}
		catch(Exception e){System.out.println(e); return "";}
	}
}
