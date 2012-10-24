/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scribbleserver_0.pkg01;

/**
 *
 * @author scribble
 */
public class PacketAnalyzer implements Runnable
{
    String toBeAnalyzed;
    PacketAnalyzer(String info)
    {
        toBeAnalyzed=info;
    }
    
    public void run()
    {
        String[] info=toBeAnalyzed.split("-");
        if(info.length>0)
        {
            int choice = Integer.parseInt(info[0]);
        }
    }
}
