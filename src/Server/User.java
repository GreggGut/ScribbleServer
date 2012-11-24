/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import java.io.PrintWriter;

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
