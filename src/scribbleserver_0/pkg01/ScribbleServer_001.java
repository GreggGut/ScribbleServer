/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scribbleserver_0.pkg01;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.Collections;
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
        Vector<Request> mRequests = new Vector<Request>();


        NewPathRequest m1 = new NewPathRequest(1, 11, true, 123, true, 1);
        NewPathRequest m2 = new NewPathRequest(2, 11, true, 123, true, 1);
        NewPathRequest m3 = new NewPathRequest(3, 11, true, 123, true, 1);
        NewPathRequest m4 = new NewPathRequest(4, 11, true, 123, true, 1);

        NewPointsRequest m5 = new NewPointsRequest(5, 12, "abc");
        NewPointsRequest m6 = new NewPointsRequest(6, 12, "abc");
        NewPointsRequest m7 = new NewPointsRequest(7, 12, "abc");
        NewPointsRequest m8 = new NewPointsRequest(8, 12, "abc");

        mRequests.add(m1);
        mRequests.add(m2);
        mRequests.add(m3);
        mRequests.add(m4);
        mRequests.add(m5);
        mRequests.add(m6);
        mRequests.add(m7);
        mRequests.add(m8);

        for (int i = 0; i < mRequests.size(); i++)// r:mRequests)
        {
            System.out.println("ID " + mRequests.get(i).getRequestID());
            //NewPathRequest a = (NewPathRequest) mRequests.get(i).getPathID();
            //System.out.println("Maybe: "+(NewPathRequest)mRequests.get(i).getPathID());
            //executeRequest(mRequests.get(i));
            mRequests.set(i, null);
        }

        System.out.println("Size: " + mRequests.size());
        mRequests.removeAll(Collections.singleton(null));

        System.out.println("Size: " + mRequests.size());
        for (Request r : mRequests)
        {
            System.out.println("ID " + r.getRequestID());
            r = null;
        }

        //Filling the vector with all the files available to the users
        HELPER.getAllFiles(mFiles);

        Thread t = new Thread(new Receiver(mUsers, mFiles, mRequests));//.start();

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
//    static private void executeRequest(Request request)
//    {
//        if (request.getClass().equals(NewPathRequest.class))
//        {
//            executeNewPathRequest((NewPathRequest) request);
//        }
//        else if (request.getClass().equals(NewPointsRequest.class))
//        {
//            executeNewPointsRequest((NewPointsRequest) request);
//        }
//        else if (request.getClass().equals(EndPathRequest.class))
//        {
//            executeEndPathRequest((EndPathRequest) request);
//        }
//    }
//
//    static private void executeNewPathRequest(NewPathRequest request)
//    {
//        System.out.println("NewPath: "+request.getColor());
//    }
//
//    static private void executeNewPointsRequest(NewPointsRequest request)
//    {
//        System.out.println("NewPoint: "+request.getPoints());
//    }
//
//    static private void executeEndPathRequest(EndPathRequest request)
//    {
//    }
}
