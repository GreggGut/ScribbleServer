package Server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author Grzegorz Gut <Gregg.Gut@gmail.com>
 */
public class ClientHandler extends Thread
{

    private Socket clientSock;    // client details
    private String cliAddr;
    private int port;
    private ScribbleClients mClients;
    private User me;

    ClientHandler(Socket socket, ScribbleClients mClients)
    {
        clientSock = socket;
        cliAddr = clientSock.getInetAddress().getHostAddress();
        port = clientSock.getPort();
        this.mClients = mClients;

        System.out.println("Client connection from (" + cliAddr + ", " + port + ")");
    }

    @Override
    public void run()
    {
        try
        {
            // Get I/O streams from the socket
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSock.getInputStream()));
            PrintWriter out = new PrintWriter(clientSock.getOutputStream(), true);  // autoflush

            me = new User(cliAddr, port, out);
            mClients.addClient(me);

            processClient(in, out);            // interact with client

            // the client has finished when execution reaches here
            mClients.delClient(cliAddr, port);       // remove client details
            clientSock.close();
            System.out.println("Client (" + cliAddr + ", " + port + ") connection closed\n");
        }
        catch (Exception e)
        {
            System.out.println(e);
            e.printStackTrace();
        }
    }

    private void processClient(BufferedReader in, PrintWriter out)
    /*
     * Stop when the input stream closes (is null) or "bye" is sent
     * Otherwise pass the input to doRequest().
     */
    {
        String line;
        boolean done = false;
        //transformPath();
        try
        {
            while ((line = in.readLine()) != null)//!done)
            {
                //line = in.readLine();
                decodeRequest(line);
            }
        }
        catch (IOException e)
        {
            System.out.println(e);
        }
    }

    /**
     * This function analyzes what has been received and acts upon it
     *
     * @param line
     */
    private void decodeRequest(String line)//, PrintWriter out)
    /*
     * The input line (client message) can be :
     * who	-- a list of users is returned
     * or	any text	-- which is broadcast with
     * (cliAddr,port) at its front
     */
    {
        //Remove all spaces
        String decodeLine = line.trim();

        //System.out.println("Received in Packet Analyzer: " + line);
        /**
         * separating received data into readable information
         */
        String[] info = decodeLine.split(NetworkProtocol.split);

        /**
         * Making sure that the received information is at least of length 1
         */
        if (info.length > 0)
        {
            int choice;
            try
            {
                choice = Integer.parseInt(info[1]);
            }
            catch (NumberFormatException e)
            {
                /**
                 * Cannot identify what was send - technically we should never
                 * get here
                 */
                System.out.println("Failed to identify the received message");
                return;
            }

            switch (choice)
            {
                /**
                 * Login Info will contain the following login - password - port
                 */
                case NetworkProtocol.LOGIN:
                    login(info);
                    break;

                /**
                 * Logout logout
                 */
                case NetworkProtocol.LOGOUT:
                    logout(info);
                    break;

                /**
                 * Request Ownership requestOwnership - ID
                 */
                case NetworkProtocol.REQUEST_OWNERSHIP:
                    requestOwnership(line);

                    break;

                /**
                 * Release Ownership releaseOwnership
                 */
                case NetworkProtocol.RELEASE_OWNERSHIP:
                    releaseOwnership(line);

                    break;

                /**
                 * Get file list getFileList
                 */
                case NetworkProtocol.GET_FILE_LIST:
                    getFileList();

                    break;

                /**
                 * Download file TOCONF what will we do about this?
                 */
                case NetworkProtocol.DOWNLOAD_FILE:
                    downloadFile(info);
                    break;

                /**
                 * New Path newPath - pathID - mode - color - active - page
                 */
                case NetworkProtocol.NEW_PATH:
                    newPath(info, line);

                    break;

                /**
                 * Add point to path AddPoints - numberOfPoints - Points
                 */
                case NetworkProtocol.ADD_POINTS:
                    addPoint(info, line);

                    break;

                /**
                 * End current path EndPath - pathID
                 */
                case NetworkProtocol.END_PATH:
                    endPath(line);
                    break;

                /**
                 * undo last action Undo - page - pathID
                 */
                case NetworkProtocol.UNDO:
                    undo(info, line);
                    break;

                /**
                 * redo last action Redo - page - pathID
                 */
                case NetworkProtocol.REDO:
                    //System.out.println("Redo, should not be here since redo will simply resend the whole path");
                    redo(info, line);
                    break;

                /**
                 * delete path Delete - page - pathID
                 *
                 * TOCONFIRM How will we implement the delete function?
                 */
                case NetworkProtocol.DELETE_PATH:
                    deletePath(info, line);

                    break;

                case NetworkProtocol.CLEAR_ALL:
                    clearAll(info, line);

                    break;

                case NetworkProtocol.UPDATE_FILE:
                    updateUserwithFileContent();
                    break;
                default:
                    System.out.println("Should NOT be here....");
                    break;
            }
        }
    }

    /**
     *
     * @param info login- username - password
     */
    private void login(String[] info)
    {
        System.out.println("Login, Not implemented");
        //TODO implement the login function
    }

    /**
     *
     * @param info logout
     */
    private void logout(String[] info)
    {
        System.out.println("Logout, Not implemented");
        //TODO implement the logout function
    }

    /**
     *
     * @param info requestOwnership - username
     */
    private void requestOwnership(String line)
    {
        System.out.println("Request_Ownership");

        if (me.getmFile().getPresentOwner() == null)
        {
            me.getmFile().setPresentOwner(me);
            mClients.broadcast(line, me, true);
        }
    }

    /**
     *
     * @param info releaseOwnership
     */
    private void releaseOwnership(String line)
    {
        System.out.println("Release Ownership");

        if (me.getmFile().getPresentOwner().equals(me))
        {
            me.getmFile().setPresentOwner(null);
            mClients.broadcast(line, me, true);
        }
    }

    /**
     *
     * @param info getFileList
     */
    private void getFileList()
    {
        System.out.println("get file list");

        String toSend = mClients.getListOfFiles();
        toSend = encriptMessage(toSend);

        me.sendMessage(toSend);
    }

    private void downloadFile(String[] info)
    {
        System.out.println("Download file"+ info);
        me.sendFile();
    }

    /**
     *
     * @param info newPath - pathID - mode - color - active - page
     */
    private void newPath(String[] info, String line)
    {
        //System.out.println("New path");

        try
        {
            /**
             * Parsing all the received info
             */
            int pathID = Integer.parseInt(info[2]);
            boolean mode;
            if (info[3].equals("1"))
            {
                mode = true;
            }
            else
            {
                mode = false;
            }
            int color = Integer.parseInt(info[4]);
//            boolean active;
//            if (info[5].equals("1"))
//            {
//                active = true;
//            }
//            else
//            {
//                active = false;
//            }
            int page = Integer.parseInt(info[5]);
            int width = Integer.parseInt(info[6]);

            Path path = new Path(pathID, mode, color/*
                     * , active
                     */, width, page);

            me.getmFile().getPages().get(page).addPath(path);
            me.setWorkingPath(path);

            //If everything got parsed then we can forward it to all clients
            mClients.broadcast(line, me, false);

        }
        catch (NumberFormatException x)
        {
            /**
             * Parsing failed - This request will be ignored
             */
        }
    }

    /**
     *
     * @param info AddPoints - Points
     */
    private void addPoint(String[] info, String line)
    {
        //System.out.println("add points");

        /**
         * Parsing all the info received
         */
        String points = info[2];

        String[] allPoints = points.split(NetworkProtocol.splitPoints);

        for (int i = 0; i < allPoints.length;)
        {
            int x = Integer.parseInt(allPoints[i++]);
            int y = Integer.parseInt(allPoints[i++]);

            Point point = new Point(x, y);
            me.getWorkingPath().AddPoint(point);
            //System.out.println("Added point in addPOint");
        }

        mClients.broadcast(line, me, false);
    }

    /**
     *
     * @param info EndPath
     */
    private void endPath(String line)
    {
        me.setWorkingPath(null);

        mClients.broadcast(line, me, false);
    }

    /**
     *
     * @param info Undo - page
     */
    private void undo(String[] info, String line)
    {
        System.out.println("undo");

        /**
         * TOCONF how will me implement this??? For now I will completely delete
         * this from the paths and on redo we will resend the whole path, all of
         * it points, and end of path
         */
        try
        {
            int page = Integer.parseInt(info[2]);

            //me.getmFile().getPages().get(page).removeLastPath();
            Path newPath = new Path(Path.UNDO, page);
            me.getmFile().getPages().get(page).addPath(newPath);

            mClients.broadcast(line, me, false);

        }
        catch (NumberFormatException x)
        {
            /**
             * Failed parsing, this request will be ignored
             */
        }
    }

    /**
     *
     * @param info Redo - page
     */
    private void redo(String[] info, String line)
    {
        System.out.println("redo");
        if (info.length > 4)
        {
            try
            {
                /**
                 * Parsing all the received info
                 */
                int page = Integer.parseInt(info[2]);

                //Remove the last item, the undo action from the list
                me.getmFile().getPages().get(page).getPaths().remove(me.getmFile().getPages().get(page).getPaths().size() - 1);

                mClients.broadcast(line, me, false);
            }
            catch (NumberFormatException x)
            {
                /**
                 * Failed parsing, this request will be ignored
                 */
            }
        }
    }

    /**
     *
     * @param info Delete - page - pathID
     */
    private void deletePath(String[] info, String line)
    {
        System.out.println("Delete path");

        try
        {
            /**
             * Parsing all the received info
             */
            int page = Integer.parseInt(info[2]);
            int pathID = Integer.parseInt(info[3]);

            me.getmFile().getPages().get(page).deletePath(pathID);

            mClients.broadcast(line, me, false);
        }
        catch (NumberFormatException x)
        {
            /**
             * Failed parsing, this request will be ignored
             */
        }
    }

    private void clearAll(String[] info, String line)
    {
        System.out.println("Clean all");

        try
        {
            /**
             * Parsing all the received info
             */
            int page = Integer.parseInt(info[2]);

            me.getmFile().getPages().get(page).clearPage();

            mClients.broadcast(line, me, false);
        }
        catch (NumberFormatException x)
        {
            /**
             * Failed parsing, this request will be ignored
             */
        }
    }

    private String encriptMessage(String toSend)
    {
        String header = String.format("%4d", toSend.length() + 1);
        toSend = header + toSend;
        return toSend;
    }

    private void updateUserwithFileContent()
    {
        SCFile file = me.getmFile();
        System.out.println("Updating file");
        for (Page page : file.getPages())
        {
            for (Path path : page.getPaths())
            {
                if (path.getType() == Path.PATH)
                {
                    transformPath(path);
                }
                else if (path.getType() == Path.UNDO)
                {
                    String toSend = NetworkProtocol.split;
                    toSend += NetworkProtocol.UNDO;
                    toSend = encriptMessage(toSend);
                    me.sendMessage(toSend);
                }
                else if (path.getType() == Path.REDO)
                {
                    String toSend = NetworkProtocol.split;
                    toSend += NetworkProtocol.REDO;
                    toSend = encriptMessage(toSend);
                    me.sendMessage(toSend);
                }
            }
        }
        System.out.println("Updating file ended");
    }

    private void transformPath(Path path)
    {
        //Path path = new Path(10, true, 32145, false, 3, 0);
        String mPath = NetworkProtocol.split;
        mPath += String.valueOf(NetworkProtocol.NEW_PATH);
        mPath += NetworkProtocol.split;

        mPath += path.getId();
        mPath += NetworkProtocol.split;

        int mode = 1;
        if (!path.getMode())
        {
            mode = 0;
        }
        mPath += mode;
        mPath += NetworkProtocol.split;

        mPath += path.getColor();
        mPath += NetworkProtocol.split;

//        int active = 1;
//        if (!path.isActive())
//        {
//            active = 0;
//        }
//        mPath += active;
//        mPath += NetworkProtocol.split;

        mPath += path.getPage();
        mPath += NetworkProtocol.split;

        mPath += path.getWidth();
        mPath += NetworkProtocol.split;

        System.out.println(mPath);

        mPath = encriptMessage(mPath);
        me.sendMessage(mPath);

        ArrayList<Point> points = path.getmPoints();
        String start = NetworkProtocol.split;
        start += String.valueOf(NetworkProtocol.ADD_POINTS);
        start += NetworkProtocol.split;
        String mPoints = start;
        for (Point point : points)
        {
            if (mPoints.length() < 220)
            {
                mPoints += point.x();
                mPoints += NetworkProtocol.splitPoints;
                mPoints += point.y();
                mPoints += NetworkProtocol.splitPoints;
            }
            else
            {
                mPoints = mPoints.substring(0, mPoints.length() - 1);
                mPoints = encriptMessage(mPoints);
                me.sendMessage(mPoints);

                mPoints = start;
            }
        }
        if (!mPoints.equals(start))
        {
            mPoints = mPoints.substring(0, mPoints.length() - 1);
            mPoints = encriptMessage(mPoints);
            me.sendMessage(mPoints);
        }

        String endpath = NetworkProtocol.split;
        endpath += NetworkProtocol.END_PATH;
        endpath = encriptMessage(endpath);
        me.sendMessage(endpath);

        //return mPath;
    }
}
