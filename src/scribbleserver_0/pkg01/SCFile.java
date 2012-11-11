/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scribbleserver_0.pkg01;

import java.util.ArrayList;

/**
 *
 * @author Grzegorz Gut <Gregg.Gut@gmail.com>
 */
public class SCFile
{

    private String name;
    private String location;
    private ArrayList<Page> mPages = new ArrayList<Page>();
    private ArrayList<User> mActiveUsers = new ArrayList<User>();
    private User presentOwner = null;

    /**
     * Default constructor
     *
     * @param name File name
     * @param location File location
     * @param nPages Number of pages the file contains
     */
    SCFile(String name, String location, int nPages)
    {
        this.name = name;
        this.location = location;

        for (int i = 0; i < nPages; i++)
        {
            Page p = new Page();
            mPages.add(p);
        }
    }

    /**
     * Get the file name
     *
     * @return The file name
     */
    public String getName()
    {
        return name;
    }

    /**
     * Get the file location on the server
     *
     * @return Path to the file on the server
     */
    public String getLocation()
    {
        return location;
    }

    /**
     * Get all the pages on this file
     *
     * @return An ArrayList of all the pages of this document
     */
    public ArrayList<Page> getmPages()
    {
        return mPages;
    }

    /**
     * Get all the user who are active on this document
     *
     * @return An ArrayList of all the users that are active on this document
     */
    public ArrayList<User> getmActiveUsers()
    {
        return mActiveUsers;
    }

    /**
     * Get the user who has the ownership of the file (write access)
     *
     * @return The user who has the ownership of the file, null if none
     */
    public User getPresentOwner()
    {
        return presentOwner;
    }

    /**
     * Add an active user - User who is working on this document
     *
     * @param mActiveUsers
     */
    public void addActiveUsers(User user)
    {
        mActiveUsers.add(user);
    }

    /**
     * Set the user who has the ownership of this file (user who will have write access to this file)
     *
     * @param presentOwner
     */
    public void setPresentOwner(User presentOwner)
    {
        this.presentOwner = presentOwner;
    }
    
    public void removeUser(User removeUser)
    {
        mActiveUsers.remove(removeUser);
    }
}