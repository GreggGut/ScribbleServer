/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scribbleserver_0.pkg01;

import java.net.InetAddress;
import java.util.Vector;

/**
 *
 * @author scribble
 */
public class PacketAnalyzer implements Runnable
{

    private String toBeAnalyzed;
    private InetAddress clientAddress;
    private Vector<User> mUsers;
    private Vector<SCFile> mFiles;
    private final String split = "---";

    PacketAnalyzer(String info, InetAddress ip, Vector<User> user, Vector<SCFile> files)
    {
        toBeAnalyzed = info;
        clientAddress = ip;
        mUsers = user;
        mFiles = files;
    }

    public void run()
    {
        System.out.println("Received in Packet Analyzer: " + toBeAnalyzed);
        String[] info = toBeAnalyzed.split(split);

        System.out.println("After seperation");
        for (int i = 0; i < info.length; i++)
        {
            System.out.println(info[i]);
        }
        if (info.length > 0)
        {
            int choice = -1;
            try
            {
                choice = Integer.parseInt(info[0]);
            }
            catch (NumberFormatException e)
            {
                System.out.append("Failed to identify the received message");
                //Cannot identify what was send
                return;
            }


            switch (choice)
            {
                case 0:
                    //login
                    if (info.length > 2)
                    {
                        User user = new User(info[1], info[2], clientAddress);

                        boolean userExists = false;
                        //allowing unique usernames to be logged in simultaniously
                        for (User findUser : mUsers)
                        {
                            if (findUser.getName().contains(user.getName()))
                            {
                                userExists = true;
                                //User already exists, cannot login
                                /*
                                 * Send login fail, user already logged in message
                                 * Test if user is still logged in by testing the connection
                                 * if user does not respond, remove user from the list
                                 */
                                break;
                            }
                        }

                        if (!userExists)
                        {
                            //try to login
                            if (user.login())
                            {
                                mUsers.add(user);
                            }
                            else
                            {
                                /*
                                 * Error!!!!!
                                 * Cannot login, wrong username/password combination
                                 */
                            }
                        }
                    }
                    break;

                case 1:
                    //logout
                    if (info.length > 1)
                    {
                        for (int i = 0; i < mUsers.size(); i++)
                        {
                            if (mUsers.elementAt(i).getName().equals(info[1]) && mUsers.elementAt(i).getAddress().equals(clientAddress))
                            {
                                if (mUsers.elementAt(i).logout())
                                {
                                    mUsers.remove(i);
                                    String toSend = "";
                                    toSend += ServerToClient.logOutSuccessful;
                                    HELPER.send(toSend, clientAddress, ServerToClient.serverPort);
                                }

                                break;
                            }
                        }
                    }
                    else
                    {
                        //Error
                        //Cannot logout not enough info
                    }
                    break;

                case 2:
                    //Request Ownership
                    if (info.length > 1)
                    {
                        String username = info[1];
                        //String fileName = info[2];
                        for (User user : mUsers)
                        {
                            //Find the requesting user
                            if (user.getName().equals(username) && user.getAddress().equals(clientAddress))
                            {
                                //Make sure user is logged in
                                if (user.isLoggedIn())
                                {
                                    //no owner
                                    if (user.getActiveFile().getPresentOwner() == null)
                                    {
                                        user.getActiveFile().setPresentOwner(user);
                                        user.setOwnership(true);
                                    }
                                }
                                else
                                {
                                    //User needs to login first
                                    break;
                                }
                            }
                        }
                    }

                    break;

                case 3:
                    //Release Ownership
                    if (info.length > 1)
                    {
                        String username = info[1];
                        for (User user : mUsers)
                        {
                            if (user.getName().contains(username) && user.getAddress().equals(clientAddress))
                            {
                                user.setOwnership(false);
                                user.getActiveFile().setPresentOwner(null);

                                /*
                                 * Broadcast to all that the ownership is free
                                 */

                                break;
                            }
                        }
                    }

                    break;

                case 4:
                    //Get file list
                    String toBeSend = "";
                    toBeSend += ServerToClient.FileListAvailable;
                    toBeSend += split;

                    for (SCFile file : mFiles)
                    {
                        toBeSend += file.getName();
                        toBeSend += split;
                    }

                    //Send file list to client
                    HELPER.send(toBeSend, clientAddress, ServerToClient.serverPort);
                    break;

                case 5:
                    //Download file
                    break;

                case 6:
                    //New Path
                    break;

                case 7:
                    //Add point to path
                    break;

                case 8:
                    //End current path
                    break;

                case 9:
                    //undo last action
                    break;

                case 10:
                    //redi last action
                    break;

                case 11:
                    //delete path
                    break;
            }

        }
    }
}
