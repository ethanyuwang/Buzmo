package buzmo;

import java.sql.*;

public class DBInteractorCirclePost {

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

        public static String[] getContactListsAsArray(Connection con){
                try {
                        String[] ret;
                        String myEmail = BuzmoJFrame.userEmail;
                        Statement st = con.createStatement();
                        String sql = "SELECT count(*) FROM CONTACT_LISTS C " +
                        "WHERE C.owner='" + myEmail + "'";
                        ResultSet rs = st.executeQuery(sql);
                        if(rs.next()){
                                ret = new String[rs.getInt(1)];
                        }
                        else{
                                return null;
                        }
                        sql = "SELECT C.friend FROM CONTACT_LISTS C " +
                        "WHERE C.owner='" + myEmail + "'";
                        rs = st.executeQuery(sql);
                        int i = 0;
                        while(rs.next()){
                                ret[i] = rs.getString(1);
                                i++;
                        }
                        return ret;
                }
                catch(Exception e){System.out.println(e); return null;}
        }
	
	public static String getCircleFeedHistory(Connection con){
		try {
			String ret = "";
			String sql;
			String myEmail = BuzmoJFrame.userEmail;
			Statement st = con.createStatement();
			Statement st2 = con.createStatement();
			ResultSet rs, rs2;
			int post_id;
			sql = "SELECT P.post_owner, P.post_string, P.post_time, P.is_public, P.post_id " + 
			"FROM CIRCLE_POSTS P WHERE (P.post_id IN " +
			" (SELECT C.post_id FROM CIRCLE_POST_RECEIVERS C " +
			"  WHERE C.post_receiver='" + myEmail + "'))" + 
			"ORDER BY P.post_time";
			rs = st.executeQuery(sql);
			while(rs.next()){
				ret += rs.getString(1);
				ret += " (" + rs.getString(3) + ", ";
				ret += "public: " + rs.getString(4) + ") ";
				ret += rs.getString(2) + "\n";
				post_id = rs.getInt(5);
				// topic words for each post
				sql = "SELECT T.topic_word FROM POST_TOPIC_WORDS T " +
				"WHERE T.post_id='" + post_id  + "'";
				rs2 = st2.executeQuery(sql);
				while(rs2.next()){
					ret += "#" + rs2.getString(1);
				}
				ret += "\n\n";
			}
			return ret;
		}
		catch(Exception e){System.out.println(e); return "Failed to load messages\n";}
	}
	public static int createCirclePost(Connection con, String message, String[] topicArray, boolean publicChecked){
		try {
			Timestamp ts = getCurrentTimeStamp();
			String is_public = "False";
			if(publicChecked) {is_public = "True";}
			String myEmail = BuzmoJFrame.userEmail;
			String hashStr = "CF" + myEmail + message + ts.toString();
			int hashCode = hashStr.hashCode();
			String sql = "INSERT INTO CIRCLE_POSTS VALUES (?,?,?,?,?)";	
			PreparedStatement ps = con.prepareStatement(sql);
			// 1. Create message
			con.setAutoCommit(false);
			ps.setInt(1, hashCode);
			ps.setString(2, message);
			ps.setTimestamp(3, ts);
			ps.setString(4, is_public);
			ps.setString(5, myEmail);
			ps.addBatch();
			ps.executeBatch();
			con.commit();
			con.setAutoCommit(true);
			// 2. Link topic words to message/user
			Statement st = con.createStatement();
			for(int i=0; i<topicArray.length; i++){
				sql = "INSERT INTO POST_TOPIC_WORDS " +
				"(SELECT'" + topicArray[i] + "' AS TOPIC_WORD, " + 
				" " + hashCode + " AS POST_ID " +
				"FROM POST_TOPIC_WORDS WHERE " +
				"TOPIC_WORD='" + topicArray[i] + "' AND " + 
				"POST_ID=" + hashCode + " HAVING COUNT(*) = 0)";
				st.executeUpdate(sql);
				sql = "INSERT INTO USER_TOPIC_WORDS " +
				"(SELECT'" + topicArray[i] + "' AS TOPIC_WORD, " + 
				" '" + myEmail + "' AS EMAIL_ADDRESS " +
				"FROM USER_TOPIC_WORDS WHERE " +
				"TOPIC_WORD='" + topicArray[i] + "' AND " + 
				"EMAIL_ADDRESS='" + myEmail + "' HAVING COUNT(*) = 0)";
				st.executeUpdate(sql);
			}
			return hashCode;
		}	
		catch(Exception e){System.out.println(e); return -1;}
	}
	public static boolean linkReceiversToCirclePost(Connection con, String receiverEmail, int post_id){
		try {
			Statement st = con.createStatement();
			String sql = "INSERT INTO CIRCLE_POST_RECEIVERS " + 
			"VALUES('" + receiverEmail + "', " + post_id + ")";
			st.executeUpdate(sql);
			return true;	
		}
		catch(Exception e){System.out.println(e); return false;}
	}
	public static String defaultUsersTopicWords(Connection con){
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
	public static String searchCirclePosts(Connection con, String[] topicArray){
		try {
			int post_id;
			ResultSet rs;
			ResultSet rs2;
			Statement st = con.createStatement();
			Statement st2 = con.createStatement();
			String ret = "Searched posts related to: ";
			String myEmail = BuzmoJFrame.userEmail;
			String sql = "SELECT DISTINCT P.post_owner, P.post_string, P.post_time, P.is_public, " +
			"P.post_id FROM CIRCLE_POSTS P WHERE (P.post_id IN " + 
			"(SELECT T.post_id FROM POST_TOPIC_WORDS T WHERE T.topic_word IN (";
			for(int i=0; i<topicArray.length; i++){
				if(i+1 == topicArray.length){
					ret += topicArray[i] + "\n\n";
					sql += "'" + topicArray[i] + "'";
				}
				else{
					ret += topicArray[i] + ", ";
					sql += "'" + topicArray[i] + "', ";
				}
			}
			sql += ")))";
			rs = st.executeQuery(sql);
			while(rs.next()){
				ret += rs.getString(1);
				ret += " (" + rs.getString(3) + ", ";
				ret += "public: " + rs.getString(4) + ") ";
				ret += rs.getString(2) + "\n";
				post_id = rs.getInt(5);
				// topic words for each post
				sql = "SELECT T.topic_word FROM POST_TOPIC_WORDS T " +
				"WHERE T.post_id='" + post_id  + "'";
				rs2 = st2.executeQuery(sql);
				while(rs2.next()){
					ret += "#" + rs2.getString(1);
				}
				ret += "\n\n";
			}
			return ret;
		}
		catch(Exception e){System.out.println(e); return "FAILED to search circle posts";}
	}

	//Deletion
	public static String getUsersPosts(Connection con){
		try {
			String ret = "";
			String sql;
			String myEmail = BuzmoJFrame.userEmail;
			Statement st = con.createStatement();
			Statement st2 = con.createStatement();
			ResultSet rs, rs2;
			int post_id;
			sql = "SELECT P.post_owner, P.post_string, P.post_time, P.is_public, P.post_id " + 
			"FROM CIRCLE_POSTS P WHERE P.post_owner='" + myEmail + "' ORDER BY P.post_time"; 
			rs = st.executeQuery(sql);
			while(rs.next()){
				post_id = rs.getInt(5);
				ret += "<post_id: " + post_id + ">\n";
				ret += rs.getString(1);
				ret += " (" + rs.getString(3) + ", ";
				ret += "public: " + rs.getString(4) + ") ";
				ret += rs.getString(2) + "\n";
				// topic words for each post
				sql = "SELECT T.topic_word FROM POST_TOPIC_WORDS T " +
				"WHERE T.post_id='" + post_id  + "'";
				rs2 = st2.executeQuery(sql);
				while(rs2.next()){
					ret += "#" + rs2.getString(1);
				}
				ret += "\n\n";
			}
			return ret;
		}
		catch(Exception e){System.out.println(e); return "FAILED to search circle posts";}
	}	
	public static boolean deletePost(Connection con, String str_post_id){
		try {
			int post_id = Integer.parseInt(str_post_id);
			String sql = "DELETE FROM CIRCLE_POSTS C WHERE C.post_id=?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, post_id);
			ps.executeUpdate();
			return true;
		}
		catch(Exception e){System.out.println(e); return false;}
	}	

	private static Timestamp getCurrentTimeStamp() {
		java.util.Date today = new java.util.Date();
		return new Timestamp(today.getTime());
	}
}
