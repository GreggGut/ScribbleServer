/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scribbleserver_0.pkg01;

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
        // TODO code application logic here
        Thread t = new Thread(new Receiver());//.start();
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
