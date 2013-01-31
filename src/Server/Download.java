/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * This class allows the user to download a file from the server
 *
 * @author Grzegorz Gut <Gregg.Gut@gmail.com>
 */
public class Download extends Thread
{

    private Socket clientSock;
    private ScribbleClients mClients;

    Download(Socket socket, ScribbleClients mClients)
    {
        clientSock = socket;
        this.mClients = mClients;
    }

    @Override
    public void run()
    {
        //Socket socket = null;
        DataInputStream in = null;
        DataOutputStream out = null;
        FileInputStream fis = null;
        String filename = null;

        try
        {
            in = new DataInputStream(new BufferedInputStream(clientSock.getInputStream()));
            out = new DataOutputStream(new BufferedOutputStream(clientSock.getOutputStream()));
            byte[] buf = new byte[512];
            in.read(buf);
            filename = new String(buf).trim();

            System.out.println("File to download: " + filename + "-------");

        }
        catch (IOException e)
        {
            System.out.println("No I/O");
            e.printStackTrace();
        }

        SCFile mFile = findMyFile(filename);

        try
        {
            File files = new File(mFile.getLocation());//"documents/" + filename);


            byte[] buf = new byte[512];
            int len;

            System.out.println("client : file name: " + files.getName());

            fis = new FileInputStream(files);

            while ((len = fis.read(buf)) != -1)
            {
                out.write(buf, 0, len);
            }

            out.flush();

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        //Clean up
        try
        {
            fis.close();
            out.close();
            in.close();
            clientSock.close();
        }
        catch (IOException e)
        {
            System.out.println("Could not close.");
            System.exit(-1);
        }
    }

    private SCFile findMyFile(String filename)
    {
        SCFile mFile = null;
        for (SCFile file : mClients.getFiles())
        {
            if (file.getName().equals(filename))
            {
                mFile = file;
                break;
            }
        }

        return mFile;
    }
}
