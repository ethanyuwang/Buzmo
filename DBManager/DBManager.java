import java.sql.*;

public class DBManager {

        public static String[] tbNames = {"TIME", "USERS", "MANAGERS", "CIRCLE_POSTS", "GROUP_CHATS", "PRIVATE_CHATS",
                                   "MESSAGES", "CONTACT_PENDING_LISTS", "CONTACT_LISTS", "GROUP_PENDING_LISTS",
                                   "GROUP_CHAT_MEMBERS", "POST_TOPIC_WORDS", "USER_TOPIC_WORDS", "CIRCLE_POST_RECEIVERS"};

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
                        //String username = "yuxiang";
                        //String password = "049";
                        Connection con = DriverManager.getConnection(url,username, password);

                        //Delete Tables
                        //deleteTables(con);

                        //Create Tables
                        //createTables(con);
                        //addForeignKeys(con);
			setTime(con, "2007-09-23 10:10:10.0");


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
	public static void createTime(Connection con){
		try{
			// set to current system time
			java.util.Date today = new java.util.Date();
			Timestamp time = new Timestamp(today.getTime());
			PreparedStatement ps;
			String sql = "INSERT INTO TIME VALUES(?, ?)";
			ps = con.prepareStatement(sql);
			ps.setTimestamp(1, time);
			ps.setInt(2, 0);
			ps.executeQuery();
		}
                catch(Exception e){System.out.println(e);}
	}
	public static void setTime(Connection con, String time_str){
                try{
			Timestamp time = Timestamp.valueOf(time_str);
                        PreparedStatement ps;
                        String sql = "UPDATE TIME SET base_time=? WHERE time_id=0";
                        ps = con.prepareStatement(sql);
                        ps.setTimestamp(1, time);
                        ps.executeUpdate();
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

                        sql = "CREATE TABLE Group_chats " +
                        "(group_id INT NOT NULL, " +
                        " group_name VARCHAR(20) NOT NULL, " +
                        " duration INT NOT NULL, " +
                        " owner VARCHAR(20) NOT NULL, " +
                        " PRIMARY KEY (group_id), " +
                        " UNIQUE (group_name))";
                        st.executeQuery(sql);

                        sql = "CREATE TABLE Private_chats " +
                        "(pc_id INT NOT NULL, " +
                        " member_1 VARCHAR(20) NOT NULL, " +
                        " member_2 VARCHAR(20) NOT NULL, " +
                        " PRIMARY KEY (pc_id))";
                        st.executeQuery(sql);

                        sql = "CREATE TABLE Messages " +
                        "(message_id INT NOT NULL, " +
                        " text_string VARCHAR(1200) NOT NULL, " +
                        " timestamp DATE NOT NULL, " +
                        " type VARCHAR(15) NOT NULL, " +
                        " owner VARCHAR(20) NOT NULL, " +
                        " sender VARCHAR(20) NOT NULL, " +
                        " receiver VARCHAR(20), " +
                        " group_id INT, " +
                        " PRIMARY KEY (message_id))";
                        st.executeQuery(sql);

                        sql = "CREATE TABLE Circle_posts " +
                        "(post_id INT NOT NULL, " +
                        " post_string VARCHAR(1400) NOT NULL, " +
                        " post_time DATE NOT NULL, " +
                        " is_public VARCHAR(15) NOT NULL, " +
                        " post_owner VARCHAR(20) NOT NULL, " +
                        " PRIMARY KEY (post_id))";
                        st.executeQuery(sql);

                        sql = "CREATE TABLE Circle_post_receivers " +
                        "(post_receiver VARCHAR(256) NOT NULL, " +
                        " post_id INT NOT NULL)";
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
                        " group_id INT NOT NULL)";
                        st.executeQuery(sql);

                        sql = "CREATE TABLE Group_chat_members " +
                        "(group_id INT NOT NULL, " +
                        " member VARCHAR(20) NOT NULL)";
                        st.executeQuery(sql);

                        sql = "CREATE TABLE Post_topic_words " +
                        "(topic_word VARCHAR(256) NOT NULL, " +
                        " post_id INT NOT NULL, " +
                        " UNIQUE (topic_word, post_id))";
                        st.executeQuery(sql);

                        sql = "CREATE TABLE User_topic_words " +
                        "(topic_word VARCHAR(256) NOT NULL, " +
                        " email_address VARCHAR(20) NOT NULL, " +
                        " UNIQUE (topic_word, email_address))";
                        st.executeQuery(sql);

			sql = "CREATE TABLE Time " + 
			"(base_time DATE NOT NULL, " + 
			" time_id INT NOT NULL, " + 
			" UNIQUE(time_id))"; 
                        st.executeQuery(sql);

			//time
			createTime(con);
                }
                catch(Exception e){System.out.println(e);}
        }

        public static void addForeignKeys(Connection con){
                try{
                        // Add foreign keys
                        Statement st = con.createStatement();

                        String sql = "ALTER TABLE Managers " +
                        "ADD FOREIGN KEY (email_address) REFERENCES Users(email_address) ON DELETE CASCADE";
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
                        "ADD FOREIGN KEY (receiver) REFERENCES Users(email_address) " +
                        "ADD FOREIGN KEY (group_id) REFERENCES Group_chats(group_id)";
                        st.executeQuery(sql);

                        sql = "ALTER TABLE Circle_posts " +
                        "ADD FOREIGN KEY (post_owner) REFERENCES Users(email_address)";
                        st.executeQuery(sql);

                         sql = "ALTER TABLE Circle_post_receivers " +
                        "ADD FOREIGN KEY (post_receiver) REFERENCES Users(email_address) ON DELETE CASCADE " +
                        "ADD FOREIGN KEY (post_id) REFERENCES Circle_posts(post_id) ON DELETE CASCADE";
                        st.executeQuery(sql);

                        sql = "ALTER TABLE User_topic_words " +
                        "ADD FOREIGN KEY (email_address) REFERENCES Users(email_address)";
                        st.executeQuery(sql);

                        sql = "ALTER TABLE Post_topic_words " +
                        "ADD FOREIGN KEY (post_id) REFERENCES Circle_posts(post_id) ON DELETE CASCADE";
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
                        "ADD FOREIGN KEY (group_id) REFERENCES Group_chats(group_id)";
                        st.executeQuery(sql);

                        sql = "ALTER TABLE Group_chat_members " +
                        "ADD FOREIGN KEY (group_id) REFERENCES Group_chats(group_id) " +
                        "ADD FOREIGN KEY (member) REFERENCES Users(email_address)";
                        st.executeQuery(sql);
                }
                catch(Exception e){System.out.println(e);}
        }

        public static void deleteTables(Connection con){
                Statement st;
                String sql;
                for(int i=0; i<tbNames.length; i++){
                        try{
                                st = con.createStatement();
                                sql = "DROP TABLE " + tbNames[i] + " CASCADE CONSTRAINTS";
                                st.executeUpdate(sql);
                        }
                        catch(Exception e){System.out.println(e);}
                }
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
                        for(int i=0; i<tbNames.length; i++){
                                printTable(con, tbNames[i]);
                                System.out.println();
                        }
                }
                catch(Exception e){System.out.println(e);}
        }
}
