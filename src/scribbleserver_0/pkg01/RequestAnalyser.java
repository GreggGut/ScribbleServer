/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scribbleserver_0.pkg01;

import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

/**
 *
 * @author Grzegorz Gut <Gregg.Gut@gmail.com>
 */
public class RequestAnalyser implements Runnable
{

    private boolean run = true;
    private Vector<Request> mRequests;
    private User user;
    private int lastRequestCompleted;

    RequestAnalyser(Vector<Request> mRequest, User user, int lastRequestCompleted)
    {
        this.mRequests = mRequest;
        this.user = user;
        this.lastRequestCompleted = lastRequestCompleted;
    }

    @Override
    public void run()
    {
        while (run)
        {
            // Sorting by request ID             
            Collections.sort(mRequests, new SortByRequestID());

            for (int i = 0; i < mRequests.size(); i++)
            {
                //If this request is the next request that needs to be completed
                if (mRequests.get(i).getRequestID() == (lastRequestCompleted + 1))
                {
                    executeRequest(mRequests.get(i));
                    lastRequestCompleted = mRequests.get(i).getRequestID();
                    mRequests.set(i, null);
                }
            }

            mRequests.removeAll(Collections.singleton(null));

            mRequests.removeAll(Collections.singletonList(null));
            if (!mRequests.isEmpty())
            {
            }

            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException x)
            {
            }
        }
    }

    public boolean areRequestPending()
    {
        return !mRequests.isEmpty();
    }

    public void stopThread()
    {
        run = false;
    }

    private void executeRequest(Request request)
    {
        if (request.getClass().equals(NewPathRequest.class))
        {
            executeNewPathRequest((NewPathRequest) request);
        }
        else if (request.getClass().equals(EndPathRequest.class))
        {
            executeEndPathRequest((EndPathRequest) request);
        }
        else if (request.getClass().equals(UndoRequest.class))
        {
            executeUndoRequest((UndoRequest) request);
        }
        else if (request.getClass().equals(RedoRequest.class))
        {
            executeRedoRequest((RedoRequest) request);
        }
        else if (request.getClass().equals(LogoutRequest.class))
        {
            executeLogoutRequest((LogoutRequest) request);
        }
        else if (request.getClass().equals(OwnershipRequest.class))
        {
            executeOwnershipRequest((OwnershipRequest) request);
        }
        else if (request.getClass().equals(ReleaseOwnershipRequest.class))
        {
            executeReleaseOwnershipRequest((ReleaseOwnershipRequest) request);
        }
        else if (request.getClass().equals(GetFileListRequest.class))
        {
            executeGetFileListRequest((GetFileListRequest) request);
        }
        else if (request.getClass().equals(AddPointsRequest.class))
        {
            executeAddPointsRequest((AddPointsRequest) request);
        }
        else if (request.getClass().equals(DeletePathRequest.class))
        {
            executeDeletePathRequest((DeletePathRequest) request);
        }
    }

    /**
     * Comparator used to sort the request by ID, from smaller (first) to larger (last)
     */
    public class SortByRequestID implements Comparator<Request>
    {

        @Override
        public int compare(Request o1, Request o2)
        {
            return o1.getRequestID().compareTo(o2.getRequestID());
        }
    }

    private void executeNewPathRequest(NewPathRequest request)
    {
        System.out.println("executeNewPathRequest");
        Path path = new Path(request.getPathID(), request.isMode(), request.getColor(), request.isActive());

        //Adding new path to the 
        //We have to make user the user cannot add new paths before he/she has chosen a file to work on
        user.getActiveFile().getmPages().get(request.getPage()).addPath(path);

        //Adding the path to the user cirrent working path
        user.setWorkingPath(path);
    }

    private void executeEndPathRequest(EndPathRequest request)
    {
        System.out.println("executeEndPathRequest");
        //Is there a better was to do it???
        user.setWorkingPath(null);
    }

    private void executeUndoRequest(UndoRequest request)
    {
        System.out.println("executeUndoRequest");
        //TODO implement Undo Request
    }

    private void executeRedoRequest(RedoRequest request)
    {
        System.out.println("executeRedoRequest");
        //TODO implement Redo Request 
    }

    private void executeLogoutRequest(LogoutRequest request)
    {
        System.out.println("executeLogoutRequest");
        user.logout();
        String toSend = "";
        toSend += ServerToClient.logOutSuccessful;
        HELPER.send(toSend, user.getAddress(), user.getPort());

        //Once we are done with this we set it as completed
        request.setAsCompleted();
    }

    private void executeOwnershipRequest(OwnershipRequest request)
    {
        System.out.println("executeOwnershipRequest");
        //Making sure there is no owner
        if (user.getActiveFile().getPresentOwner() == null)
        {
            user.getActiveFile().setPresentOwner(user);
            user.setOwnership(true);

            String toSend = "";
            toSend += ServerToClient.ownershipTakenSuccessfully;
            toSend += HELPER.split;
            toSend += user.getName();
            toSend += HELPER.split;
            //Informing all users that the file ownership has been taken
            for (User allUsers : user.getActiveFile().getmActiveUsers())
            {
                HELPER.send(toSend, allUsers.getAddress(), allUsers.getPort());
            }
        }
        else
        {
            //Ownership already taken
            String toSend = "";
            toSend += ServerToClient.ownershipTakenFailed;
            toSend += HELPER.split;

            HELPER.send(toSend, user.getAddress(), user.getPort());
        }
    }

    private void executeReleaseOwnershipRequest(ReleaseOwnershipRequest request)
    {
        System.out.println("executeReleaseOwnershipRequest");
        user.setOwnership(false);
        user.getActiveFile().setPresentOwner(null);

        /*
         * Broadcast to all that the ownership is free
         */
        String toSend = "";
        toSend += ServerToClient.noOwnerOfFile;
        toSend += HELPER.split;

        for (User allUsers : user.getActiveFile().getmActiveUsers())
        {
            HELPER.send(toSend, allUsers.getAddress(), allUsers.getPort());
        }
    }

    private void executeGetFileListRequest(GetFileListRequest request)
    {
        System.out.println("executeGetFileListRequest");
        String toBeSend = "";
        toBeSend += ServerToClient.fileListAvailable;
        toBeSend += HELPER.split;

        for (SCFile file : request.getmFiles())
        {
            toBeSend += file.getName();
            toBeSend += HELPER.split;
        }

        //Send file list to client
        HELPER.send(toBeSend, user.getAddress(), user.getPort());
    }

    private void executeAddPointsRequest(AddPointsRequest request)
    {
        System.out.println("executeAddPointsRequest");
        //TODO convert the string of points into real points
        //request.getPoints().get       
    }

    private void executeDeletePathRequest(DeletePathRequest request)
    {
        System.out.println("executeDeletePathRequest");
        //TODO implement the detele path request... this will depend on how we implement the delete function on Scribble
    }
}
