package buzmo;

import java.sql.*;

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
}
