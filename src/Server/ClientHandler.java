package Server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

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
    private boolean logoutRequest = false;
    private boolean loggedIn = false;

    ClientHandler(Socket socket, ScribbleClients mClients)
    {
        clientSock = socket;
        cliAddr = clientSock.getInetAddress().getHostAddress();
        port = clientSock.getPort();
        this.mClients = mClients;

        System.out.println("Client connection from (" + cliAddr + ", " + port + ")");
        //createNewDocument(null);
    }

    @Override
    public void run()
    {
        try
        {
            // Get I/O streams from the socket
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSock.getInputStream()));
            PrintWriter out = new PrintWriter(clientSock.getOutputStream(), true);  // autoflush

            me = new User(cliAddr, port, out, clientSock);

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
        try
        {
            while (!logoutRequest && (line = in.readLine()) != null)
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
    private void decodeRequest(String line)
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
                 * Cannot identify what was send - theoretically we should never
                 * get here
                 */
                System.out.println("Failed to identify the received message");
                return;
            }
            //If not logged in, the user can only login, no other actions are acceptable
            if (!loggedIn && choice != NetworkProtocol.LOGIN)
            {
                return;
            }

            try
            {
                switch (choice)
                {
                    /**
                     * Login Info will contain the following login - password -
                     * port
                     */
                    case NetworkProtocol.LOGIN:
                        login(info);
                        break;

                    /**
                     * Logout logout
                     */
                    case NetworkProtocol.LOGOUT:
                        logout(line);
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
                     * Download file This does not do the actual download, just
                     * sets the user working file
                     */
                    case NetworkProtocol.DOWNLOAD_FILE:
                        setDownloadedFile(info);
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
                     * TOCONFIRM How will we implement the delete function? For
                     * now we just use white color as eraser so this function is
                     * not needed, Keeping it here for future development
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
                    case NetworkProtocol.CREATE_NEW_DOCUMENT:
                        createNewDocument(info);
                        break;
                    default:
                        System.out.println("Should NOT be here....");
                        break;
                }
            }
            catch (NullPointerException x)
            {
                System.out.println("NullPointerException");
                x.printStackTrace();
            }
        }
    }

    /**
     *
     * @param info login- username - password
     */
    private void login(String[] info)
    {
        try
        {
            String username = info[2].toLowerCase();
            String password = info[3];

            //TOCONF For now I am storing the username/password in a file. This would have to be implemented in a database.
            BufferedReader buffer = new BufferedReader(new FileReader("resources//credential"));
            String lineFromFile;
            while ((lineFromFile = buffer.readLine()) != null)
            {
                String cr[] = lineFromFile.split(",");
                if (cr.length == 2 && cr[0].equals(username) && cr[1].equals(password))
                {
                    //One login per username
                    if (!mClients.isUserLoggedin(username))
                    {
                        System.out.println("Login fine");
                        loggedIn = true;
                        me.setUsername(username);
                        mClients.addClient(me);
                        break;
                    }
                    else
                    {
                        //Failed to login since user already logged in
                        break;
                    }

                }
            }

            //Sending login result
            String toSend = NetworkProtocol.split;
            toSend += NetworkProtocol.LOGIN;
            toSend += NetworkProtocol.split;
            if (loggedIn)
            {
                toSend += "1";
            }
            else
            {
                toSend += "0";
            }

            toSend = encriptMessage(toSend);
            me.sendMessage(toSend);

        }
        catch (FileNotFoundException ex)
        {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex)
        {
        }
    }

    /**
     *
     * @param info logout
     */
    private void logout(String line)
    {
        System.out.println("Logout, Not implemented");
        me.sendMessage(line);
        logoutRequest = true;
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

        if (me.getmFile() != null && me.getmFile().getPresentOwner().equals(me))
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

        //Clearing the user file list
        //TOCONF Do we need to send this here?
        String clear = NetworkProtocol.split;
        clear += NetworkProtocol.GET_FILE_LIST_CLEAR;
        //clear += NetworkProtocol.split;

        clear = encriptMessage(clear);
        me.sendMessage(clear);

        //Creating general header for sending files
        String header = NetworkProtocol.split;
        header += NetworkProtocol.GET_FILE_LIST;
        header += NetworkProtocol.split;

        //Vector<SCFile> files = mClients.getFiles();

        for (SCFile file : mClients.getFiles())
        {
            String toBeSend = header;
            toBeSend += file.getName();
            toBeSend = encriptMessage(toBeSend);
            me.sendMessage(toBeSend);
        }

        String end = NetworkProtocol.split;
        end += NetworkProtocol.GET_FILE_LIST_COMPLETED;

        end = encriptMessage(end);
        me.sendMessage(end);
    }

    /**
     * This function sets the file the user downloaded as active file
     *
     * @param info
     */
    private void setDownloadedFile(String[] info)
    {
        if (!loggedIn)
        {
            return;
        }
        ArrayList<SCFile> allFiles = mClients.getFiles();

        for (SCFile f : allFiles)
        {
            if (f.getName().equals(info[2]))
            {
                me.setBusy(true);
                System.out.println("File      " + f.getName());
                me.setmFile(f);
                break;
            }
        }
    }

    /**
     *
     * @param info newPath - pathID - mode - color - active - page
     */
    private void newPath(String[] info, String line)
    {
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
            System.out.println("Color: " + color);
            int page = Integer.parseInt(info[5]);
            int width = Integer.parseInt(info[6]);

            Path path = new Path(pathID, mode, color, width, page);

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
        me.getmFile().getPages().get(me.getWorkingPath().getPage()).addPath(me.getWorkingPath());
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

        try
        {
            /**
             * Parsing all the received info
             */
            int page = Integer.parseInt(info[2]);

            Path newPath = new Path(Path.REDO, page);
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
     * @param info Delete - page - pathID
     *
     * TODO Delete path is implemented using white color for now
     */
    private void deletePath(String[] info, String line)
    {
        System.out.println("Delete path, Should never be here");

//        try
//        {
//            /**
//             * Parsing all the received info
//             */
//            int page = Integer.parseInt(info[2]);
//            int pathID = Integer.parseInt(info[3]);
//
//            me.getmFile().getPages().get(page).deletePath(pathID);
//
//            mClients.broadcast(line, me, false);
//        }
//        catch (NumberFormatException x)
//        {
//            /**
//             * Failed parsing, this request will be ignored
//             */
//        }
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

            //me.getmFile().getPages().get(page).clearPage();
            Path newPath = new Path(Path.CLEARALL, page);
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

    private String encriptMessage(String toSend)
    {
        String header = String.format("%4d", toSend.length() + 1);
        toSend = header + toSend;
        return toSend;
    }

    /**
     * This file sends all the paths on the whole document to the user
     */
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
                    toSend += NetworkProtocol.split;
                    toSend += path.getPage();
                    toSend = encriptMessage(toSend);
                    me.sendUpdateMessage(toSend);
                }
                else if (path.getType() == Path.REDO)
                {
                    String toSend = NetworkProtocol.split;
                    toSend += NetworkProtocol.REDO;
                    toSend += NetworkProtocol.split;
                    toSend += path.getPage();
                    toSend = encriptMessage(toSend);
                    me.sendUpdateMessage(toSend);
                }
                else if (path.getType() == Path.CLEARALL)
                {
                    String toSend = NetworkProtocol.split;
                    toSend += NetworkProtocol.CLEAR_ALL;
                    toSend += NetworkProtocol.split;
                    toSend += path.getPage();
                    toSend = encriptMessage(toSend);
                    me.sendUpdateMessage(toSend);
                }
            }
        }

        //sedning who is the owner
        if (me.getmFile().getPresentOwner() == null)
        {
            String toSend = NetworkProtocol.split;
            toSend += NetworkProtocol.RELEASE_OWNERSHIP;
            toSend = encriptMessage(toSend);
            me.sendUpdateMessage(toSend);
        }
        else
        {
            String toSend = NetworkProtocol.split;
            toSend += NetworkProtocol.REQUEST_OWNERSHIP;
            toSend += NetworkProtocol.split;
            toSend += me.getmFile().getPresentOwner().getUsername();
            toSend = encriptMessage(toSend);
            me.sendUpdateMessage(toSend);
        }

        me.setBusy(false);
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

        mPath += path.getPage();
        mPath += NetworkProtocol.split;

        mPath += path.getWidth();
        mPath += NetworkProtocol.split;

        System.out.println(mPath);

        mPath = encriptMessage(mPath);
        me.sendUpdateMessage(mPath);

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
                me.sendUpdateMessage(mPoints);

                mPoints = start;
            }
        }
        if (!mPoints.equals(start))
        {
            mPoints = mPoints.substring(0, mPoints.length() - 1);
            mPoints = encriptMessage(mPoints);
            me.sendUpdateMessage(mPoints);
        }

        String endpath = NetworkProtocol.split;
        endpath += NetworkProtocol.END_PATH;
        endpath = encriptMessage(endpath);
        me.sendUpdateMessage(endpath);
    }

    private void createNewDocument(String[] info)
    {
        String fileName = info[2];
        int page = Integer.parseInt(info[3]);

        String toSend = NetworkProtocol.split;
        toSend += String.valueOf(NetworkProtocol.CREATE_NEW_DOCUMENT);
        toSend += NetworkProtocol.split;

        if (mClients.doFileExists(fileName))
        {
            System.out.println("File " + fileName + " already exists and will not be recreated!");
            toSend += String.valueOf(NetworkProtocol.FILE_EXISTS);

            toSend = encriptMessage(toSend);
            me.sendMessage(toSend);
        }
        else
        {
            try
            {
                PDDocument doc = new PDDocument();
                for (int i = 0; i < page; i++)
                {
                    PDPage p = new PDPage();
                    p.setMediaBox(PDPage.PAGE_SIZE_A4);
                    doc.addPage(p);
                }
                doc.save(SCFile.folder + fileName);
                doc.close();
                mClients.addFile(fileName);

                toSend += String.valueOf(NetworkProtocol.FILE_WAS_CREATED);
                toSend += NetworkProtocol.split;
                toSend += fileName;

                String newFile = NetworkProtocol.split;
                newFile += NetworkProtocol.GET_FILE_LIST;
                newFile += NetworkProtocol.split;
                newFile += fileName;
                newFile = encriptMessage(newFile);
                mClients.broadcast(newFile, me, true);
            }
            catch (IOException x)
            {
                toSend += String.valueOf(NetworkProtocol.FILE_CREATION_FAILED);
                System.out.println("IOException while creating new PDF file");
            }
            catch (COSVisitorException x)
            {
                toSend += String.valueOf(NetworkProtocol.FILE_CREATION_FAILED);
                System.out.println("COSVisitorException while creating new PDF file");
            }
            finally
            {
                System.out.println("in finally");
                toSend = encriptMessage(toSend);
                me.sendMessage(toSend);
            }
        }
    }
}
