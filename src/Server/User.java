/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        Socket clientSock = null;
        OutputStream out2 = null;
        DataOutputStream dos = null;
        try
        {
            ServerSocket serverSock = new ServerSocket(34567);

            clientSock = serverSock.accept();

            System.out.println("Connected");
            //create file object
            File file = new File("documents/0.pdf");

            FileInputStream fileInputStream = null;

            /*
             * Create new FileInputStream object. Constructor of FileInputStream throws
             * FileNotFoundException if the agrument File does not exist.
             */

            out2 = clientSock.getOutputStream();
            dos = new DataOutputStream(out2);
            fileInputStream = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            int number;

            while ((number = fileInputStream.read(buffer)) != -1)
            {
                dos.write(buffer, 0, number);
            }
        }
        catch (FileNotFoundException ex)
        {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex)
        {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try
            {
                clientSock.close();
                dos.close();
                out2.close();

            }
            catch (IOException ex)
            {
                Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
            }
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
