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
import java.util.Comparator;
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

        AddPointsRequest m5 = new AddPointsRequest(5, 4, 10, "0909090909090909090909090909090909090909");//est(5, 12, "abc");
        AddPointsRequest m6 = new AddPointsRequest(6, 4, 10, "0909090909090909090909090909090909090909");
        AddPointsRequest m7 = new AddPointsRequest(7, 4, 10, "0909090909090909090909090909090909090909");
        AddPointsRequest m8 = new AddPointsRequest(8, 4, 10, "0909090909090909090909090909090909090909");

        String taaa="09";//09090909090909090909090909090909090909";
        
        byte[] a = taaa.getBytes();
        
        for(int i=0;i<a.length;i++)
        {
            System.out.println(a[i]);
        }
        mRequests.add(m1);

        mRequests.add(m3);
        mRequests.add(m7);
        mRequests.add(m8);
        mRequests.add(m4);
        mRequests.add(m5);
        mRequests.add(m2);
        mRequests.add(m6);


//        for (int i = 0; i < mRequests.size(); i++)// r:mRequests)
//        {
//            System.out.println("ID " + mRequests.get(i).getRequestID());
//            //NewPathRequest a = (NewPathRequest) mRequests.get(i).getPathID();
//            //System.out.println("Maybe: "+(NewPathRequest)mRequests.get(i).getPathID());
//            //executeRequest(mRequests.get(i));
//            mRequests.set(i, null);
//        }

//        Collections.sort(mRequests, new EmpSortByName());
//
//
//        for (int i = 0; i < mRequests.size(); i++)// r:mRequests)
//        {
//            System.out.println("ID " + mRequests.get(i).getRequestID());
//            //NewPathRequest a = (NewPathRequest) mRequests.get(i).getPathID();
//            //System.out.println("Maybe: "+(NewPathRequest)mRequests.get(i).getPathID());
//            //executeRequest(mRequests.get(i));
//            mRequests.set(i, null);
//        }

//        System.out.println("Size: " + mRequests.size());
//        mRequests.removeAll(Collections.singleton(null));
//
//        System.out.println("Size: " + mRequests.size());
//        for (Request r : mRequests)
//        {
//            System.out.println("ID " + r.getRequestID());
//            r = null;
//        }

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
//
//    static public class EmpSortByName implements Comparator<Request>
//    {
//
//        @Override
//        public int compare(Request o1, Request o2)
//        {
//            return o1.getRequestID().compareTo(o2.getRequestID());
//        }
//    }
}
