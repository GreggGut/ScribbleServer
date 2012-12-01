package Server;

import java.net.*;

/**
 *
 * @author Grzegorz Gut <Gregg.Gut@gmail.com>
 */
public class ScribbleServer
{

    static final int PORT = 21223;  // port for this server
    private ScribbleClients mClients;

    public ScribbleServer()
    // wait for a client connection, spawn a thread, repeat
    {
        mClients = new ScribbleClients();
        try
        {
            ServerSocket serverSock = new ServerSocket(PORT);
            Socket clientSock;

            while (true)
            {
                System.out.println("Waiting for a client...");
                clientSock = serverSock.accept();
                new ClientHandler(clientSock, mClients).start();
            }
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }  // end of ChatServer()

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        new ScribbleServer();
    }
}
