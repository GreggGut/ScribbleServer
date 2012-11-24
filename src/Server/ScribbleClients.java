/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Grzegorz Gut <Gregg.Gut@gmail.com>
 */
public class ScribbleClients
{

    private ArrayList<User> mClient = new ArrayList<User>();
    private ArrayList<SCFile> mFiles = new ArrayList<SCFile>();

    ScribbleClients()
    {
        getAllFiles(mFiles);
    }

    synchronized public void addClient(User user)
    {
        //TODO user will have a choice of the file to use, but for now we only work on the first file
        user.setmFile(mFiles.get(0));

        mClient.add(user);
        //broadcast("Welcome a new chatter (" + cliAddr + ", " + port + ")");
    }

    synchronized public void delClient(String cliAddr, int port)
    // remove the Chatter object for this person
    {
        User c;
        for (int i = 0; i < mClient.size(); i++)
        {
            c = mClient.get(i);
            if (c.matches(cliAddr, port))
            {
                mClient.remove(i);
                System.out.println("User " + cliAddr + " " + port + " logout");
                //broadcast("(" + cliAddr + ", " + port + ") has departed");
                break;
            }
        }
    }

    /**
     * Send message to all clients of the working file, excluding the user who send it (doNotSend)
     *
     * @param msg
     * @param doNotSend
     */
    synchronized public void broadcast(String msg, User sender, boolean echoToSender)
    {
        if (echoToSender)
        {
            for (User user : sender.getmFile().getmActiveUsers())
            {
                user.sendMessage(msg);
            }
        }
        else
        {
            for (User user : sender.getmFile().getmActiveUsers())
            {
                if (!user.equals(sender))
                {
                    user.sendMessage(msg);
                }
            }
        }
    }

    /**
     * Function that finds all the files in a directory
     *
     * @param mFiles Vector of SCFile that will be filled with the files available to all users
     */
    private void getAllFiles(ArrayList<SCFile> mFiles)
    {
        /**
         * The folder where all the files are present, as seen from this application
         */
        String path = "documents";

        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();

        /**
         * sorting the files in alphabetical order
         */
        Arrays.sort(listOfFiles);

        for (File file : listOfFiles)
        {

            if (file.isFile())
            {
                //TODO Need to find a way how many pages the document has, for now using 10
                int nPages = 10;
                SCFile newFile = new SCFile(file.getName(), file.getPath(), nPages);

                mFiles.add(newFile);
                System.out.println(file.getName() + " " + file.getPath());
            }
        }
    }

    synchronized public String getListOfFiles()
    {
        String toBeSend = "";
        toBeSend += NetworkProtocol.FILE_LIST_AVAILABLE;
        toBeSend += NetworkProtocol.split;

        //user.increaseClientExpectsRequestID();

        /**
         * Creating a text file will all the files available to the user
         */
        for (SCFile file : mFiles)
        {
            toBeSend += file.getName();
            toBeSend += NetworkProtocol.splitPoints;
        }

        toBeSend = toBeSend.substring(0, toBeSend.length() - 1);

        return toBeSend;
    }
}
