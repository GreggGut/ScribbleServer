/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scribbleserver_0.pkg01;

/**
 * Release Ownership Request - This Request will hold all the info needed in order to complete a Release Ownership request
 *
 * @author Grzegorz Gut <Gregg.Gut@gmail.com>
 */
public class ReleaseOwnershipRequest implements Request
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
     *
     * @param requestID The ID of the request
     */
    ReleaseOwnershipRequest(int requestID)
    {
        this.requestID = requestID;
    }
}
