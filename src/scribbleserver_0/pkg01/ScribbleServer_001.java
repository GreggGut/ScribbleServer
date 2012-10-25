/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scribbleserver_0.pkg01;

import java.util.Vector;

/**
 *
 * @author scribble
 */
public class ScribbleServer_001
{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        Vector<User> mUsers = new Vector<User>();
        Vector<File> mFiles = new Vector<File>();
        // TODO code application logic here
        Thread t = new Thread(new Receiver(mUsers, mFiles));//.start();

        t.setPriority(Thread.MAX_PRIORITY);

        t.start();
        //r.start();
        //Sender s = new Sender();
        //s.start();

        while (t.isAlive())
        {

            try
            {
                Thread.currentThread().sleep(1000);
            }
            catch (Exception x)
            {
            }
        }
    }
}
