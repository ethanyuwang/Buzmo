import java.sql.*;

public class DBManager {

        public static void main(String[] args){

                try{
			// 1. Get connection to database
                        Class.forName("oracle.jdbc.driver.OracleDriver");
                        String url = "jdbc:oracle:thin:@uml.cs.ucsb.edu:1521:xe";
			String username = "glee";
			String password = "304";
                        Connection con = DriverManager.getConnection(url,username, password);

			//Create Tables
			//createTables(con);
			//addForeignKeys(con);

			//Print Table
			//printTable(con, "MESSAGES");

			// 4. Process the result set
                        //while(rs.next())
				//MODIFY PRINT TO FIT YOUR QUERY AND ATTRIBUTE TYPES
                        //        System.out.println(rs.getInt(1)+" "+rs.getString(2));

			// 5 Close conenction
                        con.close();
                }
                catch(Exception e){System.out.println(e);}
        }

	public static void printTable(Connection con, String TBName){
		try{
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM "+TBName);
			ResultSetMetaData rsmd = rs.getMetaData();
			int colNum = rsmd.getColumnCount();

			// print table data
			System.out.println("<Table: "+TBName+" with "+colNum+" columns>");
			for(int i=1; i<=colNum; i++){
				if (i==colNum){
					System.out.println(rsmd.getColumnName(i));
				}
				else{
					System.out.print(rsmd.getColumnName(i)+", ");
				}
			}
			while(rs.next()){
				for(int i=1; i<=colNum; i++){
					if (i==colNum){
						System.out.println(rs.getString(i));
					}
					else{
						System.out.print(rs.getString(i)+", ");
					}
				}
			}	
			System.out.println();	
			// print foreign keys
			DatabaseMetaData dm = con.getMetaData();
			rs = dm.getImportedKeys(null, null, TBName);
			while (rs.next()){
				System.out.println("foreign_key ("+rs.getString(3)+
				") references "+rs.getString(8)+"("+rs.getString(4)+")");
			}	
                }
                catch(Exception e){System.out.println(e);}
	}


	public static void createTables(Connection con){
		try{
			// Create tables without foreign keys
			Statement st = con.createStatement();
			
			String sql = "CREATE TABLE Users " +
	  		"(email_address VARCHAR(20) NOT NULL, " +
	  		" name VARCHAR(20) NOT NULL, " +
	  		" phone_number NUMERIC(10) NOT NULL, " +
	  		" password VARCHAR(10) NOT NULL, " +
	  		" screenname VARCHAR(20) NOT NULL, " +
			" Topic_words VARCHAR(30) NOT NULL, " +
	  		" Contact_pending_list INT NOT NULL, " +
			" Contact_list INT NOT NULL, " +
	  		" PRIMARY KEY (email_address))";
			st.executeQuery(sql);
	
		
			sql = "CREATE TABLE Managers " +
	  		"(email_address VARCHAR(20) NOT NULL, " +
	  		" PRIMARY KEY (email_address))";
			st.executeQuery(sql);

			sql = "CREATE TABLE Circle_feeds " +
	  		"(Circle_id INT NOT NULL, " +
	  		" message_id INT NOT NULL, " +
	  		" owner VARCHAR(20) NOT NULL, " +
	  		" PRIMARY KEY (Circle_id), " +
	  		" UNIQUE (owner))";
			st.executeQuery(sql);

			sql = "CREATE TABLE Group_chats " +
	  		"(name VARCHAR(20) NOT NULL, " +
	  		" duration INT NOT NULL, " +
	  		" owner VARCHAR(20) NOT NULL, " +
	  		" members VARCHAR(20) NOT NULL, " +
	  		" message_id INT NOT NULL, " +
	  		" Group_pending_list INT NOT NULL, " +
	  		" PRIMARY KEY (name))";
			st.executeQuery(sql);

			sql = "CREATE TABLE Private_chats " +
	  		"(message_id INT NOT NULL, " +
	  		" member_1 VARCHAR(20) NOT NULL, " +
	  		" member_2 VARCHAR(20) NOT NULL)";
			st.executeQuery(sql);

			sql = "CREATE TABLE Messages " + 
	  		"(message_id INT NOT NULL, " +
	  		" text_string VARCHAR(1400) NOT NULL, " +
	  		" timestamp DATE NOT NULL, " +
	  		" type VARCHAR(15) NOT NULL, " +
	  		" is_public VARCHAR(15) NOT NULL, " +
	  		" sender VARCHAR(20) NOT NULL, " +
	  		" receiver VARCHAR(20) NOT NULL, " +
	  		" Topic_words VARCHAR(30) NOT NULL, " +
	  		" PRIMARY KEY (message_id))";
			st.executeQuery(sql);

			sql = "CREATE TABLE Contact_Pending_lists " +
	  		"(cp_id INT NOT NULL, " +
	  		" pending_people VARCHAR(20) NOT NULL, " +
	  		" PRIMARY KEY (cp_id))";
			st.executeQuery(sql);

			sql = "CREATE TABLE Contact_lists " +
	  		"(c_id INT NOT NULL, " +
	  		" contacts VARCHAR(20) NOT NULL, " +
	  		" PRIMARY KEY (c_id))";
			st.executeQuery(sql);

			sql = "CREATE TABLE Group_pending_lists " +
	  		"(gp_id INT NOT NULL, " +
	  		" pending_people VARCHAR(20) NOT NULL, " +
	  		" PRIMARY KEY (gp_id))";
			st.executeQuery(sql);

			
			sql = "CREATE TABLE Topic_words " +
	  		"(Topic_word VARCHAR(30) NOT NULL, " +
	  		" PRIMARY KEY (Topic_word))";
			st.executeQuery(sql);
		}
		catch(Exception e){System.out.println(e);}
	}

	public static void addForeignKeys(Connection con){
		try{
			// Add foreign keys
			Statement st = con.createStatement();
			
			String sql = "ALTER TABLE Users " +
	  		" ADD FOREIGN KEY (Topic_words) REFERENCES Topic_words(Topic_word)" +
	  		" ADD FOREIGN KEY (Contact_pending_list) REFERENCES Contact_Pending_lists(cp_id)" +
			" ADD FOREIGN KEY (Contact_list) REFERENCES Contact_lists(c_id)";
			st.executeQuery(sql);
		
			sql = "ALTER TABLE Managers " +
	  		"ADD FOREIGN KEY (email_address) REFERENCES Users(email_address)";
			st.executeQuery(sql);

			sql = "ALTER TABLE Circle_feeds " +
	  		"ADD FOREIGN KEY (message_id) REFERENCES Messages(message_id) " +
	  		"ADD FOREIGN KEY (owner) REFERENCES Users(email_address)";
			st.executeQuery(sql);

			sql = "ALTER TABLE Group_chats " +
	  		"ADD FOREIGN KEY (owner) REFERENCES Users(email_address) " +
	  		"ADD FOREIGN KEY (members) REFERENCES Users(email_address) " +
	  		"ADD FOREIGN KEY (message_id) REFERENCES Messages(message_id) " +
	  		"ADD FOREIGN KEY (Group_pending_list) REFERENCES Group_pending_lists(gp_id)";
			st.executeQuery(sql);
			
			sql = "ALTER TABLE Private_chats " +
	  		"ADD FOREIGN KEY (message_id) REFERENCES Messages(message_id) " +
	  		"ADD FOREIGN KEY (member_1) REFERENCES Users(email_address) " +
	  		"ADD FOREIGN KEY (member_2) REFERENCES Users(email_address)";
			st.executeQuery(sql);

			sql = "ALTER TABLE Messages " + 
	  		"ADD FOREIGN KEY (sender) REFERENCES Users(email_address) " +
	  		"ADD FOREIGN KEY (receiver) REFERENCES Users(email_address) " +
	  		"ADD FOREIGN KEY (Topic_words) REFERENCES Topic_words(Topic_word)";
			st.executeQuery(sql);

			sql = "ALTER TABLE Contact_Pending_lists " +
	  		"ADD FOREIGN KEY (pending_people) REFERENCES Users(email_address)";
			st.executeQuery(sql);

			sql = "ALTER TABLE Contact_lists " +
	  		"ADD FOREIGN KEY (contacts) REFERENCES Users(email_address)";
			st.executeQuery(sql);

			sql = "ALTER TABLE Group_pending_lists " +
	  		"ADD FOREIGN KEY (pending_people) REFERENCES Users(email_address)";
			st.executeQuery(sql);
			
		}
		catch(Exception e){System.out.println(e);}
	}
}
