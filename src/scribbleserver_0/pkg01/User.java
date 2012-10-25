/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scribbleserver_0.pkg01;

import java.net.InetAddress;

/**
 *
 * @author scribble
 */
public class User
{

    private String name;
    private InetAddress address;
    private String password;
    private String activeFile;
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
        if(loggedIn)
        {
            loggedIn=false;
            //ownership=false;
            return true;
        }
        return false;
    }
}
