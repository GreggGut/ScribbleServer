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

    /**
     * User to split the info received from the clients
     */
    public static String split = "&";
    /**
     * Used to split the points received from the clients
     */
    public static String splitPoints = "#";

    /**
     * Static function that allows the server to send messages to the clients
     *
     * @param toSend Message to be send to a user
     * @param clientAddress User Address (IP)
     * @param clientPort User listening port
     */
    synchronized static void send(String toSend, InetAddress clientAddress, int clientPort)
    {
        int counter = 0;
        /*
         * Sending function, will get repeated up to 5 times in case of failures
         */
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

                System.out.println("Cannot connect to server... retrying: " + toSend + " to: " + clientAddress.getHostAddress() + " Port: " + clientPort);

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
            //TESTING
            System.out.println("Failed sending... <---------------------------");
        }
        else
        {
            //TESTING
            System.out.println("------------------->Send completed: " + toSend + "<---------------------------");
        }
    }

    /**
     * Function that finds all the files in a directory
     *
     * @param mFiles Vector of SCFile that will be filled with the files available to all users
     */
    static void getAllFiles(Vector<SCFile> mFiles)
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
}