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

    private int requestID;
    private boolean completed = false;

    /**
     * Get the request ID - Request are executed in order starting from the smallest (earlier request) to a larger (newer request) request
     * ID
     *
     * @return This request ID
     */
    @Override
    public Integer getRequestID()
    {
        return requestID;
    }

    /**
     * Default constructor
     *
     * @param requestID The ID of the request
     */
    LogoutRequest(int requestID)
    {
        this.requestID = requestID;
    }

    /**
     * Set the request as completed
     */
    public void setAsCompleted()
    {
        completed = true;
    }

    /**
     * Is this request completed
     *
     * @return True if the request is completed, otherwise false
     */
    public boolean isCompleted()
    {
        return completed;
    }
}
