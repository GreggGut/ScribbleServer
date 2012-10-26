/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scribbleserver_0.pkg01;

import java.util.ArrayList;

/**
 *
 * @author scribble
 */
public class SCFile
{

    private String name;
    private String location;
    private ArrayList<Page> mPages = new ArrayList<Page>();
    private ArrayList<User> mActiveUsers = new ArrayList<User>();
    private User presentOwner=null;

    SCFile(String n, String l)
    {
        name=n;
        location=l;
    }

    public String getName()
    {
        return name;
    }

    public String getLocation()
    {
        return location;
    }

    public ArrayList<Page> getmPages()
    {
        return mPages;
    }

    public ArrayList<User> getmActiveUsers()
    {
        return mActiveUsers;
    }

    public User getPresentOwner()
    {
        return presentOwner;
    }

    public void setmActiveUsers(ArrayList<User> mActiveUsers)
    {
        this.mActiveUsers = mActiveUsers;
    }

    public void setPresentOwner(User presentOwner)
    {
        this.presentOwner = presentOwner;
    }
    
}