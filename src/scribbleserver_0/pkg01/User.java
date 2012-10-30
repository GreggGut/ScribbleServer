/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scribbleserver_0.pkg01;

import java.net.InetAddress;
import java.util.Vector;

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
    private Page workingPage;
    private SCFile workingFile;
    private int currentRequestID;
    private Vector<Request> mRequests = new Vector<Request>();
    private RequestAnalyser analyser;
    private int requestID;

    /**
     *
     * @param n
     * @param pass
     * @param add
     * @param p
     */
    User(String n, String pass, InetAddress add, int p, int requestID)
    {
        name = n;
        address = add;
        password = pass;
        port = p;
        this.requestID = requestID;
    }

    public boolean login()
    {
        System.out.println(name);
        System.out.println(password);
        System.out.println(address.getHostAddress());

        //This will need to check a database, make sure the user/password combination is good and then login/fail the users
        loggedIn = true;
        analyser = new RequestAnalyser(mRequests, this, requestID);

        return true;
    }

    public boolean logout()
    {
        if (loggedIn)
        {
            loggedIn = false;
            if (activeFile != null && activeFile.getPresentOwner() != null && activeFile.getPresentOwner().equals(this))
            {
                activeFile.setPresentOwner(null);
            }
            //ownership=false;

            System.out.println(name + " logged out!");

            return true;
        }
        return false;
    }

    public String getName()
    {
        return name;
    }

    public InetAddress getAddress()
    {
        return address;
    }

    public SCFile getActiveFile()
    {
        return activeFile;
    }

    public boolean hasOwnership()
    {
        return ownership;
    }

    public boolean isLoggedIn()
    {
        return loggedIn;
    }

    public void setActiveFile(SCFile activeFile)
    {
        this.activeFile = activeFile;
    }

    public void setOwnership(boolean ownership)
    {
        this.ownership = ownership;
    }

    public int getPort()
    {
        return port;
    }

    public void setWorkingPage(Page workingPage)
    {
        this.workingPage = workingPage;
    }

    public Page getWorkingPage()
    {
        return workingPage;
    }

    public void setWorkingFile(SCFile workingFile)
    {
        this.workingFile = workingFile;
    }

    public SCFile getWorkingFile()
    {
        return workingFile;
    }

    public void addRequest(Request r)
    {
        mRequests.add(r);
    }
}
