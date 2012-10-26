/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scribbleserver_0.pkg01;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.Arrays;
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
        Vector<SCFile> mFiles = new Vector<SCFile>();
        getAllFiles(mFiles);

        /*
         * Need to fill the Vector of files
         */
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

    private static void getAllFiles(Vector<SCFile> mFiles)
    {
        // Directory path here
        String path = "documents";

        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();
        
        //sorting the files in alphabetical order
        Arrays.sort(listOfFiles);

        for (File file:listOfFiles)//int i = 0; i < listOfFiles.length; i++)
        {

            if (file.isFile())
            {
                SCFile newFile = new SCFile(file.getName(), file.getPath());
                mFiles.add(newFile);
                               
                System.out.println(file.getName() +" "+file.getPath());
            }
        }
    }
}
