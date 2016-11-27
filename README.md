# Buzmo for F16 CS174a
* Geon Lee, Ethan Wang

# To-do:
* ~~Merge: DBInteractor.java, DBManager.java~~

* PrivateChats currently does not use Private_chats table. Update interactor so that it is used.

* Delete/delete on cascade

* Manager mode

* ~~Post a message: The message can be a private message, or in the MyCircle, or in a ChatGroup. Note that
there are different parameters in these three settings.~~

* Delete a message: Remove the message in the private conversation, ~~the MyCircle~~, or a ChatGroup.

* ~~Create a ChatGroup: The user creates a new ChatGroup, group properties are also set.~

* ~~Modify ChatGroup properties: Change title, duration for the ChatGroup.~~

* ~~Invite and accept: invite a user (email address) to join a ChatGroup and accept the invitation.~~

* ~~Search recent messages: Given one or more topic words, find n most recent public messages matching all
or at least one given topic words (two different types of searches). The default set of topic words is the
set of topic words associated with the user.~~

* Search for users: search criteria include a combination of email address, one or more matching topic words,
most recent posting is within the last n days (n ≤ 7), n or more number of messaged posted within the
last 7 days.

* ~~Request to join friend circle: Once a new user (email address) is found, send a request to be a friend of
that user.~~

* Summary reports: Every 7 days there will be a usage summer report generated. The report include during
the 7 day period, total number of new messages, total number of message reads, average number of new
message reads, average number of reads for messages sent in the last period, top 3 messages by read
counts (including their authors), top 3 users by new message counts, the number of users who send less
than 3 messages, and for each topic word, the most read message with that topic word.
