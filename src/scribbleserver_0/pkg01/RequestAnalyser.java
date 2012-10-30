/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scribbleserver_0.pkg01;

import java.util.Collections;
import java.util.Vector;

/**
 *
 * @author Grzegorz Gut <Gregg.Gut@gmail.com>
 */
public class RequestAnalyser implements Runnable
{

    private boolean run = true;
    private Vector<Request> mRequest;
    private User user;
    private int lastRequestCompleted;

    RequestAnalyser(Vector<Request> mRequest, User user, int lastRequestCompleted)
    {
        this.mRequest = mRequest;
        this.user = user;
        this.lastRequestCompleted = lastRequestCompleted;
    }

    @Override
    public void run()
    {
        while (run)
        {
            for (int i = 0; i < mRequest.size(); i++)
            {
                //If this request is the next request that needs to be completed
                if (mRequest.get(i).getRequestID() == (lastRequestCompleted + 1))
                {
                    executeRequest(mRequest.get(i));
                    mRequest.set(i, null);
                    lastRequestCompleted++;
                }
            }

            mRequest.removeAll(Collections.singleton(null));

            mRequest.removeAll(Collections.singletonList(null));
            if (!mRequest.isEmpty())
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
        return !mRequest.isEmpty();
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
        else if (request.getClass().equals(NewPointsRequest.class))
        {
            executeNewPointsRequest((NewPointsRequest) request);
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
    }

    private void executeNewPathRequest(NewPathRequest request)
    {
    }

    private void executeNewPointsRequest(NewPointsRequest request)
    {
    }

    private void executeEndPathRequest(EndPathRequest request)
    {
    }

    private void executeUndoRequest(UndoRequest request)
    {
    }

    private void executeRedoRequest(RedoRequest request)
    {
    }

    private void executeLogoutRequest(LogoutRequest request)
    {
    }

    private void executeOwnershipRequest(OwnershipRequest request)
    {
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
}
