/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Vector;
import java.util.concurrent.Semaphore;
import org.apache.pdfbox.pdmodel.PDDocument;

/**
 *
 * @author Grzegorz Gut <Gregg.Gut@gmail.com>
 */
public class ScribbleClients
{

    private ArrayList<User> mClient = new ArrayList<User>();
    private Vector<SCFile> mFiles = new Vector<SCFile>();
    //TODO use semaphores for all shared objects
    private final Semaphore clientsSemaphore = new Semaphore(1);

    ScribbleClients()
    {
        getAllFiles();
    }

    synchronized public void addClient(User user)
    {
        try
        {
            clientsSemaphore.acquire();
            mClient.add(user);
            clientsSemaphore.release();
        }
        catch (InterruptedException e)
        {
            System.out.println("InterruptedException in ScribbleClients addClient()");
        }

    }

    synchronized public boolean isUserLoggedin(String username)
    {
        System.out.println("In isUserLoggedin()");
        try
        {
            clientsSemaphore.acquire();
            for (User user : mClient)
            {
                System.out.println(user.getUsername() + " " + username);
                if (user.getUsername().equals(username))
                {
                    clientsSemaphore.release();
                    return true;
                }
            }
            clientsSemaphore.release();
        }
        catch (InterruptedException e)
        {
            System.out.println("InterruptedException in ScribbleClients isUserLoggedin()");
        }
        return false;
    }

    //TOCONF this didn't work on the school computer for some reason , need to be fixed/investigated!
    synchronized public void delClient(String cliAddr, int port)
    {

        try
        {
            clientsSemaphore.acquire();
            Iterator<User> clientsIterator = mClient.iterator();
            while (clientsIterator.hasNext())
            {
                User u = clientsIterator.next();
                if (u.matches(cliAddr, port))
                {
                    if (u.getmFile() != null)
                    {
                        if (u.getmFile().getPresentOwner() != null)
                        {
                            if (u.getmFile().getPresentOwner().equals(u))
                            {
                                u.getmFile().setPresentOwner(null);
                                String toSend = NetworkProtocol.split;
                                toSend += NetworkProtocol.RELEASE_OWNERSHIP;
                                toSend = encriptMessage(toSend);
                                broadcast(toSend, u, true);
                            }
                        }
                        u.getmFile().removeUser(u);
                    }
                    clientsIterator.remove();
                    System.out.println("User " + cliAddr + " " + port + " logout");
                    break;
                }
            }
            clientsSemaphore.release();
        }
        catch (InterruptedException e)
        {
            System.out.println("InterruptedException in ScribbleClients delClient()");
        }
    }

    private String encriptMessage(String toSend)
    {
        String header = String.format("%4d", toSend.length() + 1);
        toSend = header + toSend;
        return toSend;
    }

    /**
     * Send message to all clients of the working file, excluding the user who
     * send it (doNotSend)
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
     * @param mFiles Vector of SCFile that will be filled with the files
     * available to all users
     */
    private void getAllFiles()
    {
        /**
         * The folder where all the files are present, as seen from this
         * application
         */
        File folder = new File(SCFile.folder);
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
                            int nPages = doc.getNumberOfPages();
                            doc.close();
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

    public void addFile(String fileName)
    {
        File file = new File(SCFile.folder + "//" + fileName);
        if (file.isFile())
        {
            try
            {
                PDDocument doc = PDDocument.load(file);
                int nPages = doc.getNumberOfPages();
                doc.close();
                SCFile newFile = new SCFile(file.getName(), file.getPath(), nPages);

                mFiles.add(newFile);
            }
            catch (IOException ex)
            {
                System.out.println("Failed opening the " + file.getName() + ", File will not be added.");
            }
        }
    }

    synchronized public Vector<SCFile> getFiles()
    {
        return mFiles;
    }

    synchronized public boolean doFileExists(String fileName)
    {
        for (SCFile file : mFiles)
        {
            if (file.getName().equals(fileName))
            {
                return true;
            }
        }
        return false;
    }

    public void printUsers()
    {
        try
        {
            clientsSemaphore.acquire();
            if (mClient.isEmpty())
            {
                System.out.println("No users are logged in");
            }
            else
            {
                for (User user : mClient)
                {
                    System.out.println(user.getUsername() + " Address: " + user.getClientAdd() + " Port: " + user.getPort());
                    if (user.getmFile() != null)
                    {
                        if (user.getmFile().getPresentOwner() != null && user.getmFile().getPresentOwner().equals(user))
                        {
                            System.out.println("\t" + user.getmFile().getName() + " current owner");
                        }
                        else
                        {
                            System.out.println("\t" + user.getmFile().getName());
                        }
                    }
                }
            }
            clientsSemaphore.release();
        }
        catch (InterruptedException x)
        {
            System.out.println("InterruptedException in Scribbleclient printUsers()");
        }
    }
//    TODO This should be removed and functions using mClients should be implemented in this class with semaphores
//    public Vector<User> getClients()
//    {
//        return mClient;
//    }
}