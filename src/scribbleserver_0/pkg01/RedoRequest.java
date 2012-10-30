/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scribbleserver_0.pkg01;

/**
 *
 * @author Grzegorz Gut <Gregg.Gut@gmail.com>
 */
public class RedoRequest implements Request
{

    @Override
    public Integer getRequestID()
    {
        return requestID;
    }
    private int requestID;
    private int pathID;
    private int page;

    RedoRequest(int requestID, int page, int pathID)
    {
        this.requestID = requestID;
        this.page = page;
        this.pathID = pathID;
    }
}
