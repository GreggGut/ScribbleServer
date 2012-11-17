/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scribbleserver_0.pkg01;

import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

/**
 * Request analyzer - periodically checks if there exists some request for the given user (Each user has their own Request analyzer)
 *
 * @author Grzegorz Gut <Gregg.Gut@gmail.com>
 */
public class RequestAnalyser implements Runnable
{

    /**
     * The request list for the given user
     */
    private Vector<Request> mRequests;
    /**
     * The user to whom all the request belong
     */
    private User user;
    /**
     * Request counter
     * TODO Limit the counter to some number
     */
    private int lastRequestCompleted;
    /**
     * This is a periodical function/thread and the delay below indicated how often this thread should execute
     */
    private int delay = 100;

    /**
     * Default constructor
     *
     * @param mRequest A vector of the user requests
     * @param user The requesting user
     * @param lastRequestCompleted The ID of the request that has been completed last (Usually corresponds to the user login request ID)
     */
    RequestAnalyser(Vector<Request> mRequest, User user, int lastRequestCompleted)
    {
        this.mRequests = mRequest;
        this.user = user;
        this.lastRequestCompleted = lastRequestCompleted;
    }

    @Override
    public void run()
    {

        try
        {
            while (!Thread.interrupted())
            {
                /**
                 * Sorting by request ID
                 */
                Collections.sort(mRequests, new SortByRequestID());

                /**
                 * For all requests in the queue, find the next one that needs to be executed and execute it
                 * Since this list sorted it is possible to execute more than one request in the loop
                 */
                for (int i = 0; i < mRequests.size(); i++)
                {
                    /**
                     * Executing the next request only if it is the one expected (Last executed ID +1)
                     */
                    if (mRequests.get(i).getRequestID() == (lastRequestCompleted + 1))
                    {
                        executeRequest(mRequests.get(i));
                        lastRequestCompleted++;// = mRequests.get(i).getRequestID();
                        mRequests.set(i, null);
                    }
                }

                /**
                 * Removing all the null (completed) from the request list
                 */
                mRequests.removeAll(Collections.singleton(null));

                /**
                 * Putting the thread to sleep for a predetermine period of time. Once that period expires the thread will run again
                 * TODO Users who have a file ownership should have this thread run more frequently than users who do not have ownership (1
                 * to 4)
                 */
                try
                {
                    Thread.sleep(delay);
                }
                catch (InterruptedException x)
                {
                }
            }
        }
        finally
        {
            //Clean up if needed, nothing for now
        }

        System.out.println("RequestAnalyser Thread Ended");
    }

    /**
     * Execute a request - This function find what type of request has been placed by the user and then executes it
     *
     * @param request All the info and data needed to execute a request
     */
    private void executeRequest(Request request)
    {
        /**
         * If new Path request
         */
        if (request.getClass().equals(NewPathRequest.class))
        {
            executeNewPathRequest((NewPathRequest) request);
        }
        /**
         * If end Path request
         */
        else if (request.getClass().equals(EndPathRequest.class))
        {
            executeEndPathRequest((EndPathRequest) request);
        }
        /**
         * if Undo request
         */
        else if (request.getClass().equals(UndoRequest.class))
        {
            executeUndoRequest((UndoRequest) request);
        }
        /**
         * if Redo request
         */
        else if (request.getClass().equals(RedoRequest.class))
        {
            executeRedoRequest((RedoRequest) request);
        }
        /**
         * if Logout request
         */
        else if (request.getClass().equals(LogoutRequest.class))
        {
            executeLogoutRequest((LogoutRequest) request);
        }
        /**
         * if Ownership request
         */
        else if (request.getClass().equals(OwnershipRequest.class))
        {
            executeOwnershipRequest((OwnershipRequest) request);
        }
        /**
         * if release Ownership request
         */
        else if (request.getClass().equals(ReleaseOwnershipRequest.class))
        {
            executeReleaseOwnershipRequest((ReleaseOwnershipRequest) request);
        }
        /**
         * if get files list request
         */
        else if (request.getClass().equals(GetFileListRequest.class))
        {
            executeGetFileListRequest((GetFileListRequest) request);
        }
        /**
         * if add Points request
         */
        else if (request.getClass().equals(AddPointsRequest.class))
        {
            executeAddPointsRequest((AddPointsRequest) request);
        }
        /**
         * if delete Path request
         */
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

    /**
     * New Path Request - This function will create a new path
     *
     * @param request All the info needed to create a new path
     */
    private void executeNewPathRequest(NewPathRequest request)
    {
        System.out.println("executeNewPathRequest");
        Path path = new Path(request.getPathID(), request.isMode(), request.getColor(), request.isActive(), request.getWidth());

        /**
         * Adding new path to the
         * TODO We have to make user the user cannot add new paths before he/she has chosen a file to work on
         */
        user.getActiveFile().getmPages().get(request.getPage()).addPath(path);

        /**
         * Adding the path to the user current working path
         */
        user.setWorkingPath(path);
    }

    /**
     * End Path Request - This function will end the creation of the current path. This means we cannot add any other points to it
     *
     * @param request The info needed to complete the end path request
     */
    private void executeEndPathRequest(EndPathRequest request)
    {
        System.out.println("executeEndPathRequest");
        //TOCONF Is there a better was to do it???
        user.setWorkingPath(null);
    }

    /**
     * Undo Request - This request will undo the last action done by an user
     *
     * @param request All the info needed in order to complete the undo request
     */
    private void executeUndoRequest(UndoRequest request)
    {
        System.out.println("executeUndoRequest");
        //TODO implement Undo Request
    }

    /**
     * Redo Request - This request will redo the last action that has been undone
     *
     * @param request All the info needed in order to complete this request
     */
    private void executeRedoRequest(RedoRequest request)
    {
        System.out.println("executeRedoRequest");
        //TODO implement Redo Request 
    }

    /**
     * Logout Request - This request will logout the user from the system
     *
     * @param request All the info needed in order to logout the user
     */
    private void executeLogoutRequest(LogoutRequest request)
    {
        System.out.println("executeLogoutRequest");
        user.logout();
        String toSend = "";
        toSend += ServerToClient.LOG_OUT_SUCCESSFUL;
        toSend += HELPER.split;        
        
        toSend += user.getClientExpectsRequestID();
        toSend += HELPER.split;

        //TOCONF This is most likely completely useless since user will be delete after this request
        //user.increaseClientExpectsRequestID();

        HELPER.send(toSend, user.getAddress(), user.getPort());

        /**
         * Once we are done with this we set it as completed. This is done so that the user can be removed from the active users list
         */
        request.setAsCompleted();
    }

    /**
     * Ownership Request - This request will grant the user the ownership of the file he/she is working on (1 owner per file max)
     *
     * @param request All the info needed in order to complete the ownership request
     */
    private void executeOwnershipRequest(OwnershipRequest request)
    {
        System.out.println("executeOwnershipRequest");
        /**
         * Making sure there is no owner
         * TOCONF We might need a lock here so that no 2 user get the ownership of the same file simultaneously
         */
        if (user.getActiveFile().getPresentOwner() == null)
        {
            user.getActiveFile().setPresentOwner(user);
            user.setOwnership(true);

            /**
             * Informing all users that the file ownership has been taken, including the requesting user
             */
            for (User allUsers : user.getActiveFile().getmActiveUsers())
            {
                /**
                 * Creating the message that will inform all concerned users of who has the file ownership
                 */
                String toSend = "";
                toSend += ServerToClient.ALLOW_OWNERSHIP;
                toSend += HELPER.split;

                toSend += user.getName();
                toSend += HELPER.split;

                /**
                 * This is so that the respective clients acknowledge this request and change what they expect to the
                 * request.getRequestID();
                 */
                toSend += allUsers.getClientExpectsRequestID(); //request.getRequestID();
                toSend += HELPER.split;

                //allUsers.increaseClientExpectsRequestID();

                /**
                 * The request ID will be used by the receivers to know where to start their expected request ID
                 */
                toSend += request.getRequestID();
                toSend += HELPER.split;

                HELPER.send(toSend, allUsers.getAddress(), allUsers.getPort());

                user.setClientExpectsRequestID(request.getRequestID() + 1);
            }
        }
        //TOCONF This is removed for now
//        else
//        {
//            /**
//             * Ownership already taken, inform requesting user
//             */
//            String toSend = "";
//            toSend += ServerToClient.DISALLOW_OWNERSHIP;
//            toSend += HELPER.split;
//
//            toSend += user.getClientExpectsRequestID();
//            toSend += HELPER.split;
//
//            HELPER.send(toSend, user.getAddress(), user.getPort());
//        }
    }

    /**
     * Release Ownership Request - This request will release the write access of the file the user has access to
     * This should be only received from the user who has the writing access presently - This will be done on the client side.
     *
     * @param request All the info in order to complete the release request
     */
    private void executeReleaseOwnershipRequest(ReleaseOwnershipRequest request)
    {
        System.out.println("executeReleaseOwnershipRequest");
        user.setOwnership(false);
        user.getActiveFile().setPresentOwner(null);

        /*
         * Broadcast to all that the ownership is free
         */
        String toSend = "";
        toSend += ServerToClient.OWNERSHIP_IS_AVAILABLE;
        toSend += HELPER.split;

        //All users should expect the same request ID
        toSend += user.getClientExpectsRequestID();
        toSend += HELPER.split;

        /**
         * Broadcasting to all active users that the write access is for grabs
         */
        for (User allUsers : user.getActiveFile().getmActiveUsers())
        {
            HELPER.send(toSend, allUsers.getAddress(), allUsers.getPort());
            if(!user.equals(allUsers))
            {
                allUsers.increaseClientExpectsRequestID();
            }
            
            //user.increaseClientExpectsRequestID();
        }
    }

    /**
     * Get Files List Request - This request will send a list of all the files available on the server to the requesting use
     *
     * @param request All the info needed in order to complete the get files list request
     */
    private void executeGetFileListRequest(GetFileListRequest request)
    {
        //TOCONF We might need to have a request ID with this
        System.out.println("executeGetFileListRequest");
        String toBeSend = "";
        toBeSend += ServerToClient.FILE_LIST_AVAILABLE;
        toBeSend += HELPER.split;

        toBeSend += user.getClientExpectsRequestID();
        toBeSend += HELPER.split;
        
        //user.increaseClientExpectsRequestID();

        /**
         * Creating a text file will all the files available to the user
         */
        for (SCFile file : request.getmFiles())
        {
            toBeSend += file.getName();
            toBeSend += HELPER.splitPoints;
        }

        /**
         * Send file list to client
         */
        HELPER.send(toBeSend, user.getAddress(), user.getPort());
    }

    /**
     * Add Points Request - This request will add points to a path
     *
     * @param request The Request data that includes all the info needed to complete this request
     */
    private void executeAddPointsRequest(AddPointsRequest request)
    {
        System.out.println("executeAddPointsRequest");

        /*
         * Seperating the points
         */
        String[] bytes = request.getPoints().split(HELPER.splitPoints);

        /**
         * Making sure that the number of data is even since each point has 2 coordinates: x and y.
         */
        if (bytes.length % 2 == 0)
        {
            /**
             * For each data create an x and y coordinate and the proceed to create a point, which will be address to the current path
             */
            for (int i = 0; i < bytes.length;)
            {
                int x = Integer.parseInt(bytes[i++]);
                int y = Integer.parseInt(bytes[i++]);

                System.out.println("X: " + x + " Y: " + y);

                Point mPoint = new Point(x, y);
                user.getWorkingPath().AddPoint(mPoint);
            }
        }
    }

    /**
     * Delete Path Request - This request will delete a path from a page of the user active document
     * TOCONF This will depend on how we implement the delete function on Scribble
     *
     * @param request
     */
    private void executeDeletePathRequest(DeletePathRequest request)
    {
        System.out.println("executeDeletePathRequest");
        //TODO implement the detele path request... this will depend on how we implement the delete function on Scribble
    }
}