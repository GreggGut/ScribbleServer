package scribbleserver_0.pkg01;

import java.io.*;   // for IOException and Input/OutputStream
import java.net.*;  // for Socket, ServerSocket, and InetAddress

/**
 *
 * @author scribble
 */
public class Receiver implements Runnable
{

    private static final int BUFSIZE = 1000;   // Size of receive buffer
    boolean run = false;

    Receiver()
    {
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
                byte[] byteBuffer = new byte[BUFSIZE];  // Receive buffer
                // Run forever, accepting and servicing connections
                Socket clntSock = servSock.accept();     // Get client connection
                System.out.println("Handling client at " + clntSock.getInetAddress().getHostAddress() + " on port " + clntSock.getPort());
                InputStream in = clntSock.getInputStream();
                OutputStream out = clntSock.getOutputStream();

                // Receive until client closes connection, indicated by -1 return

                while ((recvMsgSize = in.read(byteBuffer)) != -1)
                {
                    out.write(byteBuffer, 0, recvMsgSize);
                }

                s = new String(byteBuffer);
                System.out.print(s);
                
                

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
