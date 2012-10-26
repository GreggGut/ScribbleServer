package scribbleserver_0.pkg01;

import java.io.*;   // for IOException and Input/OutputStream
import java.net.*;  // for Socket, ServerSocket, and InetAddress
import java.util.Vector;

/**
 *
 * @author scribble
 */
public class Receiver implements Runnable
{

    private boolean run = false;
    private Vector<User> mUsers;
    private Vector<SCFile> mFiles;

    Receiver(Vector<User> user, Vector<SCFile> files)
    {
        mUsers = user;
        mFiles = files;
    }

    public void run()
    {
        int servPort = 21223;//Integer.parseInt(args[0]);

        try
        {
            // Create a server socket to accept client connection requests
            ServerSocket servSock = new ServerSocket(servPort);

            int recvMsgSize;   // Size of received message

            String s;
            run = true;

            while (run)
            {
                // Run forever, accepting and servicing connections
                Socket clntSock = servSock.accept();     // Get client connection
                System.out.println("Handling client at " + clntSock.getInetAddress().getHostAddress() + " on port " + clntSock.getPort());
                //InputStream in = clntSock.getInputStream();
                OutputStream out = clntSock.getOutputStream();

                BufferedReader in = new BufferedReader(new InputStreamReader(clntSock.getInputStream()));

                s = in.readLine();

                new Thread(new PacketAnalyzer(s, clntSock.getInetAddress(), mUsers, mFiles)).start();

                clntSock.close();  // Close the socket.  We are done with this client!

            }
        }
        catch (IOException exception)
        {
            exception.printStackTrace();
        }
        System.out.println("Ended");
    }
}
