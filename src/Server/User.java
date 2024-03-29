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
import java.sql.Time;
import java.util.Random;
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
    private SCFile mTempFile;
//    private Path workingPath;
    private Socket clientSock;
    private String username;
//    private boolean busy = false;

    User(String clientAdd, int port, PrintWriter out, Socket clientSock)
    {
        this.clientAdd = clientAdd;
        this.port = port;
        this.out = out;
        this.clientSock = clientSock;
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

//    public void setBusy(boolean t)
//    {
//        busy=t;
//    }

    public boolean matchesUser(String user)
    {
        if (user.equals(username))
        {
            return true;
        }
        return false;
    }

    synchronized public void sendMessage(String msg)
    {
//        Random generator = new Random();
//        while (busy)
//        {
//            System.out.println("Busy..."+generator.nextInt());
//            try
//            {
//                Thread.currentThread().sleep(50);
//            }
//            catch (InterruptedException ex)
//            {
//                System.out.println("Busy failed...");
//                Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
        out.println(msg);
    }

//    synchronized public void sendUpdateMessage(String msg)
//    {
//        out.println(msg);
//    }

    private String encriptMessage(String toSend)
    {
        String header = String.format("%4d", toSend.length() + 1);
        toSend = header + toSend;
        return toSend;
    }

    synchronized public void sendFile(SCFile mFile)
    {
        Socket socket = null;
        DataInputStream in = null;
        DataOutputStream out = null;
        FileInputStream fis = null;

        try
        {
            socket = new Socket(clientAdd, 34567);
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
            File files = new File(mFile.getLocation());//"documents/" + filename);

            byte[] buf = new byte[512];
            int len;

            System.out.println("client : file name: " + files.getName());

            fis = new FileInputStream(files);

            while ((len = fis.read(buf)) != -1)
            {
                out.write(buf, 0, len);
            }

            this.setmFile(mFile);
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
        if (mFile != null)
        {
            this.mFile = mFile;
            mFile.addActiveUsers(this);
        }
    }

    public void setTempFile(SCFile mFile)
    {
        mTempFile = mFile;
    }

    public SCFile getTempFile()
    {
        return mTempFile;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getClientAdd()
    {
        return clientAdd;
    }

    public int getPort()
    {
        return port;
    }
}
