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
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * @author Grzegorz Gut <Gregg.Gut@gmail.com>
 */
public class User
{

    private String clientAdd;
    private int port;
    private PrintWriter out;
    private SCFile mFile;
    private Path workingPath;

    User(String clientAdd, int port, PrintWriter out)
    {
        this.clientAdd = clientAdd;
        this.port = port;
        this.out = out;
    }

    public boolean matches(String ca, int p)
    // the address and port of a client are used to uniquely identify it
    {
        if (clientAdd.equals(ca) && (port == p))
        {
            return true;
        }
        return false;
    }

    synchronized public void sendMessage(String msg)
    {
        out.println(msg);
    }

    synchronized public void sendFile()
    {
        Socket socket = null;
        DataInputStream in = null;
        DataOutputStream out = null;
        FileInputStream fis = null;

        try
        {
            socket = new Socket("127.0.0.1", 34567);
            in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        }
        catch (UnknownHostException e)
        {
            System.out.println("Unknown host: 127.0.0.1");
            e.printStackTrace();
        }
        catch (IOException e)
        {
            System.out.println("No I/O");
            e.printStackTrace();
        }

        try
        {
            File files = new File("documents/0.pdf");


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
            socket.close();
        }
        catch (IOException e)
        {
            System.out.println("Could not close.");
            System.exit(-1);
        }

    }

    public SCFile getmFile()
    {
        return mFile;
    }

    public void setmFile(SCFile mFile)
    {
        //Remove myself from current file
        if (this.mFile != null)
        {
            this.mFile.removeUser(this);
        }
        //Set new file
        this.mFile = mFile;
        mFile.addActiveUsers(this);
    }

    public Path getWorkingPath()
    {
        return workingPath;
    }

    public void setWorkingPath(Path workingPath)
    {
        this.workingPath = workingPath;
    }
}
