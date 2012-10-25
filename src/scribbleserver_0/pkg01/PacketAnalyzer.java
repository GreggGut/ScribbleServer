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

    PacketAnalyzer(String info, InetAddress ip, Vector<User> user)
    {
        toBeAnalyzed = info;
        from = ip;
        mUsers=user;
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
                        if(user.login())
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
                    break;

                case 2:
                    //Request Ownership
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
