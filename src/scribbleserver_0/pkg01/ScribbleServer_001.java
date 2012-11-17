/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scribbleserver_0.pkg01;

import java.util.Vector;

/**
 * This is the server module on Scribble
 *
 * @author Grzegorz Gut <Gregg.Gut@gmail.com>
 */
public class ScribbleServer_001
{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        /**
         * All the users logged in to the server
         */
        Vector<User> mUsers = new Vector<User>();
        /**
         * All the files available on the server
         */
        Vector<SCFile> mFiles = new Vector<SCFile>();

        /**
         * Filling the vector with all the files available to the users
         */
        HELPER.getAllFiles(mFiles);

        /**
         * Creating the Receiver thread and giving it the highest possible priority
         */
        Thread receiver = new Thread(new Receiver(mUsers, mFiles));
        receiver.setPriority(Thread.MAX_PRIORITY);
        receiver.start();

        /**
         * This thread checks a queue periodically and if there is anything in it it sends it
         */
        Thread sender = new Thread(new HELPER());
        sender.start();

        /**
         * Loop until the thread is stopped
         */
        while (receiver.isAlive())
        {
            try
            {
                Thread.sleep(1000);
            }
            catch (Exception x)
            {
            }
        }
    }
}
