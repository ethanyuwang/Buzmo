package buzmo;

import java.sql.*;
import java.io.*;
import java.net.*;
import java.lang.*;

public class DBInteractorManager {

	public enum loadDBControl {
		none, addUser, addContacts, addPrivateMessages, addGroups, addCircle, addTopicWords, addManager, finish
	} 

	public static Boolean loadDB(Connection con){
		
		loadDBControl ldbc=loadDBControl.none;
		String database="resource/data_base_sampler.txt";
		//URL url=getClass().getResource(database);
		//File db = new File(url.getPath());
		File db = new File(database);

		try (BufferedReader br = new BufferedReader(new FileReader(db))){
			String line;
			line = br.readLine();

			while ((line = br.readLine())!=null){
				//decide what to do
				if (line.equals("Users"))
				{
					ldbc=loadDBControl.addUser;
					System.out.println("Start adding" + line);
					continue;
				}
				else if(line.equals("Friends"))
				{
					ldbc=loadDBControl.addContacts;
					System.out.println("Start adding" + line);
					continue;
				}
				else if(line.equals("Private_Messages"))
				{
					ldbc=loadDBControl.addPrivateMessages;
					continue;
				}
				else if(line.equals("Chat_groups"))
				{
					ldbc=loadDBControl.addGroups;
					continue;
				}
				else if(line.equals("Circle_feeds"))
				{
					ldbc=loadDBControl.addCircle;
					continue;
				}
				else if(line.equals("User_Topic_words"))
				{
					ldbc=loadDBControl.addTopicWords;
					continue;
				}
				else if(line.equals("Managers"))
				{
					ldbc=loadDBControl.addManager;
					continue;
				}
				else if(line.equals("Finish"))
				{
					ldbc=loadDBControl.finish;
					continue;
				}

				//do specific task
				switch(ldbc) {
					case addUser: {
						System.out.println("Adding user " + line);
						if (addUserWithString(con, line)==false)
							return false;
						break;
					}
					case addContacts: {
						System.out.println("Adding contacts " + line);
						if (addContactsCircle(con, line)==false)
							return false;
						break;
					}
					case addPrivateMessages: {
						//if (addUser(con, line)==false)
						//	return false;
						break;
					}
					case addGroups: {
						//if (addContactsCircle(con, line)==false)
						//	return false;
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
		String[] informations = line.split(",");
		//System.out.println(informations[1]+ informations[2]+ informations[0]+ informations[3]+ informations[4]);
		return DBInteractor.addUser(con, informations[1], informations[2], informations[0], informations[3], informations[4]);
	}

	public static Boolean addContactsCircle(Connection con, String line){
		String[] contacts = line.split(",");
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
	//Used for GroupChatJPanel 
	public static String getEmialWithName(Connection con, String name){
		try {
			String ret ="";
			Statement st = con.createStatement();
			String sql = "SELECT U.email_address FROM Users U " +
			"WHERE U.name='" + name + "'";
			ResultSet rs = st.executeQuery(sql);
			if(rs.next()){
				ret = rs.getString(1);
			}
			return ret;
		}
		catch(Exception e){System.out.println(e); return "";}
	}
}
