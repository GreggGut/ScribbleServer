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
                    mRequest.set(i,null);
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
        
    }
}
