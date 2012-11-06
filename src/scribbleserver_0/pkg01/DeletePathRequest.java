/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scribbleserver_0.pkg01;

/**
 *
 * @author Grzegorz Gut <Gregg.Gut@gmail.com>
 */
public class DeletePathRequest implements Request
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
     * @param requestID The ID f the request
     * @param page The page on which we want to delete the Path
     * @param pathID The path ID we want to delete
     */
    DeletePathRequest(int requestID, int page, int pathID)
    {
        this.requestID = requestID;
        this.page = page;
        this.pathID = pathID;
    }
}