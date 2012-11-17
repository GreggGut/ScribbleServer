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
import java.util.Collections;
import java.util.Vector;

/**
 *
 * @author Grzegorz Gut <Gregg.Gut@gmail.com>
 */
public class HELPER implements Runnable
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
     * Used as a queue that stores everything that needs to be send
     */
    private static Vector<ToBeSend> toBeSend = new Vector<ToBeSend>();

    @Override
    public void run()
    {
        while (true)
        {
            for (int i = 0; i < toBeSend.size(); i++)
            {
                try
                {
                    Socket socket = new Socket(toBeSend.elementAt(i).getClientAddress(), toBeSend.elementAt(i).getClientPort());

                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    out.print(toBeSend.elementAt(i).getToSend());
                    out.close();
                    socket.close();  // Close the socket and its streams

                    toBeSend.set(i, null);

                    break;
                }
                catch (ConnectException x)
                {
                    if (toBeSend.elementAt(i).getCounter() > 100)
                    {
                        System.out.println("Cannot connect to client... Will not retry this: " + toBeSend.elementAt(i).getToSend() + " to: " + toBeSend.elementAt(i).getClientAddress().getHostAddress() + " Port: " + toBeSend.elementAt(i).getClientPort());
                        toBeSend.set(i, null);
                        break;
                    }
                }
                catch (IOException x)
                {
                    if (toBeSend.elementAt(i).getCounter() > 100)
                    {
                        System.out.println("Cannot connect to client...  Will not retry this: " + toBeSend.elementAt(i).getToSend() + " to: " + toBeSend.elementAt(i).getClientAddress().getHostAddress() + " Port: " + toBeSend.elementAt(i).getClientPort());
                        toBeSend.set(i, null);
                        break;
                    }
                    System.out.println("IOException");
                    x.printStackTrace();
                }
            }

            /**
             * Removing all the send requests that have been completed or that the client do not respond to
             */
            toBeSend.removeAll(Collections.singleton(null));


            try
            {
                Thread.currentThread().sleep(40);
            }
            catch (Exception cannotSleep)
            {
            }
        }
    }

    /**
     * This function adds to the send Vector, which is checked periodically by the sending thread
     *
     * @param toSend String that needs to be send
     * @param clientAddress The client address
     * @param clientPort The client listening port
     */
    static void AddToSend(String toSend, InetAddress clientAddress, int clientPort)
    {
        ToBeSend request = new ToBeSend(toSend, clientAddress, clientPort);
        toBeSend.add(request);
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