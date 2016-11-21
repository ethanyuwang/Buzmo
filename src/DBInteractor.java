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
			"'" + screenname + "', " +
			"null, null, null)";
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
			while(rs.next()){
				System.out.println(rs.getString(1));
				System.out.println(pass);
				if (pass.equals(rs.getString(1))){
					return true;
				}
			}
			return false;
		}
		catch(Exception e){System.out.println(e); return false;}
	}
}
