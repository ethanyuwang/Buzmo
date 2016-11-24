import java.sql.*;

public class DBManager {

        public static void main(String[] args){

                try{
			// Get connection to database
                        Class.forName("oracle.jdbc.driver.OracleDriver");
                        //use this for csil machine
                        String url = "jdbc:oracle:thin:@uml.cs.ucsb.edu:1521:xe";
                        //use this for non csil machine
                        //String url = "jdbc:oracle:thin:@localhost:1521:xe";
			String username = "glee";
			String password = "304";
                        Connection con = DriverManager.getConnection(url,username, password);

			//Create Tables
			createTables(con);
			addForeignKeys(con);

			//Delete Tables
			//deleteTables(con);

			//Print Table
			//printTable(con, "USERS");

			//Print GLEE Tables
			//printGLEETables(con);

			//Print all tables
			printAllTables(con);

			// Close conenction
                        con.close();
                }
                catch(Exception e){System.out.println(e);}
        }

	public static void createTables(Connection con){
		try{
			// Create tables without foreign keys
			Statement st = con.createStatement();
			
			String sql = "CREATE TABLE Users " +
	  		"(email_address VARCHAR(20) NOT NULL, " +
	  		" password VARCHAR(10) NOT NULL, " +
	  		" name VARCHAR(20) NOT NULL, " +
	  		" phone_number NUMERIC(10) NOT NULL, " +
	  		" screenname VARCHAR(20), " +
	  		" PRIMARY KEY (email_address))";
			st.executeQuery(sql);	
		
			sql = "CREATE TABLE Managers " +
	  		"(email_address VARCHAR(20) NOT NULL)";
			st.executeQuery(sql);

			sql = "CREATE TABLE Circle_feeds " +
	  		"(circle_id INT NOT NULL, " +
	  		" owner VARCHAR(20) NOT NULL, " +
	  		" PRIMARY KEY (Circle_id), " +
	  		" UNIQUE (owner))";
			st.executeQuery(sql);

			sql = "CREATE TABLE Group_chats " +
	  		"(group_name VARCHAR(20) NOT NULL, " +
	  		" duration INT NOT NULL, " +
	  		" owner VARCHAR(20) NOT NULL, " +
	  		" PRIMARY KEY (group_name))";
			st.executeQuery(sql);

			sql = "CREATE TABLE Private_chats " +
	  		"(pc_id INT NOT NULL, " +
	  		" member_1 VARCHAR(20) NOT NULL, " +
	  		" member_2 VARCHAR(20) NOT NULL, " +
			" PRIMARY KEY (pc_id))";
			st.executeQuery(sql);

			sql = "CREATE TABLE Messages " + 
	  		"(message_id INT NOT NULL, " +
	  		" text_string VARCHAR(1400) NOT NULL, " +
	  		" timestamp DATE NOT NULL, " +
	  		" type VARCHAR(15) NOT NULL, " +
	  		" is_public VARCHAR(15) NOT NULL, " +
	  		" owner VARCHAR(20) NOT NULL, " +
	  		" sender VARCHAR(20) NOT NULL, " +
	  		" receiver VARCHAR(20) NOT NULL, " +
	  		" PRIMARY KEY (message_id))";
			st.executeQuery(sql);

			sql = "CREATE TABLE Contact_pending_lists " +
	  		"(receiver VARCHAR(20) NOT NULL, " +
	  		" sender VARCHAR(20) NOT NULL)";
			st.executeQuery(sql);

			sql = "CREATE TABLE Contact_lists " +
	  		"(owner VARCHAR(20) NOT NULL, " +
	  		" friend VARCHAR(20) NOT NULL)";
			st.executeQuery(sql);

			sql = "CREATE TABLE Group_pending_lists " +
	  		"(pending_people VARCHAR(20) NOT NULL, " +
  			" group_name VARCHAR(20) NOT NULL)";
			st.executeQuery(sql);

			sql = "CREATE TABLE Group_chat_members " +
	  		"(group_name VARCHAR(20) NOT NULL, " +
  			" member VARCHAR(20) NOT NULL)";
			st.executeQuery(sql);
			
			sql = "CREATE TABLE Message_topic_words " +
	  		"(Topic_word VARCHAR(256) NOT NULL, " +
	  		" message_id INT NOT NULL)";
			st.executeQuery(sql);

			sql = "CREATE TABLE User_topic_words " +
	  		"(Topic_word VARCHAR(256) NOT NULL, " +
	  		" email_address VARCHAR(20) NOT NULL)";
			st.executeQuery(sql);

			sql = "CREATE TABLE Group_chat_messages " +
	  		"(group_name VARCHAR(20) NOT NULL, " +
	  		" message_id INT NOT NULL)";
			st.executeQuery(sql);

			sql = "CREATE TABLE Circle_feed_messages " +
	  		"(circle_id INT NOT NULL, " +
	  		" message_id INT NOT NULL)";
			st.executeQuery(sql);

			sql = "CREATE TABLE Private_chat_messages " +
	  		"(pc_id INT NOT NULL, " +
	  		" message_id INT NOT NULL)";
			st.executeQuery(sql);

		}
		catch(Exception e){System.out.println(e);}
	}

	public static void addForeignKeys(Connection con){
		try{
			// Add foreign keys
			Statement st = con.createStatement();
		
			String sql = "ALTER TABLE Managers " +
	  		"ADD FOREIGN KEY (email_address) REFERENCES Users(email_address)";
			st.executeQuery(sql);

			sql = "ALTER TABLE Circle_feeds " +
	  		"ADD FOREIGN KEY (owner) REFERENCES Users(email_address)";
			st.executeQuery(sql);

			sql = "ALTER TABLE Group_chats " +
	  		"ADD FOREIGN KEY (owner) REFERENCES Users(email_address)";
			st.executeQuery(sql);
			
			sql = "ALTER TABLE Private_chats " +
	  		"ADD FOREIGN KEY (member_1) REFERENCES Users(email_address) " +
	  		"ADD FOREIGN KEY (member_2) REFERENCES Users(email_address)";
			st.executeQuery(sql);

			sql = "ALTER TABLE Messages " + 
	  		"ADD FOREIGN KEY (owner) REFERENCES Users(email_address) " +
	  		"ADD FOREIGN KEY (sender) REFERENCES Users(email_address) " +
	  		"ADD FOREIGN KEY (receiver) REFERENCES Users(email_address)";
			st.executeQuery(sql);

			sql = "ALTER TABLE User_topic_words " + 
	  		"ADD FOREIGN KEY (email_address) REFERENCES Users(email_address)";
			st.executeQuery(sql);

			sql = "ALTER TABLE Contact_pending_lists " + 
	  		"ADD FOREIGN KEY (sender) REFERENCES Users(email_address) " +
	  		"ADD FOREIGN KEY (receiver) REFERENCES Users(email_address)";
			st.executeQuery(sql);

			sql = "ALTER TABLE Contact_lists " +
	  		"ADD FOREIGN KEY (owner) REFERENCES Users(email_address) " +
	  		"ADD FOREIGN KEY (friend) REFERENCES Users(email_address)";
			st.executeQuery(sql);

			sql = "ALTER TABLE Group_pending_lists " +
	  		"ADD FOREIGN KEY (pending_people) REFERENCES Users(email_address) " +
	  		"ADD FOREIGN KEY (group_name) REFERENCES Group_chats(group_name)";
			st.executeQuery(sql);

			sql = "ALTER TABLE Message_topic_words " +
	  		"ADD FOREIGN KEY (message_id) REFERENCES Messages(message_id)";
			st.executeQuery(sql);

			sql = "ALTER TABLE Group_chat_messages " +
			"ADD FOREIGN KEY (group_name) REFERENCES Group_chats(group_name) " +
	  		"ADD FOREIGN KEY (message_id) REFERENCES Messages(message_id)";
			st.executeQuery(sql);

			sql = "ALTER TABLE Circle_feed_messages " +
			"ADD FOREIGN KEY (circle_id) REFERENCES Circle_feeds(circle_id) " +
	  		"ADD FOREIGN KEY (message_id) REFERENCES Messages(message_id)";
			st.executeQuery(sql);

			sql = "ALTER TABLE Group_chat_members " +
			"ADD FOREIGN KEY (group_name) REFERENCES Group_chats(group_name) " +
	  		"ADD FOREIGN KEY (member) REFERENCES Users(email_address)";
			st.executeQuery(sql);

			sql = "ALTER TABLE Private_chat_messages " +
			"ADD FOREIGN KEY (pc_id) REFERENCES Private_chats(pc_id) " +
	  		"ADD FOREIGN KEY (message_id) REFERENCES Messages(message_id)";
			st.executeQuery(sql);
			
		}
		catch(Exception e){System.out.println(e);}
	}

	public static void deleteTables(Connection con){
		try{
			Statement st = con.createStatement();
			String sql = "DROP TABLE USERS CASCADE CONSTRAINTS";
			st.executeUpdate(sql);
			sql = "DROP TABLE MANAGERS CASCADE CONSTRAINTS";
			st.executeUpdate(sql);
			sql = "DROP TABLE CIRCLE_FEEDS CASCADE CONSTRAINTS";
			st.executeUpdate(sql);
			sql = "DROP TABLE GROUP_CHATS CASCADE CONSTRAINTS";
			st.executeUpdate(sql);
			sql = "DROP TABLE PRIVATE_CHATS CASCADE CONSTRAINTS";
			st.executeUpdate(sql);
			sql = "DROP TABLE MESSAGES CASCADE CONSTRAINTS";
			st.executeUpdate(sql);
			sql = "DROP TABLE CONTACT_PENDING_LISTS CASCADE CONSTRAINTS";
			st.executeUpdate(sql);
			sql = "DROP TABLE CONTACT_LISTS CASCADE CONSTRAINTS";
			st.executeUpdate(sql);
			sql = "DROP TABLE GROUP_PENDING_LISTS CASCADE CONSTRAINTS";
			st.executeUpdate(sql);
			sql = "DROP TABLE GROUP_CHAT_MEMBERS CASCADE CONSTRAINTS";
			st.executeUpdate(sql);
			sql = "DROP TABLE Message_topic_Words CASCADE CONSTRAINTS";
			st.executeUpdate(sql);
			sql = "DROP TABLE User_topic_words CASCADE CONSTRAINTS";
			st.executeUpdate(sql);
			sql = "DROP TABLE Group_chat_messages CASCADE CONSTRAINTS";
			st.executeUpdate(sql);
			sql = "DROP TABLE Circle_feed_messages CASCADE CONSTRAINTS";
			st.executeUpdate(sql);
			sql = "DROP TABLE Private_chat_messages CASCADE CONSTRAINTS";
			st.executeUpdate(sql);
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
			// print primary keys
			DatabaseMetaData dm = con.getMetaData();
			rs = dm.getPrimaryKeys(null, null, TBName);
			while(rs.next()){
				System.out.println("primary key ("+rs.getString("COLUMN_NAME")+")");
			} 
			// print foreign keys
			rs = dm.getImportedKeys(null, null, TBName);
			while(rs.next()){
				System.out.println("foreign_key ("+rs.getString(3)+
				") references "+rs.getString(8)+"("+rs.getString(4)+")");
			}	
                }
                catch(Exception e){System.out.println(e);}
	}

	public static void printGLEETables(Connection con){
		try{
			DatabaseMetaData md = con.getMetaData();
			ResultSet rs = md.getTables(null, "GLEE", "%", null);
			while (rs.next()) {
  				System.out.println(rs.getString(3));
			}
		}
		catch(Exception e){System.out.println(e);}
	}

	public static void printAllTables(Connection con){
		try{
			String[] tbNames = {"USERS", "MANAGERS", "CIRCLE_FEEDS", "GROUP_CHATS", "PRIVATE_CHATS", "MESSAGES", "CONTACT_PENDING_LISTS", "CONTACT_LISTS", "GROUP_PENDING_LISTS", "GROUP_CHAT_MEMBERS", "MESSAGE_TOPIC_WORDS", "USER_TOPIC_WORDS", "GROUP_CHAT_MESSAGES", "CIRCLE_FEED_MESSAGES", "PRIVATE_CHAT_MESSAGES"};
			for(int i=0; i<15; i++){
				printTable(con, tbNames[i]);
				System.out.println();
			}
		}
		catch(Exception e){System.out.println(e);}
	}

}
