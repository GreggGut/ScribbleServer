/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scribbleserver_0.pkg01;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.Vector;

/**
 *
 * @author Grzegorz Gut <Gregg.Gut@gmail.com>
 */
public class HELPER
{

    public static int MESSAGESIZE = 300;
    public static long delay = 5 * 60 * 1000;    //5 minnute delay

    /*
     * Item ID used by the offer function
     */
    public static int ItemID = 0;
    /*
     * Used when reading/writing to the user Array
     */
//    private static final ReentrantReadWriteLock readWriteUserLock = new ReentrantReadWriteLock();
//    public static Lock readUsersList = readWriteUserLock.readLock();
//    public static Lock writeUsersList = readWriteUserLock.writeLock();
//    /*
//     * Used when reading/writing to the user Array     * 
//     */
//    private static final ReentrantReadWriteLock readWriteItemsLock = new ReentrantReadWriteLock();
//    public static Lock readItemsList = readWriteItemsLock.readLock();
//    public static Lock writeItemsList = readWriteItemsLock.writeLock();
//    /*
//     * Used when writing to the users.txt file
//     */
//    static final private ReentrantReadWriteLock userFileLock = new ReentrantReadWriteLock();
//    static public Lock writeUserFile = userFileLock.writeLock();
//    /*
//     * Used when writing to the itmes.txt file
//     */
//    static final private ReentrantReadWriteLock itemFileLock = new ReentrantReadWriteLock();
//    static public Lock writeItemsFile = itemFileLock.writeLock();
    /*
     * Function used to send data to different users through a UDP socket
     */

    synchronized static void send(String toSend, InetAddress clientAddress, int clientPort)
    {
        int counter = 0;
        //Sending function, will get repeated up to 5 times in case of failures
        boolean failedSending = true;
        while (counter < 5)
        {
            try
            {
                Socket socket = new Socket(clientAddress, clientPort);

                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                out.print(toSend);
                out.close();
                socket.close();  // Close the socket and its streams

                failedSending = false;
                break;
            }
            catch (ConnectException x)
            {
                counter++;

                System.out.println("Cannot connect to server... retrying");

                try
                {
                    Thread.currentThread().sleep(1000);
                }
                catch (Exception cannotSleep)
                {
                }
            }
            catch (IOException x)
            {
                counter++;
                System.out.println("IOException");
                x.printStackTrace();
            }
        }

        if (failedSending)
        {
            System.out.println("Failed sending... ");
        }
    }

    /**
     * Function that finds all the files in a directory
     *
     * @param mFiles - Vector of SCFile that will be filled with the files available to all users
     */
    static void getAllFiles(Vector<SCFile> mFiles)
    {
        // Directory path here
        String path = "documents";

        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();

        //sorting the files in alphabetical order
        Arrays.sort(listOfFiles);

        for (File file : listOfFiles)//int i = 0; i < listOfFiles.length; i++)
        {

            if (file.isFile())
            {
                SCFile newFile = new SCFile(file.getName(), file.getPath());
                mFiles.add(newFile);

                System.out.println(file.getName() + " " + file.getPath());
            }
        }
    }
}