package scribbleserver_0.pkg01;

import java.io.*;   // for IOException and Input/OutputStream
import java.net.*;  // for Socket, ServerSocket, and InetAddress
import java.util.Vector;

/**
 * This is the receiver thread (Server)
 *
 * @author scribble
 */
public class Receiver implements Runnable
{

    private boolean run = false;
    private Vector<User> mUsers;
    private Vector<SCFile> mFiles;

    /**
     * Default constructor
     *
     * @param user A vector of users that logged in to the server
     * @param files A vector of files available on the server
     */
    Receiver(Vector<User> user, Vector<SCFile> files)
    {
        mUsers = user;
        mFiles = files;
    }

    /**
     * This is the main loop of the server. It receives requests from different users and starts threads to analyze them
     */
    @Override
    public void run()
    {
        try
        {
            /**
             * Create a server socket to accept client connection requests
             */
            ServerSocket servSock = new ServerSocket(ClientToServer.SERVERPORT);

            String s;
            run = true;

            /**
             * Run forever, accepting and servicing connections
             */
            while (run)
            {
                /**
                 * Get client connection
                 */
                Socket clntSock = servSock.accept();     // Get client connection

                /**
                 * Get access to the socket
                 */
                OutputStream out = clntSock.getOutputStream();

                /**
                 * Read from socket
                 */
                BufferedReader in = new BufferedReader(new InputStreamReader(clntSock.getInputStream()));

                /**
                 * convert received data to string
                 */
                s = in.readLine();

                /**
                 * start Packet Analyzer thread
                 */
                new Thread(new PacketAnalyzer(s, clntSock.getInetAddress(), mUsers, mFiles)).start();

                /**
                 * Close the socket. We are done with this client!
                 */
                clntSock.close();

            }
        }
        catch (IOException exception)
        {
            exception.printStackTrace();
        }
        System.out.println("Ended");
    }
}
