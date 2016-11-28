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
}
