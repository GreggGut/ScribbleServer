package scribbleserver_0.pkg01;

import java.net.InetAddress;
import java.util.Vector;

/**
 *
 * @author Grzegorz Gut <Gregg.Gut@gmail.com>
 */
public class PacketAnalyzer implements Runnable
{

    private String toBeAnalyzed;
    private InetAddress clientAddress;
    private Vector<User> mUsers;
    private Vector<SCFile> mFiles;

    /**
     * Default constructor
     *
     * @param info Data that has been received by the Receiver thread
     * @param ip The IP of the sender
     * @param users A list of all the user logged in to the server
     * @param files A list of all the files available on the server
     */
    PacketAnalyzer(String info, InetAddress ip, Vector<User> users, Vector<SCFile> files)
    {
        this.toBeAnalyzed = info;
        this.clientAddress = ip;
        this.mUsers = users;
        this.mFiles = files;
    }

    @Override
    public void run()
    {
        //TESTING
        System.out.println("Received in Packet Analyzer: " + toBeAnalyzed);
        /**
         * separating received data into readable information
         */
        String[] info = toBeAnalyzed.split(HELPER.split);

        /**
         * Making sure that the received information is at least of length 1
         */
        if (info.length > 0)
        {
            int choice;
            try
            {
                choice = Integer.parseInt(info[0]);
            }
            catch (NumberFormatException e)
            {
                /**
                 * Cannot identify what was send - technically we should never get here
                 */
                System.out.append("Failed to identify the received message");
                return;
            }


            switch (choice)
            {
                /**
                 * Login
                 * Info will contain the following
                 * login - username - requestID - password - port
                 */
                case ClientToServer.LOGIN:
                    if (info.length > 4)
                    {
                        try
                        {
                            /**
                             * Parsing all the required integers
                             */
                            int requestID = Integer.parseInt(info[2]);
                            int port = Integer.parseInt(info[4]);

                            /**
                             * Creating a new user
                             * TODO For now the user gets the first file available, but that should not be the case - User should choose
                             */
                            User user = new User(info[1], info[3], clientAddress, port, requestID, mFiles.get(0));

                            /**
                             * Allowing unique usernames to be logged in simultaneously
                             */
                            boolean userExists = false;
                            for (User findUser : mUsers)
                            {
                                if (findUser.getName().contains(user.getName()))
                                {
                                    /**
                                     * User already exists, cannot login
                                     */
                                    userExists = true;

                                    break;
                                }
                            }

                            /**
                             * If user does not exist then try to login, and if successful then add it to the user list
                             */
                            if (!userExists)
                            {
                                /**
                                 * try to login
                                 * If login successful send a LoginFine message to the requesting user, otherwise send a LoginFailed
                                 */
                                if (user.login())
                                {
                                    mUsers.add(user);

                                    String toSend = "";
                                    toSend += ServerToClient.LOG_IN_SUCCESSFUL;
                                    toSend += HELPER.split;

                                    HELPER.send(toSend, user.getAddress(), user.getPort());
                                }
                                else
                                {
                                    String toSend = "";
                                    toSend += ServerToClient.LOG_IN_FAILED_WRONG_PASSWORD;
                                    toSend += HELPER.split;

                                    HELPER.send(toSend, user.getAddress(), user.getPort());
                                }
                            }
                            /**
                             * If user already exists then send a LoginFailedUserExists message to the requester
                             * TOCONF We might want to check if the user who is logged in responds to our request, if not then we might be
                             * able to login this user...
                             */
                            else
                            {
                                String toSend = "";
                                toSend += ServerToClient.LOG_IN_FAIL_USER_ALREADY_LOGGED_IN;
                                toSend += HELPER.split;

                                HELPER.send(toSend, user.getAddress(), user.getPort());
                            }
                        }
                        catch (NumberFormatException e)
                        {
                            /**
                             * if the parsing fails to be translated into a number we cannot do anything and therefore we drop this request
                             */
                        }
                    }
                    break;

                /**
                 * Logout
                 * logout - username - requestID
                 */
                case ClientToServer.LOGOUT:
                    if (info.length > 2)
                    {
                        try
                        {
                            /**
                             * Parsing request ID
                             */
                            int requestID = Integer.parseInt(info[2]);
                            /**
                             * Find the requesting user, create the request and loop until the request had been completed
                             * Once the request has been completed we remove the user from the active/logged in users on the server
                             */
                            for (int i = 0; i < mUsers.size(); i++)
                            {
                                if (mUsers.elementAt(i).getName().equals(info[1]) && mUsers.elementAt(i).getAddress().equals(clientAddress))
                                {
                                    LogoutRequest request = new LogoutRequest(requestID);
                                    mUsers.elementAt(i).addRequest(request);

                                    /**
                                     * waiting until the request has been completed
                                     */
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
                                    /**
                                     * Once the request has been completed we can remove the user from the list
                                     */
                                    mUsers.remove(i);
                                    System.out.println("User logout and removed\nUsers logged in:");
                                    
                                    for(User user:mUsers)
                                    {
                                        System.out.println("User: "+user.getName()+" "+user.getAddress());
                                    }
                                    

                                    break;
                                }
                            }
                        }
                        catch (NumberFormatException x)
                        {
                            /**
                             * Parsing failed, this request will not be processed
                             */
                        }
                    }
                    else
                    {
                        /**
                         * Error -Cannot logout not enough info
                         */
                    }
                    break;

                /**
                 * Request Ownership
                 * requestOwnership - username - ID
                 */
                case ClientToServer.REQUEST_OWNERSHIP:
                    if (info.length > 2)
                    {
                        try
                        {
                            /**
                             * Parsing all the received info
                             */
                            String username = info[1];
                            int requestID = Integer.parseInt(info[2]);

                            /**
                             * Finding the requesting user and creating the Ownership request
                             */
                            for (User user : mUsers)
                            {
                                /**
                                 * Find the requesting user
                                 */
                                if (user.getName().equals(username) && user.getAddress().equals(clientAddress))
                                {

                                    OwnershipRequest request = new OwnershipRequest(requestID);
                                    user.addRequest(request);

                                    //TESTING
                                    System.out.println("OwnershipRequest request placed");
                                    break;
                                }
                            }
                        }
                        catch (Exception x)
                        {
                            /**
                             * Failed parsing information, this request will be ignored
                             */
                        }
                    }

                    break;

                /**
                 * Release Ownership
                 * releaseOwnership - username - requestID
                 */
                case ClientToServer.RELEASE_OWNERSHIP:
                    if (info.length > 2)
                    {
                        try
                        {
                            /**
                             * Paring all the received info
                             */
                            String username = info[1];
                            int requestID = Integer.parseInt(info[2]);

                            /**
                             * Finding the requesting user and creating the Release ownership request
                             */
                            for (User user : mUsers)
                            {
                                /**
                                 * Find the requesting user
                                 */
                                if (user.getName().contains(username) && user.getAddress().equals(clientAddress))
                                {
                                    ReleaseOwnershipRequest request = new ReleaseOwnershipRequest(requestID);
                                    user.addRequest(request);

                                    //TESTING
                                    System.out.println("ReleaseOwnershipRequest request placed");

                                    break;
                                }
                            }
                        }
                        catch (NumberFormatException x)
                        {
                            /**
                             * Failed parsing, this request will be ignored
                             */
                        }
                    }

                    break;

                /**
                 * Get file list
                 * getFileList - username - requestID
                 */
                case ClientToServer.GET_FILE_LIST:
                    if (info.length > 2)
                    {
                        try
                        {
                            /**
                             * Parsing all the received info
                             */
                            String username = info[1];
                            int requestID = Integer.parseInt(info[2]);

                            /**
                             * Find the requesting user and starting the get files list request
                             */
                            for (User user : mUsers)
                            {
                                if (user.getName().equals(username) && user.getAddress().equals(clientAddress))
                                {
                                    /**
                                     * We find the user who placed the request
                                     */
                                    GetFileListRequest request = new GetFileListRequest(requestID, mFiles);
                                    user.addRequest(request);

                                    //TESTING
                                    System.out.println("GetFileListRequest request placed");

                                    break;
                                }
                            }
                        }
                        catch (NumberFormatException x)
                        {
                            /**
                             * Failed parsing, this request will be ignored
                             */
                        }
                    }

                    break;

                /**
                 * Download file
                 * TOCONF what will we do about this?
                 */
                case ClientToServer.DOWNLOAD_FILE:
                    break;

                /**
                 * New Path
                 * newPath - username - requestID - pathID - mode - color - active - page
                 */
                case ClientToServer.NEW_PATH:
                    if (info.length > 7)
                    {
                        try
                        {
                            /**
                             * Parsing all the received info
                             */
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

                            /**
                             * Find the requesting user, starting the new path request, and broadcasting this request to all active users of
                             * this file
                             */
                            for (User user : mUsers)
                            {
                                if (user.getName().equals(username) && user.getAddress().equals(clientAddress))
                                {
                                    NewPathRequest request = new NewPathRequest(requestID, pathID, mode, color, active, page);
                                    user.addRequest(request);

                                    //TESTING
                                    System.out.println("NewPathRequest request placed");

                                    /**
                                     * Broadcasting all the requests
                                     * For now we are sending the request exactly as it came to us
                                     * TOCONF Maybe we should have a new request numbering to match what the receiver expects...
                                     */
                                    broadcastRequest(user, toBeAnalyzed);
                                    break;
                                }
                            }
                        }
                        catch (NumberFormatException x)
                        {
                            /**
                             * Parsing failed - This request will be ignored
                             */
                        }
                    }

                    break;

                /**
                 * Add point to path
                 * AddPoints - username - requestID - pathID - numberOfPoints - Points
                 *
                 * TOCONF Do we need the pathID???
                 */
                case ClientToServer.ADD_POINTS:
                    if (info.length > 5)
                    {
                        try
                        {
                            /**
                             * Parsing all the info received
                             */
                            String username = info[1];
                            int requestID = Integer.parseInt(info[2]);
                            int pathID = Integer.parseInt(info[3]);
                            int numberOfPoints = Integer.parseInt(info[4]);
                            String points = info[5];

                            /**
                             * Finding the requesting user, starting the Add points request and broadcasting this request to all active
                             * users on this file
                             */
                            for (User user : mUsers)
                            {
                                if (user.getName().equals(username) && user.getAddress().equals(clientAddress))
                                {
                                    AddPointsRequest request = new AddPointsRequest(requestID, pathID, numberOfPoints, points);
                                    user.addRequest(request);

                                    //TESTING
                                    System.out.println("AddPointsRequest request placed");

                                    /**
                                     * Broadcasting all the requests
                                     * For now we are sending the request exactly as it came to us
                                     * TOCONF Maybe we should have a new request numbering to match what the receiver expects...
                                     */
                                    broadcastRequest(user, toBeAnalyzed);
                                    break;
                                }
                            }
                        }
                        catch (NumberFormatException x)
                        {
                            /**
                             * Failed parsing, this request will be ignored
                             */
                        }
                    }

                    break;

                /**
                 * End current path
                 * EndPath - username - requestID - pathID
                 */
                case ClientToServer.END_PATH:
                    if (info.length > 3)
                    {
                        try
                        {
                            /**
                             * Parsing all the received info
                             */
                            String username = info[1];
                            int requestID = Integer.parseInt(info[2]);
                            int pathID = Integer.parseInt(info[3]);

                            /**
                             * Finding the requesting user, creating the end path request and broadcasting the request to all the active
                             * users of this file
                             */
                            for (User user : mUsers)
                            {
                                if (user.getName().equals(username) && user.getAddress().equals(clientAddress))
                                {
                                    EndPathRequest request = new EndPathRequest(requestID, pathID);
                                    user.addRequest(request);

                                    //TESTING
                                    System.out.println("EndPathRequest request placed");

                                    /**
                                     * Broadcasting all the requests
                                     * For now we are sending the request exactly as it came to us
                                     * TOCONF Maybe we should have a new request numbering to match what the receiver expects...
                                     */
                                    broadcastRequest(user, toBeAnalyzed);
                                    break;
                                }
                            }
                        }
                        catch (NumberFormatException x)
                        {
                            /**
                             * Failed parsing, this request will be ignored
                             */
                        }
                    }
                    break;

                /**
                 * undo last action
                 * Undo - username - requestID - page - pathID
                 */
                case ClientToServer.UNDO:
                    if (info.length > 4)
                    {
                        try
                        {
                            /**
                             * Parsing all the received info
                             */
                            String username = info[1];
                            int requestID = Integer.parseInt(info[2]);
                            int page = Integer.parseInt(info[3]);
                            int pathID = Integer.parseInt(info[4]);

                            /**
                             * Find the requesting user, starting the Undo request, and broadcasting it to all active users of this file
                             */
                            for (User user : mUsers)
                            {
                                if (user.getName().equals(username) && user.getAddress().equals(clientAddress))
                                {
                                    UndoRequest request = new UndoRequest(requestID, page, pathID);
                                    user.addRequest(request);

                                    //TESTING
                                    System.out.println("UndoRequest request placed");

                                    /**
                                     * Broadcasting all the requests
                                     * For now we are sending the request exactly as it came to us
                                     * TOCONF Maybe we should have a new request numbering to match what the receiver expects...
                                     */
                                    broadcastRequest(user, toBeAnalyzed);

                                    break;
                                }
                            }
                        }
                        catch (NumberFormatException x)
                        {
                            /**
                             * Failed parsing, this request will be ignored
                             */
                        }
                    }
                    break;

                /**
                 * redo last action
                 * Redo - username - requestID - page - pathID
                 */
                case ClientToServer.REDO:
                    if (info.length > 4)
                    {
                        try
                        {
                            /**
                             * Parsing all the received info
                             */
                            String username = info[1];
                            int requestID = Integer.parseInt(info[2]);
                            int page = Integer.parseInt(info[3]);
                            int pathID = Integer.parseInt(info[4]);

                            /**
                             * Find the requesting user, starting the Redo request, and broadcasting it to all active users of this file
                             */
                            for (User user : mUsers)
                            {
                                if (user.getName().equals(username) && user.getAddress().equals(clientAddress))
                                {
                                    RedoRequest request = new RedoRequest(requestID, page, pathID);
                                    user.addRequest(request);

                                    //TESTING
                                    System.out.println("RedoRequest request placed");

                                    /**
                                     * Broadcasting all the requests
                                     * For now we are sending the request exactly as it came to us
                                     * TOCONF Maybe we should have a new request numbering to match what the receiver expects...
                                     */
                                    broadcastRequest(user, toBeAnalyzed);

                                    break;
                                }
                            }
                        }
                        catch (NumberFormatException x)
                        {
                            /**
                             * Failed parsing, this request will be ignored
                             */
                        }
                    }
                    break;

                /**
                 * delete path
                 * Delete - username - requestID - page - pathID
                 *
                 * TOCONFIRM How will we implement the delete function?
                 */
                case ClientToServer.DELETE_PATH:
                    if (info.length > 4)
                    {
                        try
                        {
                            /**
                             * Parsing all the received info
                             */
                            String username = info[1];
                            int requestID = Integer.parseInt(info[2]);
                            int page = Integer.parseInt(info[3]);
                            int pathID = Integer.parseInt(info[4]);

                            /**
                             * Find the requesting user, starting the delete path request, and broadcasting it to all active users of this
                             * file
                             */
                            for (User user : mUsers)
                            {
                                if (user.getName().equals(username) && user.getAddress().equals(clientAddress))
                                {
                                    DeletePathRequest request = new DeletePathRequest(requestID, page, pathID);
                                    user.addRequest(request);

                                    //TESTING
                                    System.out.println("DeletePathRequest request placed");

                                    /**
                                     * Broadcasting all the requests
                                     * For now we are sending the request exactly as it came to us
                                     * TOCONF Maybe we should have a new request numbering to match what the receiver expects...
                                     */
                                    broadcastRequest(user, toBeAnalyzed);

                                    break;
                                }
                            }
                        }
                        catch (NumberFormatException x)
                        {
                            /**
                             * Failed parsing, this request will be ignored
                             */
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
        //TESTING exclude = null; shouldn't be.. it is only used for testing
        exclude = null;
        for (User user : mUsers)
        {

            if (!user.equals(exclude))
            {
                HELPER.send(message, user.getAddress(), user.getPort());
            }
        }

    }
}
