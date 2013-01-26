package Server;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Grzegorz Gut <Gregg.Gut@gmail.com>
 */
public class ScribbleServer
{

    static int PORT;// = 21223;  // port for this server
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
        try
        {
            BufferedReader buffer = new BufferedReader(new FileReader("resources/NetworkSetting.info"));
            PORT = Integer.parseInt(buffer.readLine());
        }
        catch (FileNotFoundException ex)
        {
            Logger.getLogger(ScribbleServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex)
        {
        }
        new ScribbleServer();
    }
}
