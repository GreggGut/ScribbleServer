/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scribbleserver_0.pkg01;

import java.net.InetAddress;

/**
 *
 * @author Grzegorz Gut <Gregg.Gut@gmail.com>
 */
public class User
{

    private String name;
    private InetAddress address;
    private String password;
    private SCFile activeFile;
    private boolean ownership = false;
    private boolean loggedIn = false;

    /**
     *
     * @param n
     * @param pass
     * @param add
     */
    User(String n, String pass, InetAddress add)
    {
        name = n;
        address = add;
        password = pass;
    }

    public boolean login()
    {
        System.out.println(name);
        System.out.println(password);
        System.out.println(address.getHostAddress());

        //This will need to check a database, make sure the user/password combination is good and then login/fail the users
        loggedIn = true;

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

            System.out.println(name+ " logged out!");
            
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
}
