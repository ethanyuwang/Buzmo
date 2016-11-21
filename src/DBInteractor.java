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
			/*
			String sql = "INSERT INTO USERS " +
			"VALUES (\'" + email + "\', \'" + 
			pass + "\', \'" +
			name + "\', \'" +
			phone + "\', \'" +
			screenname  + "\', \'" +
			"null, null, null)";
			*/
			String sql = "INSERT INTO USERS " +
			"VALUES ('1', '2', 3, '4', '5', '6', 7, 8)";
			st.executeUpdate(sql);
			return true;
		}
		catch(Exception e){System.out.println(e); return false;}
	}
}
