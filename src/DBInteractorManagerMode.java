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
					 String timestamp, String postnum){
		try {
			String ret = "";
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

