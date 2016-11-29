package buzmo;

import java.sql.*;
import java.io.*;
import java.net.*;
import java.lang.*;

public class DBInteractorManagerMode {
	public static boolean addManager(Connection con, String email){
                try {
                        Statement st = con.createStatement();
			// check if user exists 
                        String sql = "SELECT * FROM USERS U WHERE U.email_address='" + email + "'";
                        ResultSet rs = st.executeQuery(sql);
                        if(rs.next()){
				sql = "INSERT INTO MANAGERS VALUES('" + email + "')";
				st.executeUpdate(sql);
				return true;
                        }
			else{
				return false;
			}
                }
                catch(Exception e){System.out.println(e); return false;}
	}
	public static String searchUsers(Connection con, String[] emailArray, String[] topicArray,
					 String timestamp, String postnum_str){
		try {
			// Check null inputs
			if(timestamp.equals("")){
				timestamp = "9000-01-11 10:10:10.0";
			}
			if(postnum_str.equals("")){
				postnum_str = "9999999";
			}

			// Variables
			String ret = "";
			String sql;
			// Convert post number count
			int postnum = Integer.parseInt(postnum_str);
			// Convert given timestamp
			Timestamp ts = Timestamp.valueOf(timestamp);
			// 7 days before current timestamp
			Timestamp ts7 = new Timestamp(DBInteractor.getCurrentTimeStamp().getTime() - (7 * 24 * 60 * 60 * 1000));
			PreparedStatement ps;
			ResultSet rs;

			// Query
			sql = "SELECT DISTINCT U.email_address FROM USERS U WHERE " +
			// 1. matching email
			"(U.email_address IN (";
			for(int i=0; i<emailArray.length; i++){
				if(i+1 == emailArray.length){
					sql += "'" + emailArray[i] + "'";
				}
				else{
					sql += "'" + emailArray[i] + "', ";
				}	
			}
			sql += ")) OR " + 
			// 2. matching topic word
			"(U.email_address IN (SELECT T.email_address FROM USER_TOPIC_WORDS T " + 
			"  WHERE T.topic_word IN (";
			for(int i=0; i<topicArray.length; i++){
				if(i+1 == topicArray.length){
					sql += "'" + topicArray[i] + "'";
				}
				else{
					sql += "'" + topicArray[i] + "', ";
				}
			}
			sql += "))) OR " + 
			// 3. posted after timestamp
			"(U.email_address IN (SELECT P.post_owner FROM CIRCLE_POSTS P " + 
			"  WHERE P.post_time >= ?)) OR " + 
			// 4. posted n times within 7 days
			"(U.email_address IN (SELECT P.post_owner FROM CIRCLE_POSTS P " + 
			"  WHERE P.post_time >= ? " + 
			"  GROUP BY P.post_owner " + 
			"  HAVING COUNT(*) >= ?))";
                        ps = con.prepareStatement(sql);
			ps.setTimestamp(1, ts);
			ps.setTimestamp(2, ts7);
			ps.setInt(3, postnum);
                        rs = ps.executeQuery();			
			while(rs.next()){
				ret += rs.getString(1) + "\n";
			}
			return ret;
		}
                catch(Exception e){System.out.println(e); return "SEARCH FAILED: double check your inputs";}
	}
	public static String generateReport(Connection con){
		try {
			int totalNewPostViews = 1;
			int totalNewPosts = 1;

			// 7 days before current timestamp
			Timestamp ts7 = new Timestamp(DBInteractor.getCurrentTimeStamp().getTime() - (7 * 24 * 60 * 60 * 1000));
			PreparedStatement ps;
			ResultSet rs;
			String sql;
			String ret = "-7 Day Report-\n\n";

			sql = "SELECT COUNT(*) FROM CIRCLE_POSTS P WHERE P.post_time >= ?";
			ps = con.prepareStatement(sql);
			ps.setTimestamp(1, ts7);
			rs = ps.executeQuery();
			if(rs.next()){
				totalNewPosts = rs.getInt(1);
				ret += "Total number of new posts " + String.valueOf(rs.getInt(1)) + "\n";
			}

			sql = "SELECT SUM(P.view_count) FROM CIRCLE_POSTS P";
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			if(rs.next()){
				ret += "Total number of post reads " + String.valueOf(rs.getInt(1)) + "\n";
			}

			//ret += "Average number of new message reads " + ;

			sql = "SELECT SUM(P.view-count) FROM CIRCLE_POSTS P WHERE P.post_time >= ?";
			ps = con.prepareStatement(sql);
			ps.setTimestamp(1, ts7);
			rs = ps.executeQuery();
			if(rs.next()){
				totalNewPostViews = rs.getInt(1);
				ret += "Total number of new post reads " + String.valueOf(rs.getInt(1)) + "\n";
			}

			ret += "Average number of reads for new messages " + String.valueOf(totalNewPostViews/(float)totalNewPosts) + "\n";

			//ret += "Top 3 most viewed messages: " + ;
			//ret += "Top 3 users by new message count: " + ;
			
			sql = "SELECT COUNT(*) FROM USERS U WHERE U.user_id IN " + 
			"(SELECT P.post_owner FROM CIRCLE_POSTS P GROUP BY P.post_owner " + 
			"HAVING COUNT(*) < 3)";
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			ret += "Number of users who sent less than 3 messages: \n" ;
			while(rs.next()){
				ret += rs.getString(1) + "\n";
			}

			//ret += "For each topic word, most read message: " ;
			return ret;
		}
                catch(Exception e){System.out.println(e); return "REPORT FAILED";}
	}
}

