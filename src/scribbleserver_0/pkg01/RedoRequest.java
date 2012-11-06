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

    private int requestID;
    private int pathID;
    private int page;

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
     * @param page The page to which this request belongs
     * @param pathID The path ID to which this request belongs
     */
    RedoRequest(int requestID, int page, int pathID)
    {
        this.requestID = requestID;
        this.page = page;
        this.pathID = pathID;
    }
}
