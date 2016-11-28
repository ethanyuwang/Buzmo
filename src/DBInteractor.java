package buzmo;

import java.sql.*;
import java.io.*;
import java.net.*;
import java.lang.*;

public class DBInteractor {

	public static Connection connectToDB(){
		try {	
                        Class.forName("oracle.jdbc.driver.OracleDriver");
                        String url = "jdbc:oracle:thin:@uml.cs.ucsb.edu:1521:xe";
			String username = "glee";
			String password = "304";
                        //String username = "yuxiang";
                        //String password = "049";
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
			ResultSet rs2;
			if(rs.next()){
				System.out.println(rs.getString(1));
				System.out.println(pass);
				if (pass.equals(rs.getString(1))){
					BuzmoJFrame.userEmail = email;
					// Check if it's a manager
					sql = "SELECT * FROM MANAGERS M WHERE M.EMAIL_ADDRESS='" + email + "'";
					rs2 = st.executeQuery(sql);
					if(rs2.next()){
						BuzmoJFrame.is_manager = true;
					}
					else{
						BuzmoJFrame.is_manager = false;
					}
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

	//User topic words
	public static boolean addUserTopicWords(Connection con, String[] topicArray){
		try {
			String sql;
			String myEmail = BuzmoJFrame.userEmail;	
			Statement st = con.createStatement();
			for(int i=0; i<topicArray.length; i++){
                                sql = "INSERT INTO USER_TOPIC_WORDS " +
                                "(SELECT'" + topicArray[i] + "' AS TOPIC_WORD, " +
                                " '" + myEmail + "' AS EMAIL_ADDRESS " +
                                "FROM USER_TOPIC_WORDS WHERE " +
                                "TOPIC_WORD='" + topicArray[i] + "' AND " +
                                "EMAIL_ADDRESS='" + myEmail + "' HAVING COUNT(*) = 0)";
                                st.executeUpdate(sql);
			}
			return true;
		}
		catch(Exception e){System.out.println(e); return false;}
	}
        public static String getUserTopicWords(Connection con){
                try {
                        Statement st = con.createStatement();
                        String ret = "";
                        String myEmail = BuzmoJFrame.userEmail;
                        String sql = "SELECT T.topic_word FROM USER_TOPIC_WORDS T " +
                        "WHERE T.email_address='" + myEmail + "'";
                        ResultSet rs = st.executeQuery(sql);
                        while(rs.next()){
                                ret += rs.getString(1) + " ";
                        }
                        return ret;
                }
                catch(Exception e){System.out.println(e); return "FAILED to get user's topic words";}
        }

	//Helper functions
	public static Timestamp getBaseTime(Connection con){
		try {
                        Statement st = con.createStatement();
			String sql = "SELECT T.base_time FROM TIME T WHERE T.time_id=0";
			ResultSet rs = st.executeQuery(sql);
			if(rs.next()){
				return rs.getTimestamp(1);
			}
			else{
				return null;
			}
		}
		catch(Exception e){System.out.println(e); return null;}
	}
	public static Timestamp getCurrentTimeStamp() {
		java.util.Date today = new java.util.Date();
		Timestamp start_time = BuzmoJFrame.start_time;
		Timestamp base_time = BuzmoJFrame.base_time;
		Timestamp current_time = new Timestamp(today.getTime());
		long ret_time = base_time.getTime() + current_time.getTime() - start_time.getTime();
		return new Timestamp(ret_time);
	}
}
