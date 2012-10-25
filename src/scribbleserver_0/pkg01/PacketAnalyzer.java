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
    private InetAddress from;
    private Vector<User> mUsers;
    private Vector<File> mFiles;

    PacketAnalyzer(String info, InetAddress ip, Vector<User> user, Vector<File> files)
    {
        toBeAnalyzed = info;
        from = ip;
        mUsers = user;
        mFiles = files;
    }

    public void run()
    {
        String[] info = toBeAnalyzed.split("-");
        if (info.length > 0)
        {
            int choice = Integer.parseInt(info[0]);

            switch (choice)
            {
                case 0:
                    //login
                    if (info.length > 2)
                    {
                        User user = new User(info[1], info[2], from);
                        if (user.login())
                        {
                            mUsers.add(user);
                        }
                        else
                        {
                            //Error!!!!! 
                            //Cannot login
                        }

                    }
                    break;

                case 1:
                    //logout
                    if (info.length > 1)
                    {
                        for (int i = 0; i < mUsers.size(); i++)
                        {
                            if (mUsers.elementAt(i).getName().equals(info[1]) && mUsers.elementAt(i).getAddress().equals(from))
                            {
                                mUsers.elementAt(i).logout();
                                mUsers.remove(i);
                                break;
                            }
                        }
                    }
                    else
                    {
                        //Error
                        //Cannot logout
                    }
                    break;

                case 2:
                    //Request Ownership
                    if (info.length > 2)
                    {
                        String username = info[1];
                        String fileName = info[2];
                        for (User user : mUsers)
                        {
                            //Find the requesting user
                            if (user.getName().equals(username) && user.getAddress().equals(from))
                            {
                                //Make user user id logged in
                                if (user.isLoggedIn())
                                {
                                    for (File file : mFiles)
                                    {
                                        if (file.getName().equals(fileName))
                                        {
                                            //of there is no ownership on the file the user can take it
                                            if (file.getPresentOwner() == null)
                                            {
                                                file.setPresentOwner(user);
                                                user.setOwnership(true);

                                                //Broadcast to all users of that file that 
                                            }
                                            else
                                            {
                                                //repy to user that the ownership is taken
                                            }

                                            break;
                                        }
                                    }
                                }
                                else
                                {
                                    //User needs to login first
                                    break;
                                }
                            }
                        }
                    }

                    break;

                case 3:
                    //Release Ownership
                    break;

                case 4:
                    //Get file list
                    break;

                case 5:
                    //Download file
                    break;

                case 6:
                    //New Path
                    break;

                case 7:
                    //Add point to path
                    break;

                case 8:
                    //End current path
                    break;

                case 9:
                    //undo last action
                    break;

                case 10:
                    //redi last action
                    break;

                case 11:
                    //delete path
                    break;
            }

        }
    }
}
