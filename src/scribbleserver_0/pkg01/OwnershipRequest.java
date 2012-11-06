/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scribbleserver_0.pkg01;

/**
 * Ownership request
 *
 * @author Grzegorz Gut <Gregg.Gut@gmail.com>
 */
public class OwnershipRequest implements Request
{

    private int requestID;

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
     * @param requestID The ID of the request
     */
    OwnershipRequest(int requestID)
    {
        this.requestID = requestID;
    }
}
