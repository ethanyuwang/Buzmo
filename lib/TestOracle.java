import java.sql.*;

public class TestOracle {

        public static void main(String[] args){

                try{
			// 1. Get connection to database
                        Class.forName("oracle.jdbc.driver.OracleDriver");
                        String url = "jdbc:oracle:thin:@uml.cs.ucsb.edu:1521:xe";
			String username = "glee";
			String password = "304";
                        Connection con=DriverManager.getConnection(url,username, password);

			// 2. Create a statement
                        Statement st = con.createStatement();
                        String sql = "WRITE YOUR QUERY HERE";

			// 3. Execute SQL query
                        ResultSet rs = st.executeQuery(sql);

			// 4. Process the result set
                        while(rs.next())
				//MODIFY PRINT TO FIT YOUR QUERY AND ATTRIBUTE TYPES
                                System.out.println(rs.getInt(1)+" "+rs.getString(2));

			// 5 Close conenction
                        con.close();
                }
                catch(Exception e){System.out.println(e);}
        }
}



