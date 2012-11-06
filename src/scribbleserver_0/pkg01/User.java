/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scribbleserver_0.pkg01;

import java.net.InetAddress;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Grzegorz Gut <Gregg.Gut@gmail.com>
 */
public class User
{

    private String name;
    private InetAddress address;
    private int port;
    private String password;
    private SCFile activeFile;
    private boolean ownership = false;
    private boolean loggedIn = false;
    private Path workingPath;
    private Vector<Request> mRequests = new Vector<Request>();
    private RequestAnalyser analyser;
    private int requestID;
    private Thread mAnalyser;

    /**
     *
     * @param name Username
     * @param password Password
     * @param address IP address
     * @param port Listening port
     * @param requestID The ID of the request
     * @param file User working file
     */
    User(String name, String password, InetAddress address, int port, int requestID, SCFile file)
    {
        this.name = name;
        this.address = address;
        this.password = password;
        this.port = port;
        this.requestID = requestID;
        this.activeFile = file;
    }

    /**
     * Start the login process
     *
     * @return True if login was successful, otherwise false
     */
    public boolean login()
    {
        //User for testing purposes
        System.out.println(name);
        System.out.println(password);
        System.out.println(address.getHostAddress());

        //TODO This will need to check a database, make sure the user/password combination is good and then login/fail the users
        loggedIn = true;

        //TODO For now we inly have 1 active file (the first one in the list) and adding all users to it
        activeFile.addActiveUsers(this);

        //Starting the Request analyzer for this user
        analyser = new RequestAnalyser(mRequests, this, requestID);
        mAnalyser = new Thread(analyser);
        mAnalyser.start();

        return true;
    }

    /**
     * Start the logout process
     *
     * @return True if logout was successful, otherwise false
     */
    public boolean logout()
    {
        /**
         * You can only logout if you're login (Redundant for now since if you're not logged in then you do not exist
         */
        if (loggedIn)
        {
            /**
             * Setting loggedIn to false and
             */
            loggedIn = false;
            if (activeFile != null && activeFile.getPresentOwner() != null && activeFile.getPresentOwner().equals(this))
            {
                activeFile.setPresentOwner(null);
            }

            /**
             * This interrupts the Request analyzer thread since it is no longer needed
             */
            mAnalyser.interrupt();

            //For testing
            System.out.println(name + " logged out!");

            return true;
        }
        return false;
    }

    /**
     * Get username
     *
     * @return Username
     */
    public String getName()
    {
        return name;
    }

    /**
     * Get user IP address
     *
     * @return IP address
     */
    public InetAddress getAddress()
    {
        return address;
    }

    /**
     * Get the file the user is currently working on
     *
     * @return User working file
     */
    public SCFile getActiveFile()
    {
        return activeFile;
    }

    /**
     * Is the user the present owner of the file (does he has write access)
     *
     * @return True if user has write access, otherwise false
     */
    public boolean hasOwnership()
    {
        return ownership;
    }

    /**
     * Is user logged in?
     *
     * @return True if user is logged in, otherwise false
     */
    public boolean isLoggedIn()
    {
        return loggedIn;
    }

    /**
     * Set the file the user is working on
     *
     * @param activeFile The file the user is working on
     */
    public void setActiveFile(SCFile activeFile)
    {
        this.activeFile = activeFile;
    }

    /**
     * Set the user working file ownership (write access). Only one user can have write access to a file at one time.
     *
     * @param ownership True is we want to give the user write access, otherwise false.
     */
    public void setOwnership(boolean ownership)
    {
        this.ownership = ownership;
    }

    /**
     * Get the port on which the user is listening to the server
     *
     * @return The user listening port
     */
    public int getPort()
    {
        return port;
    }

    /**
     * Add a request that has been received from the user Scribble
     *
     * @param request A request with all the required data
     */
    public void addRequest(Request request)
    {
        mRequests.add(request);
    }

    /**
     * Get user working path
     *
     * @return The path the user is working on if the path is not completed, otherwise null
     */
    public Path getWorkingPath()
    {
        return workingPath;
    }

    /**
     * Set the working path for the user
     *
     * @param workingPath
     */
    public void setWorkingPath(Path workingPath)
    {
        this.workingPath = workingPath;
    }
}