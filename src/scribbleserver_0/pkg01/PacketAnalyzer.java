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
    //private final String split = "---";
    //private Vector<Request> mRequests;

    PacketAnalyzer(String info, InetAddress ip, Vector<User> user, Vector<SCFile> files, Vector<Request> mRequests)
    {
        toBeAnalyzed = info;
        clientAddress = ip;
        mUsers = user;
        mFiles = files;
        //this.mRequests = mRequests;
    }

    public void run()
    {
        System.out.println("Received in Packet Analyzer: " + toBeAnalyzed);
        String[] info = toBeAnalyzed.split(HELPER.split);

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
                    //login - username - requestID - password - port
                    if (info.length > 4)
                    {
                        try
                        {
                            int port = Integer.parseInt(info[4]);
                            int requestID = Integer.parseInt(info[2]);

                            User user = new User(info[1], info[3], clientAddress, port, requestID, mFiles.get(0));

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

                                    String toSend = "";
                                    toSend += ServerToClient.logInSuccessful;
                                    toSend += HELPER.split;

                                    HELPER.send(toBeAnalyzed, user.getAddress(), user.getPort());
                                }
                                else
                                {
                                    String toSend = "";
                                    toSend += ServerToClient.logInFailedWrongPassword;
                                    toSend += HELPER.split;

                                    HELPER.send(toSend, user.getAddress(), user.getPort());
                                }
                            }
                            else
                            {
                                String toSend = "";
                                toSend += ServerToClient.logInFailedUsernameAlreadyLoggedIn;
                                toSend += HELPER.split;

                                HELPER.send(toSend, user.getAddress(), user.getPort());
                            }
                        }
                        catch (NumberFormatException e)
                        {
                            //if the port fails to be translated into a number we cannot do anything and therefore we drop this request
                        }
                    }
                    break;

                case 1:
                    //logout
                    //logout - username - requestID
                    if (info.length > 2)
                    {
                        try
                        {
                            int requestID = Integer.parseInt(info[2]);
                            for (int i = 0; i < mUsers.size(); i++)
                            {
                                if (mUsers.elementAt(i).getName().equals(info[1]) && mUsers.elementAt(i).getAddress().equals(clientAddress))
                                {
                                    LogoutRequest request = new LogoutRequest(requestID);
                                    mUsers.elementAt(i).addRequest(request);

                                    //waiting until the request has been completed
                                    while (!request.isCompleted())
                                    {
                                        try
                                        {
                                            Thread.sleep(300);
                                        }
                                        catch (InterruptedException x)
                                        {
                                        }
                                    }
                                    //Once the request has been completed we can remove the user from the list
                                    mUsers.remove(i);
                                    
                                    System.out.println("User logout and removed");

                                    break;
                                }
                            }
                        }
                        catch (NumberFormatException x)
                        {
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

                                    OwnershipRequest request = new OwnershipRequest(requestID);
                                    user.addRequest(request);
                                    
                                    System.out.println("OwnershipRequest request placed");
                                    break;
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
                    //releaseOwnership - username - requestID
                    if (info.length > 2)
                    {
                        try
                        {
                            String username = info[1];
                            int requestID = Integer.parseInt(info[2]);
                            for (User user : mUsers)
                            {
                                if (user.getName().contains(username) && user.getAddress().equals(clientAddress))
                                {
                                    ReleaseOwnershipRequest request = new ReleaseOwnershipRequest(requestID);
                                    user.addRequest(request);
                                    System.out.println("ReleaseOwnershipRequest request placed");
                                    
                                    break;
                                }
                            }
                        }
                        catch (NumberFormatException x)
                        {
                        }
                    }

                    break;

                case 4:
                    //Get file list
                    //getFileList - username - requestID
                    if (info.length > 2)
                    {
                        try
                        {
                            String username = info[1];
                            int requestID = Integer.parseInt(info[2]);

                            for (User user : mUsers)
                            {
                                if (user.getName().equals(username) && user.getAddress().equals(clientAddress))
                                {
                                    //We find the user who placed the request
                                    GetFileListRequest request = new GetFileListRequest(requestID, mFiles);
                                    user.addRequest(request);
                                    System.out.println("GetFileListRequest request placed");
                                    
                                    break;
                                }
                            }
                        }
                        catch (NumberFormatException x)
                        {
                        }
                    }

                    break;

                case 5:
                    //Download file
                    break;

                case 6:
                    //New Path
                    //newPath - username - requestID - pathID - mode - color - active - page

                    if (info.length > 7)
                    {
                        try
                        {
                            //Parsing all the received info
                            String username = info[1];
                            int requestID = Integer.parseInt(info[2]);
                            int pathID = Integer.parseInt(info[3]);
                            boolean mode;
                            if (info[4].equals("1"))
                            {
                                mode = true;
                            }
                            else
                            {
                                mode = false;
                            }
                            int color = Integer.parseInt(info[5]);
                            boolean active;
                            if (info[6].equals("1"))
                            {
                                active = true;
                            }
                            else
                            {
                                active = false;
                            }
                            int page = Integer.parseInt(info[7]);

                            for (User user : mUsers)
                            {
                                if (user.getName().equals(username) && user.getAddress().equals(clientAddress))
                                {
                                    NewPathRequest request = new NewPathRequest(requestID, pathID, mode, color, active, page);
                                    user.addRequest(request);
                                    
                                    System.out.println("NewPathRequest request placed");

                                    //to be broadcasted:
                                    //For now we are sending the request exactly as it came to us
                                    //TOCONFIRM Maybe we should have a new request numbering
                                    broadcastRequest(user, toBeAnalyzed);
                                    break;
                                }
                            }
                        }
                        catch (NumberFormatException x)
                        {
                        }
                    }

                    //byte[] infos = info.getBytes();                    
                    break;

                case 7:
                    //Add point to path
                    //AddPoints - username - requestID - pathID - numberOfPoints - Points

                    if (info.length > 5)
                    {
                        try
                        {
                            String username = info[1];
                            int requestID = Integer.parseInt(info[2]);
                            int pathID = Integer.parseInt(info[3]);
                            int numberOfPoints = Integer.parseInt(info[4]);
                            String points = info[5];

                            for (User user : mUsers)
                            {
                                if (user.getName().equals(username) && user.getAddress().equals(clientAddress))
                                {
                                    AddPointsRequest request = new AddPointsRequest(requestID, pathID, numberOfPoints, points);
                                    user.addRequest(request);

                                    System.out.println("AddPointsRequest request placed");
                                    
                                    //to be broadcasted:
                                    //For now we are sending the request exactly as it came to us
                                    //TOCONFIRM Maybe we should have a new request numbering
                                    broadcastRequest(user, toBeAnalyzed);
                                    break;
                                }
                            }
                        }
                        catch (NumberFormatException x)
                        {
                        }
                    }

                    break;

                case 8:
                    //End current path
                    //EndPath - username - requestID - pathID
                    if (info.length > 3)
                    {
                        try
                        {
                            String username = info[1];
                            int requestID = Integer.parseInt(info[2]);
                            int pathID = Integer.parseInt(info[3]);

                            for (User user : mUsers)
                            {
                                if (user.getName().equals(username) && user.getAddress().equals(clientAddress))
                                {
                                    EndPathRequest request = new EndPathRequest(requestID, pathID);
                                    user.addRequest(request);

                                    System.out.println("EndPathRequest request placed");
                                    
                                    //to be broadcasted:
                                    //For now we are sending the request exactly as it came to us
                                    //TOCONFIRM Maybe we should have a new request numbering
                                    broadcastRequest(user, toBeAnalyzed);
                                    break;
                                }
                            }
                        }
                        catch (NumberFormatException x)
                        {
                        }
                    }
                    break;

                case 9:
                    //undo last action
                    //Undo - username - requestID - page - pathID
                    if (info.length > 4)
                    {
                        try
                        {
                            String username = info[1];
                            int requestID = Integer.parseInt(info[2]);
                            int page = Integer.parseInt(info[3]);
                            int pathID = Integer.parseInt(info[4]);

                            for (User user : mUsers)
                            {
                                if (user.getName().equals(username) && user.getAddress().equals(clientAddress))
                                {
                                    UndoRequest request = new UndoRequest(requestID, page, pathID);
                                    user.addRequest(request);

                                    System.out.println("UndoRequest request placed");
                                    
                                    //to be broadcasted:
                                    //For now we are sending the request exactly as it came to us
                                    //TOCONFIRM Maybe we should have a new request numbering
                                    broadcastRequest(user, toBeAnalyzed);

                                    break;
                                }
                            }

                        }
                        catch (NumberFormatException x)
                        {
                        }
                    }
                    break;

                case 10:
                    //redo last action
                    //Redo - username - requestID - page - pathID
                    if (info.length > 4)
                    {
                        try
                        {
                            String username = info[1];
                            int requestID = Integer.parseInt(info[2]);
                            int page = Integer.parseInt(info[3]);
                            int pathID = Integer.parseInt(info[4]);

                            for (User user : mUsers)
                            {
                                if (user.getName().equals(username) && user.getAddress().equals(clientAddress))
                                {
                                    RedoRequest request = new RedoRequest(requestID, page, pathID);
                                    user.addRequest(request);

                                    System.out.println("RedoRequest request placed");
                                    
                                    //to be broadcasted:
                                    //For now we are sending the request exactly as it came to us
                                    //TOCONFIRM Maybe we should have a new request numbering
                                    broadcastRequest(user, toBeAnalyzed);

                                    break;
                                }
                            }

                        }
                        catch (NumberFormatException x)
                        {
                        }
                    }
                    break;

                case 11:
                    //delete path
                    //TOCONFIRM How will we implement the delete function?
                    //Delete - username - requestID - page - pathID

                    if (info.length > 4)
                    {
                        try
                        {
                            String username = info[1];
                            int requestID = Integer.parseInt(info[2]);
                            int page = Integer.parseInt(info[3]);
                            int pathID = Integer.parseInt(info[4]);

                            for (User user : mUsers)
                            {
                                if (user.getName().equals(username) && user.getAddress().equals(clientAddress))
                                {
                                    DeletePathRequest request = new DeletePathRequest(requestID, page, pathID);
                                    user.addRequest(request);

                                    System.out.println("DeletePathRequest request placed");
                                    
                                    //to be broadcasted:
                                    //For now we are sending the request exactly as it came to us
                                    //TOCONFIRM Maybe we should have a new request numbering
                                    broadcastRequest(user, toBeAnalyzed);

                                    break;
                                }
                            }
                        }
                        catch (NumberFormatException x)
                        {
                        }
                    }
                    break;
            }
        }
    }

    /**
     * Broadcast function. A request is forwarded to all
     *
     * @param exclude To which user we should not send this message
     * @param message The message that needs to be send
     */
    private void broadcastRequest(User exclude, String message)
    {
        for (User user : mUsers)
        {
            if (!user.equals(exclude))
            {
                HELPER.send(message, user.getAddress(), user.getPort());
            }
        }

    }
}
