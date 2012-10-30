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
    private Vector<Request> mRequests;

    PacketAnalyzer(String info, InetAddress ip, Vector<User> user, Vector<SCFile> files, Vector<Request> mRequests)
    {
        toBeAnalyzed = info;
        clientAddress = ip;
        mUsers = user;
        mFiles = files;
        this.mRequests = mRequests;
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
                    //Info will contain the following
                    //login - username - password
                    if (info.length > 3)
                    {
                        int port;
                        try
                        {
                            port = Integer.parseInt(info[3]);
                        }
                        catch (NumberFormatException e)
                        {
                            //if the port fails to be translated into a number we cannot do anything and therefore we drop this request
                            break;
                        }

                        //String name, String password, InetAddress address, int port
                        User user = new User(info[1], info[2], clientAddress, port);

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
                            }
                        }
                        else
                        {
                            String toSend = "";
                            toSend += ServerToClient.logInFailed;
                            toSend += split;
                            toSend += ServerToClient.usernameAlreadyLoggedIn;
                            toSend += split;

                            HELPER.send(toSend, user.getAddress(), user.getPort());
                        }
                    }
                    break;

                case 1:
                    //logout
                    //logout - username
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
                    //requestOwnership - username - ID
                    if (info.length > 2)
                    {
                        try
                        {
                            String username = info[1];
                            int requestID = Integer.parseInt(info[2]);

                            for (User user : mUsers)
                            {
                                //Find the requesting user
                                if (user.getName().equals(username) && user.getAddress().equals(clientAddress))
                                {
                                    //Making sure there is no owner
                                    if (user.getActiveFile().getPresentOwner() == null)
                                    {
                                        user.getActiveFile().setPresentOwner(user);
                                        user.setOwnership(true);

                                        String toSend = "";
                                        toSend += ServerToClient.ownershipTaken;
                                        toSend += split;
                                        toSend += user.getName();
                                        toSend += split;

                                        //Informing all users that the file ownership has been taken
                                        for (User allUsers : user.getActiveFile().getmActiveUsers())
                                        {
                                            HELPER.send(toSend, allUsers.getAddress(), allUsers.getPort());
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
                        catch (Exception x)
                        {
                        }

                    }

                    break;

                case 3:
                    //Release Ownership
                    //releaseOwnership - username
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
                                String toSend = "";
                                toSend += ServerToClient.noOwnerOfFile;
                                toSend += split;

                                for (User allUsers : user.getActiveFile().getmActiveUsers())
                                {
                                    HELPER.send(toSend, allUsers.getAddress(), allUsers.getPort());
                                }

                                break;
                            }
                        }
                    }

                    break;

                case 4:
                    //Get file list
                    //getFileList
                    String toBeSend = "";
                    toBeSend += ServerToClient.fileListAvailable;
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
                    //newPath - username - requestD - pathID - mode - color - active - page

                    //find the user who is sending this
                    SCFile mfile = null;
                    User currentUser = null;
                    for (User user : mUsers)
                    {
                        if (user.getName().equals(info[1]) && user.getAddress().equals(clientAddress))
                        {
                            mfile = user.getActiveFile();
                            currentUser = user;
                            break;
                        }
                    }

                    if (mfile != null && currentUser != null)
                    {
                        //Create new path for the user decument
                        //(int id, boolean mode, int color, boolean active)
                        try
                        {
                            int requestID = Integer.parseInt(info[2]);
                            int pathID = Integer.parseInt(info[3]);
                            boolean mode;
                            if (info[4].equals("0"))
                            {
                                mode = false;
                            }
                            {
                                mode = true;
                            }
                            int color = Integer.parseInt(info[5]);
                            boolean active;
                            if (info[6].equals("0"))
                            {
                                active = false;
                            }
                            {
                                active = true;
                            }

                            int page = Integer.parseInt(info[7]);

                            Path p = new Path(pathID, mode, color, active);

                            mfile.getmPages().get(page).addPath(p);
                            currentUser.setWorkingPage(mfile.getmPages().get(page));

                        }
                        catch (Exception e)
                        {
                        }

                    }

                    //byte[] infos = info.getBytes();                    
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
                    //redo last action
                    break;

                case 11:
                    //delete path
                    break;
            }

        }
    }
}
