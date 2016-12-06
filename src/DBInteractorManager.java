package buzmo;

import java.sql.*;
import java.io.*;
import java.net.*;
import java.lang.*;
import java.util.Arrays;

public class DBInteractorManager {

	public enum loadDBControl {
		none, addUser, addContacts, addPrivateMessages, addGroups, addCircle, addTopicWords, addManager, finish
	} 

	public static Boolean loadDB(Connection con){
		
		loadDBControl ldbc=loadDBControl.none;
		String database="resource/data_base_sampler.txt";

		Boolean addGroupInfo = false;
		Boolean addGroupMessages = false;
		String currentGroupName = "";
		//URL url=getClass().getResource(database);
		//File db = new File(url.getPath());
		File db = new File(database);

		try (BufferedReader br = new BufferedReader(new FileReader(db))){
			String line;
			line = br.readLine();

			while ((line = br.readLine())!=null){
				//decide what to do
				if (line.equals("-Users"))
				{
					ldbc=loadDBControl.addUser;
					System.out.println("Start adding" + line);
					continue;
				}
				else if(line.equals("-Friends"))
				{
					ldbc=loadDBControl.addContacts;
					System.out.println("Start adding" + line);
					continue;
				}
				else if(line.equals("-Private_Messages"))
				{
					ldbc=loadDBControl.addPrivateMessages;
					System.out.println("Start adding" + line);
					continue;
				}
				else if(line.equals("-Chat_groups"))
				{
					ldbc=loadDBControl.addGroups;
					System.out.println("Start adding" + line);
					continue;
				}
				else if(line.equals("-Circle_feeds"))
				{
					ldbc=loadDBControl.addCircle;
					System.out.println("Start adding" + line);
					continue;
				}
				else if(line.equals("-User_Topic_words"))
				{
					ldbc=loadDBControl.addTopicWords;
					System.out.println("Start adding" + line);
					continue;
				}
				else if(line.equals("-Managers"))
				{
					ldbc=loadDBControl.addManager;
					System.out.println("Start adding" + line);
					continue;
				}
				else if(line.equals("-Finish"))
				{
					ldbc=loadDBControl.finish;
					System.out.println("Start adding" + line);
					continue;
				}
				else if(line.equals("---------------------------------------------------------------------"))
				{
					ldbc=loadDBControl.none;
					System.out.println(line);
					continue;
				}

				//do specific task
				switch(ldbc) {
					case addUser: {
						System.out.println("Adding user " + line);
						if (addUserWithString(con, line)==false)
							System.out.println("Error at " + line);
						break;
					}
					case addContacts: {
						System.out.println("Adding contacts " + line);
						if (addContactsCircle(con, line)==false)
							System.out.println("Error at " + line);
						break;
					}
					case addPrivateMessages: {
						System.out.println("Adding private messages " + line);
						if (addPrivateMessagesWithString(con, line)==false)
							System.out.println("Error at " + line);
						break;
					}
					case addGroups: {
						System.out.println("Adding groups " + line);
						//-----------------------need to factor out-------------------
						if (addGroupInfo&&(!line.equals("-group_info"))&&(!line.equals("-group_messages")))
						{
							String[] informations = line.split(", ");
							currentGroupName = informations[0];
							createGroupWithOwner(con, currentGroupName, getEmialWithName(con, informations[1]));

							for (int member = 2; member<informations.length; member++) {
								if (addGroupChatDirectly(con, currentGroupName, getEmialWithName(con, informations[member]))==false)
								{
									System.out.println("error at addGroupChatDirectly" + line);
								}
							}
						}
						else if (addGroupMessages&&(!line.equals("-group_info"))&&(!line.equals("-group_messages")))
						{
							String[] informations = line.split("; ");
							/*System.out.println("informations[0]" + informations[0]);
							System.out.println("informations[1]" + informations[1]);
							System.out.println("informations[2]" + informations[2]);*/

							if (addMessageToGroupChatDirectly(con, informations[1], 
										currentGroupName, getEmialWithName(con, informations[0]), 
										parseTimeStamp(informations[2]))==false)
							{
								System.out.println("error at addGroupChatDirectly" + line);
							}
						}

						if (line.equals("-group_info"))
						{
							addGroupInfo=true;
							addGroupMessages=false;
						}
						else if (line.equals("-group_messages"))
						{
							addGroupInfo=false;
							addGroupMessages=true;
						}						
						//------------------------------------------------------------	
						break;
					}
					case addCircle: {
						System.out.println("Adding circle feed " + line);
						if (addCircle(con, line)==false)
							System.out.println("Error at " + line);
						break;
					}
					case addTopicWords: {
						System.out.println("Adding user topic words " + line);
						//if (addUserTopicWordsLine(con, line)==false)
						//	System.out.println("Error at " + line);
						break;
					}
					case addManager: {
						System.out.println("Adding manager " + line);
						//if (addManagerLine(con, line)==false)
						//	System.out.println("Error at " + line);
						break;
					}

					default: {
						break;
					}				
				}
			}
			if (ldbc==loadDBControl.finish)
				return true;
			else
				return false;
		}
		catch(Exception e){System.out.println(e); return false;}
	}

	public static Boolean addUserWithString(Connection con, String line){
		String[] informations = line.split(", ");
		//System.out.println(informations[1]+ informations[2]+ informations[0]+ informations[3]+ informations[4]);
		return DBInteractor.addUser(con, informations[1], informations[2], informations[0], informations[3], informations[4]);
	}

	public static Boolean addContactsCircle(Connection con, String line){
		String[] contacts = line.split(", ");
		//for (int i=0; i<contacts.length;i++)
		//	System.out.println("We are contacts: "+contacts[i]+"\n");

		for (int owner = 0; owner<(contacts.length-1); owner++) {
			for (int contact = owner+1; contact<(contacts.length); contact++) {
				if (addContactsDirectly(con, contacts[owner], contacts[contact])==false)
					return false;
			}
		}
		return true;
	}

	public static Boolean addPrivateMessagesWithString(Connection con, String line){
		String[] info = line.split("; ");
		return(addMessageToPrivateChatWithMoreInfo(con, info[2], getEmialWithName(con, info[1]), getEmialWithName(con, info[0]), info[3]));
	}
	
		//debug and managing purpose, not normal program flow


	//debug and managing purpose, not normal program flow
	public static Boolean addContactsDirectly(Connection con, String owner, String contact){
		try {
			System.out.println("Adding contacts: "+owner+contact);
			Statement st = con.createStatement();
			// check pending list
			String acceptEmail = getEmialWithName(con, owner);
			String receiverEmail = getEmialWithName(con, contact);
			// add to contact list both ways
			String sql = "INSERT INTO CONTACT_LISTS " + 
			"VALUES ('" + acceptEmail + "', " + 
			" '" + receiverEmail + "')";
			st.executeUpdate(sql);
			sql = "INSERT INTO CONTACT_LISTS " + 
			"VALUES ('" + receiverEmail + "', " + 
			" '" + acceptEmail + "')";
			st.executeUpdate(sql);
			return true;
		}
		catch(Exception e){System.out.println(e); return false;}
	}



	public static Boolean addMessageToPrivateChatWithMoreInfo(Connection con, String message, String recipientEmail, String SenderEmail, String time){
		try {
			String myEmail = SenderEmail;
			Timestamp ts = parseTimeStamp(time);
			String messageWithTime = message+ts.toString();

			//Add a copy to sender
			String sql = "INSERT INTO MESSAGES VALUES (?,?,?,?,?,?,?,?)";	
			PreparedStatement ps = con.prepareStatement(sql);
			con.setAutoCommit(false);
			String messageWithTimeAndOwner = messageWithTime + myEmail;
			ps.setInt(1, messageWithTimeAndOwner.hashCode());
			ps.setString(2, message);
			ps.setTimestamp(3, ts);
			ps.setString(4, "private");
			ps.setString(5, myEmail);
			ps.setString(6, myEmail);
			ps.setString(7, recipientEmail);
			ps.setNull(8, java.sql.Types.INTEGER);
			ps.addBatch();
			ps.executeBatch();
			con.commit();

			//Add a copy to recipient
			ps = con.prepareStatement(sql);
			messageWithTimeAndOwner = messageWithTime + recipientEmail;
			ps.setInt(1, messageWithTimeAndOwner.hashCode());
			ps.setString(2, message);
			ps.setTimestamp(3, ts);
			ps.setString(4, "private");
			ps.setString(5, recipientEmail);
			ps.setString(6, myEmail);
			ps.setString(7, recipientEmail);
			ps.setNull(8, java.sql.Types.INTEGER);
			ps.addBatch();
			ps.executeBatch();
			con.commit();
			con.setAutoCommit(true);
			return true;
		}
		catch(Exception e){System.out.println(e); return false;}
	}

	//Used for GroupChatJPanel 
	public static String getEmialWithName(Connection con, String name){
		try {
			String ret ="";
			Statement st = con.createStatement();
			String sql = "SELECT U.email_address FROM Users U " +
			"WHERE U.name='" + name + "'";
			ResultSet rs = st.executeQuery(sql);
			while(rs.next()){
				ret = rs.getString(1);
			}
			return ret;
		}
		catch(Exception e){System.out.println(e); return "";}
	}

	public static Boolean createGroupWithOwner(Connection con, String groupName, String ownerEmail){
		try { 
			Statement st = con.createStatement();
			int groupId = groupName.hashCode();
			if (groupId == 0)
			{
				System.out.println("groupID not found\n");
				return false;
			}
			// Create table
			String sql = "INSERT INTO Group_chats " +
			"VALUES (" + groupId + ", '" +
			groupName + "', " +
			7 + ", " +
			"'" + ownerEmail + "')";
			st.executeUpdate(sql);

			// link to group members
			sql = "INSERT INTO Group_chat_members " +
			"VALUES (" + groupId + ", '" +
			ownerEmail + "')";

			st.executeUpdate(sql);
			return true;
		}
		catch(Exception e){System.out.println(e); return false;}
}

	//Used for GroupChatJPanel 
	public static Boolean addGroupChatDirectly(Connection con, String groupName, String memeberEmail){
		try {
			int groupId = DBInteractorGroupChat.getGroupID(con, groupName);
			if (groupId == 0)
			{
				System.out.println("groupID not found\n");
				return false;
			}
			String receiverEmail = BuzmoJFrame.userEmail;
			Statement st = con.createStatement();
	
			// add to GROUP MEMEBERS list
			String sql = "INSERT INTO Group_chat_members " + 
			"VALUES ('" + groupId + "', " + 
			" '" + memeberEmail + "')";
			st.executeUpdate(sql);
			
			return true;
		}
		catch(Exception e){System.out.println(e); return false;}
	}
	public static Boolean addCircle(Connection con, String line) {
		String[] informations = line.split("; ");
		System.out.println("informations[0] "+informations[0]);
		System.out.println("informations[1] "+informations[1]);
		System.out.println("informations[2] "+informations[2]);
		System.out.println("informations[3] "+informations[3]);

		String[] social =informations[0].split(" \\(");
		String userEmail = getEmialWithName(con, social[0]);
		String[] topics = (informations[2].replace("Topic words: ", "")).split(", ");
		Boolean isPublic = false;
		Timestamp ts = parseTimeStamp(informations[3]);



		String[] receivers = (social[1].replace(")", "")).split(", ");

		System.out.println("social[0] "+social[0]);
		System.out.println("social[1] "+social[1].replace(")", ""));

		social[1].replace(")", "");


		if (receivers[0].equals("all"))
		{
			isPublic = true;
			System.out.println("isPublic  "+isPublic);
			receivers = getContactListsAsArray(BuzmoJFrame.con, userEmail);
			for(int i=0; i<receivers.length; i++){
				System.out.println("receivers  "+i+receivers[i]);
			}
		}
		
		int post_id = createCirclePostDirectly(con, userEmail, informations[1], topics, isPublic, ts);

		if (post_id==-1)
		{
			System.out.println("FAILED to create circle feed: " +userEmail+ informations[1]+ topics[0]+ isPublic+ ts);
			return true;
		}
		//link owner
		if(DBInteractorCirclePost.linkReceiversToCirclePost(BuzmoJFrame.con, userEmail, post_id)){
					System.out.println("SUCCESS linked user to circle feed");}
		else{System.out.println("FAILED to link user to circle feed");return false;}

		//link recivers

		if (isPublic)
		{
			for(int i=0; i<receivers.length; i++){
				System.out.println("receivers[i]  "+receivers[i]);

				if(DBInteractorCirclePost.linkReceiversToCirclePost(BuzmoJFrame.con, receivers[i], post_id)){
					System.out.println("SUCCESS linked user to circle feed");}
				else{System.out.println("FAILED to link user to circle feed");return false;}	
			}
		}
		else
		{
			for(int i=0; i<receivers.length; i++){
				System.out.println("receivers[i]  "+receivers[i]);

				if(DBInteractorCirclePost.linkReceiversToCirclePost(BuzmoJFrame.con, getEmialWithName(con, receivers[i]), post_id)){
					System.out.println("SUCCESS linked user to circle feed");}
				else{System.out.println("FAILED to link user to circle feed");return false;}	
			}
		}
		
		return true;
	}

	public static boolean addUserTopicWordsLine(Connection con, String line){
		String[] info = line.split("; ");
		String[] topics = info[1].split(", ");
		return addUserTopicWordsDirectly(con, getEmialWithName(con, info[0]), topics);

	}
	public static boolean addManagerLine(Connection con, String line){
		DBInteractorManagerMode.addManager(con, getEmialWithName(con, "Tim Cook"));
		DBInteractorManagerMode.addManager(con, getEmialWithName(con, "Ariana Grande"));


		/*String[] names = line.split("; ");
		for (int i = 0; i<names.length; i++)
		{
			if (DBInteractorManagerMode.addManager(con, getEmialWithName(con, names[i])==false))
				return false;
		}*/
		return true;
	}

		//User topic words
	public static boolean addUserTopicWordsDirectly(Connection con, String userEmail, String[] topicArray){
		try {
			String sql;
			String myEmail = userEmail;	
			Statement st = con.createStatement();
			for(int i=0; i<topicArray.length; i++){
                                sql = "INSERT INTO USER_TOPIC_WORDS " +
                                "(SELECT'" + topicArray[i] + "' AS TOPIC_WORD, " +
                                " '" + myEmail + "' AS EMAIL_ADDRESS " +
                                "FROM USER_TOPIC_WORDS WHERE " +
                                "TOPIC_WORD='" + topicArray[i] + "' AND " +
                                "EMAIL_ADDRESS='" + myEmail + "' HAVING COUNT(*) = 0)";
                                st.executeUpdate(sql);
			}
			return true;
		}
		catch(Exception e){System.out.println(e); return false;}
	}

	public static int createCirclePostDirectly(Connection con, String userEmail, String message, String[] topicArray, boolean publicChecked, Timestamp ts){
		try {
			String is_public = "False";
			if(publicChecked) {is_public = "True";}
			String myEmail = userEmail;
			String hashStr = "CF" + myEmail + message + ts.toString();
			int hashCode = hashStr.hashCode();
			String sql = "INSERT INTO CIRCLE_POSTS VALUES (?,?,?,?,?,?)";	
			PreparedStatement ps = con.prepareStatement(sql);
			// 1. Create message
			con.setAutoCommit(false);
			ps.setInt(1, hashCode);
			ps.setString(2, message);
			ps.setTimestamp(3, ts);
			ps.setString(4, is_public);
			ps.setString(5, myEmail);
			ps.setInt(6, 0);
			ps.addBatch();
			ps.executeBatch();
			con.commit();
			con.setAutoCommit(true);
			// 2. Link topic words to message/user
			Statement st = con.createStatement();
			for(int i=0; i<topicArray.length; i++){
				sql = "INSERT INTO POST_TOPIC_WORDS " +
				"(SELECT'" + topicArray[i] + "' AS TOPIC_WORD, " + 
				" " + hashCode + " AS POST_ID " +
				"FROM POST_TOPIC_WORDS WHERE " +
				"TOPIC_WORD='" + topicArray[i] + "' AND " + 
				"POST_ID=" + hashCode + " HAVING COUNT(*) = 0)";
				st.executeUpdate(sql);
			}
			return hashCode;
		}	
		catch(Exception e){System.out.println(e); return -1;}
	}

	public static Boolean addMessageToGroupChatDirectly(Connection con, String message, String groupName, String memeberEmail, Timestamp ts){
		try {

			String myEmail = memeberEmail;
			String messageWithTime = message+ts.toString();
			int groupId = DBInteractorGroupChat.getGroupID(con, groupName);
			if (groupId == 0)
			{
				System.out.println("groupID not found\n");
				return false;
			}
			//Add a copy to sender
			String sql = "INSERT INTO MESSAGES VALUES (?,?,?,?,?,?,?,?)";	
			PreparedStatement ps = con.prepareStatement(sql);
			con.setAutoCommit(false);
			String messageWithTimeAndOwner = messageWithTime + myEmail;
			ps.setInt(1, messageWithTimeAndOwner.hashCode());
			ps.setString(2, message);
			ps.setTimestamp(3, ts);
			ps.setString(4, "group");
			ps.setString(5, myEmail);
			ps.setString(6, myEmail);
			ps.setString(7, myEmail);
			ps.setInt(8, groupId);

			ps.addBatch();
			ps.executeBatch();
			con.commit();
			return true;
		}
		catch(Exception e){System.out.println(e); return false;}
}

	public static Timestamp parseTimeStamp(String line) {
		String properFormat;
		System.out.println("time parser line: "+line);

		String[] dateTime = line.split(", ");
		String[] date = dateTime[0].split("\\.");
		String[] time = dateTime[1].split(" ");

		String month = String.format("%02d", Integer.parseInt(date[0]));
		String day = String.format("%02d", Integer.parseInt(date[1]));

		properFormat = date[2]+"-"+month+"-"+day+" ";

		String[] exactTime = time[0].split(":");

		if (time[1].equals("PM"))
			exactTime[0]=String.valueOf(Integer.parseInt(exactTime[0])+12);

		properFormat += (exactTime[0]+":"+exactTime[1]+":00.0");

		System.out.println("properFormat: "+properFormat);
		java.sql.Timestamp timestamp = java.sql.Timestamp.valueOf(properFormat);
		return timestamp;
	}

	public static String[] getContactListsAsArray(Connection con, String myEmail){
                try {
                        String[] ret;
                        Statement st = con.createStatement();
                        String sql = "SELECT count(*) FROM CONTACT_LISTS C " +
                        "WHERE C.owner='" + myEmail + "'";
                        ResultSet rs = st.executeQuery(sql);
                        if(rs.next()){
                                ret = new String[rs.getInt(1)];
                        }
                        else{
                                return null;
                        }
                        sql = "SELECT C.friend FROM CONTACT_LISTS C " +
                        "WHERE C.owner='" + myEmail + "'";
                        rs = st.executeQuery(sql);
                        int i = 0;
                        while(rs.next()){
                                ret[i] = rs.getString(1);
                                i++;
                        }
                        return ret;
                }
                catch(Exception e){System.out.println(e); return null;}
        }

}
