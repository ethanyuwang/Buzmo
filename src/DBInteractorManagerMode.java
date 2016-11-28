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
			java.util.Date date7 = new java.util.Date();
			Timestamp ts7 = new Timestamp(date7.getTime() - (7 * 24 * 60 * 60 * 1000));
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
			String ret = "";
			return ret;
		}
                catch(Exception e){System.out.println(e); return "REPORT FAILED";}
	}
}

