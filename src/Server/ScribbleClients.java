/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import org.apache.pdfbox.pdmodel.PDDocument;

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
        getAllFiles();
    }

    synchronized public void addClient(User user)
    {
        mClient.add(user);
    }

    synchronized public boolean isUserLoggedin(String username)
    {
        System.out.println("In isUserLoggedin()");
        for (User user : mClient)
        {
            System.out.println(user.getUsername() + " " + username);
            if (user.getUsername().equals(username))
            {
                return true;
            }
        }
        return false;
    }

    synchronized public void delClient(String cliAddr, int port)
    {
        User c;
        for (int i = 0; i < mClient.size(); i++)
        {
            c = mClient.get(i);
            if (c.matches(cliAddr, port))
            {
                mClient.remove(i);
                System.out.println("User " + cliAddr + " " + port + " logout");
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
    private void getAllFiles()
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
                {
                    //TOCONF This is only for PDF documents.... will we have any other type of documents?
                    String fileName = file.getName();
                    String extension = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
                    if (extension.equals("pdf"))
                    {
                        try
                        {
                            PDDocument doc = PDDocument.load(file);
                            int count = doc.getNumberOfPages();
                            doc.close();
                            int nPages = count;
                            SCFile newFile = new SCFile(file.getName(), file.getPath(), nPages);

                            mFiles.add(newFile);
                            System.out.println(file.getName() + " " + file.getPath() + " number of pages: " + nPages);
                        }
                        catch (IOException ex)
                        {
                            System.out.println("Failed opening the " + file.getName() + " PDF, this file will be ignored");
                        }
                    }
                }
            }
        }
    }

    synchronized public ArrayList<SCFile> getFiles()
    {
        return mFiles;
    }
}