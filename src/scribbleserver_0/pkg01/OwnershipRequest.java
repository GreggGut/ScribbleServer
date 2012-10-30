/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scribbleserver_0.pkg01;

/**
 *
 * @author Grzegorz Gut <Gregg.Gut@gmail.com>
 */
public class OwnershipRequest implements Request
{

    @Override
    public Integer getRequestID()
    {
        return requestID;
    }
    private int requestID;

    OwnershipRequest(int requestID)
    {
        this.requestID = requestID;
    }
}
