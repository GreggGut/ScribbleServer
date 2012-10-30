/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scribbleserver_0.pkg01;

/**
 *
 * @author Grzegorz Gut <Gregg.Gut@gmail.com>
 */
public class LogoutRequest implements Request
{

    @Override
    public int getRequestID()
    {
        return requestID;
    }
    private int requestID;
    private boolean completed = false;

    LogoutRequest(int requestID)
    {
        this.requestID = requestID;
    }

    public void setAsCompleted()
    {
        completed = true;
    }

    public boolean isCompleted()
    {
        return completed;
    }
}
