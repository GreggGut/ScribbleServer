/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * This class is waiting for clients to connect in order to download a file from the server
 * The port used is the regular port +1
 *
 * @author Grzegorz Gut <Gregg.Gut@gmail.com>
 */
public class FileDownloader extends Thread
{

    private ScribbleClients mClients;

    FileDownloader(ScribbleClients mClients)
    {
        this.mClients = mClients;
    }

    @Override
    public void run()
    {
        try
        {
            ServerSocket serverSock = new ServerSocket(ScribbleServer.PORT + 1);
            Socket clientSock;

            while (true)
            {
                System.out.println("Waiting for a client to download a file...");
                clientSock = serverSock.accept();
                new Download(clientSock, mClients).start();
            }
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }
}
